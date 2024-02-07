package com.imam.service

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val btn: Button = findViewById(R.id.btn)

        btn.setOnClickListener {
            startNotificationService()
        }


    }

    private fun startNotificationService() {
        val serviceIntent = Intent(this, MyBackgroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}