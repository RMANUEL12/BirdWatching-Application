package com.example.bobolinkbirdwatching

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity




class Register : AppCompatActivity() {

    private lateinit var buttonRegister: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonRegister = findViewById(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            //storing user input
            val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val password = findViewById<EditText>(R.id.editTextConfirmPassword).text.toString()
            val firstName = findViewById<EditText>(R.id.editTextName).text.toString()
            val surname = findViewById<EditText>(R.id.editTextSurname).text.toString()
            val area = findViewById<EditText>(R.id.editTextArea).text.toString()

            //Creating a new user
            val newUser = UserData(email, password, firstName, surname, area)

            //Adding the user to the list
            com.example.bobolinkbirdwatching.User.addUser(newUser)

        }
    }
}