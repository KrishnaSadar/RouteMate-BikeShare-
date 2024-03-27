package com.example.driverapp.ongoingTrip

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.driverapp.MainActivity
import com.example.driverapp.R
import com.example.driverapp.RatingActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class OngoingWay : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ongoing_way)
        val m=intent.getStringExtra("ongoingkey")
        val n=intent.getStringExtra("other")
//        ongoing.addValueEventListener(object :ValueEventListener{
//            override fun onDataChange(snapshot: DataSnapshot) {
//                if(!snapshot.exists()){
//                    Toast.makeText(this@OngoingWay, "Trip is finished by your routmate", Toast.LENGTH_SHORT).show()
//                        updateBalance()
//                        val driverpayment = getSharedPreferences("Driver", MODE_PRIVATE)
//                        val driver = driverpayment.getString("driverId", "null")
//                        val rate=Intent(this@OngoingWay,Rating2::class.java)
//                        rate.putExtra("other",driver.toString())
//                        startActivity(rate)
//                        finish()
//
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//
//            }
//
//        })

        findViewById<TextView>(R.id.textView32).setOnClickListener {
            val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@OngoingWay)

            // Set the message show for the Alert time

            // Set the message show for the Alert time
            builder.setMessage("Do you want to sent alert and call to our security service?")

            // Set Alert Title

            // Set Alert Title
            builder.setTitle("Are you in Any Danger!")

            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show

            // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
            builder.setCancelable(false)

            // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.

            // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setPositiveButton("Call",
                DialogInterface.OnClickListener { dialog: DialogInterface?, which: Int ->
                    // Create an Intent object with ACTION_DIAL to initiate a phone call
                    val intent = Intent(Intent.ACTION_DIAL)
                    // Set the phone number to dial
                    val phoneNumber = "9527588063" // Replace this with the phone number you want to call
                    intent.data = Uri.parse("tel:$phoneNumber")
                    // Start the activity with the intent
                    startActivity(intent)
                } as DialogInterface.OnClickListener)

            // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.

            // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
            builder.setNegativeButton("Cancle",
                DialogInterface.OnClickListener { dialog: DialogInterface, which: Int ->
                    // If user click no then dialog box is canceled.
                    dialog.cancel()
                } as DialogInterface.OnClickListener)

            // Create the Alert dialog

            // Create the Alert dialog
            val alertDialog: android.app.AlertDialog? = builder.create()
            // Show the Alert Dialog box
            // Show the Alert Dialog box
            if (alertDialog != null) { alertDialog.show() }

        }
        findViewById<Button>(R.id.button6).setOnClickListener {
           val intent =Intent(this@OngoingWay,RatingActivity::class.java)
            intent.putExtra("other",n.toString())
            intent.putExtra("ongoingkey",m.toString())
            startActivity(intent)
            finish()
        }
    }


}




