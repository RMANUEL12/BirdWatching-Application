package com.example.bobolinkbirdwatching

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.bobolinkbirdwatching.R
import com.example.bobolinkbirdwatching.StartUp

class GetStarted : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_started)

        val buttonStartApp = findViewById<View>(R.id.buttonStartApp)

        buttonStartApp.setOnClickListener {
            // Create an Intent to start the StartUp activity
            val intent = Intent(this, StartUp::class.java)
            startActivity(intent)
        }
    }
}
