package com.example.driverapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.driverapp.credentialsId.UserBalance
import com.example.driverapp.databinding.ActivityMainBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {
    private val binding:ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var j: SharedPreferences
    private val networkChangeReceiver = NetworkChangeReceiver()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        registerNetworkChangeReceiver()

        // Check initial internet connection status
        checkInternetConnection()
binding.button7.setOnClickListener {
    val intent=Intent(this@MainActivity,BikeShare::class.java)
    intent.putExtra("rOd","RiderTrip")
    startActivity(intent)
    finish()
}
        binding.button9.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder
                .setMessage("Are you sure Want to log out?")
                .setTitle("Warning")
                .setPositiveButton("Yes") { dialog, which ->
                    // Do something.
                    val k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE).edit()
                    val sharedPreferences = getSharedPreferences("login_pref", Context.MODE_PRIVATE).edit()
                    val user=k.putString("user","null")
                    val name=k.putString("name","null")
                    val surname=k.putString("surname","null")
                    val MobileNo=k.putString("mobileNo","null")
                    val Adress=k.putString("address","null")
                    val drivingbool=k.putString("booldrivinglicence","0")
                    val drivingurl=k.putString("DrvingLicenurl","null")
                    val Adharbool=k.putString("boolAdhar","0")
                    val Adharurl=k.putString("Adharurl","null")
                    val adharnumber=k.putString("AdharNumber","000000000000")
                    val panbool=k.putString("boolpan","0")
                    val Panurl=k.putString("Panurl","null")
                    val pannumber=k.putString("PanNumber","ABCCD1234A")
                    val profilebool=k.putString("boolprofille","0")
                    val Profileurl=k.putString("Profileurl","null")
                    val rcbool=k.putString("kk","0")
                    val rcurl=k.putString("rcurl","null")
                    val rcnumber=k.putString("RcNumber","000000000000")
                    k.apply()
                    auth = FirebaseAuth.getInstance()
                    sharedPreferences.putBoolean("isLoggedIn",false)
                    sharedPreferences.apply()
                    logout()
                    val intent = Intent(this, ConfirmActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .setNegativeButton("Cancel") { dialog, which ->
                    // Do something else.
                    dialog.cancel()
                }

            val dialog: AlertDialog = builder.create()
            dialog.show()

        }
        binding.textView17.setOnClickListener {
            val intent=Intent(this,UserBalance::class.java)
            intent.putExtra("through","home")
            startActivity(intent)
            finish()

        }
        binding.button8.setOnClickListener {
            val intent=Intent(this@MainActivity,DriverActivity::class.java)
            intent.putExtra("rOd","DriverTrip")
            startActivity(intent)
            finish()
        }
       binding.profile.setOnClickListener {
         startActivity(Intent(this@MainActivity,Activity3::class.java))
           finish()
        }
    }


    private val connectionStatusReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("NetworkChangeReceiver", "Network state changed")
            val isConnected = intent?.getBooleanExtra("isConnected", false) ?: false
           //Toast.makeText(this@MainActivity, "$isConnected", Toast.LENGTH_SHORT).show()
            if (isConnected) {
                // Internet connection is available
                binding.status.text = "Let's go"
            } else {
                // Internet connection is not available
                binding.status.text = "You are offline"
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        // Unregister network change receiver to prevent memory leaks
        unregisterNetworkChangeReceiver()
    }

    private fun checkInternetConnection() {
        val isConnected = isNetworkConnected()
        updateUI(isConnected)
    }

    private fun isNetworkConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network)
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        }
    }

    private fun registerNetworkChangeReceiver() {
        val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeReceiver, intentFilter)
    }

    private fun unregisterNetworkChangeReceiver() {
        unregisterReceiver(networkChangeReceiver)
    }

    private fun updateUI(isConnected: Boolean) {
        if (isConnected) {
            binding.status.text = "Let's go"
        } else {
            binding.status.text = "You are offline"
        }
    }
    private fun logout() {
        // Firebase Logout
        auth.signOut()

    }
}