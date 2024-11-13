package com.application.entity;

import javafx.beans.property.*;

public class Student {

    private LongProperty id;
    private StringProperty name;
    private IntegerProperty age;
    private StringProperty gender;
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
        this.majorName = new SimpleStringProperty(majorName); // Initializes majorName
        this.className = new SimpleStringProperty(className);  // Initializes className
        this.gpa = new SimpleDoubleProperty(gpa);
    }

    public Student(long id, String name, int age, String gender, int majorId, int classId, double gpa) {
        this.id = new SimpleLongProperty(id);
        this.name = new SimpleStringProperty(name);
        this.age = new SimpleIntegerProperty(age);
        this.gender = new SimpleStringProperty(gender);
        this.majorId = new SimpleIntegerProperty(majorId);
        this.classId = new SimpleIntegerProperty(classId);
        this.majorName = new SimpleStringProperty(""); // Initializes majorName to an empty string
        this.className = new SimpleStringProperty(""); // Initializes className to an empty string
        this.gpa = new SimpleDoubleProperty(gpa);
    }
    // Property methods
    public LongProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public IntegerProperty ageProperty() { return age; }
    public StringProperty genderProperty() { return gender; }
    public IntegerProperty majorIdProperty() { return majorId; }
    public IntegerProperty classIdProperty() { return classId; }
    public DoubleProperty gpaProperty() { return gpa; }

    // Getters and Setters
    public long getId() { return id.get(); }
    public void setId(long id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public int getAge() { return age.get(); }
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


    public double getGpa() { return gpa.get(); }
    public void setGpa(double gpa) { this.gpa.set(gpa); }

}
