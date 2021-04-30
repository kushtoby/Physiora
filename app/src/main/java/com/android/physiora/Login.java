package com.android.physiora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button loginBtn;
    CheckBox rememberMe;
    TextView redirect;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email = findViewById(R.id.emailLoginET);
        password = findViewById(R.id.passwordLoginET);
        loginBtn = findViewById(R.id.loginBtn);
        rememberMe = findViewById(R.id.LoginCheckBox);
        redirect = findViewById(R.id.registerRedirectTV);
        FirebaseApp.initializeApp(this);
        loadingBar = new ProgressDialog(this);

        preferences = getApplicationContext().getSharedPreferences("MyUserPrefs", Context.MODE_PRIVATE);
            String emailAddress = preferences.getString("email", "default");
            String userPassword = preferences.getString("password", "default");
            login(emailAddress, userPassword);

        if (email.getText().toString().isEmpty() && password.getText().toString().isEmpty()) {
            loginBtn.setEnabled(false);
        }
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    loginBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (password.getText().toString().trim().length() <= 8) {
                    loginBtn.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().toString().trim().length() >= 8) {
                    loginBtn.setEnabled(true);
                }
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailAddress = email.getText().toString().trim();
                final String userPassword = password.getText().toString().trim();
                login(emailAddress, userPassword);
            }
        });
        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Registration.class);
                startActivity(intent);
            }
        });

    }

    private void login(final String emailAddress, final String userPassword) {
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();
        mAuth = FirebaseAuth.getInstance();
        mAuth.signInWithEmailAndPassword(emailAddress, userPassword)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            if (rememberMe.isChecked()) {
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("email", emailAddress);
                                editor.putString("password", userPassword);
                                editor.apply();
                            }
                            loadingBar.dismiss();
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            loadingBar.dismiss();
                        }
                    }
                });
    }

}