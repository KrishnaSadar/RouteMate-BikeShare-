package com.example.driverapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

//insertNavigationSlideBar
class MainActivity5 : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main5)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        // Customize the map as needed
        // For example, enable user gestures:
        map.uiSettings.isZoomControlsEnabled = true
        map.uiSettings.isMapToolbarEnabled = true

        // Add a marker at a specific location
        // For example, Sydney, Australia
        val sydney = LatLng(-33.852, 151.211)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))

        // Move the camera to the marker's position with a zoom level
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 10f))
    }
}