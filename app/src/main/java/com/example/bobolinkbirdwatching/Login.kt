package com.example.bobolinkbirdwatching

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast

class Login : AppCompatActivity() {

    private lateinit var btnLogin: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.buttonLogin)

        btnLogin.setOnClickListener {
            try {
                val email = findViewById<EditText>(R.id.inputEmail).text.toString()
                val password = findViewById<EditText>(R.id.inputPassword).text.toString()

                val user =
                    com.example.bobolinkbirdwatching.User.findUserByEmailAndPassword(email, password)
                if (user != null) {

                    //--REDIRECTING TO HOME(MAP)
                    //Creating an Intent to navigate to the activity
                    val intent = Intent(this, MainActivity::class.java)

                    //Starting the activity
                    startActivity(intent)

                } else {
                    // Showing message for invalid user details
                    Toast.makeText(applicationContext, "Invalid Details. Please Retry or Register...", Toast.LENGTH_SHORT).show()
                }
            }
            //error handling
            catch(e: Exception){

                //error message
                Log.e(ContentValues.TAG, "Something went wrong, Please Try Again.")
            }

        }

        //Register Link Button functionality
        val registerLinkText = findViewById<TextView>(R.id.buttonRegister)

        //Making the TextView clickable
        registerLinkText.isClickable = true

        //Adding an OnClickListener to handle the click event
        registerLinkText.setOnClickListener {

            //Creating an Intent to navigate to the activity
            val regIntent = Intent(this, Register::class.java)

            //Starting the activity
            startActivity(regIntent)
        }

        //Back Button(Exit) functionality

        val closeButton = findViewById<ImageButton>(R.id.closePage)

//Adding an OnClickListener to handle the click event
        closeButton.setOnClickListener {

            //Creating an Intent to navigate to the activity
             val regIntent = Intent(this, StartUp::class.java)

            //Starting the activity
            startActivity(regIntent)
        }

    }
}