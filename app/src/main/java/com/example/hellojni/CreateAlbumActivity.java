package com.example.hellojni;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.hellojni.adapter.MuteVideoAdapter;
import com.example.hellojni.model.Album;
import com.example.hellojni.model.Image;
import com.example.hellojni.model.Video;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by satohjohn on 16/04/23.
 */
public class CreateAlbumActivity extends Activity {

    private String moviewPath = "/Movies/preload_xperia_hd2.mp4";
    private String imagePath = "/image/alpha_amalfi_coast.jpg";

    LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_album_activity);

        // TODO:Videoを組み立てる手段
        List<Album> albums = new ArrayList<>();
        {
            albums.add(new Album(new Video(moviewPath), new Image("")));
            albums.add(new Album(new Video(""), new Image(imagePath)));
            albums.add(new Album(new Video(""), new Image("")));
        }

        // folderがあるかないかを見る
        String.valueOf(R.string.directory_name);

        linearLayout = (LinearLayout) findViewById(R.id.content_create_album_layout_01);
        View resource = createResource(albums.get(0));
        if (resource != null) {
            linearLayout.addView(resource);
        } else {
            linearLayout.setBackgroundResource(R.drawable.select_album_design_01_bgi);
        }
        linearLayout = (LinearLayout) findViewById(R.id.content_create_album_layout_02);
        resource = createResource(albums.get(1));
        if (resource != null) {
            linearLayout.addView(resource);
        } else {
            linearLayout.setBackgroundResource(R.drawable.select_album_design_01_bgi);
        }
        linearLayout = (LinearLayout) findViewById(R.id.content_create_album_layout_03);
        resource = createResource(albums.get(2));
        if (resource != null) {
            linearLayout.addView(resource);
        } else {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.select_album_design_01_bgi);
            linearLayout.addView(imageView);
        }
    }

    private View createResource(Album album) {
        if (album.hasImage()) {
            ImageView imageView = new ImageView(this);
            File file = new File(Environment.getExternalStorageDirectory().getPath() + album.image.path);
            if (file.exists()) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getPath()));
            }
            return imageView;
        } else if (album.hasVideo()) {
            VideoView videoView = new VideoView(this);
            MediaController mc = new MediaController(this);
            mc.setAnchorView(videoView);
            mc.setMediaPlayer(videoView);
            videoView.setOnPreparedListener(new MuteVideoAdapter());
            videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + album.video.path);
            videoView.start();
            return videoView;
        }
        return null;
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
