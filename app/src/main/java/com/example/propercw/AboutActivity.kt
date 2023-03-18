package com.example.propercw

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AboutActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        var back_button = findViewById<Button>(R.id.back_button)            //backbutton

        back_button.setOnClickListener{
            var intent = Intent(this, MainActivity::class.java)         //simple back to the main activity intent
            startActivity(intent)
        }
    }
}