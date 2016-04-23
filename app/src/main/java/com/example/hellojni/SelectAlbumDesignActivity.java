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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_album_design_activity);

        GridView gridView = (GridView) findViewById(R.id.select_album_design_activity_list);
        List<String> list = new ArrayList<>();
        list.add("design01");
        list.add("design02");
        list.add("design03");
        list.add("design04");
        list.add("design05");
        list.add("design06");
        gridView.setAdapter(new SelectAlbumDesignListAdapter(this, 0, list));

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Intent intent = new Intent(SelectAlbumDesignActivity.this, CreateAlbumActivity.class);
                intent.putExtra("designNumber", position);
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
