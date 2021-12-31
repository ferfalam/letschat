package com.faridcodeur.letschat.adapters;

import android.annotation.SuppressLint;
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
import com.google.firebase.firestore.FirebaseFirestore;

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
        @SuppressLint("ViewHolder") MaterialCardView myView = (MaterialCardView) LayoutInflater.from(context).inflate(R.layout.regular_discussion_items, viewGroup, false);
        TextView nameContact = myView.findViewById(R.id.contactName);
        TextView message = myView.findViewById(R.id.extraitChat);
        TextView time = myView.findViewById(R.id.timeSend);
        nameContact.setText(discussions.get(i).getTargetName());
        message.setText(discussions.get(i).getLastMessage().getMessageText());
        time.setText(discussions.get(i).getLastTime());
        myView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, ChatScreenActivity.class);
            intent.putExtra("discussion", discussions.get(i).getReceiverID());
            context.startActivity(intent);
        });
        return myView;
    }
}