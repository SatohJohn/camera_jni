package com.example.hellojni;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class MovieActivity extends Activity {
    MovieView mv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Log.d("aaaaaaaaaaaa", "MovieActivity");
        int shotNumber = getIntent().getIntExtra("shotNumber", 0);
        mv =new MovieView(this, shotNumber);
        setContentView(mv);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //mv.movieFinsih();
        Intent intent = new Intent(this, CreateAlbumActivity.class);
        Log.d("aaaaaaaa", mv.getFullpath());
        intent.putExtra("fileName", mv.getFullpath());
        startActivity(intent);
        return true;
    }

}