package com.example.bobolinkbirdwatching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.maps.model.LatLng
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import android.content.pm.PackageManager
import android.Manifest
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast

class SaveBirdObservation : AppCompatActivity() {
    private var buttonSave = Button(this);
    private val sightingsList = mutableListOf<BirdObservation>()
    private lateinit var userLocation: String
    private lateinit var selectedBirdBreed: String
    private lateinit var mMap: GoogleMap
    companion object {
        const val REQUEST_LOCATION_PERMISSION_CODE = 1001
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_bird_observation)

        buttonSave = findViewById<Button>(R.id.buttonSave)

        //Getting a reference to the Spinner
        val birdSpinner = findViewById<Spinner>(R.id.birdBreedSpinner)

        //Creating an ArrayAdapter with a list of bird breeds
        //https://birdwatchinghq.com/birds-of-south-africa/
        val birdBreeds = arrayOf("African Fish Eagle","African Penguin", "Blue Crane", "Southern Yellow-billed Hornbill",
                "Lilac-breasted Roller", "Secretarybird", "Southern Red Bishop", "Greater Flamingo", "African Hoopoe",
                "Cape Glossy Starling", "Cape Weaver", "Cape Canary", "Cape Robin-Chat", "Hadeda Ibis", "Knysna Turaco",
                "Egyptian Goose", "Helmeted Guineafowl", "Black-collared Barbet", "Yellow-billed Duck", "African Sacred Ibis" ,
                "Southern Masked Weaver","Parrot", "Eagle", "Sparrow", "Ostrich", "Hummingbird")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, birdBreeds)

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
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.isMyLocationEnabled = true

                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            //saving location
                            userLocation = LatLng(location.latitude, location.longitude).toString()
                        }
                    }
                }
                else {
                    ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION_CODE)
                }

                //saving input for new breed
                val breed = findViewById<EditText>(R.id.editTextAddBreed).text.toString()

                //ensuring that field is not empty
                if (breed.isNotEmpty()){

                    //copying user's location
                    val sightLocation = userLocation

                    //saving observation
                    val sighting = BirdObservation(breed, sightLocation, sightDate)
                    sightingsList.add(sighting)

                    // Showing success message
                    Toast.makeText(applicationContext, "Observation Successfully Saved!", Toast.LENGTH_SHORT).show()
                }
                else{

                    //copying user's location
                    val sightLocation = userLocation

                    //saving observation
                    val sighting = BirdObservation(selectedBirdBreed, sightLocation, sightDate)
                    sightingsList.add(sighting)

                    // Showing success message
                    Toast.makeText(applicationContext, "Observation Successfully Saved!", Toast.LENGTH_SHORT).show()
                }

            }
            //error handling
            catch(e: Exception){

                //error message
                Log.e(TAG, "Something went wrong, Please Try Again.")
            }
        }

    }
}