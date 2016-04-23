package com.example.hellojni;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.widget.ListView;
import android.widget.VideoView;

import com.example.hellojni.adapter.VideoListAdapter;
import com.example.hellojni.model.Video;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by satohjohn on 16/04/23.
 */
public class VideoViewActivity extends Activity {

    private String moviewPath = "/Movies/preload_xperia_hd2.mp4";
    private String moviewPath2 = "/Movies/preload_ps4_montage.mp4";
    List<Video> videos = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_view_activity);
//
//        videoView = (VideoView) findViewById(R.id.video_view_activity_video);
//        videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + moviewPath);
//        videoView.start();

        // TODO:Videoを組み立てる手段
        videos.add(new Video(moviewPath));
        videos.add(new Video(moviewPath2));
        ListView listView = (ListView) findViewById(R.id.video_view_activity_list);
        listView.setAdapter(new VideoListAdapter(this, 0, videos));
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
