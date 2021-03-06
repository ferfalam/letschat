package com.faridcodeur.letschat.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.activity.SondageBoxActivity;
import com.faridcodeur.letschat.entities.Surveys;
import com.google.android.material.card.MaterialCardView;

import java.util.Date;
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        @SuppressLint("ViewHolder") MaterialCardView myView = (MaterialCardView) LayoutInflater.from(context).inflate(R.layout.survey_items, viewGroup, false);
        TextView title = myView.findViewById(R.id.survey_title);
        TextView description = myView.findViewById(R.id.survey_description);
        TextView created_at = myView.findViewById(R.id.survey_created_at);
        title.setText(surveys.get(i).getTitle());
        description.setText(surveys.get(i).getDescription());
        created_at.setText(DateUtils.getRelativeTimeSpanString(surveys.get(i).getCreated_at().getTime(), new Date().getTime(), 0));
        long time = new Date().getTime() - surveys.get(i).getCreated_at().getTime();
        Log.e("TAG", "getView: " + time );
        myView.setOnClickListener(view1 -> {
            Intent intent = new Intent(context, SondageBoxActivity.class);
            intent.putExtra("survey", surveys.get(i));
            context.startActivity(intent);
        });
        return myView;
    }
}
