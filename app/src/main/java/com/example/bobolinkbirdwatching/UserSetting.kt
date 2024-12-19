package com.example.bobolinkbirdwatching

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [UserSetting.newInstance] factory method to
 * create an instance of this fragment.
 */

class UserSetting : Fragment() {

        private lateinit var btnUserSet: Button

        private lateinit var btnLogout: Button

        // TODO: Rename and change types of parameters
        private var param1: String? = null
        private var param2: String? = null

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            arguments?.let {
                param1 = it.getString(ARG_PARAM1)
                param2 = it.getString(ARG_PARAM2)
            }
        }

        override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            val view = inflater.inflate(R.layout.fragment_user_setting, container, false)

            val displayName = view.findViewById<TextView>(R.id.textViewUserName)
            val displayEmail = view.findViewById<TextView>(R.id.textViewUserEmail)

            // Create an instance of UserSession
            val userSession = UserSession(requireContext())

            // Email of the user currently logged in
            val givenEmail = userSession.currentEmail
            val firstName = userSession.currentName
            val surname = userSession.currentSurname

            displayName.text = (firstName + " " + surname)
            displayEmail.text = (givenEmail)

            btnUserSet = view.findViewById(R.id.btn_userDetails)

            btnUserSet.setOnClickListener {
                // Creating an Intent to navigate to the activity
                val intent = Intent(requireContext(), PersonalInformation::class.java)

                // Starting the activity
                startActivity(intent)
            }

            // Button reference
            btnLogout = view.findViewById(R.id.buttonLogOut)

            // Logout functionality
            btnLogout.setOnClickListener {

                // Instance of the Login class
                val loginInstance = Login()

                // Calling method to log user out
                loginInstance.signOut()


            }
            return view
        }

        companion object {
            /**
             * Use this factory method to create a new instance of
             * this fragment using the provided parameters.
             *
             * @param param1 Parameter 1.
             * @param param2 Parameter 2.
             * @return A new instance of fragment UserSetting.
             */
            // TODO: Rename and change types and number of parameters
            @JvmStatic
            fun newInstance(param1: String, param2: String) =
                UserSetting().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
        }
}

