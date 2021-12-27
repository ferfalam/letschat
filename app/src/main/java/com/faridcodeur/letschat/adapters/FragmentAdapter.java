package com.faridcodeur.letschat.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.faridcodeur.letschat.contact.ContactFragment;
import com.faridcodeur.letschat.fragments.DiscussionsFragment;
import com.faridcodeur.letschat.fragments.SurveysFragment;

public class FragmentAdapter extends FragmentStateAdapter {
    private final String[] titles = new String[]{"Discussions", "Sondages"};

    public FragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 1) {
            return ContactFragment.newInstance();
        }
        return DiscussionsFragment.newInstance();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
