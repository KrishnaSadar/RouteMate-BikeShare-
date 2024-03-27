package com.example.driverapp.ongoingTrip

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.driverapp.BikeShare
import com.example.driverapp.R
import com.example.driverapp.Trip
import com.example.driverapp.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class RequestActivity : AppCompatActivity() {

    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        val k=intent.getStringExtra("body")
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(k.toString()).child("details")
       var username=findViewById<TextView>(R.id.textView21)
       var mobileNo=findViewById<TextView>(R.id.textView22)
       var prize=findViewById<TextView>(R.id.textView339)
        var dp=findViewById<ImageView>(R.id.imageView3)
//        if(intent.getBooleanExtra("finishActivity",false)){
//            (BikeShare::class.java as AppCompatActivity).finish()
//        }
        userRef.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        // This method is called once with the initial value and whenever data at this location is updated.
                        if (dataSnapshot.exists()) {
                            val userdetails = dataSnapshot.getValue(User::class.java)
                            if (userdetails != null) {
                                username.text="Name: ${userdetails.name.toString()}"
                                mobileNo.text="Mobile No:${userdetails.mobileNumber.toString()}"
                                prize.text="Prize: 10"
                                var k=userdetails.profilepic.toString()
                                k?.let {
                                    // Load image into ImageView using Glide
                                    Glide.with(this@RequestActivity).load(it).into(dp)
                                }
                            }

                        }
                            // Now you can use the userdetails object
            }


            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }


        })
        val forrating = FirebaseDatabase.getInstance().getReference("users").child(k.toString()).child("details").child("Rating")
        forrating.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    val rating = snapshot.getValue(String::class.java)
                    var rate=findViewById<TextView>(R.id.ratingtext)
                    rate.text="Rating:$rating"
                }


            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
var c="gj";
        findViewById<Button>(R.id.button4).setOnClickListener {
          val  n=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
           val me= n.getString("user","null")
            val ongoing = FirebaseDatabase.getInstance().getReference("OnGoingTrip").child(k.toString()+me.toString())
            ongoing.child("request").setValue("true").addOnCompleteListener {

                ongoing.child("IsPayment").addValueEventListener(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        progressDialog = ProgressDialog(this@RequestActivity)
                        progressDialog.setMessage("Waiting For Payment Conformation...")
                        progressDialog.setCancelable(true)
                        progressDialog.show()
                        c= snapshot.getValue(String::class.java).toString()
                        if(c=="true"){
                            //only go to ongoing when user succesfully payment
                            val intent=Intent(this@RequestActivity, OngoingWay::class.java)
                            intent.putExtra("other",k.toString())
                            intent.putExtra("me",me.toString())
                            intent.putExtra("ongoingkey",k.toString()+me.toString())
                            val driverpayment=getSharedPreferences("Driver", MODE_PRIVATE).edit()
                            driverpayment.putString("driverId",me.toString())
                            driverpayment.apply()
                            startActivity(intent)
                            progressDialog.dismiss()
                            finish()
                        }else{
                            MotionToast.createToast(this@RequestActivity,
                                "Wait buddy",
                                "Waiting for payment conformation",
                                MotionToastStyle.WARNING,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@RequestActivity, www.sanju.motiontoast.R.font.montserrat_bold))
                           // Toast.makeText(this@RequestActivity, "Waiting for payment conformation", Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                        //Toast.makeText(this@RequestActivity, error.toString(), Toast.LENGTH_SHORT).show()
                    }


                })


            }



        }


    }

    override fun onBackPressed() {
        super.onBackPressed()
        val k=intent.getStringExtra("body")

        val  n=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
        val me= n.getString("user","null")
        val ongoing = FirebaseDatabase.getInstance().getReference("OnGoingTrip").child(k.toString()+me.toString())
        ongoing.child("request").setValue("false")
        ongoing.removeValue()
        MotionToast.createToast(this@RequestActivity,
            "Oops!",
            "Request denied",
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this@RequestActivity, www.sanju.motiontoast.R.font.montserrat_bold))
        //Toast.makeText(this, "Request denied", Toast.LENGTH_SHORT).show()
    }
}