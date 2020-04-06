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

    EditText Email, ConfirmEmail;
    EditText Password, ConfirmPassword;
    EditText FullName;
    Button registerB;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Email = findViewById(R.id.register_email);
        Password = findViewById(R.id.register_password);
        ConfirmEmail = findViewById(R.id.register_email2);
        ConfirmPassword = findViewById(R.id.register_password2);
        registerB = findViewById(R.id.register_button);
        FullName = findViewById(R.id.register_name);

        mAuth = FirebaseAuth.getInstance();

        if(mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }



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
                String finalEmail2 = ConfirmEmail.getText().toString().trim();
                String finalPassword = Password.getText().toString().trim();
                String finalPassword2 = ConfirmPassword.getText().toString().trim();
                String finalName = FullName.getText().toString().trim();


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

                if(TextUtils.isEmpty(finalName)){
                    FullName.setError("Username is empty");
                }

                if (Password.length() < 6) {
                    Password.setError("Password Must be >= 6 Characters");
                    return;
                }

                if (FullName.length() < 2) {
                    FullName.setError("Full name must be greater than 2 characters");
                    return;
                }

                if(!finalEmail.equals(finalEmail2)){
                    ConfirmEmail.setError("Emails do not match");
                    return;
                }


                if(!finalPassword.equals(finalPassword2)){
                    ConfirmPassword.setError("Passwords do not match");
                    return;
                }


                mAuth.createUserWithEmailAndPassword(finalEmail, finalPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Toast.makeText(register.this, "User Created.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(register.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        }else {
                            Toast.makeText(register.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
