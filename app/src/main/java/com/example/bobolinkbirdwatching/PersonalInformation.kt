package com.example.bobolinkbirdwatching

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PersonalInformation : AppCompatActivity() {

    // Database references
    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_personal_information)

        // Create an instance of UserSession

        val userSession = UserSession(this)

        // Details of the user currently logged in
        val loggedEmail = userSession.currentEmail
        val loggedFirstName = userSession.currentName
        val loggedSurname = userSession.currentSurname
        val loggedArea = userSession.currentArea


        // Referencing layout elements
        val firstNameEditText = findViewById<EditText>(R.id.editTextNewName)
        val lastNameEditText = findViewById<EditText>(R.id.editTextNewSurname)
        val areaEditText = findViewById<EditText>(R.id.editTextNewArea)


        // Populating the EditTexts with the current user's details
        firstNameEditText.setText(loggedFirstName)
        lastNameEditText.setText(loggedSurname)
        areaEditText.setText(loggedArea)

        val saveButton = findViewById<Button>(R.id.buttonUpdate)

        saveButton.setOnClickListener {
            try {
                // Getting the updated details from the EditTexts
                val updatedName = firstNameEditText.text.toString()
                val updatedSurname = lastNameEditText.text.toString()
                val updatedArea = areaEditText.text.toString()

                // Calling method to update user details
                updateUserDetails(loggedEmail, updatedName, updatedSurname, updatedArea, userSession)


            } catch (e: Exception) {
                // Error handling
                Log.e(ContentValues.TAG, "Something went wrong, Please Try Again.")
            }
        }

        // Back button functionality
        val backButton = findViewById<ImageButton>(R.id.closeLoginPage)

        backButton.setOnClickListener {

            // Redirecting user
            navigateToUserSetting()

        }
    }


    // Method to update details
    private fun updateUserDetails(oldEmail: String?, newName: String, newSurname: String, newArea: String, userSession: UserSession) {
        oldEmail?.let {
            // Query the database using the old email to get the UID
            databaseRef.child("tbl_users").orderByChild("email").equalTo(oldEmail)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (userSnapshot in snapshot.children) {
                                // Update user details
                                userSnapshot.ref.child("firstName").setValue(newName)
                                userSnapshot.ref.child("surname").setValue(newSurname)
                                userSnapshot.ref.child("area").setValue(newArea)

                                // Success message
                                Toast.makeText(this@PersonalInformation, "Details Updated Successfully!", Toast.LENGTH_SHORT).show()

                                // Instance of the Login class
                                val loginInstance = Login()


                                // Calling method to fetch user details again
                                loginInstance.retrieveUserDetails(oldEmail, userSession)
                          }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                        // Logging error
                        Log.e(ContentValues.TAG, "Error updating user details: $error")
                        Toast.makeText(this@PersonalInformation, "Something went wrong, Please Try Again.", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }

    // Method to move from activity to fragment
    private fun navigateToUserSetting() {

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

}