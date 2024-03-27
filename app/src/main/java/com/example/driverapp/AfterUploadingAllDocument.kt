package com.example.driverapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.driverapp.databinding.ActivityAfterUploadingAllDocumentBinding
import com.example.driverapp.databinding.ActivityDrivingLicenceBinding

class AfterUploadingAllDocument : AppCompatActivity() {
    private val binding: ActivityAfterUploadingAllDocumentBinding by lazy{
        ActivityAfterUploadingAllDocumentBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.back7.setOnClickListener {
            startActivity(Intent(this,Login_Screen::class.java))
            finish()
        }
    }
}