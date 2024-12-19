package com.example.bobolinkbirdwatching

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.google.firebase.database.FirebaseDatabase


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddSighting.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddSighting : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private var userLocation: String = ""
    private var user: String = ""
    private var selectedBirdBreed: String = ""

    // Storing a reference to the context
    private lateinit var fragmentContext: Context

    // Save Button
    private lateinit var buttonSave : Button

    //Database reference
    private lateinit var databaseRef: DatabaseReference

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

        // Initialize the DatabaseReference
        databaseRef = FirebaseDatabase.getInstance().reference

        // Store the context reference
        fragmentContext = requireContext()

        val view = inflater.inflate(R.layout.fragment_add_sighting, container, false)

        buttonSave = view.findViewById<Button>(R.id.buttonSave)

        //Getting a reference to the Spinner
        val birdSpinner = view.findViewById<Spinner>(R.id.birdBreedSpinner)

        //Creating an ArrayAdapter with a list of bird breeds
        val birdBreeds = arrayOf(
            "African Fish Eagle", "African Penguin", "Blue Crane", "Bobolink",
            "Southern Yellow-billed Hornbill", "Lilac-breasted Roller", "Secretarybird",
            "Southern Red Bishop", "Greater Flamingo", "African Hoopoe", "Cape Glossy Starling",
            "Cape Weaver", "Cape Canary", "Cape Robin-Chat", "Hadeda Ibis", "Knysna Turaco",
            "Egyptian Goose", "Helmeted Guineafowl", "Black-collared Barbet", "Yellow-billed Duck",
            "African Sacred Ibis", "Southern Masked Weaver", "Parrot", "Eagle", "Sparrow",
            "Ostrich", "Hummingbird", "African Grey Hornbill", "Yellow-throated Longclaw",
            "African Jacana", "Red-eyed Dove", "White-backed Vulture", "African Spoonbill", "Pied Crow",
            "Little Swift", "Fork-tailed Drongo", "African Pipit", "Crowned Hornbill",
            "Pied Kingfisher", "Malachite Kingfisher", "Southern Ground Hornbill"
        )

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, birdBreeds)

        //Specifying the layout style of the Spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Set the ArrayAdapter for the Spinner
        birdSpinner.adapter = adapter

        //Save button functionality
        buttonSave.setOnClickListener {

            try{
                // Set a listener to respond to item selection
                birdSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                        // Saving selection
                        selectedBirdBreed = birdSpinner.getItemAtPosition(position).toString()
                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Setting value to null
                        selectedBirdBreed = ""
                    }
                }
                //getting the current date
                val currentDate = LocalDate.now()

                //formatting the date
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val sightDate = currentDate.format(formatter).toString()

                //Session reference
                val userSession = UserSession(fragmentContext)

                //Getting user's location
                userLocation = userSession.userLocation.toString()

                //Getting ID of the logged user
                user = userSession.userID.toString()

                // Saving input for new breed
                val breed = view.findViewById<EditText>(R.id.editTextAddBreed)?.text.toString()

                // Ensuring that both fields are not empty
                if (breed.isEmpty() && selectedBirdBreed.isNullOrEmpty()){
                    // Showing message
                    Toast.makeText(requireContext(), "Observation Requires Details. Please select or enter a bird breed.", Toast.LENGTH_SHORT).show()
                }
                else{
                    // Checking if breed was selected
                    if(selectedBirdBreed != ""){ // Saving input if found

                        //Creating unique id
                        val id = databaseRef.push().key

                        //saving observation
                        val sighting = BirdObservation(id, user, selectedBirdBreed, userLocation, sightDate)
                        databaseRef.child("tbl_sightings").child(id.toString()).setValue(sighting);

                        // Showing success message
                        Toast.makeText(requireContext(), "Observation Successfully Saved!", Toast.LENGTH_SHORT).show()

                    }
                    else{

                        // Checking if breed was typed in
                        if(breed.isNotEmpty()){ // Saving input if found

                            //Creating unique id
                            val id = databaseRef.push().key

                            //Saving observation to database
                            val sighting = BirdObservation(id, user, breed, userLocation, sightDate)
                            databaseRef.child("tbl_sightings").child(id.toString()).setValue(sighting);

                            // Showing success message
                            Toast.makeText(requireContext(), "Observation Successfully Saved!", Toast.LENGTH_SHORT).show()

                        }
                    }
                }
            }
            // Error handling
            catch(e: Exception){

                // Error message
                Log.e(ContentValues.TAG, "Error adding sighting: ${e.message}")
                Toast.makeText(requireContext(), "Something went wrong, Please Try Again.", Toast.LENGTH_SHORT).show()
            }
        }
        return view
    }

    companion object {
        const val REQUEST_LOCATION_PERMISSION_CODE = 1001

        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddSighting.
         */

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddSighting().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}