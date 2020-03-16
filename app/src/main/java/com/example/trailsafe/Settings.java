package com.example.trailsafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    public Button signOutButton;

    public void init() {
        signOutButton = (Button) findViewById(R.id.sign_out_btn);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sOut = new Intent(Settings.this,LoginActivity.class);
                startActivity(sOut);
            }
        });
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


        }

    }


