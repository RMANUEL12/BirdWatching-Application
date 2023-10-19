package com.example.bobolinkbirdwatching

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton

class PersonalInformation : AppCompatActivity() {

    private val userDetailsList = mutableListOf<UserData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_information)

        // Referencing layout elements
        val firstNameEditText = findViewById<EditText>(R.id.editTextNewName)
        val lastNameEditText = findViewById<EditText>(R.id.editTextNewSurname)
        val emailEditText = findViewById<EditText>(R.id.editTextNewEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextNewPassword)
        val areaEditText = findViewById<EditText>(R.id.editTextNewArea)

        // Check if userDetailsList is not empty before accessing its elements
        if (userDetailsList.isNotEmpty()) {
            // Populating the EditTexts with the current user's details
            val currentUserDetails = userDetailsList[0]
            firstNameEditText.hint = (currentUserDetails.firstName)
            lastNameEditText.hint = (currentUserDetails.surname)
            emailEditText.hint = (currentUserDetails.email)
            passwordEditText.hint = (currentUserDetails.password)
            areaEditText.hint = (currentUserDetails.area)
        }

        val saveButton = findViewById<Button>(R.id.buttonUpdate)

        saveButton.setOnClickListener {
            try {
                // Getting the updated details from the EditTexts
                val updatedEmail = emailEditText.text.toString()
                val updatedPassword = passwordEditText.text.toString()
                val updatedArea = areaEditText.text.toString()

                // Check if userDetailsList is not empty before updating the details
                if (userDetailsList.isNotEmpty()) {
                    // Updating the user's details in the list
                    val currentUserDetails = userDetailsList[0]
                    currentUserDetails.email = updatedEmail
                    currentUserDetails.password = updatedPassword
                    currentUserDetails.area = updatedArea
                }

                // Refreshing the UI to display the updated details
                emailEditText.setText(updatedEmail)
                passwordEditText.setText(updatedPassword)
                areaEditText.setText(updatedArea)
            } catch (e: Exception) {
                // Error handling
                Log.e(ContentValues.TAG, "Something went wrong, Please Try Again.")
            }
        }

        val backButton = findViewById<ImageButton>(R.id.closeLoginPage)

        backButton.setOnClickListener {
            // Creating an Intent to navigate to the activity
            val intent = Intent(this, UserSetting::class.java)

            // Starting the activity
            startActivity(intent)
        }
    }

}