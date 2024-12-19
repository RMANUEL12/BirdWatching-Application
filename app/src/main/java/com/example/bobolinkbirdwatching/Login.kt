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
import androidx.annotation.NonNull
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Login : AppCompatActivity() {

    private lateinit var btnLogin: Button

    //Authentication service
    private lateinit var auth: FirebaseAuth

    //Database reference
    private lateinit var databaseRef: DatabaseReference

    // Object to hold user ID
    private var userId: String = "";
    private var firstName: String = ""
    private  var surname: String = ""
    private var area: String = ""


    // Database reference
    //private val databaseRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://bobolink-a2a13-default-rtdb.firebaseio.com/")

    //private val databaseRef = FirebaseDatabase.getInstance().getReference("https://bobolink-a2a13-default-rtdb.firebaseio.com/")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnLogin = findViewById(R.id.buttonLogin)

        // Initialise the DatabaseReference
        databaseRef = FirebaseDatabase.getInstance().reference

        //Initialising auth service
        auth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {
            val email = findViewById<EditText>(R.id.inputEmail).text.toString()
            val password = findViewById<EditText>(R.id.inputPassword).text.toString()

            //Checking that input is not null
            if (email.isNotEmpty() && password.isNotEmpty()) {

                //Calling method to sign in with auth service
                signInWithEmailAndPassword(email, password)

            } else {

                Toast.makeText(this@Login, "Please enter email and password", Toast.LENGTH_SHORT).show()
            }

//            databaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    if (snapshot.hasChild(email)) {
//                        val savedPassword = snapshot.child(email).child("password").getValue(String::class.java)
//
//                        if (savedPassword == password) {
//                            Toast.makeText(this@Login, "Successful Login", Toast.LENGTH_SHORT).show()
//
//                            // Redirecting to Home (Map)
//                            val intent = Intent(this@Login, FindBirds::class.java)
//                            startActivity(intent)
//                        } else {
//                            Toast.makeText(applicationContext, "Invalid Details. Please Retry or Register...", Toast.LENGTH_SHORT).show()
//                        }
//                    } else {
//                        Toast.makeText(applicationContext, "Invalid Details. Please Retry or Register...", Toast.LENGTH_SHORT).show()
//                    }
//                }
//
//                override fun onCancelled(databaseError: DatabaseError) {
//                    Log.e(ContentValues.TAG, "Database error: ${databaseError.message}")
//                }
//            })
        }

        // Register Link Button functionality
        val registerLinkText = findViewById<TextView>(R.id.buttonRegister)
        registerLinkText.isClickable = true
        registerLinkText.setOnClickListener {
            val regIntent = Intent(this@Login, Register::class.java)
            startActivity(regIntent)
        }
    }

    //Method to sign in with auth service
    private fun signInWithEmailAndPassword(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->

                // Authenticating
                if (task.isSuccessful) {

                    // Sign in success
                    val user = auth.currentUser

                    // STARTING SESSION:
                    // Create an instance of UserSession
                    val userSession = UserSession(this)

                    // Setting the user email when the user logs in
                    userSession.currentEmail = email

                    // Calling method to fetch user ID
                    retrieveUserDetails(email, userSession);

                    // Success message
                    Toast.makeText(this@Login, "Authentication Successful.", Toast.LENGTH_SHORT).show()

                    // Redirecting user
                    navigateToFindBirds()

                } else {

                    // If sign in fails, displaying a message to the user.
                    Toast.makeText(this@Login, "Authentication failed. Please try again or Register.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Method to move from activity to fragment
    private fun navigateToFindBirds() {
        // Creating an instance of the FindBirds fragment
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    // Method to get current user details
    public fun retrieveUserDetails(email: String?, userSession: UserSession) {
        // Initialise the DatabaseReference
        databaseRef = FirebaseDatabase.getInstance().reference

        email?.let {
            // Query the database using the user's email to get the details
            databaseRef.child("tbl_users").orderByChild("email").equalTo(email)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (userSnapshot in snapshot.children) {

                                // Saving user details
                                userId = userSnapshot.child("id").getValue(String::class.java).toString()
                                firstName = userSnapshot.child("firstName").getValue(String::class.java).toString()
                                surname = userSnapshot.child("surname").getValue(String::class.java).toString()
                                area = userSnapshot.child("area").getValue(String::class.java).toString()

                                //Saving ID to session
                                userSession.userID = userId
                                userSession.currentName = firstName
                                userSession.currentSurname = surname
                                userSession.currentArea = area

                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(ContentValues.TAG, "Error retrieving user details: $error")
                    }
                })
        }
    }

    //LOGOUT method
    public fun signOut() {

        //Initialising FirebaseAuth
        auth = FirebaseAuth.getInstance()

        //Signing out
        auth.signOut()

        // ENDING SESSION:
        // Create an instance of UserSession
        val userSession = UserSession(this@Login)

        // Setting the user email when the user logs in
        userSession.currentEmail = ""
        userSession.userID = ""
        userSession.currentArea = ""
        userSession.currentName = ""
        userSession.currentSurname = ""
        userSession.userLocation=""

        Toast.makeText(this, "You have been signed out.", Toast.LENGTH_SHORT).show()

        // Creating an Intent to navigate to the activity
        val intent = Intent(this, Login::class.java)

        // Starting the activity
        startActivity(intent)
    }
}