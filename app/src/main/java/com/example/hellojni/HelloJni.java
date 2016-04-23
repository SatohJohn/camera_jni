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

    private static int PREVIEW_WIDTH = 640;
    private static int PREVIEW_HEIGHT = 480;
    private Camera mCamera = null;
    private SurfaceHolder mHolder = null;
    private SurfaceTexture mSurface = null;
    private int[] mGrayImg = null;
    private Bitmap mBitmap = null;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_jni_activity);
        SurfaceView preview = (SurfaceView) findViewById(R.id.preview_id);
        mHolder = preview.getHolder();
        mHolder.addCallback(this);
        initiaizeGraphics(PREVIEW_WIDTH, PREVIEW_HEIGHT);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        releaseGraphics();
    }

    @TargetApi(Build.VERSION_CODES.ECLAIR)
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mCamera = Camera.open();
        List<Camera.Size> sizeList = mCamera.getParameters().getSupportedPreviewSizes();
        Camera.Size bestSize = sizeList.get(0);
        for(int cameraSizeIndex = 1; cameraSizeIndex < sizeList.size(); cameraSizeIndex++){
            if((sizeList.get(cameraSizeIndex).width * sizeList.get(cameraSizeIndex).height) >
                    (bestSize.width * bestSize.height)){
                bestSize = sizeList.get(cameraSizeIndex);
            }
        }
        // グレースケル画像バッファ (NV21 -> ARGB_8888)
//        PREVIEW_WIDTH = surfaceHolder.getSurfaceFrame().width();
//        PREVIEW_HEIGHT = surfaceHolder.getSurfaceFrame().height();
        PREVIEW_WIDTH = bestSize.width;
        PREVIEW_HEIGHT = bestSize.height;
        mGrayImg = new int[PREVIEW_WIDTH * PREVIEW_HEIGHT];
        mBitmap = Bitmap.createBitmap(PREVIEW_WIDTH, PREVIEW_HEIGHT, Bitmap.Config.ARGB_8888);

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
        int p;

        for (int i=0; i<1000; ++i )
            mGrayImg[i] = 0xFF00FF00;

        yuvtoargb(mGrayImg, bytes, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        mBitmap.setPixels(mGrayImg, 0, PREVIEW_WIDTH, 0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);

        Canvas canvas = mHolder.lockCanvas();
        if (canvas != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
            mHolder.unlockCanvasAndPost(canvas);
        }
    }
}
