package com.example.trailsafe

import android.content.Context
import android.content.Intent
//import kotlinx.android.synthetic.main.activity_maps
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var btn_next = findViewById<Button>(R.id.NextButton)

        btn_next.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        }


}
