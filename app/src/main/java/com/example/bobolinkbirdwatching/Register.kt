package com.example.bobolinkbirdwatching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class Register : AppCompatActivity() {
    private var buttonRegister = Button(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            //storing user input
            val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
            val password = findViewById<EditText>(R.id.editTextPassword).text.toString()
            val firstName = findViewById<EditText>(R.id.editTextName).text.toString()
            val surname = findViewById<EditText>(R.id.editTextSurname).text.toString()
            val gender = findViewById<EditText>(R.id.editTextGender).text.toString()
            val age = findViewById<EditText>(R.id.editTextAge).text.toString()
            val area = findViewById<EditText>(R.id.editTextArea).text.toString()

            //Creating a new user
            val newUser = UserData(firstName, surname, gender, age, area, email, password)

            //Adding the user to the list
            com.example.bobolinkbirdwatching.User.addUser(newUser)

        }
    }
}