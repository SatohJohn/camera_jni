package com.example.hellojni;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

public class MovieActivity extends Activity {
    MovieView mv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d("aaaaaaaaaaaa", "MovieActivity");
        int shotNumber = getIntent().getIntExtra("shotNumber", 0);
        setContentView(R.layout.movie_activity);
        mv = (MovieView) findViewById(R.id.movie_preview_id);
        mv.setShotNumber(shotNumber);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mv.movieFinish();
        Intent intent = new Intent(this, CreateAlbumActivity.class);
        Log.d("aaaaaaaa", mv.getFullPath());
        intent.putExtra("fileName", mv.getFullPath());
        startActivity(intent);
        return true;
    }

}