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
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.lang.annotation.Target;
import java.math.BigInteger;


public class MainActivity extends AppCompatActivity {

    static String KEY_ANIM = "TARGET_ANIM";
    static String Target_Move = "Translate";
    static String Target_Rotate = "Rotate";
    static String Target_Reverse = "right2left";
    String target_op = Target_Move;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        final Animation move = AnimationUtils.loadAnimation(this, R.anim.move);
        final Animation right2left = AnimationUtils.loadAnimation(this, R.anim.right2left);

        Button btn_next = (Button) findViewById(R.id.NextButton);
        ImageButton btn_profile = (ImageButton) findViewById(R.id.Profile);
        ImageButton btn_settings = (ImageButton) findViewById(R.id.Settings);

        btn_settings.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                target_op = Target_Move;
                arg0.startAnimation(move);
            }
        });

        btn_next.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                target_op = Target_Rotate;
                arg0.startAnimation(rotate);
            }
        });

        btn_profile.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0){
                target_op = Target_Reverse;
                arg0.startAnimation(right2left);
            }
        });


        rotate.setAnimationListener(animationListener);
        move.setAnimationListener(animationListener);

    }


    AnimationListener animationListener = new AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //TODO Auto-genetated method stub
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(target_op == Target_Rotate) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra(KEY_ANIM, target_op);
                    startActivity(intent);
                }
                if(target_op == Target_Move){
                    Intent intent = new Intent(MainActivity.this, Settings.class);
                    intent.putExtra(KEY_ANIM, target_op);
                    startActivity(intent);
                }

                if(target_op == Target_Reverse){
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra(KEY_ANIM, target_op);
                    startActivity(intent);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //TODO Auto-genetated method stub

            }};

}















        