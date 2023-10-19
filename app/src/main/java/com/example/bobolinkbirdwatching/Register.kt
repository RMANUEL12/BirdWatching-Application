package com.example.bobolinkbirdwatching

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

                        //--REDIRECTING TO LOGIN
                        //Creating an Intent to navigate to the activity
                        val intent = Intent(this, Login::class.java)

                        //Starting the activity
                        startActivity(intent)

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

        //Login Link Button functionality
        val loginLinkText = findViewById<TextView>(R.id.buttonLogin)

        //Making the TextView clickable
        loginLinkText.isClickable = true

        //Adding an OnClickListener to handle the click event
        loginLinkText.setOnClickListener {

            //Creating an Intent to navigate to the activity
            val logIntent = Intent(this, Login::class.java)

            //Starting the activity
            startActivity(logIntent)

        }
    }
}
