package com.example.driverapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class securityandprivacy : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_securityandprivacy)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,Activity3::class.java))
        finish()
    }
}