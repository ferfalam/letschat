package com.faridcodeur.letschat;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
    private FloatingActionButton fab1;
    private FloatingActionButton fab2;
    private FloatingActionButton fab3;


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
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        fab3 = findViewById(R.id.fab3);

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
            if (!isFabOpen) {
                showFabMenu();
                Toast.makeText(getBaseContext(), "Show", Toast.LENGTH_SHORT).show();
            }
            else {
                closeFabMenu();
                Toast.makeText(getBaseContext(), "Close", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void showFabMenu(){
        isFabOpen = true;
        fab1.setVisibility(View.VISIBLE);
        fab2.setVisibility(View.VISIBLE);
        fab3.setVisibility(View.VISIBLE);

        fab1.animate().translationY(-getResources().getDimension(R.dimen.st75));
        fab2.animate().translationY(-getResources().getDimension(R.dimen.st105));
        fab3.animate().translationY(-getResources().getDimension(R.dimen.st255));
    }

    public void closeFabMenu(){
        isFabOpen = false;
        fab1.animate().translationY(0);
        fab2.animate().translationY(0);
        fab3.animate().translationY(0);

        fab1.setVisibility(View.INVISIBLE);
        fab2.setVisibility(View.INVISIBLE);
        fab3.setVisibility(View.INVISIBLE);
    }
}