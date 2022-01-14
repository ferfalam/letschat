package com.faridcodeur.letschat.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.databinding.ActivityContactDetails2Binding;
import com.faridcodeur.letschat.fragments.SettingsFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Objects;

import com.faridcodeur.letschat.fragments.MediaFragment;

public class ContactDetailsActivity extends AppCompatActivity {

    private ActivityContactDetails2Binding binding;
    private ArrayList<String> mediasList;

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityContactDetails2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();

        setSupportActionBar(binding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mediasList = intent.getStringArrayListExtra("medias");
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("messages", mediasList);
        MediaFragment mediaFragment = new MediaFragment();
        mediaFragment.setArguments(bundle);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
        binding.contactDetailName.setText(getIntent().getStringExtra("name"));

        Uri photoImage = Uri.parse(getIntent().getStringExtra("uri"));
        if (photoImage != null){
            Glide.with(getApplicationContext()).load(photoImage).into(binding.contactDetailPp);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        @StringRes
        private final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
        private final Context mContext;

        public SectionsPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            mContext = context;
        }

        @Override
        public Fragment getItem(int position) {
            Bundle bundler = new Bundle();
            bundler.putStringArrayList("messages", mediasList);

            switch(position) {
                case 0:
                    SettingsFragment settings = new SettingsFragment();
                    return settings;
                case 1:
                    MediaFragment media = new MediaFragment();
                    media.setArguments(bundler);
                    return media;
                default:
                    return null;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mContext.getResources().getString(TAB_TITLES[position]);
        }

        @Override
        public int getCount() {
            // Show 2 total pages.
            return 2;
        }
    }
}
