package com.example.bobolinkbirdwatching

// BaseInfoWindowAdapter.kt
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

abstract class BaseInfoWindowAdapter : GoogleMap.InfoWindowAdapter {

    override fun getInfoWindow(marker: Marker): View? {
        // Default implementation here (if any), or return null
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        return provideInfoContents(marker)
    }

    abstract fun provideInfoContents(marker: Marker): View
}
