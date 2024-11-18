package com.application.controller;

import com.application.database.DBHelper;
import com.application.database.SupaBaseConnection;
import com.application.services.*;
import com.application.entity.Major;
import com.application.entity.Class;
import com.application.entity.Student;
import com.application.utils.Util;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;


import java.util.List;

public class MainController {

    @FXML
    private TextField idField, studentNameField, ageField, addressField, gpaField;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private ComboBox<Major> majorComboBox;

    @FXML
    private ComboBox<Class> classComboBox;

    @FXML
    private Button searchStudentBtn, addStudentBtn, editStudentBtn, deleteStudentBtn;
    @FXML
    private TableView<Student> studentTable;
    @FXML
    private TableColumn<Student, String> idColumn, nameColumn, classColumn, addressColumn, ageColumn, genderColumn, majorColumn, gpaColumn;

    private ObservableList<Student> studentList;

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

        // Populate genderComboBox
        genderComboBox.setItems(FXCollections.observableArrayList("Nam", "Nữ"));

        // Lấy danh sách ngành học
        ObservableList<Major> majors = dbHelper.getMajors();
        majorComboBox.setItems(majors);

        // Lấy danh sách lớp học
        ObservableList<Class> classes = dbHelper.getClasses();
        classComboBox.setItems(classes);

        majorComboBox.setOnAction(event -> {
            Major selectedMajor = majorComboBox.getSelectionModel().getSelectedItem();
            if (selectedMajor != null) {
                int majorId = selectedMajor.getId();
                List<Class> filteredClasses = SupaBaseConnection.loadClassesByMajor(majorId);
                classComboBox.getItems().setAll(filteredClasses);
            }
        });

        // Init danh sách sinh viên
        studentList = FXCollections.observableArrayList();
        studentTable.setItems(studentList);

