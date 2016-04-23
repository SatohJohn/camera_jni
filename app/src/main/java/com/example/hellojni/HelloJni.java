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
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.os.Build;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;
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
    private int[] mScaleImg = null;
    private Bitmap mBitmap = null;

    private long mTimestart = System.currentTimeMillis();

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_jni_activity);
        SurfaceView preview = (SurfaceView) findViewById(R.id.preview_id);
        mHolder = preview.getHolder();
        mHolder.addCallback(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseGraphics();
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
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
        mScaleImg  = new int[DISP_WIDTH * DISP_HEIGHT];
        mBitmap = Bitmap.createBitmap(DISP_WIDTH, DISP_HEIGHT, Bitmap.Config.ARGB_8888);
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
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        yuvtoargb(mGrayImg, bytes, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        updateFrame( mGrayImg, bytes );

        for (int y = 0; y < DISP_HEIGHT; y++)
        {
            int p_y = y * PREVIEW_HEIGHT / DISP_HEIGHT;
            for (int x = 0; x < DISP_WIDTH; x++){
                int p_x = x * PREVIEW_WIDTH / DISP_WIDTH;
                mScaleImg[y * DISP_WIDTH + x] = mGrayImg[p_y * PREVIEW_WIDTH + p_x];
            }
        }

        //mBitmap.setPixels(mGrayImg, 0, DISP_WIDTH, 0, 0, DISP_WIDTH, DISP_HEIGHT);
        mBitmap.setPixels(mScaleImg, 0, DISP_WIDTH, 0, 0, DISP_WIDTH, DISP_HEIGHT);

        Canvas canvas = mHolder.lockCanvas();
        if (canvas != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
            mHolder.unlockCanvasAndPost(canvas);
        }
        long end = System.currentTimeMillis();
        Log.d(getClass().getName(), "Time: " + (end - mTimestart));
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
