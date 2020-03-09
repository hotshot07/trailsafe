package com.example.trailsafe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import android.app.Activity;
//import android.graphics.drawable.BitmapDrawable;
//import android.widget.SearchView;
//import com.google.android.gms.common.api.Status;
//import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "LocationsDocs";

    private GoogleMap mMap;
    static final int MY_LOCATION_REQUEST_CODE = 1000;
    private int  REQUEST_LOCATION_PERMISSION = 1;

    private Location mLastLocation;
    private Marker mMarker = null;
    private LocationRequest mLocationRequest;

    FusedLocationProviderClient mFusedLocationProviderClient;
    private TextView txtLocation;
    private GoogleApiClient mGoogleApiClient;
    private Marker destMarker;
    private Marker originMarker;


    Button button;

    // Initialize Database object
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //this.txtLocation = (TextView) findViewById(R.id.txtLocation);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Button button = findViewById(R.id.DriveMode);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent driveModeIntent = new Intent(getApplicationContext(),DriveMode.class);
                startActivity(driveModeIntent);
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();

        //stop location updates when activity no longer active
        if(mFusedLocationProviderClient != null){
            mFusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
        }
    }

    public void StoreLocation(Location location) {
        //First tried to store a class object UserLocation
        //file is in the project
        //but it doesn't store has some errors


        //creating a hashmap of latitude, longitude and current time
        //then storing it in the database
        Map<String, Object> locationType = new HashMap<>();

        Map<String, Object> locationObject = new HashMap<>();

        locationObject.put("Latitude", location.getLatitude());
        locationObject.put("Longitude", location.getLongitude());

        Date currentTime = Calendar.getInstance().getTime();

        locationObject.put("Time", currentTime);

        locationType.put("Starting Location", locationObject);

        //ending location will also be pushed when available

        //Since we have only one static Location, it saves as User 1 only
        //Changing location and calling again will update the User 1 entry
        //but not create a separate entry
        db.collection("User Locations").document("User 2")
                .set(locationType)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "Location Successfully Saved");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error Writing Locations", e);
                    }
                });
    }

   /* public void onMapSearch(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;

        if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

            //search location update


            //////////////////
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }
    }*/

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(60000); // one minute interval
        mLocationRequest.setFastestInterval(120000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                mMap.setMyLocationEnabled(true);
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }

        //EditText locationSearch = (EditText) findViewById(R.id.editText);
        //String location = locationSearch.getText().toString();
        //List<Address> addressList = null;

       /* if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 1);

            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            mMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));
            mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        }*/
    }



    LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult){
            List<Location> locationList = locationResult.getLocations();
            if(locationList.size()>0){
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size()-1);
                Log.i("MapsActivity", "Location" + location.getLatitude()
                        + "" + location.getLongitude());
                mLastLocation = location;

                //call location storage function
                StoreLocation(mLastLocation);


                if(mMarker != null){
                    mMarker.remove();
                }

                //Place Marker at current location
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("You Are Here");
                markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN));
                mMarker = mMap.addMarker((markerOptions));

                //move camera
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f));
            }
        }
    };

    private void checkLocationPermission(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                //Show an explanation to the user 'asynchronously'
                //i.e don't block this thread waiting for the user response
                //after user sees request, try to request again
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app requires Location permissions, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user again after reasoning shown
                                ActivityCompat.requestPermissions(MapsActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_LOCATION_REQUEST_CODE);
                            }
                        })
                        .create()
                        .show();
            }
            else
            {
                //No explanation needed, request permission
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_LOCATION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch (requestCode){
            case MY_LOCATION_REQUEST_CODE:{
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    //Permission was granted, do location related task
                    if(ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                        mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest,mLocationCallback, Looper.myLooper());
                        mMap.setMyLocationEnabled(true);
                    }
                }
                else{
                    //permission denied, Disable the functionality
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }



}


