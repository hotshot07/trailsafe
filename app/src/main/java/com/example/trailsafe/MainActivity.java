package com.example.trailsafe;

import android.content.Context;
import android.content.Intent;
//import kotlinx.android.synthetic.main.activity_maps
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import java.math.BigInteger;


public class MainActivity extends AppCompatActivity {
    private Button btn_next;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btn_next = (Button) findViewById(R.id.NextButton);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                TapToMove(view);

                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.example_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case  R.id.item_profile:
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;

            case  R.id.item_settings:
                Intent intent = new Intent(MainActivity.this, Settings.class);
                startActivity(intent);
                return true;

            case  R.id.item2:
                Toast.makeText(this, "Item 3 selected", Toast.LENGTH_SHORT).show();
                return true;

            case  R.id.subitem1:
                Toast.makeText(this, "Sub Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;

            case  R.id.subitem2:
                Toast.makeText(this, "Sub Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }



}






        