package com.faridcodeur.letschat.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.adapters.FabActionFragmentAdapter;

import java.util.Objects;

public class FabActionActivity extends AppCompatActivity {

    private FabActionFragmentAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_action);

        setSupportActionBar(findViewById(R.id.toolbar));

        ViewPager2 viewPager2 = findViewById(R.id.fab_action_viewer);

        String action = (String) getIntent().getSerializableExtra("ACTION");
        adapter = new FabActionFragmentAdapter(getSupportFragmentManager(), getLifecycle(), "NEW_SURVEY");
        viewPager2.setAdapter(adapter);
    }

}