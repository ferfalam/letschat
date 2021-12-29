package com.faridcodeur.letschat.activity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.faridcodeur.letschat.databinding.ActivityContactDetails2Binding;
import com.faridcodeur.letschat.ui.main.SectionsPagerAdapter;
import com.google.android.material.tabs.TabLayout;

public class ContactDetailsActivity extends AppCompatActivity {

    private ActivityContactDetails2Binding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityContactDetails2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);

        binding.contactDetailsReturnButton.setOnClickListener(
                v -> onBackPressed()
        );
    }
}