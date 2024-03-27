package com.example.driverapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.driverapp.credentialsId.AdharCard
import com.example.driverapp.credentialsId.DrivingLicence
import com.example.driverapp.credentialsId.PanCard
import com.example.driverapp.credentialsId.ProfilePhoto
import com.example.driverapp.credentialsId.RegistrationActivity
import com.example.driverapp.databinding.ActivityLegalAuthenticationBinding
import com.example.driverapp.databinding.ActivityWelcomeScreenBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.FirebaseMessaging
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class legalAuthenticationActivity : AppCompatActivity() {

    private lateinit var database: DatabaseReference
    private lateinit var k:SharedPreferences
    private lateinit var n:SharedPreferences

    private val binding: ActivityLegalAuthenticationBinding by lazy {
        ActivityLegalAuthenticationBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        FirebaseMessaging.getInstance().subscribeToTopic("all")
// FCM settings for a particular user

        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    // Use the token as needed
                    val token = task.result.toString()
                    // Use the token as needed
                    val j = getSharedPreferences("Tokens", MODE_PRIVATE).edit()
                    j.putString("token", token)
                    j.apply()
                } else {
                    // Handle token generation failure
                    MotionToast.createToast(this@legalAuthenticationActivity,
                        "Error in notification service",
                        "Unable to find token",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@legalAuthenticationActivity, www.sanju.motiontoast.R.font.montserrat_bold))
                    //Toast.makeText(this@legalAuthenticationActivity, "Unable to find token", Toast.LENGTH_LONG).show()
                }
            }
        k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
n=getSharedPreferences("Tokens", MODE_PRIVATE)
        val t1=n.getString("token","null")
        val user=k.getString("user","null")
        val name=k.getString("name","null")
        val surname=k.getString("surname","null")
        val MobileNo=k.getString("mobileNo","null")
        val Adress=k.getString("address","null")
binding.textView3.setText("Welcome $name")
       //drivinglicencecheck
        val drivingbool=k.getString("booldrivinglicence","0")
        val drivingurl=k.getString("DrvingLicenurl","null")
        if(drivingbool=="1"){
            binding.drvingtick.setImageResource(R.drawable.rightick)
        }
        //Adharcard
        val Adharbool=k.getString("boolAdhar","0")
        val Adharurl=k.getString("Adharurl","null")
        val adharnumber=k.getString("AdharNumber","000000000000")
        if(Adharbool=="1"){
            binding.adhartick.setImageResource(R.drawable.rightick)
        }
        //pancard
        val panbool=k.getString("boolpan","0")
        val Panurl=k.getString("Panurl","null")
        val pannumber=k.getString("PanNumber","ABCCD1234A")
        if(panbool=="1"){
            binding.pantick.setImageResource(R.drawable.rightick)
        }
        //profilephoto
        val profilebool=k.getString("boolprofille","0")
        val Profileurl=k.getString("Profileurl","null")
        if(profilebool=="1"){
            binding.phototick.setImageResource(R.drawable.rightick)
        }
        //rcphoto
        val rcbool=k.getString("kk","0")
        val rcurl=k.getString("rcurl","null")
        val rcnumber=k.getString("RcNumber","000000000000")
        if(rcbool=="1"){
            binding.rctick.setImageResource(R.drawable.rightick)
        }

        //if everything is uploaded
        if(rcbool=="1"&&profilebool=="1"&&panbool=="1"&&Adharbool=="1"&&drivingbool=="1"){
            database = FirebaseDatabase.getInstance().getReference("users")
            val userRef = database.child(user.toString())


            val userdetails=User(user,name,surname,MobileNo,Adress,Adharurl,adharnumber,drivingurl,pannumber,Panurl,Profileurl,rcurl,rcnumber,"false",t1)

            val db=userRef.child("details")
            db.setValue(userdetails)
                  .addOnCompleteListener {
                          val intent=Intent(this,AfterUploadingAllDocument::class.java)
                      val editor=getSharedPreferences("login_pref", MODE_PRIVATE).edit()
                      editor.putString("isDocumentsUploaded","true")
                      editor.putString("token",t1)
                      editor.apply()
                      startActivity(intent)
                      finish()
              }.addOnFailureListener {
                      Toast.makeText(this, "fails to create account", Toast.LENGTH_SHORT).show()
              }
        }
        binding.drivinglicence.setOnClickListener {
            val intent=Intent(this,DrivingLicence::class.java)
            startActivity(intent)
            finish()
        }
        binding.pancard.setOnClickListener {
            val intent=Intent(this,PanCard::class.java)
            startActivity(intent)
            finish()
        }
        binding.profilephoto.setOnClickListener {
            val intent=Intent(this,ProfilePhoto::class.java)
            startActivity(intent)
            finish()
        }
        binding.registration.setOnClickListener {
            val intent=Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
            finish()
        }
        binding.adharcard.setOnClickListener {
            val intent=Intent(this,AdharCard::class.java)
            startActivity(intent)
            finish()
        }

    }
}