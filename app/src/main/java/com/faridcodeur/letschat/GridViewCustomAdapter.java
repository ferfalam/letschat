package com.faridcodeur.letschat;

import android.content.Context;
import android.media.Image;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridViewCustomAdapter extends BaseAdapter {
    Context context;
    int[] images;
    LayoutInflater layoutInflater;

    public GridViewCustomAdapter(Context applicationContext, int[] images){
        this.context = applicationContext;
        this.images = images;
        layoutInflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = layoutInflater.inflate(R.layout.gridview_layout, null);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.media_image);
        imageView.setImageResource(images[position]);
        return convertView;
    }
}
