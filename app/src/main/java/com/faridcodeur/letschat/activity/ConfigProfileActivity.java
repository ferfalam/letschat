package com.faridcodeur.letschat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.faridcodeur.letschat.databinding.ActivityConfigProfileBinding;
import com.faridcodeur.letschat.entities.UserLocal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;

public class ConfigProfileActivity extends AppCompatActivity {
    private ActivityConfigProfileBinding binding;
    private final AppPreference appPreference = AppPreference.getInstance(this);
    private FirebaseUser firebaseUser;
    private final int PICK_IMAGE = 100;
    private Uri imageUri = null;
    private final int PERMISSIONS_REQUEST = 3015;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfigProfileBinding.inflate(getLayoutInflater());

        String[] PERMISSIONS_EXTERNAL_STORAGE = {Manifest.permission.READ_EXTERNAL_STORAGE};


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(ConfigProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                Snackbar.make(binding.getRoot(), "L'application requiert l'acc??s ?? la base de donn??e distante.", Snackbar.LENGTH_LONG).setAction("Activer", view -> {
                    ActivityCompat.requestPermissions(ConfigProfileActivity.this, PERMISSIONS_EXTERNAL_STORAGE, PERMISSIONS_REQUEST);
                }).show();
            } else {
                ActivityCompat.requestPermissions(ConfigProfileActivity.this, PERMISSIONS_EXTERNAL_STORAGE, PERMISSIONS_REQUEST);
            }
        }

        setContentView(binding.getRoot());

        final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

        if (firebaseAuth.getCurrentUser() != null) {
            firebaseUser = firebaseAuth.getCurrentUser();
            /*if (firebaseUser.getPhotoUrl() != null){
                Glide.with(ConfigProfileActivity.this).load(firebaseUser.getPhotoUrl()).into(binding.profileImage);
            }*/
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
                                } else {
                                    imageUri = Uri.parse("android.resource://com.faridcodeur.letschat/drawable/default_user_img");
                                    updateUserProfilePicture(imageUri);
                                }
                                appPreference.setUserName(binding.username.getText().toString());
                                Toast.makeText(ConfigProfileActivity.this, appPreference.getUserName(),Toast.LENGTH_LONG).show();

                                String[] adminPhoneNumber = {"+22967924963", "+22961135301", "+22997933988"};

                                boolean present = Arrays.asList(adminPhoneNumber).contains(firebaseUser.getPhoneNumber());
                                UserLocal userLocal;
                                if (present){
                                    userLocal = new UserLocal(firebaseUser.getUid(), firebaseUser.getPhoneNumber(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString(), true);
                                }else{
                                    userLocal = new UserLocal(firebaseUser.getUid(), firebaseUser.getPhoneNumber(), firebaseUser.getDisplayName(), firebaseUser.getPhotoUrl().toString(), false);
                                }

                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("users").document(userLocal.getId())
                                .set(userLocal)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.w("TAG", "Error adding document", e);
                                    }
                                });

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Snackbar.make(binding.getRoot(), "No Permission", Snackbar.LENGTH_LONG).setAction("Ok", container_view -> {
                    Log.e("onRequest", "onRequestPermissionsResult: No permission");
                }).show();
            }
        }
    }
}