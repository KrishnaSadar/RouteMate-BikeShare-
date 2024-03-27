package com.example.driverapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.driverapp.credentialsId.UserBalance
import com.example.driverapp.databinding.Activity3Binding
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class Activity3 : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    private val binding: Activity3Binding by lazy {
        Activity3Binding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var googleApiClient: GoogleApiClient
    private val courses = arrayOf("Document", "Adhar card", "Pan Card", "RC", "DrivingLicence", "Profile Photo")
    private lateinit var j: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        j=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
        val name=j.getString("name","null")
        val surname=j.getString("surname","null")
        val MobileNo=j.getString("mobileNo","null")
        val Adress=j.getString("address","null")
        val userId=j.getString("user","null")
        // Set click listener on the button
        // Take the instance of Spinner from binding
        val spin = binding.button10

        // Create the instance of ArrayAdapter
        val ad: ArrayAdapter<*> = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            courses
        )

        // Set simple layout resource file for each item of spinner
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter data on the Spinner
        spin.adapter = ad
        spin.onItemSelectedListener = this
        // Apply OnItemSelectedListener on the Spinner

        // Set user's name, mobile number, and address
        binding.profilename.text = "$name $surname" // Replace with user's name
        binding.profilenumber.text = "Mo_No:$MobileNo\n$Adress" // Replace with user's mobile number and address
        val Profileurl=j.getString("Profileurl","null")
        // Load user's profile picture using Glide library
        val pic= FirebaseDatabase.getInstance().getReference("users").child(userId.toString()).child("details").child("profilepic")
        pic.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                Glide.with(this@Activity3)
                    .load(snapshot.getValue(String::class.java)) // Replace with the URL of the profile picture
                    .placeholder(R.drawable.baseline_account_circle_24) // Placeholder image while loading
                    .error(R.drawable.baseline_account_circle_24) // Image to display if loading fails
                    .into(binding.profilepic)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })


        // Set click listener on profile picture to show it in full-screen mode
        val sharedPreferences = getSharedPreferences("login_pref", Context.MODE_PRIVATE).edit()
        val k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE).edit()
        binding.paymenthistory.setOnClickListener {
            val intent = Intent(this@Activity3, UserBalance::class.java)
            startActivity(intent)
            finish()
        }
        binding.contactus.setOnClickListener {
            val intent = Intent(this@Activity3, ContactUs::class.java)
            startActivity(intent)
            finish()
        }
     binding.logout.setOnClickListener {
         val builder: AlertDialog.Builder = AlertDialog.Builder(this)
         builder
             .setMessage("Are you sure Want to log out?")
             .setTitle("Warning")
             .setPositiveButton("Yes") { dialog, which ->
                 // Do something.
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
                 auth = FirebaseAuth.getInstance()
                 sharedPreferences.putBoolean("isLoggedIn",false)
                 sharedPreferences.apply()
                 logout()
                 val intent = Intent(this@Activity3, ConfirmActivity::class.java)
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
        binding.securityandprivacy.setOnClickListener {
            val intent = Intent(this@Activity3, securityandprivacy::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // Make a toast of the selected course
        //"Document", "Adhar card", "Pan Card", "RC", "DrivingLicence", "Profile Photo"
        j=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
        if(position==1){
            val url=j.getString("Adharurl","null")
            val intent=Intent(this,DocumentViwe::class.java)
            intent.putExtra("url",url)
            startActivity(intent)
            finish()
        }
        if(position==2){
            val url=j.getString("Panurl","null")
            val intent=Intent(this,DocumentViwe::class.java)
            intent.putExtra("url",url)
            startActivity(intent)
            finish()
        }
        if(position==3){
            val url=j.getString("rcurl","null")
            val intent=Intent(this,DocumentViwe::class.java)
            intent.putExtra("url",url)
            startActivity(intent)
            finish()
        }
        if(position==4){
            val url=j.getString("DrvingLicenurl" +
                    "finish()","null")
            val intent=Intent(this,DocumentViwe::class.java)
            intent.putExtra("url",url)
            startActivity(intent)
            finish()
        }
        if(position==5){
            val url=j.getString("Profileurl","null")
            val intent=Intent(this,DocumentViwe::class.java)
            intent.putExtra("url",url)
            startActivity(intent)
            finish()
        }
    }
    private fun logout() {
        // Firebase Logout
        auth.signOut()

        // Google Logout
//        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback {
//            // After logging out, start ConfirmActivity
//            val intent = Intent(this@MainActivity, ConfirmActivity::class.java)
//            startActivity(intent)
//            finish() // Close MainActivity
//        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}
}
