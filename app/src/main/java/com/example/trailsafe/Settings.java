package com.example.trailsafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class Settings extends AppCompatActivity {
    private FirebaseAuth mAuth;
    public Button signOutButton;

    public void init() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //init();

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner1);
        Spinner mySpinner2 = (Spinner) findViewById(R.id.spinner2);

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Settings.this,
                            android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.units));
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(Settings.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.timer_length));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        mySpinner2.setAdapter(myAdapter2);



        signOutButton = (Button) findViewById(R.id.sign_out_btn);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                toastMessage("Signing Out...");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    }


