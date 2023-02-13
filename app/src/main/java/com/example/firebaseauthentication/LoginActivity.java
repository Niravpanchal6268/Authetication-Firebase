package com.example.firebaseauthentication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.SignInMethodQueryResult;

public class LoginActivity extends AppCompatActivity {

    TextInputLayout email, password;
    MaterialButton loginbtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();
        if (!IsConnected(this))
        {
            ShowInternetDialog();
        }


        email = findViewById(R.id.lemail_fields_id);
        password = findViewById(R.id.lpassword_fields_id);
        loginbtn = findViewById(R.id.login_btn_id);

        mAuth = FirebaseAuth.getInstance();

        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (email.getEditText().getText().toString().isEmpty() || password.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "fill the fileds", Toast.LENGTH_SHORT).show();
                    email.setError("Fill the fields");
                    password.setError("Fill the fields");
                } else {
                    Toast.makeText(LoginActivity.this, "fill", Toast.LENGTH_SHORT).show();
                    SharedPreferences preferences=getSharedPreferences("Auth",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("flag",true);
                    editor.apply();
                    loginuser();
                }


            }
        });

    }

    private void ShowInternetDialog() {
        AlertDialog.Builder  internetdialog=new AlertDialog.Builder(this);
        internetdialog.setCancelable(false);
        View view= LayoutInflater.from(this).inflate(R.layout.nointernetdialog,findViewById(R.id.internet_layout_id));
        view.findViewById(R.id.try_agein_btn_id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!IsConnected(LoginActivity.this))
                {
                    ShowInternetDialog();
                }
                else {
                    startActivity(new Intent(getApplicationContext(),LoginActivity.class));
                }
            }
        });
        internetdialog.setView(view);
        AlertDialog alertDialog=internetdialog.create();
        alertDialog.show();


    }

    private void loginuser() {
        mAuth.signInWithEmailAndPassword(email.getEditText().getText().toString().trim(), password.getEditText().getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "login successfull", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                            intent.putExtra("name", mAuth.getCurrentUser().getEmail());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Email & Password wrong Please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public void ToSignupActivity(View view) {
        startActivity(new Intent(LoginActivity.this, SignupActivity.class));
    }

    public void ForgotPassword(View view) {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Forgot Password!");
        builder.setPositiveButton("Send Link", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (email.getEditText().getText().toString().isEmpty()) {
                    Toast.makeText(LoginActivity.this, "fill the filds", Toast.LENGTH_SHORT).show();
                    email.setError("Fill the Email");
                } else {
                    email.setError(null);
                    email.setErrorEnabled(false);
                    mAuth.fetchSignInMethodsForEmail(email.getEditText().getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                                @Override
                                public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {

                                    boolean emailcheck = task.getResult().getSignInMethods().isEmpty();
                                    if (emailcheck) {

                                        email.setError("Enter Vaild Email");

                                    } else {

                                        mAuth.sendPasswordResetEmail(email.getEditText().getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            Toast.makeText(LoginActivity.this, "Email has send to your Email id", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            Toast.makeText(LoginActivity.this, "somethink went wrong", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }


                                }
                            });
                }
            }
        });
        builder.setNegativeButton("Cancle", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });


        builder.show();

    }
    private  boolean IsConnected(LoginActivity loginActivity)
    {
        ConnectivityManager connectivityManager= (ConnectivityManager) loginActivity.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wificonne=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileconne=connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        return (wificonne != null && wificonne.isConnected()) ||(mobileconne !=null && mobileconne.isConnected());


    }

}
