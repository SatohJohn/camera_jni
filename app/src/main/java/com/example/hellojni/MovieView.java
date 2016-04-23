package com.example.hellojni;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class MovieView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder surfaceholder;  //ホルダー
    private MediaRecorder recorder;//メディアレコーダー

    boolean mTouch = false;
    int     shotNumber = 0;
    String   mFullpath = "";

    //コンストラクタ
    public MovieView(Context context, int _shotNumber) {
        super(context);

        Log.d("aaaaaaaaaa", "movieView");
        //サーフェイスホルダーの生成
        surfaceholder=getHolder();
        surfaceholder.addCallback(this);

        //プッシュバッファの指定
        surfaceholder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        shotNumber = _shotNumber;

        mTouch = false;
        Log.d("aaaaaaaaaa", "movieViewFinish");
    }

    //サーフェイス生成イベントの処理
    public void surfaceCreated(SurfaceHolder surfaceholder) {
        try {
            Log.d("aaaaaaaaaa", "1");
            //録画の開始(3)
            recorder=new MediaRecorder();
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
            final String SAVE_DIR = "pashao";
            Log.d("aaaaaaaaaa", "2");
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/" + SAVE_DIR);
            try{
                if(!file.exists()){
                    file.mkdir();
                }
            }catch(SecurityException e){
                e.printStackTrace();
                throw e;
            }
            Log.d("aaaaaaaaaa", file.getAbsolutePath());
            Date mDate = new Date();
            SimpleDateFormat fileNameDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
            //File dir=Environment.getExternalStorageDirectory();
            String fileName = fileNameDate.format(mDate)  + "_" + shotNumber + ".mp4";
            String AttachName = file.getAbsolutePath() + "/" + fileName;

            Log.d("aaaaaaaaaa", AttachName);
            recorder.setOutputFile(AttachName);
            recorder.setPreviewDisplay(surfaceholder.getSurface());
            recorder.prepare();
            recorder.start();

            mFullpath = AttachName;
            Log.d("aaaaaaaaaa", "movie start");
        } catch (Exception e) {
            android.util.Log.e("",e.toString());
        }
    }

    public String getFullpath()
    {
        return mFullpath;
    }

        //サーフェイス変更イベントの処理
    public void surfaceChanged(SurfaceHolder surfaceholder,
                               int format,int w,int h) {
    }

    //サーフェイス解放イベントの処理
    public void surfaceDestroyed(SurfaceHolder surfaceholder) {
        try {
            Log.d("aaaaaaaaaa", "movie finish start!!");
            //録音の停止(4)
            recorder.stop();
            recorder.release();

            Log.d("aaaaaaaaaa", "movie finish!!");
        } catch (Exception e) {
        }
    }

    // 録画の中止
    public void movieFinsih()
    {
        try {
            //録音の停止(4)
            recorder.stop();
            recorder.release();
            Log.d("aaaaaaaaaa", "movie finish bb!!");
        } catch (Exception e) {
        }
    }
}
