package com.faridcodeur.letschat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.activity.SondageBoxActivity;
import com.faridcodeur.letschat.entities.Surveys;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class SurveyListAdapter extends BaseAdapter {

    final List<Surveys> surveys;
    final Context context;

    public SurveyListAdapter(Context context, List<Surveys> surveys) {
        this.surveys = surveys;
        this.context = context;
    }

    @Override
    public int getCount() {
        return surveys.size();
    }

    @Override
    public Object getItem(int i) {
        return surveys.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") MaterialCardView myView = (MaterialCardView) LayoutInflater.from(context).inflate(R.layout.survey_items, viewGroup, false);
        TextView title = myView.findViewById(R.id.survey_title);
        TextView description = myView.findViewById(R.id.survey_description);
        TextView created_at = myView.findViewById(R.id.survey_created_at);
        title.setText(surveys.get(i).getTitle());
        description.setText(surveys.get(i).getDescription());
        created_at.setText((CharSequence) surveys.get(i).getCreated_at());
        myView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, SondageBoxActivity.class);
            context.startActivity(intent);
        });
        return myView;
    }
}
