package com.example.hellojni;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageButton;

import com.example.hellojni.service.AlbumService;

/**
 * Created by satohjohn on 16/04/23.
 */
public class MainActivity extends Activity {

    ImageButton showAlbumButton;
    ImageButton createAlbumButton;

    AlbumService service = new AlbumService();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        showAlbumButton = (ImageButton) findViewById(R.id.main_activity_create_album);
        createAlbumButton = (ImageButton) findViewById(R.id.main_activity_select_album_design);

        // folderがあるかないかを見る
        service.cleanAlbum(Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.directory_name));

        createAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SelectAlbumDesignActivity.class);
                startActivity(intent);
            }
        });
        showAlbumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, CreateAlbumActivity.class);
                startActivity(intent);
            }
        });

    }
}
