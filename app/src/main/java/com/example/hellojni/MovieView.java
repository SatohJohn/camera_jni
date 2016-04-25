package com.example.hellojni;

import android.content.Context;
import android.media.MediaRecorder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.hellojni.service.FileService;

public class MovieView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceholder;
    private MediaRecorder recorder;

    FileService fileService = new FileService();
    boolean isTouch = false;
    int shotNumber = 0;
    String fullPath = "";

    //コンストラクタ
    public MovieView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        Log.d("aaaaaaaaaa", "movieView");
        //サーフェイスホルダーの生成
        surfaceholder = getHolder();
        surfaceholder.addCallback(this);

        //プッシュバッファの指定
        surfaceholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        isTouch = false;
        Log.d("aaaaaaaaaa", "movieViewFinish");
    }

    //サーフェイス生成イベントの処理
    @Override
    public void surfaceCreated(SurfaceHolder surfaceholder) {
        recorder = new MediaRecorder();
        recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);

        fileService.createDirectoryIfNotExist();
        String fileName = fileService.createMovieFileName(shotNumber);
        String AttachName = fileService.getSaveDir() + fileName;

        recorder.setOutputFile(AttachName);
        recorder.setPreviewDisplay(surfaceholder.getSurface());
        Log.d("aaaaaaaaaa", AttachName);
        try {
            //録画の開始(3)
            recorder.prepare();
            recorder.start();
            Log.d("aaaaaaaaaa", "movie start");
        } catch (Exception e) {
            Log.e("movie", e.toString());
        }
        fullPath = AttachName;
    }

    public String getFullPath() {
        return fullPath;
    }

    //サーフェイス変更イベントの処理
    @Override
    public void surfaceChanged(SurfaceHolder surfaceholder, int format, int w, int h) {
    }

    //サーフェイス解放イベントの処理
    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        Log.d("aaaaaaaaaa", "movie finish start!!");
        movieFinish();
        Log.d("aaaaaaaaaa", "movie finish!!");
    }

    // 録画の中止
    public void movieFinish() {
        if (recorder != null) {
            //録音の停止(4)
            recorder.stop();
            recorder.release();
            recorder = null;
        }
    }

    void setShotNumber(int shotNumber) {
        this.shotNumber = shotNumber;
    }

}
