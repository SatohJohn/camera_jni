package com.example.hellojni;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.example.hellojni.service.AlbumService;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by satohjohn on 16/04/23.
 */
public class CreateAlbumActivity extends Activity {

    AlbumService service = new AlbumService();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_album_activity);

        String fileName = getIntent().getStringExtra("fileName");
        Integer tokenImageNum = null;
        if (fileName != null) {
            tokenImageNum = service.getAlbumNum(fileName);
        }

        List<Album> albums = service.loadAlbum(Environment.getExternalStorageDirectory().getPath() + "/" + getString(R.string.directory_name));
        if (tokenImageNum != null) {
            albums.set(tokenImageNum, new Album(fileName));
        }

        List<LinearLayout> layouts = new ArrayList<>();
        layouts.add((LinearLayout) findViewById(R.id.content_create_album_layout_01));
        layouts.add((LinearLayout) findViewById(R.id.content_create_album_layout_02));
        layouts.add((LinearLayout) findViewById(R.id.content_create_album_layout_03));
        layouts.add((LinearLayout) findViewById(R.id.content_create_album_layout_05));
        layouts.add((LinearLayout) findViewById(R.id.content_create_album_layout_06));
        for (int layoutCount = 0; layoutCount < layouts.size(); layoutCount++) {
            View resource = createResource(albums.get(layoutCount));
            if (resource != null) {
                layouts.get(layoutCount).addView(resource);
            } else {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.camera);
                imageView.setOnClickListener(new CameraView(this, layoutCount));
                layouts.get(layoutCount).addView(imageView);
            }
        }
    }

    public static class CameraView implements View.OnClickListener {
        Context context;
        int viewNumber;
        CameraView(Context context, int viewNumber) {
            this.context = context;
            this.viewNumber = viewNumber;
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(context, HelloJni.class);
            intent.putExtra("shotNumber", viewNumber);
            context.startActivity(intent);
        }
    }

    private View createResource(Album album) {
        if (album.hasImage()) {
            ImageView imageView = new ImageView(this);
            File file = new File(album.path);
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
            videoView.setVideoPath(album.path);
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
