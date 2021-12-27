package com.faridcodeur.letschat.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.SettingActivity;
import com.faridcodeur.letschat.entities.Discussion;

import java.util.List;

public class DiscussionListAdapter extends ArrayAdapter<Discussion> {
    final List<Discussion> discussions;
    final Context context;

    public DiscussionListAdapter(Context context, List<Discussion> discussions) {
        super(context, 0, discussions);
        this.discussions = discussions;
        this.context = context;
    }

    @Override
    public int getCount() {
        return discussions.size();
    }

    @Override
    public Discussion getItem(int i) {
        return discussions.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ConstraintLayout myView = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.regular_discussion_items, viewGroup, false);
        TextView namedest = myView.findViewById(R.id.contactName);
        TextView messagerie = myView.findViewById(R.id.extraitChat);
        TextView priceView = myView.findViewById(R.id.timeSend);
        namedest.setText(discussions.get(i).getName());
        messagerie.setText(discussions.get(i).getMessage() + " ...");
        priceView.setText(discussions.get(i).getTime() + " min");
        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Ariel should add intent
                Intent intent=new Intent(getContext(), SettingActivity.class);
                context.startActivity(intent);
            }
        });
        return myView;
    }

}