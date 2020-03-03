package com.example.trailsafe;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class LaunchScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_screen);


        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    int minimum = 200;
                    int maximum = 600;
                    double loadtime = minimum + (int) (Math.random() * maximum);
                    loadtime += 1000;
                    sleep((long) loadtime);
                    Intent intent = new Intent(LaunchScreen.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();

                }


            }

        };



        myThread.start();
    }


}
