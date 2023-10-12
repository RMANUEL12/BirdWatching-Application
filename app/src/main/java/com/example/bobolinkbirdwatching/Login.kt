package com.example.bobolinkbirdwatching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Login : AppCompatActivity() {

    private var btnLogin = Button(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById<Button>(R.id.buttonLogin)

        btnLogin.setOnClickListener {
            val email = findViewById<EditText>(R.id.inputEmail).text.toString()
            val password = findViewById<EditText>(R.id.inputPassword).text.toString()

            val user =
                com.example.bobolinkbirdwatching.User.findUserByEmailAndPassword(email, password)
            if (user != null) {
                // User will be directed to home page
            } else {
                // Showing message for invalid user details
                Toast.makeText(applicationContext, "User not found. Please Register...", Toast.LENGTH_SHORT).show()
            }
        }
    }
}