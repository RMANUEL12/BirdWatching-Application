package com.example.bobolinkbirdwatching

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class MaxDistancePreference(
    private val context: Context,
    private val metricImperialPreference: MetricImperialPreference,
    private val mapActivity: MapsActivity
) {

    private val sharedPreferences = context.getSharedPreferences("MaxDistancePrefs", Context.MODE_PRIVATE)

    fun setMaxDistance(distance: Int) {
        sharedPreferences.edit().putInt("MAX_DISTANCE", distance).apply()
    }

    fun getMaxDistance(): Int {
        return sharedPreferences.getInt("MAX_DISTANCE", Int.MAX_VALUE) // default to a very large value
    }

    fun showMaxDistanceDialog() {
        val editText = EditText(context)
        editText.inputType = InputType.TYPE_CLASS_NUMBER
        editText.hint = if (metricImperialPreference.isMetric()) "Enter distance (km)" else "Enter distance (miles)"

        val dialog = AlertDialog.Builder(context)
            .setTitle("Set Maximum Distance")
            .setView(editText)
            .setPositiveButton("OK") { _, _ ->
                val distance = editText.text.toString().toFloatOrNull()
                if (distance != null) {
                    val distanceInKm = metricImperialPreference.convertFromPreferredUnit(distance)
                    setMaxDistance(distanceInKm.toInt())
                    Toast.makeText(context, "Max distance set to ${metricImperialPreference.convertToPreferredUnit(distanceInKm)}", Toast.LENGTH_SHORT).show()

                    // Fetch and display markers again after setting the max distance
                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
                    try {
                        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                            if (location != null) {
                                val userLocation = LatLng(location.latitude, location.longitude)
                                mapActivity.fetchAndDisplayMarkers(userLocation)
                            }
                        }
                    }catch (e: SecurityException){
                        Toast.makeText(context, "Location permission not granted.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Invalid distance", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }
}
