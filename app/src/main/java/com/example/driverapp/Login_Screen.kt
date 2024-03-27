
package com.example.driverapp
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.bumptech.glide.Glide
import com.example.driverapp.AfterUploadingAllDocument
import com.example.driverapp.MainActivity
import com.example.driverapp.databinding.ActivityLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class Login_Screen : AppCompatActivity() {
    private val binding: ActivityLoginScreenBinding by lazy {
        ActivityLoginScreenBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var k:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance().getReference("users")
        sharedPreferences = getSharedPreferences("login_pref", Context.MODE_PRIVATE)



        binding.login.setOnClickListener {
            val username = binding.emaillogin.text.toString()
            val password = binding.loginpassword.text.toString()

            if (username.isEmpty() || password.isEmpty()) {
                MotionToast.createToast(this@Login_Screen,
                    "Blanks!",
                    "Please fill all the mandatory fields",
                    MotionToastStyle.WARNING,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@Login_Screen, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this, "Please fill all the mandatory fields", Toast.LENGTH_SHORT).show()
            } else {
                // Sign in user with email and password
                auth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Login successful, now check the user's document upload status
                            val uid = auth.currentUser?.uid
                            val k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE).edit()
                            k.putString("user",uid.toString())
                            k.apply()
                         checkDocumentUploadStatus(uid.toString())

                        } else {
                            MotionToast.createToast(this@Login_Screen,
                                "Oops!",
                                "Login failed",
                                MotionToastStyle.ERROR,
                                MotionToast.GRAVITY_BOTTOM,
                                MotionToast.LONG_DURATION,
                                ResourcesCompat.getFont(this@Login_Screen, www.sanju.motiontoast.R.font.montserrat_bold))
                            //Toast.makeText(this, "Login failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        MotionToast.createToast(this@Login_Screen,
                            "Login failed!",
                            e.localizedMessage.toString(),
                            MotionToastStyle.ERROR,
                            MotionToast.GRAVITY_BOTTOM,
                            MotionToast.LONG_DURATION,
                            ResourcesCompat.getFont(this@Login_Screen, www.sanju.motiontoast.R.font.montserrat_bold))
                       // Toast.makeText(this, e.localizedMessage ?: "Login failed", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    private fun checkDocumentUploadStatus(uid: String) {
        database.child(uid).child("details").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val isDocumentsUploaded = snapshot.child("accountstatus").value as? String

                if (isDocumentsUploaded == "true") {
                    // Documents are uploaded, navigate to MainActivity
                    updateprefrencece(uid)
                    saveLoggedInState(true)
                    MotionToast.createToast(this@Login_Screen,
                        "Login Succesful!",
                       " ",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@Login_Screen, www.sanju.motiontoast.R.font.montserrat_bold))
                    //Toast.makeText(this@Login_Screen, "Login Succesful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Login_Screen, MainActivity::class.java))
                   finish()
                } else {
                    // Documents are not uploaded, navigate to AfterUploadingAllDocument activity
                    MotionToast.createToast(this@Login_Screen,
                        "Login Failed!",
                        " ",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this@Login_Screen, www.sanju.motiontoast.R.font.montserrat_bold))
                   // Toast.makeText(this@Login_Screen, "Login Failed", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Login_Screen, legalAuthenticationActivity::class.java))
                    finish()
                }
                // Finish the current activity
            }

            override fun onCancelled(error: DatabaseError) {
                MotionToast.createToast(this@Login_Screen,
                    "Login Failed!",
                    "Error: ${error.message}",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this@Login_Screen, www.sanju.motiontoast.R.font.montserrat_bold))
                //Toast.makeText(this@Login_Screen, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateprefrencece(user: String?) {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(user.toString()).child("details")
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // This method is called once with the initial value and whenever data at this location is updated.
                if (dataSnapshot.exists()) {
                    val userdetails = dataSnapshot.getValue(User::class.java)
                    if (userdetails != null) {
                        val k=getSharedPreferences("LegalAuthrntication", MODE_PRIVATE).edit()
                        val user=k.putString("user",userdetails.userId)
                        val name=k.putString("name",userdetails.name)
                        val surname=k.putString("surname",userdetails.surname)
                        val MobileNo=k.putString("mobileNo",userdetails.mobileNumber)
                        val Adress=k.putString("address",userdetails.adress)
                        val drivingbool=k.putString("booldrivinglicence","1")
                        val drivingurl=k.putString("DrvingLicenurl",userdetails.drivinglicencepic)
                        val Adharbool=k.putString("boolAdhar","1")
                        val Adharurl=k.putString("Adharurl",userdetails.adharpic)
                        val adharnumber=k.putString("AdharNumber",userdetails.adharnumber)
                        val panbool=k.putString("boolpan","1")
                        val Panurl=k.putString("Panurl",userdetails.panpic)
                        val pannumber=k.putString("PanNumber",userdetails.pannumber)
                        val profilebool=k.putString("boolprofille","1")
                        val Profileurl=k.putString("Profileurl",userdetails.profilepic)
                        val rcbool=k.putString("kk","1")
                        val rcurl=k.putString("rcurl",userdetails.rcpic)
                        val rcnumber=k.putString("RcNumber",userdetails.rcnumber)
                        k.apply()
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            } })


    }

    private fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("isLoggedIn", false)
    }

    private fun saveLoggedInState(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean("isLoggedIn", isLoggedIn).apply()
    }

    private fun navigateToMainOrAfterUploadActivity() {
        val isDocumentsUploaded = sharedPreferences.getString("isDocumentsUploaded", "false")
        val intent = if (isDocumentsUploaded=="true") {
            Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            Intent(this, AfterUploadingAllDocument::class.java)
            startActivity(intent)
            finish()
        }
        // Finish the current activity
    }
}
