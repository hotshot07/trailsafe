package com.example.trailsafe;

import android.content.Context;
import android.content.Intent;
//import kotlinx.android.synthetic.main.activity_maps
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.maps.model.LatLng;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "LocationsDocs";
    private Button btn_next;
    private ImageButton btn_settings;
    private ImageButton btn_profile;
    double x = 0;
    int y = 0;
    LatLng startLatLng = null;
    LatLng finishLatLng = null;
    EditText finishPoint;
    EditText startPoint;
    List<Address> addressList = null;


    // Create a new Places client instance.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_next = (Button) findViewById(R.id.NextButton);
        btn_profile = (ImageButton) findViewById(R.id.Profile);
        if(! Places.isInitialized()){
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_api_key));
        }


        // Initialize the AutocompleteSupportFragments.
        AutocompleteSupportFragment startAutocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.start_autocomplete_fragment);

    // Specify the types of place data to return.
        startAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

    // Set up a PlaceSelectionListener to handle the response.
        startAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                //Toast.makeText(getApplicationContext(), place.getName().toString(),Toast.LENGTH_LONG).show();
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    addressList = geocoder.getFromLocationName(place.getName().toString(), 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = addressList.get(0);
                com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(address.getLatitude(), address.getLongitude());
                startLatLng = new LatLng(latLng.latitude,latLng.longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }


        });

        // Initialize the AutocompleteSupportFragments.
        AutocompleteSupportFragment finishAutocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.finish_autocomplete_fragment);

        // Specify the types of place data to return.
        finishAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        finishAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                Toast.makeText(getApplicationContext(), place.getName().toString(),Toast.LENGTH_LONG).show();
                Geocoder geocoder = new Geocoder(getApplicationContext());
                try {
                    addressList = geocoder.getFromLocationName(place.getName().toString(), 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Address address = addressList.get(0);
                com.google.android.gms.maps.model.LatLng latLng = new com.google.android.gms.maps.model.LatLng(address.getLatitude(), address.getLongitude());
                finishLatLng = new LatLng(latLng.latitude,latLng.longitude);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }


        });


        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                while(x < 2000000){
                    y++;
                    x = System.currentTimeMillis();
                }
                //convert user inputs for start and end points to strings
                //check if the input is valid
                if (startLatLng != null && finishLatLng != null) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("OriginLat", startLatLng.lat);
                    intent.putExtra("OriginLong", startLatLng.lng);
                    intent.putExtra("DestLat", finishLatLng.lat);
                    intent.putExtra("DestLong", finishLatLng.lng);
                    startActivity(intent);
                }
                else{
                    Toast t = Toast.makeText(getApplicationContext(),"Please Enter Valid Start and End Point",Toast.LENGTH_SHORT);
                    t.show();
                }

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






        