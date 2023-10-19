package com.example.bobolinkbirdwatching

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
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
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.maps.model.LatLng
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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

    private val sightingsList = mutableListOf<BirdObservation>()
    private lateinit var userLocation: String
    private lateinit var selectedBirdBreed: String
    private lateinit var mMap: GoogleMap


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

        val view = inflater.inflate(R.layout.fragment_add_sighting, container, false)

        val buttonSave = view.findViewById<Button>(R.id.buttonSave)

        //Getting a reference to the Spinner
        val birdSpinner = view.findViewById<Spinner>(R.id.birdBreedSpinner)

        //Creating an ArrayAdapter with a list of bird breeds
        //https://birdwatchinghq.com/birds-of-south-africa/
        val birdBreeds = arrayOf("African Fish Eagle","African Penguin", "Blue Crane", "Southern Yellow-billed Hornbill",
            "Lilac-breasted Roller", "Secretarybird", "Southern Red Bishop", "Greater Flamingo", "African Hoopoe",
            "Cape Glossy Starling", "Cape Weaver", "Cape Canary", "Cape Robin-Chat", "Hadeda Ibis", "Knysna Turaco",
            "Egyptian Goose", "Helmeted Guineafowl", "Black-collared Barbet", "Yellow-billed Duck", "African Sacred Ibis" ,
            "Southern Masked Weaver","Parrot", "Eagle", "Sparrow", "Ostrich", "Hummingbird")

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
                        //saving selection
                        selectedBirdBreed = birdSpinner.getItemAtPosition(position).toString()

                    }

                    override fun onNothingSelected(parent: AdapterView<*>?) {
                        // Do nothing, for now
                    }
                }

                //getting the current date
                val currentDate = LocalDate.now()

                //formatting the date
                val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                val sightDate = currentDate.format(formatter)

                //getting the user's current location
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = true

                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            //saving location
                            userLocation = LatLng(location.latitude, location.longitude).toString()
                        }
                        if (location == null){
                                ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                                    SaveBirdObservation.REQUEST_LOCATION_PERMISSION_CODE
                                )
                        }
                    }
                }
                else {
                    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        SaveBirdObservation.REQUEST_LOCATION_PERMISSION_CODE
                    )
                }

                //saving input for new breed
                val breed = view.findViewById<EditText>(R.id.editTextAddBreed).text.toString()

                //ensuring that field is not empty
                if (breed.isNotEmpty()){

                    //copying user's location
                    val sightLocation = userLocation

                    //saving observation
                    val sighting = BirdObservation(breed, sightLocation, sightDate)
                    sightingsList.add(sighting)

                    // Showing success message
                    Toast.makeText(requireContext(), "Observation Successfully Saved!", Toast.LENGTH_SHORT).show()
                }

                if(selectedBirdBreed.isNotBlank()){

                    //copying user's location
                    val sightLocation = userLocation

                    //saving observation
                    val sighting = BirdObservation(selectedBirdBreed, sightLocation, sightDate)
                    sightingsList.add(sighting)

                    // Showing success message
                    Toast.makeText(requireContext(), "Observation Successfully Saved!", Toast.LENGTH_SHORT).show()
                }

            }
            //error handling
            catch(e: Exception){

                //error message
                Log.e(ContentValues.TAG, "Something went wrong, Please Try Again.")
            }
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
         * @return A new instance of fragment AddSighting.
         */
        const val REQUEST_LOCATION_PERMISSION_CODE = 1001

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