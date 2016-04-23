package com.example.hellojni.model;

/**
 * Created by satohjohn on 16/04/23.
 */
public class Video {
    public String path;

    public Video(String path) {
        this.path = path;
    }

    public boolean hasPath() {
        return path != null && path.length() > 0;
    }
}
