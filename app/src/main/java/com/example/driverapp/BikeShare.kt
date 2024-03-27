package com.example.driverapp

import android.app.ProgressDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.driverapp.databinding.ActivityBikeShareBinding
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.lang.Exception


class BikeShare : AppCompatActivity() {
    private val binding:ActivityBikeShareBinding by lazy{
        ActivityBikeShareBinding.inflate(layoutInflater)
    }
    private lateinit var k: SharedPreferences
    private lateinit var database:DatabaseReference
    private lateinit var progressDialog: ProgressDialog
    private val filteredTrips: ArrayList<Trip> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)
                  val type=intent.getStringExtra("rOd")
        Log.d("err","activity launced:$type")

        binding.find.setOnClickListener {
         if(!binding.startLocation.text.isEmpty() && !binding.endLocation.text.isEmpty()){

                 k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
                 val user=k.getString("user","null")
                 val start=binding.startLocation.text.toString().toLowerCase()
                 val end=binding.endLocation.text.toString().toLowerCase()
             val trip=Trip(user,start,end)
                 database = FirebaseDatabase.getInstance().getReference(type.toString()).child(user.toString())
             // Initialize ProgressDialog
             progressDialog = ProgressDialog(this)
             progressDialog.setMessage("Uploading trip Reqest`...")
             progressDialog.setCancelable(false)
             progressDialog.show()
                 database.setValue(trip).addOnCompleteListener {
                     var target:String="null"
                     if(type.toString()=="RiderTrip"){ target="DriverTrip"}
                     if(type.toString()=="DriverTrip"){ target="RiderTrip"}
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

                             // Update RecyclerView with filtered data
                             // Implement RecyclerView adapter and set it here
                             val layoutManager = LinearLayoutManager(this@BikeShare)
                             binding.rv.layoutManager = layoutManager

                             // Create adapter and set it to the RecyclerView
                           val  adapter = RvAdapter(filteredTrips, this@BikeShare,this@BikeShare,type.toString(),target)
                             binding.rv.adapter = adapter


                             // Example: recyclerView.adapter = TripAdapter(filteredTrips)

                             progressDialog.dismiss()
                         }

                         override fun onCancelled(error: DatabaseError) {
                             MotionToast.createToast(this@BikeShare,
                                 "Oops!",
                                 "Failed to retrieve data",
                                 MotionToastStyle.ERROR,
                                 MotionToast.GRAVITY_BOTTOM,
                                 MotionToast.LONG_DURATION,
                                 ResourcesCompat.getFont(this@BikeShare, www.sanju.motiontoast.R.font.montserrat_bold))
                             //Toast.makeText(this@BikeShare, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
                         }
                     }) }
                     .addOnFailureListener {
                         MotionToast.createToast(this@BikeShare,
                             "Oops!",
                             "Something Went Wrong",
                             MotionToastStyle.ERROR,
                             MotionToast.GRAVITY_BOTTOM,
                             MotionToast.LONG_DURATION,
                             ResourcesCompat.getFont(this@BikeShare, www.sanju.motiontoast.R.font.montserrat_bold))
                        // Toast.makeText(this@BikeShare, "Something Went Wrong", Toast.LENGTH_SHORT).show()
                     }



         }
                 else {
             MotionToast.createToast(this@BikeShare,
                 "Blanks!",
                 "plz fill all the field",
                 MotionToastStyle.WARNING,
                 MotionToast.GRAVITY_BOTTOM,
                 MotionToast.LONG_DURATION,
                 ResourcesCompat.getFont(this@BikeShare, www.sanju.motiontoast.R.font.montserrat_bold))
             //Toast.makeText(this@BikeShare, "plz fill all the field", Toast.LENGTH_SHORT).show()
         }
                  binding.find.setText("Refresh")
         }
             }
    override fun onBackPressed() {
        super.onBackPressed()
        val type=intent.getStringExtra("rOd")
        k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
        val user=k.getString("user","null")
        database = FirebaseDatabase.getInstance().getReference(type.toString()).child(user.toString())
        database.removeValue().addOnCompleteListener {
            MotionToast.createToast(this@BikeShare,
                "finish",
                "Trip Cancelled",
                MotionToastStyle.DELETE,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this@BikeShare, www.sanju.motiontoast.R.font.montserrat_bold))
            //Toast.makeText(this, "Trip Cancelled", Toast.LENGTH_SHORT).show()
            }
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }
    }
