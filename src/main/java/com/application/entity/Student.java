package com.application.entity;

import javafx.beans.property.*;

public class Student {

    private LongProperty id;
    private StringProperty name;
    private IntegerProperty age;
    private StringProperty gender;
    private StringProperty address;
    private IntegerProperty majorId; // Mã ngành
    private StringProperty majorName; // Tên ngành
    private IntegerProperty classId;
    private StringProperty className;
    private DoubleProperty gpa;

    public Student(long id, String name, int age, String gender, int majorId, String majorName, String className, double gpa) {
        this.id = new SimpleLongProperty(id);
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.gender = new SimpleStringProperty(gender);
        this.majorId = new SimpleIntegerProperty(majorId);
        this.majorName = new SimpleStringProperty(majorName);
        this.className = new SimpleStringProperty(className);
        this.gpa = new SimpleDoubleProperty(gpa);
    }

    public Student(long id, String name, int age, String gender, int majorId, int classId, double gpa, String address ) {
        this.id = new SimpleLongProperty(id);
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.address = new SimpleStringProperty(address);
        this.gender = new SimpleStringProperty(gender);
        this.majorId = new SimpleIntegerProperty(majorId);
        this.classId = new SimpleIntegerProperty(classId);
        this.majorName = new SimpleStringProperty("");
        this.className = new SimpleStringProperty("");
        this.gpa = new SimpleDoubleProperty(gpa);
    }
    // Property methods
    public LongProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public IntegerProperty ageProperty() { return age; }
    public StringProperty addressProperty() { return address; }
    public StringProperty genderProperty() { return gender; }
    public StringProperty majorNameProperty() { return majorName; }
    public IntegerProperty majorIdProperty() { return majorId; }
    public IntegerProperty classIdProperty() { return classId; }
    public DoubleProperty gpaProperty() { return gpa; }

    // Getters and Setters
    public long getId() { return id.get(); } // Trả về mã SV
    public void setId(long id) { this.id.set(id); }

    public String getName() { return name.get(); } // Trả về tên sinh viên
    public void setName(String name) { this.name.set(name); }

    public int getAge() { return age.get(); } // Trả về tuổi
    public void setAge(int age) { this.age.set(age); }

    public String getGender() { return gender.get(); }
    public void setGender(String gender) { this.gender.set(gender); }

    public int getMajorId() { return majorId.get(); } // Trả về mã ngành
    public void setMajorId(int majorId) { this.majorId.set(majorId); }

    public String getMajorName() { return majorName.get(); } // Trả về tên ngành
    public void setMajorName(String majorName) { this.majorName.set(majorName); }

    public int getClassId() { return classId.get(); } // Trả về mã lớp
    public void setClassId(int classId) { this.classId.set(classId); }

    public String getClassName() { return className.get(); } // Trả về tên lớp
    public void setClassName(String className) { this.className.set(className); }


    public double getGpa() { return gpa.get(); } // Trả về GPA
    public void setGpa(double gpa) { this.gpa.set(gpa); }

    public String getAddress() { return address.get(); } // Trả về địa chỉ
    public void setAddress(String address) {this.address.set(address); }
}
