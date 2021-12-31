package com.faridcodeur.letschat.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.faridcodeur.letschat.R;

import java.io.File;
import java.util.ArrayList;

public class GridViewCustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<String> images;
    LayoutInflater layoutInflater;

    public GridViewCustomAdapter(Context applicationContext, ArrayList<String> images){
        this.context = applicationContext;
        this.images = images;
        layoutInflater = (LayoutInflater.from(applicationContext));
    }
    @Override
    public int getCount() {
        return images.size();
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
        File imgFile = new File(images.get(position));
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            imageView.setImageBitmap(myBitmap);
        }
        return convertView;
    }
}
