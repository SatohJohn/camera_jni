package com.example.hellojni;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.hellojni.adapter.SelectAlbumDesignListAdapter;
import com.example.hellojni.model.Album;
import com.example.hellojni.model.Image;
import com.example.hellojni.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satohjohn on 16/04/23.
 */
public class SelectAlbumDesignActivity extends Activity {

    private String moviewPath = "/Movies/preload_xperia_hd2.mp4";
    private String moviewPath2 = "/Movies/preload_ps4_montage.mp4";
    List<Album> albums = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_album_design_activity);
//
//        videoView = (VideoView) findViewById(R.id.video_view_activity_video);
//        videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + moviewPath);
//        videoView.start();

        // TODO:Videoを組み立てる手段
        albums.add(new Album(new Video(moviewPath), new Image(null)));
        albums.add(new Album(new Video(moviewPath), new Image(null)));
        GridView gridView = (GridView) findViewById(R.id.select_album_design_activity_list);
        gridView.setAdapter(new SelectAlbumDesignListAdapter(this, 0, albums));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SelectAlbumDesignActivity.this, HelloJni.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStop() {
        super.onStop();
//        if (videoView != null) {
//            videoView.stop = new VideoView(this);
//        }

    }

    @Override
    public void onPause() {
        super.onPause();
    }


}
