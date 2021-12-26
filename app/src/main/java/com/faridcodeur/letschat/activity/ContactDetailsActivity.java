package com.faridcodeur.letschat.activity;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.faridcodeur.letschat.ui.main.SectionsPagerAdapter;
import com.faridcodeur.letschat.databinding.ActivityContactDetails2Binding;

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
    }
}