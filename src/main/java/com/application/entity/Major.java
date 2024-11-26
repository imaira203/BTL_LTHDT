package com.application.entity;

// Lớp Major đại diện cho một ngành học
public class Major {
    private int id; // Mã ngành
    private String name; // Tên ngành

    // ------------ Constructor -------------
    public Major(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // -------------- getter ----------------
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
