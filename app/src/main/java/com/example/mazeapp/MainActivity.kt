package com.example.mazeapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnChallenge1: Button = findViewById(R.id.btnChallenge1)
        val btnChallenge2: Button = findViewById(R.id.btnChallenge2)
        val btnChallenge3: Button = findViewById(R.id.btnChallenge3)

        btnChallenge1.setOnClickListener {
            val intent = Intent(this, MazeGameView::class.java)
            intent.putExtra("LEVEL", 1)
            startActivity(intent)
        }

        btnChallenge2.setOnClickListener {
            val intent = Intent(this, MazeGameView::class.java)
            intent.putExtra("LEVEL", 2)
            startActivity(intent)
        }
        btnChallenge3.setOnClickListener {
            val intent = Intent(this, EskeriPix::class.java)
            startActivity(intent)
        }


    }
}
