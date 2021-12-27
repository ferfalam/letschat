package com.faridcodeur.letschat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.entities.Setting;

import java.util.List;

public class ListAdapterSetting extends ArrayAdapter<Setting> {
    final List<Setting> settings;
    final Context context;

    public ListAdapterSetting(Context context, List<Setting> settings) {
        super(context, 0, settings);
        this.settings = settings;
        this.context = context;
    }

    @Override
    public int getCount() {
        return settings.size();
    }

    @Override
    public Setting getItem(int i) {
        return settings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ConstraintLayout myView = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.regular_settings_items, viewGroup, false);
        ImageView img=myView.findViewById(R.id.icon);
        TextView nameInfo = myView.findViewById(R.id.NameInfo);
        TextView info = myView.findViewById(R.id.info);
        TextView description = myView.findViewById(R.id.description);
        nameInfo.setText(settings.get(i).getNameInfo());
        info.setText(settings.get(i).getInfo() );
        description.setText(settings.get(i).getDescription());
        img.setImageResource(settings.get(i).getIcon());
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Ariel should add intent
                Toast.makeText(getContext(), settings.get(i).toString(), Toast.LENGTH_SHORT).show();
            }
        });
        return myView;
    }

}
