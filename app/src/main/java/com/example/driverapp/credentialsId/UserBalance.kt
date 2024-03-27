package com.example.driverapp.credentialsId

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.driverapp.Activity3
import com.example.driverapp.MainActivity
import com.example.driverapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.razorpay.Checkout
import org.json.JSONException
import org.json.JSONObject
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import kotlin.properties.Delegates

class UserBalance : AppCompatActivity() {
    private lateinit var progressDialog: ProgressDialog
         private var b by Delegates.notNull<Int>()
    private lateinit var stringValue: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_balance)
        var  b:Int=0
        var stringValue:String="0"
       val k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
        val user=k.getString("user","null")
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("fetching amount...")
        progressDialog.setCancelable(false)
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(user.toString()).child("details").child("IsPayment")
        progressDialog.show()
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!snapshot.exists()) {
                    // If the "payments" node doesn't exist, set it to "0"
                    userRef.setValue("0")
                    progressDialog.dismiss()
                } else {
                  val v=snapshot.getValue(String::class.java)

                     if (v != null) {
                        b=v.toInt()
                    }
                   findViewById<TextView>(R.id.textView28).text="Balance:₹$v"
                    progressDialog.dismiss()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
           findViewById<Button>(R.id.button11).setOnClickListener {
               openUpdateDialog(b, stringValue

               )
           }
    }

    private fun openUpdateDialog(b:Int,stringValue:String) {
        Checkout.preload(applicationContext)
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.withdrawldialogue, null)
        mDialog.setView(mDialogView)
          val amount=mDialogView.findViewById<EditText>(R.id.etEmpName)
        val withdraw=mDialogView.findViewById<Button>(R.id.btnUpdateData)
        mDialog.setTitle("Withdrawing amount")
        val alertDialog = mDialog.create()
        alertDialog.show()
        withdraw.setOnClickListener {
            val sv = amount.text.toString()
            if(sv.toInt()>b){
                MotionToast.createToast(this@UserBalance,
                    "Insufficient balance",
                    "Insufficient balance",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@UserBalance, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this@UserBalance, "Insufficient balance", Toast.LENGTH_SHORT).show()
                }
            else{
                alertDialog.dismiss()

                val checkout = Checkout()
                checkout.setKeyID("YOUR API KEY")
                checkout.setImage(R.drawable.logo)
                 val amt=amount.text.toString()
                val options = JSONObject()
                try {
                    options.put("name", "RouteMate")
                    options.put("description", "Test withdraw")
                    options.put("currency", "INR")
                    options.put("amount", amt.toInt()) // Amount in paise
                    options.put("prefill.contact", "9527588063")
                    options.put("prefill.email", "krishna.sadar23@vit.edu")

                    checkout.open(this, options)
                }catch (e:JSONException){
                    e.printStackTrace()
                }


            }
        }
    }
     fun onPaymentSuccess(paymentId: String?) {
        // Payment was successful, handle it accordingly
         MotionToast.createToast(this@UserBalance,
             "hurray",
             "Withdrawal Successful",
             MotionToastStyle.SUCCESS,
             MotionToast.GRAVITY_BOTTOM,
             MotionToast.LONG_DURATION,
             ResourcesCompat.getFont(this@UserBalance, www.sanju.motiontoast.R.font.montserrat_bold))
         //Toast.makeText(this, "Withdrawal Successful", Toast.LENGTH_SHORT).show()
         val k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE)
         val user=k.getString("user","null")
         val userRef = FirebaseDatabase.getInstance().getReference("users").child(user.toString()).child("details").child("IsPayment")
         userRef.addValueEventListener(object : ValueEventListener {
             override fun onDataChange(snapshot: DataSnapshot) {
                 if (!snapshot.exists()) {
                     // If the "payments" node doesn't exist, set it to "0"
                     userRef.setValue("0")
                 } else {
                     val v=snapshot.getValue(String::class.java)
                     if (v != null) {
                         b=v.toInt()-stringValue.toInt()
                         userRef.setValue(b.toString())
                     }
                     findViewById<TextView>(R.id.etEmpName).text="  "
                 }
             }

             override fun onCancelled(error: DatabaseError) {
                 // Handle onCancelled event
             }
         })

    }

     fun onPaymentError(code: Int, message: String?) {
        // Payment failed, handle it accordingly
         MotionToast.createToast(this@UserBalance,
             "Oops",
             "Withdrawal Failed",
             MotionToastStyle.ERROR,
             MotionToast.GRAVITY_BOTTOM,
             MotionToast.LONG_DURATION,
             ResourcesCompat.getFont(this@UserBalance, www.sanju.motiontoast.R.font.montserrat_bold))
         //Toast.makeText(this, "Withdrawal Failed", Toast.LENGTH_SHORT).show()
    }
    override fun onBackPressed() {
        super.onBackPressed()
        val through = intent.getStringExtra("through")
        if (through == "home") {
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            startActivity(Intent(this, Activity3::class.java))
        }
        finish()
    }

}