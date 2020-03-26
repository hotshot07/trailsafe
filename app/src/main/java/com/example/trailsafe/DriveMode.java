package com.example.trailsafe;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
import android.os.SystemClock;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.LocationListener;
import android.widget.Toast;
import android.widget.Chronometer;

import com.google.android.gms.common.wrappers.PackageManagerWrapper;

import org.w3c.dom.Text;

import java.util.Formatter;
import java.util.Locale;



public class DriveMode extends AppCompatActivity implements LocationListener {
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffSet;
    TextView SpeedNumber;
    TextView DistanceNumber;
    private Button mTimerButton;
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

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drive_mode);

        chronometer = findViewById(R.id.chronometer);




        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TrailSafe");


        //----------pictures and set them
        ImageView ProfileView = (ImageView) findViewById(R.id.ProfileView);
        //ImageView MovementView = (ImageView) findViewById(R.id.MovementView);
      //  ImageView AccelerationView = (ImageView) findViewById(R.id.AccelerationView);
      //  ImageView SpeedView = (ImageView) findViewById(R.id.SpeedView);
       // ImageView DistanceView = (ImageView) findViewById(R.id.DistanceView);


       // int currentImage = getResources().getIdentifier("@drawable/default_profile_image", null, this.getPackageName());
        //ProfileView.setImageResource(currentImage);
       // currentImage = getResources().getIdentifier("@drawable/acceleration_image", null, this.getPackageName());
       // MovementView.setImageResource(currentImage);
       // AccelerationView.setImageResource(currentImage);
       // SpeedView.setImageResource(currentImage);
      //  DistanceView.setImageResource(currentImage);


        //------------ text headings

        TextView AccelerationText = (TextView) findViewById(R.id.AccelerationText);
        TextView SpeedText = (TextView) findViewById(R.id.SpeedText);
        TextView DistanceText = (TextView) findViewById(R.id.Distancetext);


        SpeedNumber = findViewById(R.id.SpeedNumber);
         DistanceNumber =  findViewById(R.id.DistanceNumber);
        //check for gps permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            //start program if you have permission
            doSomethingElse();
        }
        this.updateTheSpeedAndDistance(null);


    }


    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Clocation myLocation = new Clocation(location);
            this.updateTheSpeedAndDistance(myLocation);
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

    private void updateTheSpeedAndDistance(Clocation location) {
        float nCurrentSpeed = 0;
        float nCurrentDistance = 0;

        if(location !=null){
            nCurrentSpeed = location.getSpeed();
            nCurrentDistance = location.distanceTo(location);
        }

        Formatter fmt = new Formatter(new StringBuilder());
        fmt.format(Locale.US, "%5.1f", nCurrentSpeed);
        String strCurrentSpeed = fmt.toString();
        strCurrentSpeed = strCurrentSpeed.replace(" ", "0");

        Formatter fmt2 = new Formatter(new StringBuilder());
        fmt2.format(Locale.US, "%5.1f", nCurrentDistance);
        String strCurrentDistance = fmt2.toString();
        strCurrentDistance = strCurrentDistance.replace(" ", "0");

        SpeedNumber.setText(strCurrentSpeed + " km/h");
        DistanceNumber.setText(strCurrentDistance + " km ");
    }


    public void startChronometer(View v) {
        if (!running) {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffSet);
            chronometer.start();
            running = true;
        }
    }


    public void pauseChronometer(View v){
        if (running) {
            chronometer.stop();
            pauseOffSet = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer(View v){
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffSet = 0;
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
                Intent intent = new Intent( DriveMode.this, Settings.class);
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}