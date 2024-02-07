package com.imam.service


import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.content.Intent
import android.net.VpnService
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import android.util.Log


class MainActivity : AppCompatActivity() {
private val TAG = "YourActivity"
        private val prepareVpnLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            // Permission granted, you can start your VPN service here
            Log.d(TAG, "VPN permission granted")
        } else {
            // Permission denied or user canceled
            Log.d(TAG, "VPN permission denied")
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
  
  
        askPermission()

        val btn: Button = findViewById(R.id.btn)

        btn.setOnClickListener {
            startNotificationService()
        }


    }
    
    
    fun askPermission() {
        

        val intent = VpnService.prepare(this)
        intent?.let {
            prepareVpnLauncher.launch(it)
        }
    }

    private fun startNotificationService() {
        val serviceIntent = Intent(this, MyBackgroundService::class.java)
        ContextCompat.startForegroundService(this, serviceIntent)
    }
}