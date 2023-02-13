package com.example.firebaseauthentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);
        getSupportActionBar().hide();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences preferences = getSharedPreferences("Auth", MODE_PRIVATE);
                Intent intent;
                boolean validate = preferences.getBoolean("flag", false);
                if (validate) {
                    Toast.makeText(Splashscreen.this, "exists user", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Splashscreen.this, HomeActivity.class);
                    finish();
                } else {
                    Toast.makeText(Splashscreen.this, "new user ", Toast.LENGTH_SHORT).show();
                    intent = new Intent(Splashscreen.this, LoginActivity.class);
                    finish();
                }
                startActivity(intent);
            }
        }, 2000);
    }
}