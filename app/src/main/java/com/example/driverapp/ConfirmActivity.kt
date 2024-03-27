package com.example.driverapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.driverapp.credentialsId.BasicDetails
import com.example.driverapp.databinding.ActivityConfirmBinding
import com.example.driverapp.databinding.ActivityWelcomeScreenBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseUser

class ConfirmActivity : AppCompatActivity() {
    private val binding: ActivityConfirmBinding by lazy {
        ActivityConfirmBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleApiClient: GoogleApiClient
    private val RC_SIGN_IN = 123 // Any integer value
    private lateinit var callbackManager: CallbackManager
    public override fun onStart() {
        super.onStart()

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
//        val currentUser = auth.currentUser
//        if (currentUser != null) {
//            // User is signed in, update UI
//            updateUI(currentUser)
//        }
        // Configure Google Sign-In
        binding.findMyAccount.setOnClickListener {
            startActivity(Intent(this,Login_Screen::class.java))
             finish()
        }
        binding.button2.setOnClickListener {
            //Toast.makeText(this, "button is clicked", Toast.LENGTH_SHORT).show()
            val k=binding.editTextText.text.toString()
            val c=Intent(this,EmaillUsernamePassword::class.java)
            c.putExtra("email",k.toString())
            startActivity(c)
            finish()
        }







    }


}