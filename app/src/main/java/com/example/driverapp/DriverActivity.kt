package com.example.driverapp

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.driverapp.databinding.ActivityBikeShareBinding
import com.example.driverapp.databinding.ActivityDriverBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class DriverActivity : AppCompatActivity() {
    private val binding: ActivityDriverBinding by lazy{
        ActivityDriverBinding.inflate(layoutInflater)
    }
    private lateinit var k: SharedPreferences
    private lateinit var database: DatabaseReference
    private lateinit var progressDialog: ProgressDialog
    private val filteredTrips: ArrayList<Trip> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val type=intent.getStringExtra("rOd")
        Log.d("err","activity launced:$type")
        binding.textView29981.visibility = View.GONE
        binding.find1.setOnClickListener {
            if(binding.startLocation1.text.isNotEmpty() && binding.endLocation1.text.isNotEmpty()){

                k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
                val user=k.getString("user","null")
                val start=binding.startLocation1.text.toString().toLowerCase()
                val end=binding.endLocation1.text.toString().toLowerCase()
                val trip=Trip(user,start,end)
                database = FirebaseDatabase.getInstance().getReference(type.toString()).child(user.toString())
                // Initialize ProgressDialog
                progressDialog = ProgressDialog(this)
                progressDialog.setMessage("Uploading trip Reqest`...")
                progressDialog.setCancelable(false)
                progressDialog.show()
                database.setValue(trip).addOnCompleteListener {
                    var target:String="RiderTrip"

                    database = FirebaseDatabase.getInstance().reference.child(target.toString())

                    // Retrieve data from Firebase and filter based on start and end locations
                    database.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            // Clear the ArrayList before adding new data
                            filteredTrips.clear()

                            // Iterate through each child node in the database
                            for (tripSnapshot in snapshot.children) {
                                val trip = tripSnapshot.getValue(Trip::class.java)
                                // Add trip to the ArrayList if start and end locations match
                                if (trip != null && trip.start ==start && trip.end==end) {
                                    filteredTrips.add(trip)
                                }
                            }

                            progressDialog.dismiss()
                            binding.textView29981.visibility = View.VISIBLE
                        }

                        override fun onCancelled(error: DatabaseError) {
                            MotionToast.createToast(this@DriverActivity,
                                "Oops!",
                                "Failed to retrieve data",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@DriverActivity, www.sanju.motiontoast.R.font.montserrat_bold))
                            //Toast.makeText(this@DriverActivity, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                        }
                    }) }
                    .addOnFailureListener {
                        MotionToast.createToast(this@DriverActivity,
                            "Oops!",
                            "Something Went Wrong",
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@DriverActivity, www.sanju.motiontoast.R.font.montserrat_bold))
                        //Toast.makeText(this@DriverActivity, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                    }



            }
            else {
                MotionToast.createToast(this@DriverActivity,
                    "Blanks!",
                    "plz fill all the field",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@DriverActivity, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this@DriverActivity, "plz fill all the field", Toast.LENGTH_SHORT).show()
            }
            binding.find1.setText("Refresh")
        }

    }
    override fun onBackPressed() {
        super.onBackPressed()
        val type=intent.getStringExtra("rOd")
        k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
        val user=k.getString("user","null")
        database = FirebaseDatabase.getInstance().getReference(type.toString()).child(user.toString())
        database.removeValue().addOnCompleteListener {
            MotionToast.createToast(this@DriverActivity,
                "finish!",
                "Trip Cancelled",
                MotionToastStyle.DELETE,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this@DriverActivity, www.sanju.motiontoast.R.font.montserrat_bold))
            //Toast.makeText(this, "Trip Cancelled", Toast.LENGTH_SHORT).show()
            }
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    override fun onDestroy() {
        super.onDestroy()
        if (::progressDialog.isInitialized && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

}