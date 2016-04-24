package com.example.hellojni.service;

import android.os.Environment;

import com.example.hellojni.model.Album;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by satohjohn on 16/04/24.
 */
public class AlbumService {
    private String moviewPath1 = Environment.getExternalStorageDirectory().getPath() + "/Download/test3.mov";
    private String imagePath1 = Environment.getExternalStorageDirectory().getPath() + "/Download/test1.png";
    private String imagePath2 = Environment.getExternalStorageDirectory().getPath() + "/Download/test2.png";

    /**
     * dirPathに存在するファイルを削除する
     * @param dirPath
     */
    public void cleanAlbum(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.exists()) {
            directory.mkdir();
        } else {
            if (!directory.isDirectory()) {
                throw new IllegalArgumentException("dirPath is not directory");
            }
            // fileのリスト
            for (File file :directory.listFiles()) {
                file.delete();
            }
        }
    }

    /**
     * dirPathに存在するファイルを取得してくる
     * @param dirPath
     * @return
     */
    public List<Album> loadAlbum(String dirPath) {
        File directory = new File(dirPath);
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException("dirPath is not directory");
        }
        // 5は決め打ち
        List<Album> albums = new ArrayList<>();
        albums.add(new Album(imagePath1));
        albums.add(new Album(imagePath2));
        albums.add(new Album(moviewPath1));
        albums.add(new Album(""));
        albums.add(new Album(""));
        if (!directory.exists()) {
            directory.mkdir();
        } else {
            // fileのリスト
            for (File file :directory.listFiles()) {
                if (albums.get(getAlbumNum(file.getName())).isNotHaveBoth()) {
                    albums.set(getAlbumNum(file.getName()), new Album(file.getPath()));
                }
            }
        }

        return albums;
    }

    public int getAlbumNum(String fileName) {
        String[] split = fileName.split("_");
        String s = split[split.length - 1];
        String substring = s.substring(0, 1);
        return Integer.parseInt(substring);
    }

}
