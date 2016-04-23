package com.example.hellojni;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by satohjohn on 16/04/23.
 */
public class MainActivity extends Activity {

    ImageButton showAlbumButton;
    ImageButton createAlbumButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        showAlbumButton = (ImageButton) findViewById(R.id.main_activity_show_album);
        createAlbumButton = (ImageButton) findViewById(R.id.main_activity_create_album);

        createAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateAlbumActivity.class);
                startActivity(intent);
            }
        });
        showAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ShowAlbumActivity.class);
                startActivity(intent);
            }
        });

    }
}
