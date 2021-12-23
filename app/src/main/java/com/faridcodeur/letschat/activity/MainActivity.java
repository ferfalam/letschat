package com.faridcodeur.letschat.activity;

import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager2.widget.ViewPager2;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.adapters.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    private final String[] titles = new String[]{"Discussions", "Sondages"};
    private FragmentAdapter adapter;

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
        new TabLayoutMediator(this.tabLayout, this.viewPager2,
                (tab, position) -> tab.setText(titles[position])).attach();

    }
}