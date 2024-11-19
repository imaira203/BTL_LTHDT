package com.application.database;

import com.application.entity.Major;
import com.application.entity.Class;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static com.application.database.SupaBaseConnection.getConnection;

public class DBHelper {
    private Map<Integer, String> majorMap = new HashMap<>();
    private Map<Integer, String> classMap = new HashMap<>();

    public DBHelper() {
        loadMajorMap();
        loadClassMap();
    }
    // Hàm để lấy danh sách ngành từ cơ sở dữ liệu
    public ObservableList<Major> getMajors() {
        ObservableList<Major> majors = FXCollections.observableArrayList();

        try (Connection connection = getConnection()) {
            String sql = "SELECT id, name FROM major"; // Truy vấn lấy mã ngành và tên ngành từ bảng major
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                majors.add(new Major(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return majors;
    }

    public ObservableList<Class> getClasses() {
        ObservableList<Class> classes = FXCollections.observableArrayList();
        // Thực hiện truy vấn để lấy các Class từ cơ sở dữ liệu
        try (Connection connection = getConnection()) {
            String sql = "SELECT id, class_name FROM class"; // Truy vấn lấy mã ngành và tên ngành từ bảng major
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("class_name");
                classes.add(new Class(id, name));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return classes;
    }

    // Lấy thông tin từ bảng major
    private void loadMajorMap() {
        String query = "SELECT id, name FROM major";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                majorMap.put(resultSet.getInt("id"), resultSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Tải thông tin lớp học vào classMap
    private void loadClassMap() {
        String query = "SELECT id, class_name FROM class";
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                classMap.put(resultSet.getInt("id"), resultSet.getString("class_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Truy cập tên ngành qua ID
    public String getMajorNameById(int majorId) {
        return majorMap.getOrDefault(majorId, "Unknown");
    }

    // Truy cập tên lớp qua ID
    public String getClassNameById(int classId) {
        return classMap.getOrDefault(classId, "Unknown");
    }
}
