package com.faridcodeur.letschat.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.faridcodeur.letschat.activity.DiscussionsFragment;
import com.faridcodeur.letschat.activity.SurveysFragment;
import com.faridcodeur.letschat.survey.fragements.NewSurveyFragment;

public class FragmentAdapter extends FragmentStateAdapter {
    private String[] titles = new String[]{"Discussions", "Sondages"};

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return SurveysFragment.newInstance();
        }
        return DiscussionsFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
