package com.developer.map


import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.developer.map.Cash.MySharedPreferences
import com.developer.map.service.LocationService
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.Task
import java.util.concurrent.TimeUnit

private const val TAG = "MapsActivity"

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val workRequest: WorkRequest =
            PeriodicWorkRequestBuilder<LocationService>(15, TimeUnit.MINUTES)
                .build()
        var btn = findViewById<Button>(R.id.btnstart)
        btn.setOnClickListener {
            WorkManager.getInstance(this)
                .enqueue(workRequest)
        }
        btn.setOnLongClickListener {
            startActivity(Intent(this, ListActivity::class.java))
            true
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        myMethod()
        currentLocation()
        MySharedPreferences.init(this)
        var listPolyline = MySharedPreferences.obektString
        val polyline = mMap.addPolyline(
            PolylineOptions().clickable(true)
                .addAll(listPolyline)
                .color(Color.GREEN)
        )
        if (listPolyline.size >= 1) {
            polyline.points = listPolyline
            Log.d(TAG, "onMapReady: $listPolyline")
        }

    }

    @SuppressLint("MissingPermission")
    private fun currentLocation() {
        fusedLocationProviderClient = FusedLocationProviderClient(this)
        val locationTask: Task<Location> = fusedLocationProviderClient.lastLocation
        locationTask.addOnSuccessListener { it: Location ->
            if (it != null) {
                var currentLatLng = LatLng(it.latitude, it.longitude)
                mMap.addMarker(
                    MarkerOptions()
                        .position(currentLatLng)
                )
                mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng))
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 35f))
            }
        }
    }

    fun myMethod() {
        askPermission(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) {

        }.onDeclined { e ->
            if (e.hasDenied()) {

                AlertDialog.Builder(this)
                    .setMessage("Please accept our permissions")
                    .setPositiveButton("yes") { dialog, which ->
                        e.askAgain();
                    }
                    .setNegativeButton("no") { dialog, which ->
                        dialog.dismiss();
                    }
                    .show();
            }

            if (e.hasForeverDenied()) {
                e.goToSettings();
            }
        }

    }

    fun start(view: android.view.View) {}

}