package com.example.bobolinkbirdwatching

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.bobolinkbirdwatching.GetStarted
import com.example.bobolinkbirdwatching.Login
import com.example.bobolinkbirdwatching.Register
import android.view.View


class StartUp : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_up)

        val buttonLogin = findViewById<View>(R.id.buttonLogin)
        val buttonRegister = findViewById<View>(R.id.buttonRegister)
        val closePage = findViewById<View>(R.id.closePage)

        buttonLogin.setOnClickListener {
            // Create an Intent to start the LoginActivity
            val intent = Intent(this, Login::class.java)
            startActivity(intent)
        }

        buttonRegister.setOnClickListener {
            // Create an Intent to start the RegisterActivity
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
        }

        closePage.setOnClickListener {
            // Create an Intent to go back to the GetStarted activity
            val intent = Intent(this, GetStarted::class.java)
            startActivity(intent)
        }
    }
}
