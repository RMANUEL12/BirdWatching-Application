package com.example.bobolinkbirdwatching

import CustomInfoWindowAdapter
import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bobolinkbirdwatching.api.EBirdApiService
import com.example.bobolinkbirdwatching.data.Observation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.bobolinkbirdwatching.databinding.ActivityMapsBinding
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener
import com.google.maps.DirectionsApi
import com.google.maps.DirectionsApiRequest
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import com.google.maps.model.DirectionsResult
import com.google.maps.model.EncodedPolyline
import com.google.maps.model.TravelMode
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    companion object {
        const val REQUEST_LOCATION_PERMISSION_CODE = 1001
    }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val metricImperialPreference by lazy { MetricImperialPreference(this) }
    private val maxDistancePreference by lazy { MaxDistancePreference(this, metricImperialPreference, this) }
    private lateinit var eBirdApiService: EBirdApiService
    private var currentPolyline: Polyline? = null // Add this as a property
    private lateinit var databaseRef: DatabaseReference
    private val apiHotspotMarkers = mutableListOf<Marker>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val metricImperialSwitch: Switch = findViewById(R.id.metricImperialSwitch)
        metricImperialSwitch.setOnCheckedChangeListener { _, isChecked ->
            metricImperialPreference.togglePreference(isChecked)
        }

        // Initialize the maximum distance button and set its listener
        val maxDistanceButton: Button = findViewById(R.id.maxDistanceButton)
        maxDistanceButton.setOnClickListener {
            maxDistancePreference.showMaxDistanceDialog()
        }

        val returnButton: Button = findViewById(R.id.btn_privacyPolicy)
        returnButton.setOnClickListener { val intent = Intent(this, MainActivity::class.java)
            startActivity(intent) }



        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.ebird.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        eBirdApiService = retrofit.create(EBirdApiService::class.java)
        // Initialize the DatabaseReference
        databaseRef = FirebaseDatabase.getInstance().getReference("tbl_sightings")
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) // Add this line

        when (requestCode) {
            REQUEST_LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If permission was granted, enable location on the map
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        mMap.isMyLocationEnabled = true
                    }
                } else {
                    // If permission was denied, show a message to the user
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }


    //------------------------------------------------------------------------------------------------------------------------------------------------------------

    fun calculateDistance(startLat: Double, startLng: Double, endLat: Double, endLng: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(startLat, startLng, endLat, endLng, results)
        return results[0] / 1000 // Convert meters to kilometers.
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        setMapStyle(mMap)
        // Check for location permission
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true

            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)

                    val userSession = UserSession(this)

                    // Setting the user email when the user logs in
                    userSession.userLocation = userLocation.toString()

                    // Create an instance of CustomInfoWindowAdapter with the current location
                    val customInfoWindowAdapter = CustomInfoWindowAdapter(this, userLocation)
                    mMap.setInfoWindowAdapter(customInfoWindowAdapter)




                    fetchAndDisplayMarkers(userLocation)

                    // Retrieve and display the bird observations
                    fetchAndDisplayObservations()


                    mMap.setOnInfoWindowClickListener { marker ->

                        // Handle info window click to show directions here
                        val selectedHotspotLocation = marker.position // Get the selected hotspot's location
                        val apiKey = "AIzaSyAsE1a23gMR3H3-vErMpPMOSxVWgCg_2Rs" // Replace with your API key
                        showDirections(userLocation, selectedHotspotLocation, apiKey)
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION_CODE)
        }

        val africa = LatLng(-33.9, 18.4)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(africa, 4.0f))
    }
    //------------------------------------------------------------------------------------------------------------------------------------------------------------

     fun fetchAndDisplayObservations() {
        // Assuming 'userSession.userID' holds the ID of the logged-in user
        val userSession = UserSession(this)
        val userId = userSession.userID
        val query: Query = databaseRef.orderByChild("user").equalTo(userId)

        query.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("MapsActivity", "onDataChange called with snapshot: $snapshot")

                //mMap.clear() // Clear existing markers before adding new ones

                for (sightingSnapshot in snapshot.children) {
                    val sighting = sightingSnapshot.getValue(BirdObservation::class.java)
                    sighting?.sightLocation?.let { location ->
                        // Attempt to extract the lat/lng part and handle potential nullability
                        val locationString = location.split("lat/lng: ")[1].trim('(', ')')
                        val locationParts = locationString.split(",").map { it.trim() }
                        if (locationParts.size == 2) {
                            val latitude = locationParts[0].toDoubleOrNull()
                            val longitude = locationParts[1].toDoubleOrNull()

                            Log.d("MapsActivity", "Adding marker at: lat=$latitude, lng=$longitude")

                            // If latitude and longitude were successfully parsed, add the marker
                            if (latitude != null && longitude != null) {
                                val observationLatLng = LatLng(latitude, longitude)
                                mMap.addMarker(
                                    MarkerOptions()
                                        .position(observationLatLng)
                                        .title(sighting.breed) // Use 'breed' as title for marker
                                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)) // Set marker color to yellow
                                )
                            }
                        }
                    }


                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("MapsActivity", "onCancelled called with error: ${error.message}")
                Log.e(ContentValues.TAG, "Failed to read sightings from database", error.toException())
                Toast.makeText(this@MapsActivity, "Failed to load observations.", Toast.LENGTH_SHORT).show()
            }
        })
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------


    private fun setMapStyle(map: GoogleMap){
        try{
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                this,
                R.raw.map_style
            ))
            if (!success){
                Log.e("Style", "Style parsing has Failed")
            }
        }catch (e: Resources.NotFoundException){
            Log.e("Style", "Can't find Style",e)

        }
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------


    private fun showDirections(origin: LatLng, destination: LatLng, apiKey: String) {
        // Remove the previous Polyline if it exists
        currentPolyline?.remove()

        // Call the drawRouteOnMap function to draw the route and assign it to currentPolyline.
        currentPolyline = drawRouteOnMap(mMap, origin, destination, apiKey)
    }


    //------------------------------------------------------------------------------------------------------------------------------------------------------------

    fun fetchAndDisplayMarkers(userLocation: LatLng) {
        eBirdApiService.getRecentObservationsForRegion().enqueue(object :
            Callback<List<Observation>> {
            override fun onResponse(call: Call<List<Observation>>, response: Response<List<Observation>>) {
                if (response.isSuccessful) {
                    val observations = response.body()
                    //mMap.clear() // Clear existing markers
                    apiHotspotMarkers.forEach { it.remove() }
                    apiHotspotMarkers.clear()

                    observations?.forEach { observation ->
                        val observationLocation = LatLng(observation.lat, observation.lng)
                        val distance = calculateDistance(userLocation.latitude, userLocation.longitude, observationLocation.latitude, observationLocation.longitude)

                        if (distance <= maxDistancePreference.getMaxDistance()) {
                           val hotspotMarker = mMap.addMarker(MarkerOptions().position(observationLocation).title(observation.comName))
                            hotspotMarker?.let {
                                apiHotspotMarkers.add(it)
                            }

                        }
                    }
                } else {
                    Log.e("API_ERROR", "Response was not successful. Code: ${response.code()}, Message: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<Observation>>, t: Throwable) {
                Log.e("EBirdAPI", "Error fetching observations", t)
            }
        })
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------

    private fun drawRouteOnMap(
        googleMap: GoogleMap,
        origin: LatLng,
        destination: LatLng,
        apiKey: String
    ): Polyline {
        val geoApiContext = GeoApiContext.Builder().apiKey(apiKey).build()

        // Create a DirectionsApiRequest.
        val request: DirectionsApiRequest = DirectionsApi.newRequest(geoApiContext)
            .mode(TravelMode.DRIVING)
            .origin(com.google.maps.model.LatLng(origin.latitude, origin.longitude))
            .destination(com.google.maps.model.LatLng(destination.latitude, destination.longitude))

        val route: DirectionsResult = request.await()

        // Decode the polyline from the response.
        val encodedPolyline: EncodedPolyline = route.routes[0].overviewPolyline
        val points: List<com.google.android.gms.maps.model.LatLng> =
            PolyUtil.decode(encodedPolyline.encodedPath)

        // Create a PolylineOptions object to draw the route on your map.
        val polylineOptions = PolylineOptions()
            .width(10f)
            .color(Color.BLUE)

        for (point in points) {
            polylineOptions.add(point)
        }

        // Add the polyline to your Google Map and return it.
        return googleMap.addPolyline(polylineOptions)
    }

}