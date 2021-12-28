package com.faridcodeur.letschat.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.ContentResolver;
import android.os.Bundle;
import android.util.Log;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.adapters.FabActionFragmentAdapter;
import com.faridcodeur.letschat.adapters.FragmentAdapter;
import com.faridcodeur.letschat.contact.ContactFragment;
import com.faridcodeur.letschat.fragments.SettingsFragment;
import com.faridcodeur.letschat.survey.fragements.NewSurveyFragment;

import java.util.Objects;

public class FabActionActivity extends AppCompatActivity {

    private FabActionFragmentAdapter adapter;
    private static ContentResolver appContentResolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fab_action);

        ViewPager2 viewPager2 = findViewById(R.id.fab_action_viewer);

        String action = (String) getIntent().getSerializableExtra("ACTION");
        switch (action) {
            case "NEW_DISCUSSION":
                appContentResolver = getApplicationContext().getContentResolver();
                adapter = new FabActionFragmentAdapter(getSupportFragmentManager(), getLifecycle(), "NEW_DISCUSSION");
                break;
            case "NEW_SURVEY":
                adapter = new FabActionFragmentAdapter(getSupportFragmentManager(), getLifecycle(), "NEW_SURVEY");
                break;
            case "SETTINGS":
                // TODO Start Settings fragment
                break;
        }
        viewPager2.setAdapter(adapter);
    }


    public static ContentResolver getAppContentResolver() {
        return appContentResolver;
    }
}