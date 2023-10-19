package com.example.bobolinkbirdwatching

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class Settings : AppCompatActivity() {

    private val userDetailsList = mutableListOf<UserData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // In your activity or fragment
        val emailEditText = findViewById<EditText>(R.id.editTextNewEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextNewPassword)
        val areaEditText = findViewById<EditText>(R.id.editTextNewArea)
        val displayName = findViewById<TextView>(R.id.textViewUserName)
        val displayEmail = findViewById<TextView>(R.id.textViewUserEmail)

        //Populating the EditTexts with the current user's details
        val currentUserDetails = userDetailsList[0]
        displayName.text = (currentUserDetails.firstName + " " + currentUserDetails.surname)
        displayEmail.text = (currentUserDetails.email)
        emailEditText.hint = currentUserDetails.email
        passwordEditText.hint = (currentUserDetails.password)
        areaEditText.hint = (currentUserDetails.area)

        val saveButton = findViewById<Button>(R.id.buttonSaveSettings)

        saveButton.setOnClickListener {

            try{
                //Getting the updated details from the EditTexts
                val updatedEmail = emailEditText.text.toString()
                val updatedPassword = passwordEditText.text.toString()
                val updatedArea = areaEditText.text.toString()

                //Updating the user's details in the list
                currentUserDetails.email = updatedEmail
                currentUserDetails.password = updatedPassword
                currentUserDetails.area = updatedArea

                //Refreshing the UI to display the updated details
                emailEditText.setText(updatedEmail)
                passwordEditText.setText(updatedPassword)
                areaEditText.setText(updatedArea)
            }
            //error handling
            catch(e: Exception){

                //error message
                Log.e(ContentValues.TAG, "Something went wrong, Please Try Again.")
            }

        }

        val backButton = findViewById<Button>(R.id.btnReturnToUserSet)

        backButton.setOnClickListener{
            //Creating an Intent to navigate to the activity
            val intent = Intent(this, UserSettings::class.java)

            //Starting the activity
            startActivity(intent)
        }



    }
}