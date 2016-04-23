package com.example.hellojni.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.hellojni.HelloJni;
import com.example.hellojni.R;
import com.example.hellojni.model.Album;

import java.util.List;

/**
 * データリストからリストビューを生み出すためのカスタムアダプター
 */
public class SelectAlbumDesignListAdapter extends ArrayAdapter<String> {

    LayoutInflater layoutInflater;

    public SelectAlbumDesignListAdapter(Context context, int resource, List<String> objects) {
        super(context, 0, objects);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.template_select_album_design_list, null);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
        imageView.setImageResource(R.drawable.design_01);
        if (position % 2 == 0) {
            imageView.setBackgroundResource(R.drawable.left);
        } else {
            imageView.setBackgroundResource(R.drawable.right);
        }

        return convertView;
    }
}

