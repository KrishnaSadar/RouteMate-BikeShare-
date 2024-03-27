package com.example.driverapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.animation.AnimationSet
import android.view.animation.AnimationUtils
import com.example.driverapp.databinding.ActivityMainBinding
import com.example.driverapp.databinding.ActivitySplashScreenBinding

class Splash_Screen : AppCompatActivity() {
    private val binding: ActivitySplashScreenBinding by lazy {
        ActivitySplashScreenBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, WelcomeScreen::class.java)

            val fadeOut = AnimationUtils.loadAnimation(this, com.google.android.material.R.anim.abc_fade_out)

            // Log to check if the animation is being applied
            Log.d("Animation", "Applying animation")

            val animationSet = AnimationSet(true)
            animationSet.addAnimation(fadeOut)

            binding.imageView.startAnimation(animationSet)

            // Log to check if the animation is started
            Log.d("Animation", "Animation started")

            startActivity(intent)
            finish()
        }, 3000)



    }
}