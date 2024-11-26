package com.application.entity;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Class {
    private int id; // Mã lớp
    private String name; // Tên lớp

    // -------- Constructor ----------
    public Class(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // -------- Getters và Setters -------
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;  // Hiển thị tên lớp trong ComboBox
    }
}
