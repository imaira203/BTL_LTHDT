package com.application.entity;

// Lớp Major đại diện cho một ngành học
public class Major {
    private int id;
    private String name;

    public Major(int id, String name) {
        this.id = id;
        this.name = name;
    }

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
