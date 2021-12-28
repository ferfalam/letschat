package com.faridcodeur.letschat.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.adapters.FragmentAdapter;
import com.faridcodeur.letschat.databinding.ActivityMainBinding;
import com.faridcodeur.letschat.survey.fragements.NewSurveyFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
    private int PERMISSIONS_REQUEST = 3015;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String[] PERMISSIONS_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(binding.getRoot(), "L'application requiert l'accès à la base de donnée distante.", Snackbar.LENGTH_LONG).setAction("Activer", view -> {
                    ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_EXTERNAL_STORAGE, PERMISSIONS_REQUEST);
                }).show();
            } else {
                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_EXTERNAL_STORAGE, PERMISSIONS_REQUEST);
            }
        }

        AppPreference appPreference = AppPreference.getInstance(this);
        if(!appPreference.isConnected()) {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
            Toast.makeText(getBaseContext(), "Go to Settings", Toast.LENGTH_SHORT).show();
        });

        findViewById(R.id.expand_button).setOnClickListener(view -> {
            if (!isFabOpen)showFabMenu();
            else closeFabMenu();
        });

        binding.settings.setOnClickListener(view -> {
            //TODO Call Settings activity here
            //Toast.makeText(getBaseContext(), "Go to Settings", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "settings");
            Intent intent = new Intent(MainActivity.this, FabActionActivity.class);
            intent.putExtra("ACTION", "SETTINGS");
            startActivity(intent);
        });

        binding.newSurveys.setOnClickListener(view -> {
            //TODO Call new Survey activity here
            //Toast.makeText(getBaseContext(), "Create new surveys", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "new surveys");
            Intent intent = new Intent(MainActivity.this, FabActionActivity.class);
            intent.putExtra("ACTION", "NEW_SURVEY");
            startActivity(intent);
        });

        binding.newSms.setOnClickListener(view -> {
            //TODO Call contact activity here
            //Toast.makeText(getBaseContext(), "Create new discussion", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "new discussion");
            Intent intent = new Intent(MainActivity.this, FabActionActivity.class);
            intent.putExtra("ACTION", "NEW_DISCUSSION");
            startActivity(intent);
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(binding.getRoot(), "No Permission", Snackbar.LENGTH_LONG).setAction("Ok", container_view -> {
                    Log.e("onRequest", "onRequestPermissionsResult: No permission");
                }).show();
            }else{
                final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

                if (firebaseAuth.getCurrentUser() != null) {
                    firebaseUser = firebaseAuth.getCurrentUser();
                }
                Uri uri = firebaseUser.getPhotoUrl();
                Glide.with(MainActivity.this).load(uri).into(binding.profileImage);
            }
        }
    }
}