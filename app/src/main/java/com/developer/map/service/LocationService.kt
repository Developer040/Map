package com.developer.map.service

import android.content.Context
import androidx.work.Worker
import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.work.WorkerParameters
import com.developer.map.Cash.MySharedPreferences
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task

@SuppressLint("StaticFieldLeak")
lateinit var fusedLocationProviderClient: FusedLocationProviderClient
private const val TAG = "LocationService"
class LocationService(val context: Context, val workerParameters: WorkerParameters) :
    Worker(context, workerParameters) {

    override fun doWork(): Result {
        loaddate()
        return Result.success()
    }

    @SuppressLint("MissingPermission", "VisibleForTests")
    private fun loaddate() {
        MySharedPreferences.init(context)
        var list = MySharedPreferences.obektString
        fusedLocationProviderClient = FusedLocationProviderClient(context)
        val locationTask: Task<Location> = fusedLocationProviderClient.lastLocation
        locationTask.addOnSuccessListener { it: Location ->
            if (it != null) {
                var currentLatLng = LatLng(it.latitude, it.longitude)
                list.add(LatLng(it.latitude, it.longitude))
                MySharedPreferences.obektString = list
                Log.d(TAG, "loaddate: Qoshildi")
            }
        }
    }


}