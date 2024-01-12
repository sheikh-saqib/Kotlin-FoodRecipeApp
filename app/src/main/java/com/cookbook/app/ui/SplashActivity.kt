package com.cookbook.app.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.cookbook.app.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Hide the title bar (action bar)
        supportActionBar?.hide()

        // Make the navigation bar and status bar transparent
        window.navigationBarColor = getColor(android.R.color.transparent)
        window.statusBarColor = getColor(android.R.color.transparent)

        // Hide the bottom navigation bar (soft keys) using WindowInsetsController
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.apply {
                hide(WindowInsets.Type.systemBars())
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        } else {
            // For versions prior to Android 11, fallback to the previous method
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            or View.SYSTEM_UI_FLAG_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    )
        }

        setContentView(R.layout.activity_splash)

        val imageView: ImageView = findViewById(R.id.splash_image)

        // Load animation
        val fadeIn: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)

        // Set animation to image view
        imageView.startAnimation(fadeIn)

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, 5000) // 5000 is the delayed time in milliseconds.
    }
}
