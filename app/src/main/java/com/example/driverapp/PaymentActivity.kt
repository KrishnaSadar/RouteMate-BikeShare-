package com.example.driverapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.example.driverapp.ongoingTrip.OngoingWay
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONException
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class PaymentActivity : AppCompatActivity() {
    private lateinit var database: DatabaseReference

    // on below line we are creating
    // variables for our edit text and button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        val k=intent.getStringExtra("other")
        val prize=intent.getStringExtra("prize")
        val mi=intent.getStringExtra("me")
        val builder: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@PaymentActivity)
        builder.setMessage("Make the payment only when driver is reched to you")
        builder.setTitle("Is Driver arrived at Starting Location?")
        builder.setCancelable(false)
        builder.setPositiveButton("Make Payment",DialogInterface.OnClickListener { dialog, which ->
    // on below line getting amount from edit text
    val amt =intent.getStringExtra("prize").toString()

    // rounding off the amount.
    val amount = Math.round(amt.toFloat() * 100).toInt()

    // on below line we are
    // initializing razorpay account
    val checkout = Checkout()

    // on the below line we have to see our id.
    checkout.setKeyID("Your Api KEy")

    // set image
    checkout.setImage(R.drawable.logo)

    // initialize json object
    val obj = JSONObject()
    try {
        // to put name
        obj.put("name", "RoutMate")

        // put description
        obj.put("description", "Test payment")

        // to set theme color
        obj.put("theme.color", "")

        // put the currency
        obj.put("currency", "INR")

        // put amount
        obj.put("amount", amount)

        // put mobile number
        obj.put("prefill.contact", "9527588063")

        // put email
        obj.put("prefill.email", "krishna.sadar23@vit.edu")

        // open razorpay to checkout activity
        checkout.open(this@PaymentActivity, obj)
    } catch (e: JSONException) {
        e.printStackTrace()
    }

})
        builder.setNegativeButton("Cancle",DialogInterface.OnClickListener { dialog, which ->
            val  n=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
            val me= n.getString("user","null")
            val ongoing = FirebaseDatabase.getInstance().getReference("OnGoingTrip").child(k.toString()+me.toString())
            ongoing.child("request").setValue("false")
            ongoing.removeValue().addOnCompleteListener {
                startActivity(Intent(this,BikeShare::class.java))
                finish()
                MotionToast.createToast(this@PaymentActivity,
                    "Request denied",
                    " ",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@PaymentActivity, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "Request denied", Toast.LENGTH_SHORT).show()
            dialog.cancel()
            }

        })
        val alertDialog: android.app.AlertDialog? = builder.create()
        if (alertDialog != null) { alertDialog.show() }



    }

     fun onPaymentSuccess(s: String?) {
        // this method is called on payment success.
         val k=intent.getStringExtra("other")
         val prize=intent.getStringExtra("prize")
         val mi=intent.getStringExtra("me")
         val  n=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
         val me= n.getString("user","null")
         val ongoing = FirebaseDatabase.getInstance().getReference("OnGoingTrip").child(me.toString()+k.toString())
         ongoing.child("IsPayment").setValue("true").addOnCompleteListener {
             MotionToast.createToast(this@PaymentActivity,
                 "Done",
                 "Payment is successful ",
                 MotionToastStyle.SUCCESS,
                 MotionToast.GRAVITY_BOTTOM,
                 MotionToast.LONG_DURATION,
                 ResourcesCompat.getFont(this@PaymentActivity, www.sanju.motiontoast.R.font.montserrat_bold))
            // Toast.makeText(this, "Payment is successful : ", Toast.LENGTH_SHORT).show();
             database = FirebaseDatabase.getInstance().getReference("RiderTrip").child(mi.toString())
             database.removeValue()
             database = FirebaseDatabase.getInstance().getReference("DriverTrip").child(k.toString())
             database.removeValue()
             ongoing.child("prize").setValue(prize).addOnCompleteListener {
                 val intent=Intent(this, OngoingWay::class.java)
                 intent.putExtra("me",mi.toString())
                 intent.putExtra("other",k.toString())
                 intent.putExtra("ongoingkey",me.toString()+k.toString())
                 val driverpayment=getSharedPreferences("Driver", MODE_PRIVATE).edit()
                 driverpayment.putString("driverId",k.toString())
                 driverpayment.apply()
                 startActivity(intent)
                 this.finish() }

         }

    }

    fun onPaymentError(p0: Int, s: String?) {
        // on payment failed.
        MotionToast.createToast(this@PaymentActivity,
            "Oops!",
            "Payment Failed due to error",
            MotionToastStyle.ERROR,
            MotionToast.GRAVITY_BOTTOM,
            MotionToast.LONG_DURATION,
            ResourcesCompat.getFont(this@PaymentActivity, www.sanju.motiontoast.R.font.montserrat_bold))
       // Toast.makeText(this, "Payment Failed due to error : " + s, Toast.LENGTH_SHORT).show();
        startActivity(Intent(this,BikeShare::class.java))
        finish()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this,BikeShare::class.java))
        finish()
    }
}