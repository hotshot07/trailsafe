package com.example.trailsafe

import androidx.appcompat.app.AppCompatActivity

import android.media.Image
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView

import org.w3c.dom.Text

class DriveMode : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_drive_mode)
        Log.d(TAG, "onCreate: ")

        //----------pictures and set them
        val ProfileView = findViewById<ImageView>(R.id.ProfileView) as ImageView
        val MovementView = findViewById<ImageView>(R.id.MovementView) as ImageView
        val AccelerationView = findViewById<ImageView>(R.id.AccelerationView) as ImageView
        val SpeedView = findViewById<ImageView>(R.id.SpeedView) as ImageView
        val DistanceView = findViewById<ImageView>(R.id.DistanceView) as ImageView

        //var is for something that changes
        var currentImage = resources.getIdentifier("@drawable/default_profile_image", null, this.packageName)
        ProfileView.setImageResource(currentImage)
        currentImage = resources.getIdentifier("@drawable/acceleration_image", null, this.packageName)
        MovementView.setImageResource(currentImage)
        AccelerationView.setImageResource(currentImage)
        SpeedView.setImageResource(currentImage)
        DistanceView.setImageResource(currentImage)


        //------------ text headings
        //val is for something that does not change
        val MovingText = findViewById<TextView>(R.id.MovingText) as TextView
        val AccelerationText = findViewById<TextView>(R.id.AccelerationText) as TextView
        val SpeedText = findViewById<TextView>(R.id.SpeedText) as TextView
        val DistanceText = findViewById<TextView>(R.id.Distancetext) as TextView


    }

    companion object {

        private val TAG = "DriveMode"
    }


}

