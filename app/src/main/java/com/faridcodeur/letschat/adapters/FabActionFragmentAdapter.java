package com.faridcodeur.letschat.adapters;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.faridcodeur.letschat.contact.ContactFragment;
import com.faridcodeur.letschat.fragments.DiscussionsFragment;
import com.faridcodeur.letschat.survey.fragements.NewSurveyFragment;

public class FabActionFragmentAdapter extends FragmentStateAdapter {

    private final Fragment[] fragments = new Fragment[]{ContactFragment.newInstance(), NewSurveyFragment.newInstance() };
    private String elmt;

    public FabActionFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String elmt) {
        super(fragmentManager, lifecycle);
        this.elmt = elmt;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.e("TAG", String.valueOf(position));
        switch (elmt) {
            case "NEW_DISCUSSION":
                return ContactFragment.newInstance();
            case "NEW_SURVEY":
                return NewSurveyFragment.newInstance();
            case "SETTINGS":
                // TODO Add Settings Fragment
        }
        return NewSurveyFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
