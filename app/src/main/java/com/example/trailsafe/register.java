package com.example.trailsafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {
    private FirebaseAuth mAuth;

    EditText Email;
    EditText Password;
    Button registerB;
    ProgressBar progressBar;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Email = findViewById(R.id.register_email);
        Password = findViewById(R.id.register_password);
        registerB = findViewById(R.id.register_button);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        backButton = (ImageButton)findViewById(R.id.closeButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.super.finish();
            }
        });
        registerB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String finalEmail = Email.getText().toString().trim();
                String finalPassword = Password.getText().toString().trim();

                if (TextUtils.isEmpty(finalEmail)) {
                    Email.setError("Email is Required.");
                    return;
                }

                if (!isEmailValid(finalEmail)){
                    Email.setError("Email is not valid");
                }

                if (TextUtils.isEmpty(finalPassword)) {
                    Password.setError("Password is Required.");
                    return;
                }

                if (Password.length() < 6) {
                    Password.setError("Password Must be >= 6 Characters");
                    return;
                }



                mAuth.createUserWithEmailAndPassword(finalEmail, finalPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (task.isSuccessful()) {
                            Toast.makeText(register.this, "User Created.", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            Intent intent = new Intent(register.this, MainActivity.class);
                            startActivity(intent);
                        }
                    }

                });
            }


        });

    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


}
