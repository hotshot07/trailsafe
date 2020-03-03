package com.example.trailsafe;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

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