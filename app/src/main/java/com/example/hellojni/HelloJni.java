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

import android.app.Activity;
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


public class HelloJni extends Activity implements SurfaceHolder.Callback, Camera.PreviewCallback
{

    private static int PREVIEW_WIDTH = 640;
    private static int PREVIEW_HEIGHT = 480;
    private Camera mCamera = null;
    private SurfaceHolder mHolder = null;
    private SurfaceTexture mSurface = null;
    private int[] mGrayImg = null;
    private Bitmap mBitmap = null;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // ステータスバーを非表示にしておく
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        /* Create a TextView and set its content.
         * the text is retrieved by calling a native
         * function.
         */
//        TextView  tv = new TextView(this);
//        tv.setText( stringFromJNI() );
//        setContentView(tv);

        setContentView(R.layout.activity_hello_jni_activity);

        SurfaceView preview = (SurfaceView) findViewById(R.id.preview_id);
        mHolder = preview.getHolder();
        mHolder.addCallback(this);
    }

    /* A native method that is implemented by the
     * 'hello-jni' native library, which is packaged
     * with this application.
     */
    public native void  stringFromJNIA(byte[] b);
    public native void  print(byte[] b);
    public native void yuvtoargb(byte[] b, int[] i, int width, int height);

    /* this is used to load the 'hello-jni' library on application
     * startup. The library has already been unpacked into
     * /data/data/com.example.hellojni/lib/libhello-jni.so at
     * installation time by the package manager.
     */
    static {
        System.loadLibrary("hello-jni");
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        // TODO Auto-generated method stub
        mCamera = Camera.open();
        // グレースケル画像バッファ (NV21 -> ARGB_8888)
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        // TODO Auto-generated method stub
        mCamera.stopPreview();
        // プレビュー画面のサイズ設定
        Camera.Parameters params = mCamera.getParameters();
        params.setPreviewSize(PREVIEW_WIDTH, PREVIEW_HEIGHT);
        mCamera.setParameters(params);
        // プレビュー開始
        mCamera.setPreviewCallback(this);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        // TODO Auto-generated method stub
        mCamera.stopPreview();
        mCamera.setPreviewCallback(null);
        mCamera.release();
        mCamera = null;
    }

    @Override
    public void onPreviewFrame(byte[] bytes, Camera camera) {
        // TODO Auto-generated method stub
        int p;

        print(bytes);
        stringFromJNIA(bytes);

        yuvtoargb(bytes, mGrayImg, PREVIEW_WIDTH, PREVIEW_HEIGHT);

        // グレースケル画像に変換
        for (int i = 0; i < mGrayImg.length; i++) {
            p = bytes[i] & 0xff;
            mGrayImg[i] = 0xff000000 | p << 16 | p << 8 | p;
        }
        mBitmap.setPixels(mGrayImg, 0, PREVIEW_WIDTH, 0, 0, PREVIEW_WIDTH, PREVIEW_HEIGHT);
        // 描画処理
        Canvas canvas = mHolder.lockCanvas();
        if (canvas != null) {
            canvas.drawBitmap(mBitmap, 0, 0, null);
            mHolder.unlockCanvasAndPost(canvas);
        }
    }
}
