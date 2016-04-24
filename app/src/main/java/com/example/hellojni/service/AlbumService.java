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
    private String moviewPath = Environment.getExternalStorageDirectory().getPath() + "/Movies/preload_xperia_hd2.mp4";
    private String imagePath = Environment.getExternalStorageDirectory().getPath() + "/image/alpha_amalfi_coast.jpg";

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
        albums.add(new Album(""));
        albums.add(new Album(""));
        albums.add(new Album(""));
        albums.add(new Album(""));
        albums.add(new Album(""));
        if (!directory.exists()) {
            directory.mkdir();
        } else {
            // fileのリスト
            for (File file :directory.listFiles()) {
                albums.set(getAlbumNum(file.getName()), new Album(file.getPath()));
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
