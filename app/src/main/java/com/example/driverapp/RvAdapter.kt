package com.example.driverapp

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.messaging.FirebaseMessaging
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class RvAdapter(private val trips: List<Trip>, val context: Context,private val bikeShare: Activity,private var triptype: String,private var target: String) : RecyclerView.Adapter<RvAdapter.ViewHolder>() {
    private lateinit var database: DatabaseReference
    private lateinit var ongoing: DatabaseReference
    private lateinit var k: SharedPreferences
    private lateinit var j: SharedPreferences.Editor
    private lateinit var m: SharedPreferences
    private lateinit var progressDialog: ProgressDialog
    private lateinit var type1: String
    private lateinit var target1: String

    init {
        // Initialize SharedPreferences in the adapter's constructor
        k = context.getSharedPreferences("LegalAuthrntication", Context.MODE_PRIVATE)
        j = context.getSharedPreferences("Tokens", Context.MODE_PRIVATE).edit()
        m = context.getSharedPreferences("Tokens", Context.MODE_PRIVATE)
        progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Reqesting as a $triptype...")
        progressDialog.setCancelable(true)
        this.type1 = triptype
        this.target1=target
    }

    // ViewHolder class to hold references to the views in each item layout
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val Payment: TextView = itemView.findViewById(R.id.textView20)
        val MobileNo: TextView = itemView.findViewById(R.id.textView19)
        val Name: TextView = itemView.findViewById(R.id.textView18)
        val profilePic: ImageView = itemView.findViewById(R.id.imageView8)
        val request: Button = itemView.findViewById(R.id.request)

    }

    // Inflate the item layout and create the ViewHolder object
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rvforbikeandrisdeshare, parent, false)
        return ViewHolder(view)
    }

    // Bind data to the views in each item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTrip = trips[position]
        // Assuming you have a reference to the database
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(currentTrip.userId.toString())

        // Add a ValueEventListener to retrieve the data
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and whenever data at this location is updated.
                if (dataSnapshot.exists()) {
                    val userdetails = dataSnapshot.child("details").getValue(User::class.java)
                    // Now you can use the userdetails object
                    // For example, log the user's name
                    if (userdetails != null) {
                        holder.MobileNo.text = userdetails.mobileNumber.toString()
                        holder.Name.text = userdetails.name.toString()
                        holder.request.setOnClickListener {
                            //code to send message to user profile
                            val name = k.getString("name", "null")
                            val user = k.getString("user", "null")
                            database = FirebaseDatabase.getInstance().getReference("users")
                            FirebaseMessaging.getInstance().subscribeToTopic("all")
// FCM settings for a particular user

                            FirebaseMessaging.getInstance().token
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful && task.result != null) {
                                        val  token = task.result.toString()
                                        // Use the token as needed
                                        j.putString("token",token.toString())
                                        j.apply()
                                    } else {
                                        // Handle token generation failure
                                        MotionToast.createToast(
                                            context as Activity,
                                            "Token not available!",
                                            "Unable to find token",
                                            MotionToastStyle.ERROR,
                                            MotionToast.GRAVITY_BOTTOM,
                                            MotionToast.LONG_DURATION,
                                            ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.montserrat_bold))
                                        //Toast.makeText(context, "Unable to find token", Toast.LENGTH_LONG).show()
                                    }
                                }
                            val t1=m.getString("token","null")
                            val userRef = database.child(user.toString())
                            val db=userRef.child("details").child("token")
                            db.setValue(t1.toString()).addOnCompleteListener{
                                //Toast.makeText(context, "Token updated succesfully", Toast.LENGTH_SHORT).show()
                            }.addOnFailureListener {
                                MotionToast.createToast(
                                    context as Activity,
                                    "Token not available!",
                                    "Failed to updated token",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(context, www.sanju.motiontoast.R.font.montserrat_bold))
                                //Toast.makeText(context, "Failed to updated token", Toast.LENGTH_SHORT).show()
                                }

                            //crete ongoin trip
                            val ongoing = FirebaseDatabase.getInstance().getReference("OnGoingTrip").child(user.toString()+userdetails.userId)
                            ongoing.child("$type1").setValue(user.toString())
                            ongoing.child("$target1").setValue(userdetails.userId)
                            val notificationsSender = FcmNotificationsSender(
                                userdetails.token.toString(),
                                name.toString(),
                                user.toString(),
                                context,
                                bikeShare
                            )
                            notificationsSender.sendNotifications()
                            progressDialog.show()
                            ongoing.addValueEventListener(object :ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()){
                                        val request1=snapshot.child("request").getValue<String>(String::class.java)
                                        if(request1=="true"&&target=="DriverTrip"){
                                            val intent=Intent(context,PaymentActivity::class.java)
                                            intent.putExtra("target",target1)
                                            intent.putExtra("type",type1)
                                            intent.putExtra("me",user.toString())
                                            intent.putExtra("other",userdetails.userId)
                                            intent.putExtra("prize","10")
                                            //implement startActivity
                                            progressDialog.dismiss()
                                            context.startActivity(intent)
                                            (bikeShare as AppCompatActivity).finish()

                                        }else{

                                        }
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }

                            })
















                           // Toast.makeText(context, userdetails.token, Toast.LENGTH_SHORT).show()
                        }
                        val url = userdetails.profilepic.toString()
                        url.let {
                            // Load image into ImageView using Glide
                            Glide.with(context).load(it).into(holder.profilePic)
                        }
                    }
                    holder.Payment.text = "₹10"
                } else {
                    Log.d("UserDetails", "No such user")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Failed to read value
                Log.w("UserDetails", "Failed to read value.", databaseError.toException())
            }
        })
    }

    // Return the size of the dataset
    override fun getItemCount(): Int {
        return trips.size
    }
}
