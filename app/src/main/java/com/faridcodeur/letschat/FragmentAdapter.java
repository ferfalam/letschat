package com.faridcodeur.letschat;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public abstract class FragmentAdapter extends FragmentStateAdapter {


    private String[] titles = new String[]{"Discussions", "Sondages"};

    public FragmentAdapter(FragmentActivity fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new SondagesFragment();
            default:
                return new DiscussionsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
