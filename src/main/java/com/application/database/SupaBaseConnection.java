package com.application.database;

import com.application.entity.Student;
import com.application.entity.Class;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupaBaseConnection {
    // URL kết nối đến database (Ở đây là dùng SupaBase)
    // Có thể dùng file properties để thay đổi linh hoạt nhưng mà lười :))))
    private static final String DB_URL = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres?user=postgres.ofmkamppewhdxaecryag&password=Trankimcuong2003";

    // khởi tạo kết nối
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    // kiểm tra đăng nhập
    public static boolean Login(String username, String password) {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM account WHERE username = ? AND password = ?")) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // load sinh viên
    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sinhvien WHERE isdeleted = false");
             ResultSet rs = stmt.executeQuery()) {

            // lấy các giá trị tương ứng các bảng thuộc tính
            // (vị trí, thứ tự tương ứng với constructor trong class SinhVien)
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("masv"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getInt("major_id"),
                        rs.getInt("class_id"),
                        (rs.getDouble("gpa")),
                        rs.getString("address")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // load sinh viên đã bị xoá
    public static List<Student> loadDeletedStudents() {
        List<Student> students = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sinhvien WHERE isdeleted = true");
             ResultSet rs = stmt.executeQuery()) {

            // lấy các giá trị tương ứng các bảng thuộc tính
            // (vị trí, thứ tự tương ứng với constructor trong class SinhVien)
            while (rs.next()) {
                Student student = new Student(
                        rs.getInt("masv"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getInt("major_id"),
                        rs.getInt("class_id"),
                        (rs.getDouble("gpa")),
                        rs.getString("address")
                );
                students.add(student);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    // thêm sinh viên
    public static boolean addStudent(Student student) throws SQLException {
        try {Connection conn = getConnection();

            String sql = "INSERT INTO sinhvien (masv, name, age, gender, major_id, class_id, gpa, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setLong(1, student.getId());
            stmt.setString(2, student.getName());
            stmt.setInt(3, student.getAge());
            stmt.setString(4, student.getGender());
            stmt.setInt(5, student.getMajorId());
            stmt.setInt(6, student.getClassId());
            stmt.setDouble(7, student.getGpa());
            stmt.setString(8, student.getAddress());

            int rowsInserted = stmt.executeUpdate();
            return rowsInserted > 0; // Thành công nếu có bản ghi được thêm
        } catch (SQLException e) {
            throw new SQLException("Lỗi cơ sở dữ liệu: " + e.getMessage(), e);
        }
    }

    // lấy danh sách các lớp của ngành
    public static List<Class> loadClassesByMajor(int majorId) {
        List<Class> classes = new ArrayList<>();
        String query = "SELECT * FROM class WHERE major_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, majorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Class cls = new Class(
                            rs.getInt("id"),
                            rs.getString("class_name")
                    );
                    classes.add(cls);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    // Update sinh vien trong db
    public static boolean updateStudent(Student student) throws SQLException {
        String query = "UPDATE sinhvien SET name=?, age=?, gender=?, major_id=?, class_id=?, gpa=?, address=? WHERE masv=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, student.getName());
            stmt.setInt(2, student.getAge());
            stmt.setString(3, student.getGender());
            stmt.setInt(4, student.getMajorId());
            stmt.setInt(5, student.getClassId());
            stmt.setDouble(6, student.getGpa());
            stmt.setString(7, student.getAddress());
            stmt.setLong(8, student.getId());


            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new SQLException("Lỗi cơ sở dữ liệu: " + e.getMessage(), e);
        }
    }

    // dùng Function của Postgre để update trạng thái sinh viên
    // ở đây đang dùng trạng thái update mặc định là true (tức là bị xoá)
    public static boolean deleteStudent(long studentId) throws SQLException {
        String query = "select update_sv_state (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, studentId);
            stmt.setBoolean(2, true);
            stmt.executeQuery();
            return stmt.execute();
        }  catch (SQLException e) {
            throw new SQLException("Lỗi cơ sở dữ liệu: " + e.getMessage(), e);
        }
    }

    public static boolean fullDeleteStudent(long studentId) throws SQLException {
        String query = "select delete_sv(?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, studentId);
            stmt.executeQuery();
            return stmt.execute();
        }  catch (SQLException e) {
            throw new SQLException("Lỗi cơ sở dữ liệu: " + e.getMessage(), e);
        }
    }

    // recover để khôi phục lại trạng thái của sinh viên
    // chưa được áp dụng
    public static boolean recoverStudent(long studentId) throws SQLException {
        String query = "select update_sv_state (?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, studentId);
            stmt.setBoolean(2, false);
            stmt.executeQuery();
            return stmt.execute();
        }  catch (SQLException e) {
            throw new SQLException("Lỗi cơ sở dữ liệu: " + e.getMessage(), e);
        }
    }

    // tìm kiếm sinh viên và thêm vào 1 list
    public static List<Student> searchStudents(String name) throws SQLException {
        List<Student> students = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM sinhvien WHERE isdeleted = false");
        List<String> parameters = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            query.append(" And name ILIKE ?"); // ILIKE == LIKE trong SQL
            parameters.add("%" + name + "%");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setString(i + 1, parameters.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                            rs.getInt("masv"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getInt("major_id"),
                            rs.getInt("class_id"),
                            rs.getDouble("gpa"),
                            rs.getString("address")
                    );
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi cơ sở dữ liệu: " + e.getMessage(), e);
        }
        return students;
    }

    // tìm kiếm sinh viên và thêm vào 1 list
    public static List<Student> searchDeletedStudent(String name) throws SQLException {
        List<Student> students = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM sinhvien WHERE isdeleted = true");
        List<String> parameters = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            query.append(" And name ILIKE ?"); // ILIKE == LIKE trong SQL
            parameters.add("%" + name + "%");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query.toString())) {

            for (int i = 0; i < parameters.size(); i++) {
                stmt.setString(i + 1, parameters.get(i));
            }

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Student student = new Student(
                            rs.getInt("masv"),
                            rs.getString("name"),
                            rs.getInt("age"),
                            rs.getString("gender"),
                            rs.getInt("major_id"),
                            rs.getInt("class_id"),
                            rs.getDouble("gpa"),
                            rs.getString("address")
                    );
                    students.add(student);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Lỗi cơ sở dữ liệu: " + e.getMessage(), e);
        }
        return students;
    }
}