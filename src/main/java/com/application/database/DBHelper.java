package com.application.database;

import com.application.entity.Major;
import com.application.entity.Class;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

import static com.application.database.SupaBaseConnection.getConnection;

public class DBHelper {
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
}
