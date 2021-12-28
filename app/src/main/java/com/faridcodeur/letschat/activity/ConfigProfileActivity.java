package com.faridcodeur.letschat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.faridcodeur.letschat.databinding.ActivityConfigProfileBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.io.File;

public class ConfigProfileActivity extends AppCompatActivity {
    private ActivityConfigProfileBinding binding;
    private final AppPreference appPreference = AppPreference.getInstance(this);
    private FirebaseUser firebaseUser;
    private final int PICK_IMAGE = 100;
    private Uri imageUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfigProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            firebaseUser = firebaseAuth.getCurrentUser();
        }
        binding.userPhoneNumber.setText(firebaseUser.getPhoneNumber());
        binding.saveUsername.setOnClickListener(saveUsername());
        binding.profileImage.setOnClickListener(changeProfileImage());
    }

    private View.OnClickListener changeProfileImage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        };
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityIfNeeded(gallery, PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            Glide.with(ConfigProfileActivity.this).load(imageUri).into(binding.profileImage);
            //updateUserProfilePicture(imageUri);
        }
    }
    private void updateUserProfilePicture(final Uri uri) {
        UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                .setPhotoUri(uri)
                .build();

        firebaseUser.updateProfile(profileChangeRequest);
    }

    private View.OnClickListener saveUsername() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.username.getText().toString().isEmpty()){
                    binding.username.setError("Veuillez entrer votre nom d'utilisateur");
                } else{

                    UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                            .setDisplayName(String.valueOf(binding.username.getText()))
                            .build();
                    firebaseUser.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                if (imageUri != null){
                                    updateUserProfilePicture(imageUri);
                                }else{
                                    imageUri = Uri.fromFile(new File("drawable/default_user_img.png"));
                                    updateUserProfilePicture(imageUri);
                                }
                                appPreference.setUserName(binding.username.getText().toString());
                                Toast.makeText(ConfigProfileActivity.this, appPreference.getUserName(),Toast.LENGTH_LONG).show();

                                Intent intent = new Intent(ConfigProfileActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    });
                }
            }
        };
    }
}