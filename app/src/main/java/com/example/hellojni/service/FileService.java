package com.example.hellojni.service;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by SatohJohn on 2016/04/25.
 */
public class FileService {

    private static final String SAVE_DIR = "pashao";

	/**
     * ディレクトリが作られていなければ作成する
     */
    public void createDirectoryIfNotExist() {
        File file = new File(getSaveDir());
        try {
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (SecurityException e) {
            e.printStackTrace();
            throw e;
        }
    }

	/**
     * directoryなので最後は/で終わります
     * @return
     */
    public String getSaveDir() {
        return Environment.getExternalStorageDirectory().getPath() + "/" + SAVE_DIR + "/";
    }

	/**
     * 動画のファイル名を取得する
     * @param shotNumber
     * @return
     */
    public String createMovieFileName(int shotNumber) {
        Date mDate = new Date();
        SimpleDateFormat fileNameDate = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN);
        return fileNameDate.format(mDate) + "_" + shotNumber + ".mp4";
    }

	/**
     * 画像のファイル名を取得する
     * @param shotNumber
     * @return
     */
    public String createImageFileName(int shotNumber) {
        Date mDate = new Date();
        SimpleDateFormat fileNameDate = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.JAPAN);
        return fileNameDate.format(mDate) + "_" + shotNumber + ".png";
    }
}
