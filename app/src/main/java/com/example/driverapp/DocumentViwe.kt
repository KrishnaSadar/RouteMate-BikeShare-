package com.example.driverapp

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.driverapp.databinding.ActivityDocumentViweBinding

class DocumentViwe : AppCompatActivity() {
    private val binding:ActivityDocumentViweBinding by lazy{
        ActivityDocumentViweBinding.inflate(layoutInflater)
    }
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val profileurl=intent.getStringExtra("url")
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Fetching Document...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        Glide.with(this)
            .load(profileurl) // Replace with the URL of the profile picture
            .placeholder(R.drawable.baseline_account_circle_24) // Placeholder image while loading
            .error(R.drawable.baseline_account_circle_24) // Image to display if loading fails
            .into(binding.documentviwe)
        progressDialog.dismiss()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,Activity3::class.java))
        finish()
    }
}