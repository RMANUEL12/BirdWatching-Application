package com.example.bobolinkbirdwatching

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class Register : AppCompatActivity() {

    private lateinit var buttonRegister: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonRegister = findViewById(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            try {
                val email = findViewById<EditText>(R.id.editTextEmail)?.text.toString()
                val passwordTemp = findViewById<EditText>(R.id.editTextPassword)?.text.toString()
                val password = findViewById<EditText>(R.id.editTextConfirmPassword)?.text.toString()
                val firstName = findViewById<EditText>(R.id.editTextfirstName)?.text.toString()
                val surname = findViewById<EditText>(R.id.editTextSurname)?.text.toString()
                val area = findViewById<EditText>(R.id.editTextArea)?.text.toString()

                if (email.isNullOrEmpty() || password.isNullOrEmpty() || firstName.isNullOrEmpty() || surname.isNullOrEmpty() || area.isNullOrEmpty()) {
                    Toast.makeText(applicationContext, "All fields are required.", Toast.LENGTH_SHORT).show()
                } else if (passwordTemp != password) {
                    Toast.makeText(applicationContext, "Passwords do not match.", Toast.LENGTH_SHORT).show()
                } else {
                    val newUser = UserData(firstName, surname, area, email, password)
                    com.example.bobolinkbirdwatching.User.addUser(newUser)

                    val intent = Intent(this, Login::class.java)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error: ${e.message}")
            }
        }

        val loginLinkText = findViewById<TextView>(R.id.buttonLogin)

        loginLinkText.isClickable = true
        loginLinkText.setOnClickListener {
            val logIntent = Intent(this, Login::class.java)
            startActivity(logIntent)
        }
    }
}
