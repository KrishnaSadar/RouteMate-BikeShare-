package com.example.driverapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.driverapp.credentialsId.BasicDetails
import com.example.driverapp.databinding.ActivityConfirmBinding
import com.example.driverapp.databinding.ActivityEmaillUsernamePasswordBinding
import com.google.firebase.auth.FirebaseAuth
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class EmaillUsernamePassword : AppCompatActivity() {
    private val binding: ActivityEmaillUsernamePasswordBinding by lazy {
        ActivityEmaillUsernamePasswordBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.signUp.setOnClickListener {
            val username=binding.Username.text.toString()
            val password=binding.password.text.toString()
            val repetepasword=binding.repetepassword.text.toString()
            if(username.isEmpty()||password.isEmpty()||repetepasword.isEmpty()){
                MotionToast.createToast(this@EmaillUsernamePassword,
                    "blanks!",
                    "please fill all the mandatory field",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@EmaillUsernamePassword, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "plese fill all the mandetory field", Toast.LENGTH_SHORT).show()
            }else{
                if(password==repetepasword){
                    val email=intent.getStringExtra("email").toString()
                    auth = FirebaseAuth.getInstance()
                    auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                        if(it.isSuccessful){
                            Toast.makeText(this, "Registration succesfull", Toast.LENGTH_SHORT).show()
                            val uid=auth.uid.toString()
                            val intent=Intent(this,BasicDetails::class.java)
                            val editor=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE).edit()
                            editor.putString("user",uid.toString())
                            editor.apply()
                            val k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE).edit()
                            val sharedPreferences = getSharedPreferences("login_pref", Context.MODE_PRIVATE).edit()
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
                            startActivity(intent)
                            finish()

                        }else{
                            MotionToast.createToast(this@EmaillUsernamePassword,
                                "Registration failed",
                                "Registration failed",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@EmaillUsernamePassword, www.sanju.motiontoast.R.font.montserrat_bold))
                            //Toast.makeText(this, "Registration failed", Toast.LENGTH_SHORT).show()
                        }
                    }.addOnFailureListener {
                        MotionToast.createToast(this@EmaillUsernamePassword,
                            "Error",
                            it.localizedMessage.toString(),
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@EmaillUsernamePassword, www.sanju.motiontoast.R.font.montserrat_bold))
                        //Toast.makeText(this, it.localizedMessage.toString(), Toast.LENGTH_SHORT).show()
                    }
                }else{
                    MotionToast.createToast(this@EmaillUsernamePassword,
                        "Error",
                        "password should be same",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@EmaillUsernamePassword, www.sanju.motiontoast.R.font.montserrat_bold))
                    Toast.makeText(this, "password should be same", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}