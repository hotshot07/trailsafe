package com.example.trailsafe;

import android.content.Context;
import android.content.Intent;
//import kotlinx.android.synthetic.main.activity_maps
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
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
import java.lang.annotation.Target;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    static String KEY_ANIM = "TARGET_ANIM";
    static String Target_Move = "Translate";
    static String profileTarget_move = "Rotate Profile button";
    static String Target_Rotate = "Rotate";
    String target_op = Target_Move;
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
    String startLocString = null;
    String destLocString = null;


    // Create a new Places client instance.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Animation rotate = AnimationUtils.loadAnimation(this, R.anim.rotate);
        final Animation move = AnimationUtils.loadAnimation(this, R.anim.move);

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

        btn_profile.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                target_op = profileTarget_move;
                arg0.startAnimation(move);
            }
        });


        if(!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_api_key));
        }
        PlacesClient placesClient = Places.createClient(this);



        // Initialize the AutocompleteSupportFragments.
        AutocompleteSupportFragment startAutocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.start_autocomplete_fragment);

        // Specify the types of place data to return.
        startAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        startAutocompleteFragment.setHint("Start Point");

        // Set up a PlaceSelectionListener to handle the response.
        startAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                startLocString = place.getName();

                Log.i(TAG, "Latlng: " + place.getLatLng().toString());

                Toast.makeText(getApplicationContext(),startLocString,Toast.LENGTH_LONG);

                startLatLng = new LatLng(place.getLatLng().latitude,place.getLatLng().longitude);
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
        finishAutocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));
        finishAutocompleteFragment.setHint("Enter Destination");

        // Set up a PlaceSelectionListener to handle the response.
        finishAutocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());

                destLocString = place.getName();
                Log.i(TAG, "Latlng: " + place.getLatLng().toString());
                finishLatLng = new LatLng(place.getLatLng().latitude,place.getLatLng().longitude);



            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
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
                    if (startLatLng != null && finishLatLng != null) {
                        Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                        intent.putExtra("OriginName", startLocString);
                        intent.putExtra("OriginLat", startLatLng.lat);
                        intent.putExtra("OriginLng", startLatLng.lng);
                        intent.putExtra("DestString", destLocString);
                        intent.putExtra("DestLat", finishLatLng.lat);
                        intent.putExtra("DestLng", finishLatLng.lng);
                        intent.putExtra(KEY_ANIM, target_op);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    }
                    else{
                        Toast t = Toast.makeText(getApplicationContext(),"Please Enter Valid Start and End Point",Toast.LENGTH_SHORT);
                        t.show();
                    }

                }
                if(target_op == Target_Move){
                    Intent intent = new Intent(MainActivity.this, Settings.class);
                    intent.putExtra(KEY_ANIM, target_op);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
                if(target_op == profileTarget_move){
                    Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.putExtra(KEY_ANIM, target_op);
                    startActivity(intent);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //TODO Auto-genetated method stub

            }};



    public void TapToBlink(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide);
        btn_settings.startAnimation(animation);
    }

    public void TapToSlide(View view) {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.slide);
        btn_profile.startAnimation(animation);
    }



}






        