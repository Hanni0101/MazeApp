package com.example.mazeapp

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class EskeriPix : AppCompatActivity() {

    private var mediaPlayer: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_eskeri_pix)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val imageView: ImageView = findViewById(R.id.fullscreen_image)
        val darkOverlay: View = findViewById(R.id.dark_overlay)

        imageView.alpha = 0f

        mediaPlayer = MediaPlayer.create(this, R.raw.scream)
        mediaPlayer?.start()

        val scale = ScaleAnimation(
            0.1f, 1f,
            0.1f, 1f,
            AnimationSet.RELATIVE_TO_SELF, 0.5f,
            AnimationSet.RELATIVE_TO_SELF, 0.5f
        ).apply {
            duration = 200
        }

        val alpha = AlphaAnimation(0f, 1f).apply {
            duration = 200
        }

        val animSet = AnimationSet(true).apply {
            addAnimation(scale)
            addAnimation(alpha)
        }

        Handler(Looper.getMainLooper()).postDelayed({
            imageView.alpha = 1f
            darkOverlay.visibility = View.GONE
            imageView.startAnimation(animSet)
        }, 750)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer = null
    }
}
