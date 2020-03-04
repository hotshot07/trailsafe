package com.example.trailsafe

import android.content.Context
import android.content.Intent
//import kotlinx.android.synthetic.main.activity_maps
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.Button
import android.widget.ImageButton
import android.widget.Spinner


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btn_next = findViewById<Button>(R.id.NextButton)

        btn_next.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        var btn_settings = findViewById<ImageButton>(R.id.Settings)

        btn_settings.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }



    }


}
