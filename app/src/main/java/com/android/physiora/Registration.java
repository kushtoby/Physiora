package com.android.physiora;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Registration extends AppCompatActivity {
    EditText username, email, password;
    Button register;
    TextView redirect;
    FirebaseAuth mAuth;
    ProgressDialog loadingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        username = findViewById(R.id.usernameRegET);
        email = findViewById(R.id.emailRegET);
        password = findViewById(R.id.passwordRegET);
        register = findViewById(R.id.regBtn);
        redirect = findViewById(R.id.loginRedirectTV);
        loadingBar = new ProgressDialog(this);

        if (username.getText().toString().isEmpty() && email.getText().toString().isEmpty() && password.getText().toString().isEmpty()) {
            register.setEnabled(false);
        }
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (username.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    register.setEnabled(false);
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
                    register.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (password.getText().toString().trim().length() >= 8) {
                    register.setEnabled(true);
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String emailAddress = email.getText().toString().trim();
                final String userPassword = password.getText().toString().trim();
                signUp (emailAddress, userPassword);
            }
        });
        redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Registration.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void signUp(String emailAddress, String userPassword) {
        mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(emailAddress, userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()) {
                    Toast.makeText(Registration.this, "sign up error", Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Registration.this, Login.class);
                    startActivity(intent);
                    Toast.makeText(Registration.this, "successful", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
    }
}