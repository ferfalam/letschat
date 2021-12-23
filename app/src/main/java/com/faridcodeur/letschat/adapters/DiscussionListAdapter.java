package com.faridcodeur.letschat.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.entities.Discussions;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class DiscussionListAdapter extends BaseAdapter {

    final List<Discussions> discussions;
    final Context context;

    public DiscussionListAdapter(Context context, List<Discussions> discussions) {
        this.discussions = discussions;
        this.context = context;
    }

    @Override
    public int getCount() {
        return discussions.size();
    }

    @Override
    public Object getItem(int i) {
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
            Log.e("CLIKCED", "LIST ITEM");
            Toast.makeText(context,"Clicked", Toast.LENGTH_SHORT).show();
        });
        return myView;
    }
}