package com.application.controller;

import com.application.database.DBHelper;
import com.application.database.SupaBaseConnection;
import com.application.entity.Class;
import com.application.entity.Major;
import com.application.entity.Student;
import com.application.services.Instance;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class RecoverController {
    @FXML
    private TextField studentNameField;

    @FXML
    private Button backMainScene, recoverStudent, fullDeleteStudent, searchDeletedStudent, reloadTable;

    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> idColumn, nameColumn, classColumn, addressColumn, ageColumn, genderColumn, majorColumn, gpaColumn;

    private ObservableList<Student> studentList;

    @FXML
    public void handleBackScene (ActionEvent event) {
        changeToMainScene();
    }

    @FXML
    public void handleReloadTable (ActionEvent event){
        initialize();
    }

    @FXML
    public void handleSearchDeletedStudent (ActionEvent event) {
        String name = studentNameField.getText();
        if (name.isEmpty()){
            Instance.showAlert(Alert.AlertType.ERROR, "Error", "Nhập tên sinh viên cần tìm");
            return;
        }
        try {
            studentList.clear();
            studentList.addAll(SupaBaseConnection.searchDeletedStudent(name));
        } catch (Exception e) {
            Instance.showAlert(Alert.AlertType.ERROR, "Search Error", "Lỗi tìm kiếm: " + e.getMessage());
        }
    }

    @FXML
    // Xoá vĩnh viễn sinh viên
    public void handleFullDeleteStudent (ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            Instance.showAlert(Alert.AlertType.ERROR, "Error", "Vui lòng chọn sinh viên cần xoá.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc chắn muốn xoá vĩnh viễn sinh viên này?",
                ButtonType.YES, ButtonType.NO);

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    if (SupaBaseConnection.fullDeleteStudent(selectedStudent.getId())) {
                        studentList.remove(selectedStudent);
                        Instance.showAlert(Alert.AlertType.INFORMATION, "Success", "Xoá vĩnh viễn sinh viên thành công!");
                    } else {
                        Instance.showAlert(Alert.AlertType.ERROR, "Error", "Không thể xoá sinh viên.");
                    }
                } catch (Exception e) {
                    Instance.showAlert(Alert.AlertType.ERROR, "Database Error", "Lỗi xoá sinh viên: " + e.getMessage());
                }
            }
        });
    }

    private void changeToMainScene() {
        try {
            // Chuyển sang mainScene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/application/main/mainScene.fxml"));
            Scene mainScene = new Scene(loader.load());
            Stage stage = (Stage) backMainScene.getScene().getWindow();

            // Load scene mới
            stage.setScene(mainScene);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load main scene");
        }
    }

    @FXML
    public void initialize() {
        DBHelper dbHelper = new DBHelper();

        // Thiết lập các cột trong TableView
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asString());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        classColumn.setCellValueFactory(cellData -> {
            int classId = cellData.getValue().getClassId();
            String className = dbHelper.getClassNameById(classId); // Lấy tên lớp từ Map
            return new SimpleStringProperty(className);
        });
        ageColumn.setCellValueFactory(cellData -> cellData.getValue().ageProperty().asString());
        addressColumn.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        genderColumn.setCellValueFactory(cellData -> cellData.getValue().genderProperty());
        majorColumn.setCellValueFactory(cellData -> {
            int majorId = cellData.getValue().getMajorId();
            String majorName = dbHelper.getMajorNameById(majorId); // Lấy tên ngành từ Map
            return new SimpleStringProperty(majorName);
        });
        gpaColumn.setCellValueFactory(cellData -> cellData.getValue().gpaProperty().asString());

        // Init danh sách sinh viên
        studentList = FXCollections.observableArrayList();
        studentTable.setItems(studentList);

        // Load sinh viên
        loadStudentsFromDatabase();
    }
    private void loadStudentsFromDatabase() {
        try {
            studentList.clear();
            studentList.addAll(SupaBaseConnection.loadDeletedStudents());
        } catch (Exception e) {
            Instance.showAlert(Alert.AlertType.ERROR, "Database Error", "Không thể tải dữ liệu sinh viên: " + e.getMessage());
        }
    }

    @FXML
    private void handleRecoverStudent(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            Instance.showAlert(Alert.AlertType.ERROR, "Error", "Vui lòng chọn sinh viên cần khôi phục.");
            return;
        }

        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION,
                "Bạn có chắc chắn muốn khôi phục sinh viên này?",
                ButtonType.YES, ButtonType.NO);

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    if (SupaBaseConnection.recoverStudent(selectedStudent.getId())) {
                        studentList.remove(selectedStudent);
                        Instance.showAlert(Alert.AlertType.INFORMATION, "Success", "Khôi phục sinh viên thành công!");
                    } else {
                        Instance.showAlert(Alert.AlertType.ERROR, "Error", "Không thể khôi phục sinh viên.");
                    }
                } catch (Exception e) {
                    Instance.showAlert(Alert.AlertType.ERROR, "Database Error", "Lỗi khôi phục sinh viên: " + e.getMessage());
                }
            }
        });
    }
}
