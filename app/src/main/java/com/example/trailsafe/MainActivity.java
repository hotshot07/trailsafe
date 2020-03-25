package com.example.trailsafe;

import android.content.Context;
import android.content.Intent;
//import kotlinx.android.synthetic.main.activity_maps
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.math.BigInteger;


public class MainActivity extends AppCompatActivity {
    private Button btn_next;
    private ImageButton btn_settings;
    private ImageButton btn_profile;
    double x = 0;
    int y = 0;


    public void TapToMove(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Magic here
            }
        }, 10000); // Millisecond 1000 = 1 sec
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.move);
        btn_next.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Magic here
            }
        }, 1000); // Millisecond 1000 = 1 sec
    }

    public void TapToSlide(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Magic here
            }
        }, 10000); // Millisecond 1000 = 1 sec
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide);
        btn_profile.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Magic here
            }
        }, 1000); // Millisecond 1000 = 1 sec
    }

    public void TapToSlideAgain(View view) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Magic here
            }
        }, 10000); // Millisecond 1000 = 1 sec
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide);
        btn_settings.startAnimation(animation);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Magic here
            }
        }, 1000); // Millisecond 1000 = 1 sec

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_next = (Button) findViewById(R.id.NextButton);
        btn_profile = (ImageButton) findViewById(R.id.Profile);
        btn_settings = (ImageButton) findViewById(R.id.Settings);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TapToMove(view);

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TapToSlideAgain(view);

                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
    }

}












        