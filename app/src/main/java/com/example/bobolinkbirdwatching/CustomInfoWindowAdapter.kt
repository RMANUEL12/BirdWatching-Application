import android.content.Context
import android.view.LayoutInflater
import android.view.View
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.example.bobolinkbirdwatching.databinding.CustomInfoWindowBinding
import com.google.android.gms.maps.model.LatLng
import com.google.android.play.core.integrity.b
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import java.lang.Math.ceil
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale



class CustomInfoWindowAdapter(private val context: Context, private val currentLocation: LatLng) : GoogleMap.InfoWindowAdapter {

    private val inflater = LayoutInflater.from(context)
    private var currentMarker: Marker? = null

    override fun getInfoWindow(marker: Marker): View? {
        // Return null to use the default info window layout
        return null
    }

    override fun getInfoContents(marker: Marker): View {
        val binding = CustomInfoWindowBinding.inflate(inflater)

        // Assuming the marker's title is the bird's name
        binding.birdName.text = marker.title

        // Calculate distance and display it
        val distance = calculateDistance(
            currentLocation.latitude,
            currentLocation.longitude,
            marker.position.latitude,
            marker.position.longitude
        )
        binding.distanceText.text = "Distance: $distance km"

        // Calculate duration and arrival time and display them
        val durationMinutes = calculateDuration(distance)
        val arrivalTime = calculateArrivalTime(durationMinutes)
        binding.durationText.text = "Duration: $durationMinutes min"
        binding.arrivalTimeText.text = "Arrival Time: $arrivalTime"

        currentMarker = marker

        return binding.root
    }

    private fun calculateDistance(
        startLat: Double,
        startLng: Double,
        endLat: Double,
        endLng: Double
    ): String {
        // Calculate straight-line distance between two points (Haversine formula)
        val radius = 6371 // Earth's radius in kilometers
        val dLat = Math.toRadians(endLat - startLat)
        val dLng = Math.toRadians(endLng - startLng)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        val distance = radius * c
        return String.format("%.2f", distance)
    }

    private fun calculateDuration(distance: String): Long {
        // Calculate a rough estimate of duration based on average speed (e.g., 50 km/h)
        val averageSpeedKmPerHour = 50.0
        val distanceKm = distance.toDouble()
        return ceil(distanceKm / averageSpeedKmPerHour * 60).toLong()
    }

    private fun calculateArrivalTime(durationMinutes: Long): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.MINUTE, durationMinutes.toInt())

        val dateFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }
}



