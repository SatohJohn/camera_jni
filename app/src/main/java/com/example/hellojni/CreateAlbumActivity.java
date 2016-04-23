package com.example.hellojni;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.example.hellojni.adapter.CreateAlbumListAdapter;
import com.example.hellojni.model.Album;
import com.example.hellojni.model.Image;
import com.example.hellojni.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satohjohn on 16/04/23.
 */
public class CreateAlbumActivity extends Activity {

    private String moviewPath = "/Movies/preload_xperia_hd2.mp4";
    private String moviewPath2 = "/Movies/preload_ps4_montage.mp4";
    List<Album> albums = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_album_activity);

        // TODO:Videoを組み立てる手段
        albums.add(new Album(new Video(moviewPath2), new Image("")));
        GridView gridView = (GridView) findViewById(R.id.create_album_activity_list);
        gridView.setAdapter(new CreateAlbumListAdapter(this, 0, albums));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}