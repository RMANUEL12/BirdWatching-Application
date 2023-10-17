package com.example.bobolinkbirdwatching

import android.content.Context
import android.widget.Toast

class MetricImperialPreference(private val context: Context) {
    private val sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)

    fun isMetric(): Boolean {
        return sharedPreferences.getBoolean("IS_METRIC", true)
    }

    fun togglePreference(isMetric: Boolean) {
        sharedPreferences.edit().putBoolean("IS_METRIC", isMetric).apply()
        Toast.makeText(context, if (isMetric) "Using Metric System" else "Using Imperial System", Toast.LENGTH_SHORT).show()
    }

    fun convertToPreferredUnit(distanceInKm: Float): Float {
        return if (isMetric()) {
            distanceInKm
        } else {
            distanceInKm * 0.621371f // Convert km to miles
        }
    }

    fun convertFromPreferredUnit(distance: Float): Float {
        return if (isMetric()) {
            distance
        } else {
            distance / 0.621371f // Convert miles to km
        }
    }
}