        // Load sinh viên
        loadStudentsFromDatabase();

        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                populateFields(newSelection);
            }
        });
    }

    private void loadStudentsFromDatabase() {
        try {
            studentList.clear();
            studentList.addAll(SupaBaseConnection.loadStudents());
        } catch (Exception e) {
            Instance.showAlert(AlertType.ERROR, "Database Error", "Không thể tải dữ liệu sinh viên: " + e.getMessage());
        }
    }

    private void populateFields(Student student) {
        idField.setText(String.valueOf(student.getId()));
        studentNameField.setText(student.getName());
        ageField.setText(String.valueOf(student.getAge()));
        genderComboBox.setValue(student.getGender());
        addressField.setText(student.getAddress());

        // Khởi tạo danh sách Ngành và Lớp để hiển thị
        ObservableList<Major> majors = majorComboBox.getItems();
        ObservableList<Class> classes = classComboBox.getItems();

        // Tìm đối tượng tương ứng trong danh sách và đặt đối tượng đó cho ComboBox
        Major selectedMajor = majors.stream()
                .filter(major -> major.getId() == student.getMajorId())
                .findFirst()
                .orElse(null);
        majorComboBox.setValue(selectedMajor);

        Class selectedClass = classes.stream()
                .filter(clazz -> clazz.getId() == student.getClassId())
                .findFirst()
                .orElse(null);
        // Đoạn trên không hiểu lắm :)))
        classComboBox.setValue(selectedClass);
        gpaField.setText(String.valueOf(student.getGpa()));
    }

    @FXML
    private void handleSearchStudent(ActionEvent event) {
        String name = studentNameField.getText();
        if (name.isEmpty()){
            Instance.showAlert(AlertType.ERROR, "Error", "Nhập tên sinh viên cần tìm");
            return;
        }
        try {
            studentList.clear();
            studentList.addAll(SupaBaseConnection.searchStudents(name));
        } catch (Exception e) {
            Instance.showAlert(AlertType.ERROR, "Search Error", "Lỗi tìm kiếm: " + e.getMessage());
        }
    }

    @FXML
    private void handleAddStudent(ActionEvent event) {
        if (!validateInput()) {
            return;
        }

        Student newStudent = createNewStudent();

        try {
            if (SupaBaseConnection.addStudent(newStudent)) {
                studentList.add(newStudent);
                clearFields();
                Instance.showAlert(AlertType.INFORMATION, "Success", "Thêm sinh viên thành công!");
            } else {
                Instance.showAlert(AlertType.ERROR, "Error", "Không thể thêm sinh viên.");
            }
        } catch (Exception e) {
            Instance.showAlert(AlertType.ERROR, "Database Error", "Lỗi thêm sinh viên: " + e.getMessage());
        }
    }

    @FXML
    private void handleEditStudent(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            Instance.showAlert(AlertType.ERROR, "Error", "Vui lòng chọn sinh viên cần sửa.");
            return;
        }

        if (!validateInput()) {
            return;
        }

        Student updatedStudent = createNewStudent();

        try {
            if (SupaBaseConnection.updateStudent(updatedStudent)) {
                int index = studentList.indexOf(selectedStudent);
                studentList.set(index, updatedStudent);
                clearFields();
                Instance.showAlert(AlertType.INFORMATION, "Success", "Cập nhật sinh viên thành công!");
            } else {
                Instance.showAlert(AlertType.ERROR, "Error", "Không thể cập nhật sinh viên.");
            }
        } catch (Exception e) {
            Instance.showAlert(AlertType.ERROR, "Database Error", "Lỗi cập nhật sinh viên: " + e.getMessage());
        }
    }

    @FXML
    private void handleDeleteStudent(ActionEvent event) {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            Instance.showAlert(AlertType.ERROR, "Error", "Vui lòng chọn sinh viên cần xóa.");
            return;
        }

        Alert confirmDialog = new Alert(AlertType.CONFIRMATION,
                "Bạn có chắc chắn muốn xóa sinh viên này?",
                ButtonType.YES, ButtonType.NO);

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                try {
                    if (SupaBaseConnection.deleteStudent(selectedStudent.getId())) {
                        studentList.remove(selectedStudent);
                        clearFields();
                        Instance.showAlert(AlertType.INFORMATION, "Success", "Xóa sinh viên thành công!");
                    } else {
                        Instance.showAlert(AlertType.ERROR, "Error", "Không thể xóa sinh viên.");
                    }
                } catch (Exception e) {
                    Instance.showAlert(AlertType.ERROR, "Database Error", "Lỗi xóa sinh viên: " + e.getMessage());
                }
            }
        });
    }

    // Kiểm tra tính hợp lệ của đầu vào
    private boolean validateInput() {
        if (!Util.isNumeric(idField.getText()) || !Util.isNumeric(gpaField.getText())) {
            Instance.showAlert(AlertType.ERROR, "Error", "Mã sinh viên và GPA phải là số");
            return false;
        }
        String gpaText = gpaField.getText().replace(",", ".");
        double gpa = Double.parseDouble(gpaText);
        if (gpa > 4) {
            Instance.showAlert(AlertType.ERROR, "Error", "GPA không thể lớn hơn 4");
            return false;
        }
        if (!Util.isNumeric(ageField.getText())){
            Instance.showAlert(AlertType.ERROR, "Error", "Tuổi phải là 1 số");
            return false;
        }
        if (idField.getText().isEmpty() ||
                studentNameField.getText().isEmpty() ||
                ageField.getText().isEmpty() ||
                genderComboBox.getValue() == null ||
                majorComboBox.getValue() == null ||
                classComboBox.getValue() == null ||
                gpaField.getText().isEmpty()) {

            Instance.showAlert(AlertType.ERROR, "Error", "Vui lòng nhập đủ thông tin");
            return false;
        }
        return true;
    }

    private Student createNewStudent() {
        Major selectedMajor = majorComboBox.getValue();
        Class selectedClass = classComboBox.getValue();
        return new Student(
                Long.parseLong(idField.getText()),
                studentNameField.getText(),
                Integer.parseInt(ageField.getText()),
                genderComboBox.getValue(),
                selectedMajor.getId(),
                selectedClass.getId(),
                Double.parseDouble(gpaField.getText()),
                addressField.getText()
                );
    }
    @FXML
    private void handleReloadTable(ActionEvent event) {
        initialize();
    }

    private void clearFields() {
        idField.clear();
        studentNameField.clear();
        ageField.clear();
        addressField.clear();
        gpaField.clear();
        classComboBox.setPromptText("Lớp");
        genderComboBox.setPromptText("Giới tính");
        majorComboBox.setPromptText("Ngành học");
    }
}
