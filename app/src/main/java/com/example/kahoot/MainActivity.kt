package com.example.kahoot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnConfig = findViewById<Button>(R.id.btnConfig)
        val btnPlay = findViewById<Button>(R.id.btnPlay)

        btnConfig.setOnClickListener {
            startActivity(Intent(this, ConfigActivity::class.java))
        }

        btnPlay.setOnClickListener {
            startActivity(Intent(this, PlayActivity::class.java))
        }
    }
}
