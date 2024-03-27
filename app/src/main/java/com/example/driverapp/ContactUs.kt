package com.example.driverapp

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.driverapp.databinding.ActivityContactUsBinding
import com.example.driverapp.databinding.ActivityPanCardBinding
import com.google.firebase.database.FirebaseDatabase
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class ContactUs : AppCompatActivity() {
    private val binding:ActivityContactUsBinding by lazy {
        ActivityContactUsBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.mail.setOnClickListener {
       sendEmail(this,"sadarkrishna985@gmail.com"," "," ")
        }
        binding.number.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            startActivity(intent)
        }
        binding.buttonSubmitFeedback.setOnClickListener {
            var feedback = binding.editTextText2.text
            val db = FirebaseDatabase.getInstance().getReference("feedback").push()

            db.setValue(feedback.toString())
                .addOnCompleteListener {
                    MotionToast.createToast(this@ContactUs,
                        "Done",
                        "feedback submitted successfully",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@ContactUs, www.sanju.motiontoast.R.font.montserrat_bold))
                    //Toast.makeText(this, "feedback submitted successfully", Toast.LENGTH_SHORT).show()
                    binding.editTextText2.setText(" ")
                }
                .addOnFailureListener {
                    MotionToast.createToast(this@ContactUs,
                        "Oops!",
                        "something went wrong",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@ContactUs, www.sanju.motiontoast.R.font.montserrat_bold))
                    //Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show()
                }
        }
    }
    fun sendEmail(context: Context, emailAddress: String, subject: String, body: String) {
        // Create an intent with action ACTION_SEND
        val intent = Intent(Intent.ACTION_SEND)
        // Set the type of data to send (email)
        intent.type = "message/rfc822"
        // Set the recipient email address
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(emailAddress))
        // Set the email subject
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        // Set the email body
        intent.putExtra(Intent.EXTRA_TEXT, body)
        // Check if there's an email client available to handle this intent
        if (intent.resolveActivity(context.packageManager) != null) {
            // Start the activity with the email intent
            context.startActivity(intent)
        } else {
            // If no email client is available, show a toast or handle the error accordingly
            MotionToast.createToast(this@ContactUs,
                "Oops!",
                "No email client found",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this@ContactUs, www.sanju.motiontoast.R.font.montserrat_bold))
            //Toast.makeText(context, "No email client found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,Activity3::class.java))
        finish()
    }
}