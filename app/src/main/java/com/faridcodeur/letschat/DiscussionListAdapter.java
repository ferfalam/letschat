package com.faridcodeur.letschat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.List;

public class DiscussionListAdapter extends BaseAdapter {
    final List<Discussions> discussions;
    final Context context;
    LayoutInflater inflter;

    public DiscussionListAdapter(Context context, List<Discussions> discussions) {
        this.discussions = discussions;
        this.context = context;
        inflter = (LayoutInflater.from(context));
    }

    @Override
    public int getCount() {

        return discussions.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
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
        messagerie.setText(new StringBuilder().append(discussions.get(i).getMessage()).append(" ...").toString());
        priceView.setText(new StringBuilder().append(discussions.get(i).getTime()).append(" min").toString());
        return myView;

    }
}