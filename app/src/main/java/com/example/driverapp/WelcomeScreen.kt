package com.example.driverapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.driverapp.databinding.ActivitySplashScreenBinding
import com.example.driverapp.databinding.ActivityWelcomeScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class WelcomeScreen : AppCompatActivity() {
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var k:SharedPreferences
    private val binding: ActivityWelcomeScreenBinding by lazy {
        ActivityWelcomeScreenBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")
        sharedPreferences = getSharedPreferences("login_pref", Context.MODE_PRIVATE)
        binding.continue1.setOnClickListener {
            if(checkInternetConnection()){
            if(sharedPreferences.getBoolean("isLoggedIn", false)){
                k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)

                val user=k.getString("user","null")
                //Toast.makeText(this@WelcomeScreen, "$user", Toast.LENGTH_SHORT).show()
              checkDocumentUploadStatus(user.toString())
                }else{
                startActivity(Intent(this,ConfirmActivity::class.java))
                finish()
            }

        }else{
                MotionToast.createToast(this,
                    "Oops!",
                    "Please check your internet connection",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "Please check your internet connection", Toast.LENGTH_SHORT).show()
        }}
    }
    private fun checkDocumentUploadStatus(uid: String) {
        database.child(uid).child("details").addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isDocumentsUploaded = snapshot.child("accountstatus").value as? String
               // Toast.makeText(this@WelcomeScreen, "$isDocumentsUploaded", Toast.LENGTH_SHORT).show()
                if (isDocumentsUploaded == "true") {
                    // Documents are uploaded, navigate to MainActivity

                   startActivity(Intent(this@WelcomeScreen, MainActivity::class.java))
                    finish()
                } else {
                    // Documents are not uploaded, navigate to AfterUploadingAllDocument activity

                    startActivity(Intent(this@WelcomeScreen, AfterUploadingAllDocument::class.java))
                    finish()
                }
                // Finish the current activity
            }
            override fun onCancelled(error: DatabaseError) {
                MotionToast.createToast(this@WelcomeScreen,
                    "Error!",
                    "Error: ${error.message}",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@WelcomeScreen, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this@WelcomeScreen, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun checkInternetConnection(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            val isConnected = networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) ?: false
            if (isConnected) {
                // Internet connection is available
               return true
            } else {
                // Internet connection is not available
                return false
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            val isConnected = networkInfo?.isConnected ?: false
            if (isConnected) {
                // Internet connection is available
                return true
            } else {
                // Internet connection is not available
                return false
            }
        }
    }
}