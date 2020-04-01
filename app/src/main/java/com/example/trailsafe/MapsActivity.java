package com.example.trailsafe;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.trailsafe.helper.DirectionsJSONParser;
import com.example.trailsafe.helper.PolylineData;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.PendingResult;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.Bounds;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

//import android.app.Activity;
//import android.graphics.drawable.BitmapDrawable;
//import android.widget.SearchView;
//import com.google.android.gms.common.api.Status;
//import java.util.Locale;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
GoogleMap.OnPolylineClickListener{

    private static final String TAG = "LocationsDocs";

    private GoogleMap mMap;
    static final int MY_LOCATION_REQUEST_CODE = 1000;
    private int  REQUEST_LOCATION_PERMISSION = 1;

    private Location mLastLocation;

    private Marker mMarker = null;
    private LocationRequest mLocationRequest;
    private GeoApiContext mGeoApiContext = null;


    FusedLocationProviderClient mFusedLocationProviderClient;
    private TextView txtLocation;
    private GoogleApiClient mGoogleApiClient;
    private Marker destMarker;
    private Marker originMarker;
    List<Address> addressList = null;
    ArrayList<LatLng> listPoints;
    ArrayList markerPoints = new ArrayList();
    ArrayList<Marker> markers = new ArrayList<>();
    LatLng origin;
    LatLng destination;
    double defaultValue = 0.0;
    private long duration;
    private String durString;
    TextView durationDisplay;
    private ArrayList<PolylineData> mPolyLinesData = new ArrayList<>();
    private String originString;
    private String destString;
    private double originLat;
    private double originLng;
    private double destLat, destLng;
    private LatLngBounds bounds;


    Button button;

    // Initialize Database object
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("TrailSafe");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        //retrieve the origin as a string from previous activity
        Intent receive = getIntent();
        originString = receive.getStringExtra("OriginString");
        destString = receive.getStringExtra("DestString");

        originLat = receive.getDoubleExtra("OriginLat",defaultValue);
        originLng = receive.getDoubleExtra("OriginLng",defaultValue);
        destLat = receive.getDoubleExtra("DestLat", defaultValue);
        destLng = receive.getDoubleExtra("DestLng",defaultValue);



        origin = new LatLng(originLat,originLng);
        destination = new LatLng(destLat, destLng);

        //bounds = new LatLngBounds(origin,destination);






        //this.txtLocation = (TextView) findViewById(R.id.txtLocation);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        Button button = findViewById(R.id.DriveMode);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Intent driveModeIntent = new Intent(getApplicationContext(),DriveMode.class);
                startActivity(driveModeIntent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }

        });
        listPoints = new ArrayList<>();
        if (mGeoApiContext == null){
            {
                mGeoApiContext = new GeoApiContext.Builder()
                        .apiKey(getString(R.string.google_maps_api_key))
                        .build();

            }
        }







    }

    private void calculateDirections(){
        Log.d(TAG, "calculateDirections: calculating directions.");

        com.google.maps.model.LatLng destination = new com.google.maps.model.LatLng(
                destMarker.getPosition().latitude,
                destMarker.getPosition().longitude
        );
        DirectionsApiRequest directions = new DirectionsApiRequest(mGeoApiContext);

        directions.alternatives(true);
        directions.mode(TravelMode.BICYCLING);
        directions.origin(
                new com.google.maps.model.LatLng(
                        originMarker.getPosition().latitude,
                        originMarker.getPosition().longitude
                )
        );
        Log.d(TAG, "calculateDirections: destination: " + destination.toString());
        directions.destination(destination).setCallback(new PendingResult.Callback<DirectionsResult>() {
            @Override
            public void onResult(DirectionsResult result) {
                Log.d(TAG, "calculateDirections: routes: " + result.routes[0].toString());
                Log.d(TAG, "calculateDirections: duration: " + result.routes[0].legs[0].duration);
                Log.d(TAG, "calculateDirections: distance: " + result.routes[0].legs[0].distance);
                Log.d(TAG, "calculateDirections: geocodedWayPoints: " + result.geocodedWaypoints[0].toString());
                duration = (result.routes[0].legs[0].duration.inSeconds)/60;
                addPolylinesToMap(result);

                duration = result.routes[0].legs[0].duration.inSeconds;
                durString = result.routes[0].legs[0].duration.humanReadable;
                durationDisplay.setText("ETA = " + durString);
            }

            @Override
            public void onFailure(Throwable e) {
                Log.e(TAG, "calculateDirections: Failed to get directions: " + e.getMessage() );

            }
        });
    }

    private void addPolylinesToMap(final DirectionsResult result){
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "run: result routes: " + result.routes.length);
                if(!mPolyLinesData.isEmpty()){
                    for (PolylineData polylineData: mPolyLinesData){
                        polylineData.getPolyline().remove();
                    }
                    mPolyLinesData.clear();
                    mPolyLinesData = new ArrayList<>();
                }

                for(DirectionsRoute route: result.routes){
                    Log.d(TAG, "run: leg: " + route.legs[0].toString());
                    List<com.google.maps.model.LatLng> decodedPath = PolylineEncoding.decode(route.overviewPolyline.getEncodedPath());

                    List<LatLng> newDecodedPath = new ArrayList<>();

                    // This loops through all the LatLng coordinates of ONE polyline.
                    for(com.google.maps.model.LatLng latLng: decodedPath){

//                        Log.d(TAG, "run: latlng: " + latLng.toString());

                        newDecodedPath.add(new LatLng(
                                latLng.lat,
                                latLng.lng
                        ));
                    }
                    Polyline polyline = mMap.addPolyline(new PolylineOptions().addAll(newDecodedPath));
                    polyline.setColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGrey));
                    polyline.setClickable(true);
                    mPolyLinesData.add(new PolylineData(polyline,route.legs[0]));

                    onPolylineClick(polyline);

                }
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



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnPolylineClickListener(this);
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
        } else {
            mFusedLocationProviderClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            mMap.setMyLocationEnabled(true);
        }

        if(originLat != 0 && originLng != 0) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(origin)
                    .title("Start")
                    .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.footorange_icon));
            originMarker = mMap.addMarker(markerOptions);
            markers.add(originMarker);

            markerOptions = new MarkerOptions()
                    .position(destination)
                    .title("End")
                    .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.footblue_icon));
            destMarker = mMap.addMarker(markerOptions);
            markers.add(destMarker);
            listPoints.add(origin);
            listPoints.add(destination);


            markerPoints.add(origin);
            markerPoints.add(destination);


            calculateDirections();

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            builder.include(origin);
            builder.include(destination);
            LatLngBounds bounds = builder.build();

            int padding = 200; // offset from edges of the map in pixels
            CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            googleMap.moveCamera(cu);

        }





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




                //Place Marker at current location
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

                if (originLat == 0 && originLng == 0){
                    origin = new LatLng(location.getLatitude(), location.getLongitude());
                    MarkerOptions markerOptions = new MarkerOptions()
                            .position(origin)
                            .title("Start")
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.footorange_icon));
                    originMarker = mMap.addMarker(markerOptions);
                    markers.add(originMarker);

                    markerOptions = new MarkerOptions()
                            .position(destination)
                            .title("End")
                            .icon(bitmapDescriptorFromVector(getApplicationContext(), R.drawable.footblue_icon));
                    destMarker = mMap.addMarker(markerOptions);
                    markers.add(destMarker);
                    listPoints.add(origin);
                    listPoints.add(destination);


                    markerPoints.add(origin);
                    markerPoints.add(destination);


                    calculateDirections();

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(origin);
                    builder.include(destination);
                    LatLngBounds bounds = builder.build();

                    int padding = 200; // offset from edges of the map in pixels
                    CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.moveCamera(cu);

                }


                //move camera
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11f));
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
                Intent intent = new Intent( MapsActivity.this, ProfileActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
                return true;

            case  R.id.item_settings:
                intent = new Intent( MapsActivity.this, Settings.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_down);
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

    @Override
    public void onPolylineClick(Polyline polyline) {
        int index = 0;
        for(PolylineData polylineData: mPolyLinesData){
            index++;
            Log.d(TAG, "onPolylineClick: toString: " + polylineData.toString());
            if(polyline.getId().equals(polylineData.getPolyline().getId())){
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.design_default_color_primary_dark));
                polylineData.getPolyline().setZIndex(1);

                LatLng endLocation = new LatLng(
                        polylineData.getLeg().endLocation.lat,
                        polylineData.getLeg().endLocation.lng
                );


                MarkerOptions markerOptions = new MarkerOptions()
                        .position(endLocation)
                        .title("Trip #: " + index)
                        .snippet("Duration: " + polylineData.getLeg().duration)
                        .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.footblue_icon));
                destMarker = mMap.addMarker(markerOptions);
                destMarker.showInfoWindow();
            }

            else{
                polylineData.getPolyline().setColor(ContextCompat.getColor(getApplicationContext(), R.color.lightGrey));
                polylineData.getPolyline().setZIndex(0);
            }
        }
    }
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}


