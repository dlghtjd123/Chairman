package com.example.myapplication;

public class Wheelchair {
    private String id;
    private String status;

    public Wheelchair(String id, String status) {
        this.id = id;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }
}
