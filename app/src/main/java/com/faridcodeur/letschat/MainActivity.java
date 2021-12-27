package com.faridcodeur.letschat;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.faridcodeur.letschat.adapter.FragmentAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {
    CircleImageView img;
    ViewPager2 viewPager2;
    TabLayout tabLayout;
    ArrayList<Fragment> fragments;
    Toolbar toolbar;
    private String[] titles = new String[]{"Discussions", "Sondages"};

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //toolbar initialisation
        toolbar=(Toolbar) findViewById(R.id.toolbar1);
        img=(CircleImageView) findViewById(R.id.profile_Img);
        /*
         toolbar.setTitle("Let's Chat");
         toolbar.setSubtitle("Subtitle");
        */
        //setting toolbar on main activity interface
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // setting event on  toolbar when we touch it
        // toolbar.setNavigationIcon(R.drawable.ic_action_profile);

//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "Toolbar", Toast.LENGTH_SHORT).show();
//
//            }
//        });
        viewPager2=(ViewPager2) findViewById(R.id.viewer);
        tabLayout=(TabLayout) findViewById(R.id.tablayout1);

        fragments=new ArrayList<>();
        tabLayout.addTab(tabLayout.newTab().setText("Discussions"));
        tabLayout.addTab(tabLayout.newTab().setText("Sondages"));
        tabLayout.setTabGravity(tabLayout.GRAVITY_FILL);
        init();




    }


    //Search view menu  creation
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.search_btn) {
//            Toast.makeText(this, "Search view ", Toast.LENGTH_LONG).show();
//        }
//        return true;
//    }


    //init
    public void init() {
        getSupportActionBar().setElevation(0);

        viewPager2.setAdapter(new FragmentAdapter(this) {});

        // attaching tab mediator
        new TabLayoutMediator(this.tabLayout, this.viewPager2,
                (tab, position) -> tab.setText(titles[position])).attach();

    }
}