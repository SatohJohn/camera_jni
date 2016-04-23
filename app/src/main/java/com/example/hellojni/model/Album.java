package com.example.hellojni.model;

/**
 * Created by satohjohn on 16/04/23.
 */
public class Album {
    public Video video;
    public Image image;

    public Album(Video video, Image image) {
        if (video == null) {
            throw new IllegalArgumentException("video is not found");
        }
        if (image == null) {
            throw new IllegalArgumentException("image is not found");
        }
        this.video = video;
        this.image = image;
        if (video.hasPath() && image.hasPath()) {
            throw new IllegalArgumentException("album canot have video and image");
        }
    }

    public boolean isNotHaveBoth() {
        return !video.hasPath() && !image.hasPath();
    }

    public boolean hasVideo() {
        return video.hasPath();
    }

    public boolean hasImage() {
        return image.hasPath();
    }
}
