package com.example.bobolinkbirdwatching
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class FindBirds : Fragment() {
    // ... (parameters and other methods)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_find_birds, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the button with the ID buttonOpenMap
        val buttonOpenMap = view.findViewById<Button>(R.id.buttonOpenMap)

        // Set an OnClickListener for the button
        buttonOpenMap.setOnClickListener {
            // Create an Intent to start the MapsActivity
            val intent = Intent(requireContext(), MapsActivity::class.java)
            startActivity(intent)
        }
    }
}
