package com.faridcodeur.letschat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.faridcodeur.letschat.databinding.ActivityConfigProfileBinding;
import com.faridcodeur.letschat.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppPreference appPreference = AppPreference.getInstance(this);
        Log.e("USERNAME", appPreference.getUserName());
        if(!appPreference.isConnected()) {
            Intent intent = new Intent(this, AuthActivity.class);
            startActivity(intent);
            finish();
        }else if(appPreference.getUserName().equals("")) {
            Intent intent = new Intent(this, ConfigProfileActivity.class);
            startActivity(intent);
            finish();
        }

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.testname.setText(appPreference.getUserName());
    }
}