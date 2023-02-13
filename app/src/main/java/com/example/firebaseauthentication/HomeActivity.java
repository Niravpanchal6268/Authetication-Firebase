package com.example.firebaseauthentication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    TextView getname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences preferences=getSharedPreferences("Auth",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        setContentView(R.layout.activity_home);
        editor.putBoolean("flag",true);
        editor.apply();
        getname = findViewById(R.id.getName_id);
        getname.setText(getIntent().getStringExtra("name"));

    }

    public void logout_btn(View view) {

        FirebaseAuth.getInstance().signOut();
        SharedPreferences preferences=getSharedPreferences("Auth",MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putBoolean("flag",false);
        editor.apply();

        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
        finish();
    }
}