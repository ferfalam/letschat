package com.faridcodeur.letschat.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.adapters.FragmentAdapter;
import com.faridcodeur.letschat.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private final String[] titles = new String[]{"Discussions", "Sondages"};
    private FragmentAdapter adapter;
    private FirebaseUser firebaseUser;
    private boolean isFabOpen = false;
    private final int PROFILE_ACTIVITY = 3025;
    private final int MY_CONTACT_ACTIVITY = 3035;
    private final int SURVEY_FRAGMENT = 3045;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            firebaseUser = firebaseAuth.getCurrentUser();
            Uri photoImage = firebaseUser.getPhotoUrl();
            if (photoImage != null){
                Glide.with(MainActivity.this).load(photoImage).into(binding.profileImage);
            }
        }

        //setting toolbar on main activity interface
        setSupportActionBar(binding.toolbar1);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        // setting event on  toolbar when we touch it

        binding.tablayout1.addTab(binding.tablayout1.newTab().setText("Discussions"));
        binding.tablayout1.addTab(binding.tablayout1.newTab().setText("Sondages"));
        binding.tablayout1.setTabGravity(TabLayout.GRAVITY_FILL);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    private void init() {
        Objects.requireNonNull(getSupportActionBar()).setElevation(0);
        adapter = new FragmentAdapter(getSupportFragmentManager(), getLifecycle());
        binding.viewer.setAdapter(adapter);

        // attaching tab mediator
        new TabLayoutMediator(this.binding.tablayout1, this.binding.viewer, (tab, position) -> tab.setText(titles[position])).attach();

        findViewById(R.id.profile_image).setOnClickListener(view -> {
            //TODO Call Setting activity here
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivityIfNeeded(intent, PROFILE_ACTIVITY);
        });

        findViewById(R.id.expand_button).setOnClickListener(view -> {
            if (!isFabOpen)showFabMenu();
            else closeFabMenu();
        });

        binding.settings.setOnClickListener(view -> {
            //TODO Call Settings activity here
            //Toast.makeText(getBaseContext(), "Go to Settings", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
            startActivityIfNeeded(intent, PROFILE_ACTIVITY);
        });

        binding.newSurveys.setOnClickListener(view -> {
            //TODO Call new Survey activity here
            //Toast.makeText(getBaseContext(), "Create new surveys", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(MainActivity.this, FabActionActivity.class);
            startActivityIfNeeded(intent, SURVEY_FRAGMENT);
        });

        binding.newSms.setOnClickListener(view -> {
            //TODO Call contact activity here

            Intent intent = new Intent(MainActivity.this, MyContactActivity.class);
            //Toast.makeText(getBaseContext(), "Create new discussion", Toast.LENGTH_SHORT).show();
            startActivityIfNeeded(intent, MY_CONTACT_ACTIVITY);
        });
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void showFabMenu(){
        isFabOpen = true;
        ((FloatingActionButton)findViewById(R.id.expand_button)).setImageDrawable(getResources().getDrawable(R.drawable.ic_close));
        binding.settings.setVisibility(View.VISIBLE);
        binding.newSurveys.setVisibility(View.VISIBLE);
        binding.newSms.setVisibility(View.VISIBLE);

        binding.settings.animate().translationY(-getResources().getDimension(R.dimen.st75));
        binding.newSurveys.animate().translationY(-getResources().getDimension(R.dimen.st105));
        binding.newSms.animate().translationY(-getResources().getDimension(R.dimen.st255));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void closeFabMenu(){
        isFabOpen = false;
        binding.settings.animate().translationY(0);
        binding.newSurveys.animate().translationY(0);
        binding.newSms.animate().translationY(0);

        binding.settings.setVisibility(View.INVISIBLE);
        binding.newSurveys.setVisibility(View.INVISIBLE);
        binding.newSms.setVisibility(View.INVISIBLE);
        ((FloatingActionButton)findViewById(R.id.expand_button)).setImageDrawable(getResources().getDrawable(R.drawable.ic_navigation));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_ACTIVITY){
            if (resultCode == Activity.RESULT_OK){
                Uri photoImage = firebaseUser.getPhotoUrl();
                Glide.with(MainActivity.this).load(photoImage).into(binding.profileImage);
            }
        }
    }
}