package com.example.hellojni.model;

/**
 * Created by satohjohn on 16/04/23.
 */
public class Album {
    public Video video;
    public Image image;
    public Album(Video video, Image image) {
        this.video = video;
        this.image = image;
    }

}
