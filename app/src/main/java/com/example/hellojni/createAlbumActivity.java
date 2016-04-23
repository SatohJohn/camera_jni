package com.example.hellojni;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.hellojni.adapter.VideoListAdapter;
import com.example.hellojni.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satohjohn on 16/04/23.
 */
public class CreateAlbumActivity extends Activity {

    private String moviewPath = "/Movies/preload_xperia_hd2.mp4";
    private String moviewPath2 = "/Movies/preload_ps4_montage.mp4";
    List<Video> videos = new ArrayList<>();

    Button camera;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_album_activity);
//
//        videoView = (VideoView) findViewById(R.id.video_view_activity_video);
//        videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + moviewPath);
//        videoView.start();

        // TODO:Videoを組み立てる手段
        videos.add(new Video(moviewPath));
        ListView listView = (ListView) findViewById(R.id.create_album_activity_list);
        listView.setAdapter(new VideoListAdapter(this, 0, videos));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CreateAlbumActivity.this, HelloJni.class);
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
