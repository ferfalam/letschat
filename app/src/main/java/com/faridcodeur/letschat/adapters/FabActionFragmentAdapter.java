package com.faridcodeur.letschat.adapters;

import android.content.ContentResolver;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.faridcodeur.letschat.survey.fragements.NewSurveyFragment;

public class FabActionFragmentAdapter extends FragmentStateAdapter {

    private final Fragment[] fragments = new Fragment[]{NewSurveyFragment.newInstance() };
    private String elmt;

    public FabActionFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, String elmt) {
        super(fragmentManager, lifecycle);
        this.elmt = elmt;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.e("TAG", elmt);
        return NewSurveyFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return fragments.length;
    }
}
