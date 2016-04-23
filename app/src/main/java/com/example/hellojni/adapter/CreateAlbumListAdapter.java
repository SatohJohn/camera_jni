package com.example.hellojni.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.hellojni.HelloJni;
import com.example.hellojni.R;
import com.example.hellojni.model.Album;
import com.example.hellojni.model.Video;

import java.util.List;

/**
 * データリストからリストビューを生み出すためのカスタムアダプター
 */
public class CreateAlbumListAdapter extends ArrayAdapter<Album> {

    LayoutInflater layoutInflater;

    public CreateAlbumListAdapter(Context context, int resource, List<Album> objects) {
        super(context, 0, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.template_create_album_list, null);
        }

        Album data = getItem(position);
        VideoView videoView = (VideoView) convertView.findViewById(R.id.video);
        videoView.setVideoPath(Environment.getExternalStorageDirectory().getPath() + data.video.path);
        MediaController mc = new MediaController(getContext());
        mc.setAnchorView(videoView);
        mc.setMediaPlayer(videoView);
        videoView.setOnPreparedListener(new MuteVideoAdapter());

        videoView.start();
        Button button = (Button) convertView.findViewById(R.id.template_video_camera);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), HelloJni.class);
                getContext().startActivity(intent);
            }
        });

        return convertView;
    }
}

