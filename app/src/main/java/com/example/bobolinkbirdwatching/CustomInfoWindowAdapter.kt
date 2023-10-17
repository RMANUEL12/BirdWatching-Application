package com.example.bobolinkbirdwatching

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.example.bobolinkbirdwatching.databinding.CustomInfoWindowBinding

class CustomInfoWindowAdapter(private val context: Context) : GoogleMap.InfoWindowAdapter {

    private val inflater = LayoutInflater.from(context)
    var currentMarker: Marker? = null

    override fun getInfoWindow(marker: Marker): View? {
        // This method returns null if you just want to modify the content of the default InfoWindow
        // but still keep its default design (bubble shape, etc.)
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val binding = CustomInfoWindowBinding.inflate(inflater)

        // Assuming the marker's title is the bird's name
        binding.birdName.text = marker.title

        // You can set other views in your custom layout similarly
        // For example, if you had a description TextView:
        // binding.description.text = marker.snippet

        currentMarker = marker

        return binding.root
    }
}
