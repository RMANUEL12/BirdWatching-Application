package com.example.bobolinkbirdwatching

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class UserSetting : Fragment() {
    private lateinit var btnUserSet: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user_setting, container, false)

        btnUserSet = view.findViewById(R.id.btn_userDetails)

        btnUserSet.setOnClickListener {
            // Creating an Intent to navigate to the activity
            val intent = Intent(requireContext(), PersonalInformation::class.java)

            // Starting the activity
            startActivity(intent)
        }

        return view
    }
}
