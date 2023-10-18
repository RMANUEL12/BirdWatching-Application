package com.example.bobolinkbirdwatching

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class Register : AppCompatActivity() {
    private var buttonRegister = Button(this);

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        buttonRegister = findViewById<Button>(R.id.buttonRegister)

        buttonRegister.setOnClickListener {

            try{
                //storing user input
                val email = findViewById<EditText>(R.id.editTextEmail).text.toString()
                val passwordTemp = findViewById<EditText>(R.id.editTextPassword).text.toString()
                val password = findViewById<EditText>(R.id.editTextConfirmPassword).text.toString()
                val firstName = findViewById<EditText>(R.id.editTextName).text.toString()
                val surname = findViewById<EditText>(R.id.editTextSurname).text.toString()
                val area = findViewById<EditText>(R.id.editTextArea).text.toString()

                //verifying that no fields are empty
                if (email.isNotEmpty() && password.isNotEmpty() && firstName.isNotEmpty() &&
                    surname.isNotEmpty() && area.isNotEmpty()){

                    //checking that password and confirmed password match
                    if (passwordTemp == password){

                        //Creating a new user
                        val newUser = UserData(firstName, surname, area, email, password)

                        //Adding the user to the list
                        com.example.bobolinkbirdwatching.User.addUser(newUser)

                    }
                    else{
                        // Showing error message
                        Toast.makeText(applicationContext, "Please ensure that both passwords match.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            //error handling
            catch(e: Exception){

                //error message
                Log.e(ContentValues.TAG, "Something went wrong, Please Try Again.")
            }
        }
    }
}
