package com.faridcodeur.letschat.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.activity.ChatScreenActivity;
import com.faridcodeur.letschat.entities.Discussion;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DiscussionListAdapter extends BaseAdapter {

    final List<Discussion> discussions;
    final Context context;

    public DiscussionListAdapter(Context context, List<Discussion> discussions) {
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
        MaterialCardView myView = (MaterialCardView) LayoutInflater.from(context).inflate(R.layout.regular_discussion_items, viewGroup, false);
        TextView nameContact = myView.findViewById(R.id.contactName);
        TextView message = myView.findViewById(R.id.extraitChat);
        TextView time = myView.findViewById(R.id.timeSend);
        nameContact.setText(discussions.get(i).getName());
        message.setText(discussions.get(i).getMessage());
        time.setText(discussions.get(i).getTime());
        myView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ChatScreenActivity.class);
            context.startActivity(intent);
        });
        return myView;
    }
}