package com.example.driverapp.credentialsId

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.driverapp.R
import com.example.driverapp.databinding.ActivityBasicDetailsBinding
import com.example.driverapp.databinding.ActivityWelcomeScreenBinding
import com.example.driverapp.legalAuthenticationActivity
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class BasicDetails : AppCompatActivity() {
    private val binding: ActivityBasicDetailsBinding by lazy {
        ActivityBasicDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button3.setOnClickListener {
            // Get values from EditText fields
            val name = binding.firstname.text.toString()
            val surname = binding.lastname.text.toString()
            val mobileNo = binding.mobileNo.text.toString()
            val address = binding.adres.text.toString()


            // Validate each field
            if (name.isEmpty() || surname.isEmpty() || mobileNo.isEmpty() || address.isEmpty()) {
                MotionToast.createToast(this@BasicDetails,
                    "blanks!",
                    "All fields are required",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@BasicDetails, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Validate mobile number length
            if (mobileNo.length != 10) {
                MotionToast.createToast(this@BasicDetails,
                    "Wrong Format",
                    "Mobile number should be 10 digits",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@BasicDetails, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "Mobile number should be 10 digits", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // If all validations pass, create an Intent and send the details as extras
            val intent = Intent(this, legalAuthenticationActivity::class.java)
            val editor=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE).edit()
            editor.putString("name",name)
            editor.putString("surname",surname)
            editor.putString("mobileNo",mobileNo)
            editor.putString("address",address)
            editor.apply()
            // Start the next activity with the user details
            startActivity(intent)
            finish()
        }
    }
}
