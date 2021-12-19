package com.faridcodeur.letschat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.faridcodeur.letschat.databinding.ActivityAuthBinding;
import com.faridcodeur.letschat.databinding.ActivityConfigProfileBinding;
import com.google.android.material.textfield.TextInputEditText;

public class ConfigProfileActivity extends AppCompatActivity {
    private ActivityConfigProfileBinding binding;
    private final AppPreference appPreference = AppPreference.getInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfigProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.saveUsername.setOnClickListener(saveUsername());
        binding.profileImage.setOnClickListener(changeProfileImage());
    }

    private View.OnClickListener changeProfileImage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ConfigProfileActivity.this, "Vous aviez cliqué sur l'image", Toast.LENGTH_LONG).show();
            }
        };
    }

    private View.OnClickListener saveUsername() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appPreference.setUserName(binding.username.getText().toString());
                Toast.makeText(ConfigProfileActivity.this, appPreference.getUserName(),Toast.LENGTH_LONG).show();

                Intent intent = new Intent(ConfigProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
    }
}