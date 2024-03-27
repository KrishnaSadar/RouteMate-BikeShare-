package com.example.driverapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.RatingBar
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import com.example.driverapp.ongoingTrip.OngoingWay
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class RatingActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rating)

        val rBar = findViewById<RatingBar>(R.id.ratingBar)
        val button = findViewById<Button>(R.id.button18)
        if (rBar != null) {
            val k=intent.getStringExtra("other")
            val m=intent.getStringExtra("ongoingkey")
            button.setOnClickListener {
                updateBalance()
                val ongoing=FirebaseDatabase.getInstance().getReference("OnGoingTrip").child(m.toString())
                ongoing.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!snapshot.exists()){
                    MotionToast.createToast(this@RatingActivity,
                        "Oh!",
                        "Trip is finished by your routmate",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@RatingActivity, www.sanju.motiontoast.R.font.montserrat_bold))
                    //Toast.makeText(this@RatingActivity, "Trip is finished by your routmate", Toast.LENGTH_SHORT).show()
                    val msg = rBar.rating.toString()
                    val userRef = FirebaseDatabase.getInstance().getReference("users").child(k.toString())
                        .child("details").child("Rating")
                    userRef.setValue(msg)
                        val rate=Intent(this@RatingActivity,MainActivity::class.java)
                        startActivity(rate)
                       finish()

                }
            }
            override fun onCancelled(error: DatabaseError) {

            }
                })
                ongoing.removeValue().addOnCompleteListener {
                    val msg = rBar.rating.toString()
                    val userRef = FirebaseDatabase.getInstance().getReference("users").child(k.toString()).child("details").child("Rating")
                    userRef.setValue(msg)
                    val rate=Intent(this@RatingActivity, MainActivity::class.java)
                    startActivity(rate)
                    finish()

                }.
                addOnFailureListener {
                    MotionToast.createToast(this@RatingActivity,
                        "Alredy finished!",
                        "Trip is finished by your routmate",
                        MotionToastStyle.WARNING,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@RatingActivity, www.sanju.motiontoast.R.font.montserrat_bold))
                    //Toast.makeText(this, "Alredy finished", Toast.LENGTH_SHORT).show()
                    val msg = rBar.rating.toString()
                    val userRef = FirebaseDatabase.getInstance().getReference("users").child(k.toString()).child("details").child("Rating")
                    userRef.setValue(msg)
                    val rate=Intent(this@RatingActivity, MainActivity::class.java)
                    startActivity(rate)
                    this.finish()

                }
                //rating part

                }
            }
            }
    private fun updateBalance() {
        val driverpayment = getSharedPreferences("Driver", MODE_PRIVATE)
        val driver = driverpayment.getString("driverId", "null")
        if (driver != "null") {
            val userRef = FirebaseDatabase.getInstance().getReference("users").child(driver.toString()).child("details")
            userRef.child("IsPayment").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val m = snapshot.getValue(String::class.java)
                        val current = m?.toInt()?.plus(10)
                        if (current != null) {
                            userRef.child("IsPayment").setValue(current.toString())
                                .addOnSuccessListener {
                                    // Successfully updated balance
                                    Log.d("Firebase", "Balance updated successfully")
                                }
                                .addOnFailureListener { e ->
                                    // Failed to update balance
                                    Log.e("Firebase", "Failed to update balance", e)
                                }
                        }
                    } else {
                        // "IsPayment" node does not exist, set it to default value
                        userRef.child("IsPayment").setValue("10")
                            .addOnSuccessListener {
                                // Successfully set default balance
                                Log.d("Firebase", "Default balance set successfully")
                            }
                            .addOnFailureListener { e ->
                                // Failed to set default balance
                                Log.e("Firebase", "Failed to set default balance", e)
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle onCancelled event
                    Log.e("Firebase", "Database operation cancelled", error.toException())
                }
            })
        } else {
            MotionToast.createToast(this@RatingActivity,
                "Null Exception",
                "Driver Id is null",
                MotionToastStyle.ERROR,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this@RatingActivity, www.sanju.motiontoast.R.font.montserrat_bold))
            //Toast.makeText(this, "Driver Id is null", Toast.LENGTH_SHORT).show()
        }
    }
    }
