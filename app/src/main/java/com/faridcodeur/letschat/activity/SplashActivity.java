package com.faridcodeur.letschat.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.faridcodeur.letschat.R;
import com.faridcodeur.letschat.entities.Contact;
import com.faridcodeur.letschat.entities.UserLocal;
import com.faridcodeur.letschat.utiles.Global;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SplashActivity extends AppCompatActivity {

    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_splash);

            handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    AppPreference appPreference = AppPreference.getInstance(SplashActivity.this);
                    if(!appPreference.isConnected()) {
                        Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                        startActivity(intent);
                        finish();
                    }else if(appPreference.getUserName().equals("")) {
                        Intent intent = new Intent(SplashActivity.this, ConfigProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        Intent intent=new Intent(SplashActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            },3000);
        }catch (Throwable th){
            th.printStackTrace();
            Log.e("ERROR", th.getMessage());
        }
    }
}