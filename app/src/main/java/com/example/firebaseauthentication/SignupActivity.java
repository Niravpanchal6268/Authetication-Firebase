package com.example.firebaseauthentication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    TextInputLayout sname, sphone, semail, spassword;
    MaterialButton signup_btn;
    String emailformate = "[a-zA-Z0-9._-]+@[a-z]+\\.[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        sname = findViewById(R.id.sname_fields_id);
        sphone = findViewById(R.id.sphone_fields_id);
        semail = findViewById(R.id.semail_fields_id);
        spassword = findViewById(R.id.spassword_fields_id);
        signup_btn = findViewById(R.id.sign_btn_id);


        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Name = sname.getEditText().getText().toString().trim();
                String Phone = sphone.getEditText().getText().toString().trim();
                String Email = semail.getEditText().getText().toString().trim();
                String Password = spassword.getEditText().getText().toString().trim();

                if (Email.isEmpty() || Password.isEmpty() || Name.isEmpty() || Phone.isEmpty()) {
                    Toast.makeText(SignupActivity.this, "fill the fields", Toast.LENGTH_SHORT).show();
                    sname.setError("Fill the fields");
                    sphone.setError("Fill the fields below");
                    semail.setError("Fill the fields below");
                    sphone.setError("Fill the fields below");
                    spassword.setError("Fill the fields below");

                } else if (!Email.matches(emailformate)) {

                    semail.setError("Please Enter Vaild Email");

                } else if (Password.length() < 8) {
                    Toast.makeText(SignupActivity.this, "Use 8 characters or more for your password", Toast.LENGTH_SHORT).show();
                    spassword.getEditText().setError("Use 8 characters or more for your password");
                    sname.setErrorEnabled(false);
                } else {
                    checkEmail();

                    Toast.makeText(SignupActivity.this, "checkemail", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    private void checkEmail() {
        ////checkeamil

        String Name = sname.getEditText().getText().toString().trim();
        String Phone = sphone.getEditText().getText().toString().trim();
        String Email = semail.getEditText().getText().toString().trim();
        String Password = spassword.getEditText().getText().toString().trim();

        ModelCalss modelCalss = new ModelCalss(Name, Phone, Email);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference node = database.getReference("UserList");
        mAuth = FirebaseAuth.getInstance();

        mAuth.fetchSignInMethodsForEmail(semail.getEditText().getText().toString())
                .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                        boolean check = task.getResult().getSignInMethods().isEmpty();

                        if (check) {

                            Toast.makeText(SignupActivity.this, "new user" + Email, Toast.LENGTH_SHORT).show();
                            node.child(Name).setValue(modelCalss);
                            createuserauth();

                        } else {
                            Toast.makeText(SignupActivity.this, "old user", Toast.LENGTH_SHORT).show();
                            semail.setError("This Email is  Taken.Try another");

                        }

                    }
                });
    }

    private void createuserauth() {

        mAuth.createUserWithEmailAndPassword(semail.getEditText().getText().toString().trim(), spassword.getEditText().getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignupActivity.this, "sign up successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "sign fail", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }
}