package com.faridcodeur.letschat.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.adapters.FabActionFragmentAdapter;

public class FabActionActivity extends AppCompatActivity {

    private FabActionFragmentAdapter adapter;
    private static ContentResolver appContentResolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_action);

        ViewPager2 viewPager2 = findViewById(R.id.fab_action_viewer);

        String action = (String) getIntent().getSerializableExtra("ACTION");
        adapter = new FabActionFragmentAdapter(getSupportFragmentManager(), getLifecycle(), "NEW_SURVEY");
        viewPager2.setAdapter(adapter);
    }

    public static ContentResolver getAppContentResolver() {
        return appContentResolver;
    }
}