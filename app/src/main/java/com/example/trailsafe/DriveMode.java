package com.example.trailsafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.LocationListener;
import android.widget.Toast;

import com.google.android.gms.common.wrappers.PackageManagerWrapper;

import org.w3c.dom.Text;

import java.util.Formatter;
import java.util.Locale;



public class DriveMode extends AppCompatActivity implements LocationListener {
    TextView SpeedNumber;
    private Button mTimerButton;

    private long routeDuration;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerBoolean;
    private long mTimeLeftMillis = 10000;

    @SuppressLint("MissingPermission")
    public void doSomethingElse() {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        Toast.makeText(this, "waiting for a GPS connection!", Toast.LENGTH_SHORT).show();

        Button button = findViewById(R.id.Emergency);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent countDownIntent = new Intent(getApplicationContext(),CountDown.class);
                startActivity(countDownIntent);
            }
        });

        startTimer();

    }

    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftMillis = millisUntilFinished;
            }

            @Override
            public void onFinish() {
                // Add the move functionality.
                startActivity(new Intent(DriveMode.this, CountDown.class));
            }
        }.start();

        mTimerBoolean = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_mode);

        routeDuration = 1000 * 10;
        startTimer();
        //----------pictures and set them
        ImageView ProfileView = (ImageView) findViewById(R.id.ProfileView);
        ImageView MovementView = (ImageView) findViewById(R.id.MovementView);
        ImageView AccelerationView = (ImageView) findViewById(R.id.AccelerationView);
        ImageView SpeedView = (ImageView) findViewById(R.id.SpeedView);
        ImageView DistanceView = (ImageView) findViewById(R.id.DistanceView);


        int currentImage = getResources().getIdentifier("@drawable/default_profile_image", null, this.getPackageName());
        ProfileView.setImageResource(currentImage);
        currentImage = getResources().getIdentifier("@drawable/acceleration_image", null, this.getPackageName());
        MovementView.setImageResource(currentImage);
        AccelerationView.setImageResource(currentImage);
        SpeedView.setImageResource(currentImage);
        DistanceView.setImageResource(currentImage);


        //------------ text headings
        TextView MovingText = (TextView) findViewById(R.id.MovingText);
        TextView AccelerationText = (TextView) findViewById(R.id.AccelerationText);
        TextView SpeedText = (TextView) findViewById(R.id.SpeedText);
        TextView DistanceText = (TextView) findViewById(R.id.Distancetext);


        SpeedNumber = findViewById(R.id.SpeedNumber);
        //check for gps permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            //start program if you have permission
            doSomethingElse();
        }
        this.updateTheSpeed(null);


    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Clocation myLocation = new Clocation(location);
            this.updateTheSpeed(myLocation);
        }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doSomethingElse();
            } else {
                finish();
            }
        }
    }

    private void updateTheSpeed(Clocation location) {
        float nCurrentSpeed = 0;

        if(location !=null){
            nCurrentSpeed = location.getSpeed();
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(" ", "0");

        SpeedNumber.setText(strCurrentSpeed + " km/h");
    }



}