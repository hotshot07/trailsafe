package com.example.trailsafe;

import android.content.Context;
import android.content.Intent;
//import kotlinx.android.synthetic.main.activity_maps
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_next = (Button) findViewById(R.id.NextButton);
        btn_profile = (ImageButton) findViewById(R.id.Profile);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                while(x < 2000000){
                    y++;
                    x = System.currentTimeMillis();
                }
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        btn_settings = (ImageButton) findViewById(R.id.Settings);
        btn_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                x = 0;
                while(x < 2000000){
                    y++;
                    x = System.currentTimeMillis();
                }
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
            }
        });
    }

    public void TapToMove(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.move);
        btn_next.startAnimation(animation);
    }

    public void TapToBlink(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide);
        btn_settings.startAnimation(animation);
    }

    public void TapToSlide(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide);
        btn_profile.startAnimation(animation);
    }



}






        