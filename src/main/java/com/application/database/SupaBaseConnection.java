package com.application.database;

import com.application.entity.Student;
import com.application.entity.Class;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupaBaseConnection {
    private static final String DB_URL = "jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:6543/postgres?user=postgres.ofmkamppewhdxaecryag&password=Trankimcuong2003";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

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

    public static List<Student> loadStudents() {
        List<Student> students = new ArrayList<>();

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM sinhvien");
             ResultSet rs = stmt.executeQuery()) {

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

    public static boolean addStudent(Student student) {
        String query = "INSERT INTO sinhvien (masv, name, age, gender, major_id, class_id, gpa, address) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, student.getId());
            stmt.setString(2, student.getName());
            stmt.setInt(3, student.getAge());
            stmt.setString(4, student.getGender());
            stmt.setInt(5, student.getMajorId());
            stmt.setInt(6, student.getClassId());
            stmt.setDouble(7, student.getGpa());
            stmt.setString(8, student.getAddress());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Class> loadClassesByMajor(int majorId) {
        List<Class> classes = new ArrayList<>();
        String query = "SELECT * FROM class WHERE major_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, majorId);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Class cls = new Class(
                            rs.getInt("id"),    // Replace with actual column name for class ID
                            rs.getString("class_name") // Replace with actual column name for class name
                    );
                    classes.add(cls);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }


    public static boolean updateStudent(Student student) {
        String query = "UPDATE sinhvien SET name=?, age=?, gender=?, major_id=?, class_id=?, gpa=? WHERE masv=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, student.getName());
            stmt.setInt(2, student.getAge());
            stmt.setString(3, student.getGender());
            stmt.setInt(4, student.getMajorId());
            stmt.setInt(5, student.getClassId());
            stmt.setDouble(6, student.getGpa());
            stmt.setLong(7, student.getId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean deleteStudent(long studentId) {
        String query = "DELETE FROM sinhvien WHERE masv=?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, studentId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static List<Student> searchStudents(String name) {
        List<Student> students = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM sinhvien");
        List<String> parameters = new ArrayList<>();

        if (name != null && !name.trim().isEmpty()) {
            query.append(" WHERE name ILIKE ?"); // ILIKE == LIKE trong SQL
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
            e.printStackTrace();
        }
        return students;
    }
}