package com.example.hellojni;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
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
            View resource = createResource(albums.get(layoutCount), layouts.get(layoutCount));
            if (resource != null) {
//                layouts.get(layoutCount).removeAllViews();
                layouts.get(layoutCount).addView(resource);
            } else {
                ImageView imageView = new ImageView(this);
                imageView.setImageResource(R.drawable.camera_icon_02);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                imageView.setOnClickListener(new CameraView(this, layoutCount));
                imageView.setBackgroundResource(R.drawable.create_album_bgi_01);
                layouts.get(layoutCount).addView(imageView);
            }
        }
//        RelativeLayout relative = (RelativeLayout) findViewById(R.id.relative);
//        View resource1 = createResource(albums.get(0));
//        relative.addView(resource1);
//        resource1.requestLayout();
//        ViewGroup.LayoutParams lp = resource1.getLayoutParams();
//        ViewGroup.MarginLayoutParams param = (ViewGroup.MarginLayoutParams) lp;
//        param.setMargins(10, 10, 10, 10);
//        resource1.setLayoutParams(param);
//
//        for (int layoutCount = 0; layoutCount < albums.size(); layoutCount++) {
//            View resource = createResource(albums.get(layoutCount), albums.get(layoutCount));
//            if (resource != null) {
//                layouts.get(layoutCount).addView(resource);
//            } else {
//                ImageView imageView = new ImageView(this);
//                imageView.setImageResource(R.drawable.camera_icon_02);
//                imageView.setScaleType(ImageView.ScaleType.CENTER);
//                imageView.setOnClickListener(new CameraView(this, layoutCount));
//                imageView.setBackgroundResource(R.drawable.create_album_bgi_01);
//                layouts.get(layoutCount).addView(imageView);
//            }
//        }

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
            if (viewNumber == 4) {
                Intent intent = new Intent(context, MovieActivity.class);
                intent.putExtra("shotNumber", viewNumber);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, HelloJni.class);
                intent.putExtra("shotNumber", viewNumber);
                context.startActivity(intent);
            }
        }
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private View createResource(Album album, LinearLayout layout) {
        if (album.hasImage()) {

            ImageView imageView = new ImageView(this);
//            layout.addView(imageView);
            File file = new File(album.path);
            Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
//            bitmap = bitmap.createScaledBitmap(bitmap, 1000, 1000, false);
            imageView.setImageBitmap(bitmap);

//            imageView.setRotation(90.0f);
//
            return imageView;
        } else if (album.hasVideo()) {
            VideoView videoView = new VideoView(this);
//            layout.addView(videoView);
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
