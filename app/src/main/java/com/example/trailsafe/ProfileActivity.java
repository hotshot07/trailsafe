package com.example.trailsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ProfileActivity extends AppCompatActivity {

    private static final String name_file = "NameFile";
    private static final String DOB_file = "DateOfBirthFile";
    private static final String Gender_file = "GenderFile";

    public ImageButton backButton;
    public Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);


        String name = loadName();
        EditText editText = (EditText)findViewById(R.id.editText);
        editText.setText(name, TextView.BufferType.EDITABLE);

        String DOB = loadDob();
        EditText editText2 = (EditText)findViewById(R.id.editText4);
        editText2.setText(DOB, TextView.BufferType.EDITABLE);

        String Gnd = loadGender();
        EditText editText3 = (EditText)findViewById(R.id.editText5);
        editText3.setText(Gnd, TextView.BufferType.EDITABLE);


        backButton = (ImageButton)findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProfileActivity.super.finish();
                overridePendingTransition(R.anim.slide_in_down,R.anim.slide_out_up);
            }
        });

        //declaring save button
        saveButton = (Button) findViewById(R.id.button2);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //call storing function
                saveProfileName();
                saveProfileDob();
                saveProfileGender();
                toastMessage("SAVED!");
            }
        });
    }

    private String loadName() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(name_file);
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

    private void saveProfileName() {
        EditText Name = (EditText)findViewById(R.id.editText);
        String value = Name.getText().toString();

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(name_file, MODE_PRIVATE);
            fos.write(value.getBytes());

//            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + name_file,
//                    Toast.LENGTH_LONG).show();
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

    private String loadDob() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(DOB_file);
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

    private void saveProfileDob() {
        EditText Dob = (EditText)findViewById(R.id.editText4);
        String value = Dob.getText().toString();

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(DOB_file, MODE_PRIVATE);
            fos.write(value.getBytes());

//            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + DOB_file,
//                    Toast.LENGTH_LONG).show();
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

    private String loadGender() {
        FileInputStream fis = null;

        try {
            fis = openFileInput(Gender_file);
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

    private void saveProfileGender() {
        EditText gender = (EditText)findViewById(R.id.editText5);
        String value = gender.getText().toString();

        FileOutputStream fos = null;

        try {
            fos = openFileOutput(Gender_file, MODE_PRIVATE);
            fos.write(value.getBytes());

//            Toast.makeText(this, "Saved to " + getFilesDir() + "/" + name_file,
//                    Toast.LENGTH_LONG).show();
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


    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_down, R.anim.slide_out_up);
    }
}
