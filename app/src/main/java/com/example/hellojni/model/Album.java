package com.example.hellojni.model;

/**
 * Created by satohjohn on 16/04/23.
 */
public class Album {
    public String path;

    public Album(String path) {
        if (path == null) {
            throw new IllegalArgumentException("path is null");
        }
        this.path = path;
    }

    public boolean isNotHaveBoth() {
        return !hasVideo() && !hasImage();
    }

    public boolean hasVideo() {
        String suffix = getSuffix(path);
        return suffix.equals("mp4") || suffix.equals("mpeg");
    }

    public boolean hasImage() {
        String suffix = getSuffix(path);
        return suffix.equals("png") || suffix.equals("jpg");
    }

    private String getSuffix(String fileName) {
        int point = fileName.lastIndexOf(".");
        if (point != -1) {
            return fileName.substring(point + 1);
        }
        return "";
    }
}
