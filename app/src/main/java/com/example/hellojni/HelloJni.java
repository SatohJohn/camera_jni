/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.hellojni;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class HelloJni extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback
{
    static{
        System.loadLibrary("hello-jni");
    }
    private native void yuvtoargb(int[] out,byte[] in,  int width, int height);
    private native void initiaizeGraphics(int width, int height);
    private native void releaseGraphics();
    private native void updateFrame(int[] rgb, byte[] yuv);

    private static int PREVIEW_WIDTH  = 640;
    private static int PREVIEW_HEIGHT = 480;
    private static int DISP_WIDTH     = 1920;
    private static int DISP_HEIGHT    = 1340;
    private Camera mCamera = null;
    private SurfaceHolder mHolder = null;
    private SurfaceTexture mSurface = null;
    private int[] mGrayImg = null;
    private Bitmap mBitmap = null;

    private long mTimestart = System.currentTimeMillis();
    private boolean mTouch = false;
    private int shotNumber = 0;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_jni_activity);
        SurfaceView preview = (SurfaceView) findViewById(R.id.preview_id);
        mHolder = preview.getHolder();
        mHolder.addCallback(this);
        mTouch = false;
        shotNumber = getIntent().getIntExtra("shotNumber", 0);
        ImageView imageView = (ImageView) findViewById(R.id.movie);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HelloJni.this, MovieActivity.class);
                intent.putExtra("shotNumber", shotNumber);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d("TouchEvent", "X:" + event.getX() + ",Y:" + event.getY());
        if (mTouch == true)
        {
            return false;
        }
        mTouch = true;
        final String SAVE_DIR = getString(R.string.directory_name);
        File file = new File(Environment.getExternalStorageDirectory().getPath()  + "/" + SAVE_DIR);
        try{
            if(!file.exists()){
                file.mkdir();
            }
        }catch(SecurityException e){
            e.printStackTrace();
            throw e;
        }

        Date mDate = new Date();
        SimpleDateFormat fileNameDate = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String fileName = fileNameDate.format(mDate) + "_" + shotNumber + ".png";
        String AttachName = file.getAbsolutePath() + "/" + fileName;
        Log.d("aaaaaaaaa", AttachName);

        try {
            FileOutputStream out = new FileOutputStream(AttachName);
            mBitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
            Log.d("aaaaaaaaa", "save to file");
        } catch(IOException e) {
            e.printStackTrace();
        }

        ContentValues values = new ContentValues();
        ContentResolver contentResolver = getContentResolver();
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.TITLE, fileName);
        values.put("_data", AttachName);
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(this, CreateAlbumActivity.class);
        intent.putExtra("fileName", AttachName);
        startActivity(intent);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseGraphics();
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (mTouch == true)
        {
            return;
        }

        int cameraId = 0;
        mCamera = Camera.open(cameraId);
        // ディスプレイの向き設定
        //setCameraDisplayOrientation(cameraId);

        List<Camera.Size> sizeList = mCamera.getParameters().getSupportedPreviewSizes();
        Camera.Size bestSize = sizeList.get(0);
        for(int cameraSizeIndex = 1; cameraSizeIndex < sizeList.size(); cameraSizeIndex++){
            Log.d("aaaaaaaaa", "index = " + cameraSizeIndex  +  ", "+ sizeList.get(cameraSizeIndex).width + "," + sizeList.get(cameraSizeIndex).height);
            if((sizeList.get(cameraSizeIndex).width * sizeList.get(cameraSizeIndex).height) >
                    (bestSize.width * bestSize.height)){
                bestSize = sizeList.get(cameraSizeIndex);
            }
        }

        mGrayImg   = new int[PREVIEW_WIDTH * PREVIEW_HEIGHT];
        mBitmap    = Bitmap.createBitmap(DISP_WIDTH, DISP_HEIGHT, Bitmap.Config.ARGB_8888);
        initiaizeGraphics(PREVIEW_WIDTH, PREVIEW_HEIGHT);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { // API Level 11以上
                mSurface = new SurfaceTexture(0);
                mCamera.setPreviewTexture(mSurface);
            } else {
                mCamera.setPreviewDisplay(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int indent, int i1, int i2) {
        mCamera.stopPreview();
        // プレビュー画面のサイズ設定
        Camera.Parameters params = mCamera.getParameters();
//        List<Camera.Size> sizeList = mCamera.getParameters().getSupportedPreviewSizes();
//        Camera.Size bestSize = sizeList.get(0);
//        for(int cameraSizeIndex = 1; cameraSizeIndex < sizeList.size(); cameraSizeIndex++){
//            if((sizeList.get(cameraSizeIndex).width * sizeList.get(cameraSizeIndex).height) >
//                    (bestSize.width * bestSize.height)){
//                bestSize = sizeList.get(cameraSizeIndex);
//            }
//        }

        params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);

        mCamera.setParameters(params);
        // プレビュー開始
        mCamera.setPreviewCallback(this);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mCamera.stopPreview();
        mCamera.setPreviewCallback(null);
        mCamera.release();
        mCamera = null;
        releaseGraphics();
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        yuvtoargb(mGrayImg, bytes, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        updateFrame( mGrayImg, bytes );

        mBitmap.setPixels(mGrayImg, 0, PREVIEW_WIDTH, 0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);

        Canvas canvas = mHolder.lockCanvas();
        if (canvas != null) {
            canvas.drawBitmap(mBitmap, new Rect(0,0,PREVIEW_WIDTH,PREVIEW_HEIGHT), new Rect(0,0,DISP_WIDTH,DISP_HEIGHT), null);
            mHolder.unlockCanvasAndPost(canvas);
        }
        long end = System.currentTimeMillis();
        //Log.d(getClass().getName(), "Time: " + (end - mTimestart));
        mTimestart = end;
    }


    // ディスプレイの向き設定
    public void setCameraDisplayOrientation(int cameraId) {
    // カメラの情報取得
    Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
    Camera.getCameraInfo(cameraId, cameraInfo);
    // ディスプレイの向き取得
    int rotation = getWindowManager().getDefaultDisplay().getRotation();
    int degrees = 0;
    switch (rotation) {
        case Surface.ROTATION_0:
            degrees = 0; break;
        case Surface.ROTATION_90:
            degrees = 90; break;
        case Surface.ROTATION_180:
            degrees = 180; break;
        case Surface.ROTATION_270:
            degrees = 270; break;
        }
    // プレビューの向き計算
    int result;
    if (cameraInfo.facing == cameraInfo.CAMERA_FACING_FRONT) {
        result = (cameraInfo.orientation + degrees) % 360;
        result = (360 - result) % 360; // compensate the mirror
        }
    else {// back-facing
        result = (cameraInfo.orientation - degrees + 360) % 360;
        }
    // ディスプレイの向き設定
    mCamera.setDisplayOrientation(result);
    }
}
