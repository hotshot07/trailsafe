package com.example.trailsafe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class CountDown extends AppCompatActivity {
    private static final long START_MILLIS = 10000;
    private TextView mCountDownText;
    private Button mButtonStartStop;
    private Button mButtonAddTime;
    private Button mButtonIAmOkay;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerBoolean;
    private long mTimeLeftMillis = START_MILLIS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_countdown_timer);

        mCountDownText = findViewById(R.id.text_view_countdown);

        mButtonStartStop = findViewById(R.id.start_stop_button);
        mButtonAddTime = findViewById(R.id.add_time_button);
        mButtonIAmOkay = findViewById(R.id.all_ok_button);


        mButtonStartStop.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                if(mTimerBoolean){
                    pauseTimer();
                }else{
                    startTimer();
                }
            }
        });

        mButtonAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                addTime();
                updatedCountdownTimerText();
            }
        });

        mButtonIAmOkay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeLeftMillis = 0;
                pauseTimer();
                updatedCountdownTimerText();
                mCountDownText.setText("ALL OK");

                // RETURN TO MAIN ACTIVITY.
            }
        });
    startTimer();
    updatedCountdownTimerText();


    }

private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftMillis = millisUntilFinished;
                updatedCountdownTimerText();
            }

            @Override
            public void onFinish() {
                mTimerBoolean = false;
                mTimeLeftMillis = 0;
                updatedCountdownTimerText();
                openDialog();
            }
        }.start();

        mTimerBoolean = true;
    }

    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerBoolean = false;
    }

    private void addTime(){
        pauseTimer();
        mTimeLeftMillis = mTimeLeftMillis + 1000 * 60;
        startTimer();
    }

    private void updatedCountdownTimerText(){
        int mins = (int)mTimeLeftMillis/(1000 * 60);
        int secs = ((int)mTimeLeftMillis/1000) % 60;

        String updatedCountdownText = String.format(Locale.getDefault(),"%02d:%02d", mins, secs);
        mCountDownText.setText((updatedCountdownText));
    }

    private void openDialog(){
        myDialog outOfTimeDialog = new myDialog();
        // Contact help here.
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage("00353857082526",
                null,
                "HELP needed.",
                null,
                null);
        // ----
        outOfTimeDialog.show(getSupportFragmentManager(), "dialog");
    }
}
