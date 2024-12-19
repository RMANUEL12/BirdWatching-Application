
package com.example.bobolinkbirdwatching

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.gms.maps.GoogleMap


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

    }
    }
