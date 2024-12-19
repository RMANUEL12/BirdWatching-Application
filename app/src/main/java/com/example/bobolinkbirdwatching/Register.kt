package com.example.bobolinkbirdwatching

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Register : AppCompatActivity() {

    private lateinit var buttonRegister: Button

    //Database reference
    private lateinit var databaseRef: DatabaseReference

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize the DatabaseReference
        databaseRef = FirebaseDatabase.getInstance().reference
        // Authentication service
        auth = FirebaseAuth.getInstance()

        buttonRegister = findViewById(R.id.buttonRegister)

        buttonRegister.setOnClickListener {
            try {
                val email = findViewById<EditText>(R.id.editTextEmail)?.text.toString()
                val passwordTemp = findViewById<EditText>(R.id.editTextPassword)?.text.toString()
                val password = findViewById<EditText>(R.id.editTextConfirmPassword)?.text.toString()
                val firstName = findViewById<EditText>(R.id.editTextfirstName)?.text.toString()
                val surname = findViewById<EditText>(R.id.editTextSurname)?.text.toString()
                val area = findViewById<EditText>(R.id.editTextArea)?.text.toString()

                if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || surname.isEmpty() || area.isEmpty()) {
                    Toast.makeText(applicationContext, "All fields are required.", Toast.LENGTH_SHORT).show()

                } else if (passwordTemp != password) {
                    Toast.makeText(applicationContext, "Passwords do not match.", Toast.LENGTH_SHORT).show()

                } else {
                    //SAVING TO DATABASE
                    val id = databaseRef.push().key

                    val newUser = UserData(id, firstName, surname, area, email, password)

                    databaseRef.child("tbl_users").child(id.toString()).setValue(newUser)

                    //Registering with auth service
                    registerWithEmailAndPassword(email, password, firstName, surname, area)

                }
            } catch (e: Exception) {
                Log.e(ContentValues.TAG, "Error: ${e.message}")
            }
        }

        val loginLinkText = findViewById<TextView>(R.id.buttonLogin)

        loginLinkText.isClickable = true
        loginLinkText.setOnClickListener {
            val logIntent = Intent(this@Register, Login::class.java)
            startActivity(logIntent)
        }

    }

    //Registering User with Authentication Service
    private fun registerWithEmailAndPassword(email: String, password: String, firstName: String, surname: String, area: String) {

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Registration success
                    val user = auth.currentUser

                    // Saving details
                    saveUserToDatabase(user, firstName, surname, area)

                    // Success message
                    Toast.makeText(this@Register, "Account Created Successfully!", Toast.LENGTH_SHORT).show()

                    // Redirecting user to login page
                    val intent = Intent(this@Register, Login::class.java)
                    startActivity(intent)

                } else {

                    // If registration fails, display a message to the user.
                    Toast.makeText(this@Register, "Registration failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun saveUserToDatabase(user: FirebaseUser?, firstName: String, surname: String, area: String) {
        user?.let {

            val userId = user.uid
            val userMap = HashMap<String, Any>()
            userMap["firstName"] = firstName
            userMap["surname"] = surname
            userMap["area"] = area

            databaseRef.child("users").child(userId).setValue(userMap)
        }
    }

}
