package com.faridcodeur.letschat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.activity.ChatScreenActivity;
import com.faridcodeur.letschat.activity.MainActivity;
import com.faridcodeur.letschat.fragments.DiscussionsFragment;
import com.faridcodeur.letschat.entities.Discussion;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.util.List;

public class DiscussionListAdapter extends BaseAdapter {
    private FirebaseFirestore database;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

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
        ImageView imageView = myView.findViewById(R.id.profile_Img);

            Uri photoImage = Uri.parse(discussions.get(i).getProfileImg());
            if (photoImage != null){
                Glide.with(context).load(photoImage).into(imageView);
            }

        nameContact.setText(discussions.get(i).getTargetName());
        message.setText(discussions.get(i).getLastMessage().getMessageText());
        time.setText(DateFormat.getTimeInstance(DateFormat.SHORT).format(discussions.get(i).getLastTime()));
        myView.setOnClickListener(view1 -> {
            GsonBuilder builder = new GsonBuilder();
            builder.setPrettyPrinting();
            Gson gson = builder.create();
            Intent intent = new Intent(context, ChatScreenActivity.class);
            String state = "";
            if (discussions.get(i).getSenderId().equals(user.getUid())){
                state = "sender";
            } else {
                state = "receiver";
            }
            String outdata = gson.toJson(state);
            String indata = gson.toJson(discussions.get(i).getTarget());
            intent.putExtra("user", indata);
            intent.putExtra("type", outdata);
            context.startActivity(intent);
        });
        return myView;
    }
}