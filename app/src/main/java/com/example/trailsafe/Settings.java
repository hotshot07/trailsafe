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
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Settings extends AppCompatActivity {

    private static final String TAG = "SettingDoc";
    private static final String filename = "settingsFile";
    private static final String file2 = "contactFile";

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


        //try to create a cache file
//        try {
//            File.createTempFile(filename, null, getCacheDir());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        File cacheFile = new File(getCacheDir(), filename);


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


        //now read from local cache and make spinner according to that
        String value = load_preferences();
        toastMessage(value);
//        int pos = selectSpinnerItemByValue(mySpinner, value);

//        mySpinner.post(new Runnable() {
//            @Override
//            public void run() {
//                mySpinner.setSelection(pos);
//            }
//        });

        //setSavedItem(mySpinner, 1);


        //load contact number
//        DocumentReference docRef = db.collection("User Settings").document("david@test.com");
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//                settingsClass contactNo = documentSnapshot.toObject(settingsClass.class);
//
//                EditText editText = (EditText)findViewById(R.id.emergeny_contact);
//                editText.setText(contactNo.emergencyNumber, TextView.BufferType.EDITABLE);
//            }
//        });


        //load from local internal storage
        String emergencyNumber = load_phone();
        EditText editText = (EditText)findViewById(R.id.emergeny_contact);
        editText.setText(emergencyNumber, TextView.BufferType.EDITABLE);


        backButton = (ImageButton)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Settings.super.finish();
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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

//
                //call storing function
                storeContact();
                storeSettingData();
                save(additionalData);
            }
        });


    }

    private String load_phone() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(file2);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            return(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private void storeContact() {
        EditText Number = (EditText)findViewById(R.id.emergeny_contact);
        String value = Number.getText().toString();

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(file2, MODE_PRIVATE);
            fos.write(value.getBytes());

            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + filename,
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setSavedItem(final Spinner mySpinner, final int pos) {
        mySpinner.post(new Runnable() {
            @Override
            public void run() {
                mySpinner.setSelection(pos);
            }
        });
    }

    public static int selectSpinnerItemByValue(Spinner spnr, String value) {
        SimpleCursorAdapter adapter = (SimpleCursorAdapter) spnr.getAdapter();
        for (int position = 0; position < adapter.getCount(); position++) {
            if(value.equals(spnr.getItemAtPosition(position))) {

//                final int finalPosition = position;
//                spnr.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        spnr.setSelection(finalPosition);
//                    }
//                });

                return position;
            }
        }
//        toastMessage("No item found");
        return -1;
    }

    private String load_preferences() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(filename);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null) {
                sb.append(text).append("\n");
            }

            return(sb.toString());

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    private void save(String data[]) {

        String preferences = data[0];

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(filename, MODE_PRIVATE);
            fos.write(preferences.getBytes());

            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + filename,
                    Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class settingsClass {
        private String emergencyNumber;
        private Date thatTime;

        public settingsClass() {
        }

        public settingsClass(String emergencyNumber, Date thatTime) {
            this.emergencyNumber = emergencyNumber;
            this.thatTime = thatTime;
        }

        public String getEmergencyNumber() {
            return emergencyNumber;
        }

        public void setEmergencyNumber(String emergencyNumber) {
            this.emergencyNumber = emergencyNumber;
        }

        public Date getThatTime() {
            return thatTime;
        }

        public void setThatTime(Date thatTime) {
            this.thatTime = thatTime;
        }
    }

    private void readSettings() {

    }


    public void storeSettingData() {
            phoneNumber = (EditText)findViewById(R.id.emergeny_contact);
            String value = phoneNumber.getText().toString();

            Map<String, Object> settingType = new HashMap<>();

            Map<String, Object> settingObject = new HashMap<>();

//            settingObject.put("Distance Units", additionalData[0]);
//
//            settingObject.put("Timer Length (minutes)", additionalData[1]);

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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}


