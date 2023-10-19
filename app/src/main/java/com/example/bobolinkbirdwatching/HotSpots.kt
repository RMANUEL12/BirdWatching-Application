package com.example.bobolinkbirdwatching

import CustomInfoWindowAdapter
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.bobolinkbirdwatching.data.Observation
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
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
import com.example.bobolinkbirdwatching.api.EBirdApiService




class HotSpots : Fragment(), OnMapReadyCallback {

    companion object {
        const val REQUEST_LOCATION_PERMISSION_CODE = 1001
    }

    private lateinit var mMap: GoogleMap
    private val metricImperialPreference by lazy { MetricImperialPreference(requireContext()) }
    private val maxDistancePreference by lazy { MaxDistancePreference(requireContext(), metricImperialPreference, this) }
    private lateinit var eBirdApiService: EBirdApiService
    private var currentPolyline: Polyline? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_hot_spots, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val metricImperialSwitch: Switch = view.findViewById(R.id.metricImperialSwitch)
        metricImperialSwitch.setOnCheckedChangeListener { _, isChecked ->
            metricImperialPreference.togglePreference(isChecked)
        }

        // Initialize the maximum distance button and set its listener
        val maxDistanceButton: Button = view.findViewById(R.id.maxDistanceButton)
        maxDistanceButton.setOnClickListener {
            maxDistancePreference.showMaxDistanceDialog()
        }

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.ebird.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        eBirdApiService = retrofit.create(EBirdApiService::class.java)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_LOCATION_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If permission was granted, enable location on the map
                    if (ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        mMap.isMyLocationEnabled = true
                    }
                } else {
                    // If permission was denied, show a message to the user
                    Toast.makeText(requireContext(), "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Other methods like onMapReady, calculateDistance, showDirections, fetchAndDisplayMarkers, decodePolyline, and drawRouteOnMap
    fun calculateDistance(startLat: Double, startLng: Double, endLat: Double, endLng: Double): Float {
        val results = FloatArray(1)
        Location.distanceBetween(startLat, startLng, endLat, endLng, results)
        return results[0] / 1000 // Convert meters to kilometers.
    }

    //------------------------------------------------------------------------------------------------------------------------------------------------------------

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Set the custom info window adapter
        val customInfoWindowAdapter = CustomInfoWindowAdapter(requireContext())

        mMap.setInfoWindowAdapter(customInfoWindowAdapter)

        mMap.uiSettings.isZoomControlsEnabled = true

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true

            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())

            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val userLocation = LatLng(location.latitude, location.longitude)
                    fetchAndDisplayMarkers(userLocation)

                    mMap.setOnInfoWindowClickListener { marker ->
                        // Handle info window click to show directions here
                        val selectedHotspotLocation = marker.position // Get the selected hotspot's location
                        val apiKey = "AIzaSyAsE1a23gMR3H3-vErMpPMOSxVWgCg_2Rs" // Replace with your API key
                        showDirections(userLocation, selectedHotspotLocation, apiKey)
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION_CODE
            )
        }


        val africa = LatLng(-33.9, 18.4)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(africa, 4.0f))
    }

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
                    mMap.clear() // Clear existing markers
                    observations?.forEach { observation ->
                        val observationLocation = LatLng(observation.lat, observation.lng)
                        val distance = calculateDistance(userLocation.latitude, userLocation.longitude, observationLocation.latitude, observationLocation.longitude)

                        if (distance <= maxDistancePreference.getMaxDistance()) {
                            mMap.addMarker(
                                MarkerOptions().position(observationLocation)
                                    .title(observation.comName)
                            )
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

    fun decodePolyline(polyline: String): List<LatLng> {
        val points = ArrayList<LatLng>()
        var index = 0
        val len = polyline.length
        var lat = 0
        var lng = 0

        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = polyline[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat

            shift = 0
            result = 0
            do {
                b = polyline[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng

            val p = LatLng(
                lat / 1e5,
                lng / 1e5
            )
            points.add(p)
        }
        return points
    }

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


