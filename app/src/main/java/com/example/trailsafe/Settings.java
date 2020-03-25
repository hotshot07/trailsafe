package com.example.trailsafe;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Settings extends AppCompatActivity {

    private static final String TAG = "SettingDocs";

    private FirebaseAuth mAuth;
    public Button signOutButton;
    public Button SaveButton;
    public ImageButton backButton;
    public EditText phoneNumber;

    public Spinner mySpinner, mySpinner2;

    public String[] additionalData;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

//    public void init() {
//
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        mySpinner = (Spinner) findViewById(R.id.spinner1);
        mySpinner2 = (Spinner) findViewById(R.id.spinner2);

        additionalData = new String[2];
        //init();

        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(Settings.this,
                            android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.units));
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(Settings.this,
                android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.timer_length));

        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);

        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter2);

        mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                additionalData[0] = mySpinner.getSelectedItem().toString();
//                        toastMessage(additionalData[0]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                additionalData[1] = mySpinner2.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        backButton = (ImageButton)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.super.finish();
            }
        });



        signOutButton = (Button) findViewById(R.id.sign_out_btn);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                toastMessage("Signing Out...");
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });


        //declaring save button
        SaveButton = (Button) findViewById(R.id.save_set_btn);
        SaveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Do something in response to button click

//                final String[] additionalData = new String[2];

//                mySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        additionalData[0] = mySpinner.getSelectedItem().toString();
////                        toastMessage(additionalData[0]);
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });

//                mySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        additionalData[1] = mySpinner2.getSelectedItem().toString();
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });

                //call storing function
                storeSettingData();
            }
        });


    }


        public void storeSettingData() {
            phoneNumber = (EditText)findViewById(R.id.emergeny_contact);
            String value = phoneNumber.getText().toString();

            Map<String, Object> settingType = new HashMap<>();

            Map<String, Object> settingObject = new HashMap<>();

            settingObject.put("Distance Units", additionalData[0]);

            settingObject.put("Timer Length (minutes)", additionalData[1]);

            settingObject.put("Emergency Contact Number", value);

            Date currentTime = Calendar.getInstance().getTime();

            settingObject.put("Time", currentTime);

            settingType.put("App Settings", settingObject);


            //Since we have only one static Location, it saves as User 1 only
            //Changing location and calling again will update the User 1 entry
            //but not create a separate entry
            db.collection("User Settings").document("david@test.com")
                    .set(settingType)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "Settings Successfully Saved");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error Writing Settings", e);
                        }
                    });
        }

    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}


