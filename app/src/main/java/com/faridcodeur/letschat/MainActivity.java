package com.faridcodeur.letschat;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.faridcodeur.letschat.adapters.FragmentAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    private final String[] titles = new String[]{"Discussions", "Sondages"};
    private FragmentAdapter adapter;
    private boolean isFabOpen = false;
    private FloatingActionButton settings;
    private FloatingActionButton new_surveys;
    private FloatingActionButton new_sms;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting toolbar on main activity interface
        setSupportActionBar(findViewById(R.id.toolbar1));
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // setting event on  toolbar when we touch it

        viewPager2= findViewById(R.id.viewer);
        tabLayout= findViewById(R.id.tablayout1);
        settings = findViewById(R.id.settings);
        new_surveys = findViewById(R.id.new_surveys);
        new_sms = findViewById(R.id.new_sms);

        tabLayout.addTab(tabLayout.newTab().setText("Discussions"));
        tabLayout.addTab(tabLayout.newTab().setText("Sondages"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    public void init() {
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        adapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapter);

        // attaching tab mediator
        new TabLayoutMediator(this.tabLayout, this.viewPager2, (tab, position) -> tab.setText(titles[position])).attach();
        findViewById(R.id.profile_image).setOnClickListener(view -> {
            //TODO Call Setting activity here
            Toast.makeText(getBaseContext(), "Go to Settings", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.expand_button).setOnClickListener(view -> {
            if (!isFabOpen)showFabMenu();
            else closeFabMenu();
        });

        settings.setOnClickListener(view -> {
            //TODO Call Settings activity here
            Toast.makeText(getBaseContext(), "Go to Settings", Toast.LENGTH_SHORT).show();
        });

        new_surveys.setOnClickListener(view -> {
            //TODO Call new Survey activity here
            Toast.makeText(getBaseContext(), "Create new surveys", Toast.LENGTH_SHORT).show();
        });

        new_sms.setOnClickListener(view -> {
            //TODO Call contact activity here
            Toast.makeText(getBaseContext(), "Create new discussion", Toast.LENGTH_SHORT).show();
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void showFabMenu(){
        isFabOpen = true;
        ((FloatingActionButton)findViewById(R.id.expand_button)).setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
        settings.setVisibility(View.VISIBLE);
        new_surveys.setVisibility(View.VISIBLE);
        new_sms.setVisibility(View.VISIBLE);

        settings.animate().translationY(-getResources().getDimension(R.dimen.st75));
        new_surveys.animate().translationY(-getResources().getDimension(R.dimen.st105));
        new_sms.animate().translationY(-getResources().getDimension(R.dimen.st255));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void closeFabMenu(){
        isFabOpen = false;
        settings.animate().translationY(0);
        new_surveys.animate().translationY(0);
        new_sms.animate().translationY(0);

        settings.setVisibility(View.INVISIBLE);
        new_surveys.setVisibility(View.INVISIBLE);
        new_sms.setVisibility(View.INVISIBLE);
        ((FloatingActionButton)findViewById(R.id.expand_button)).setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation));
    }
}