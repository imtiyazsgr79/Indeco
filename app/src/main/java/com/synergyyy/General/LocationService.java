package com.synergyyy.General;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {

    public static final String SHARED_PREFS = "sharedPrefs";
    public SharedPreferences.Editor editor;

    public LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);
            Log.d("TAG", "onLocationResult: method called");
            if (locationResult != null && locationResult.getLocations() != null) {
                SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                editor = sharedPreferences.edit();

                Log.d("TAG", "onLocationResult: "+(locationResult.getLocations().get(0).getLongitude()));
                editor.putString("long", String.valueOf(locationResult.getLocations().get(0).getLongitude()));
                editor.putString("lat", String.valueOf(locationResult.getLocations().get(0).getLatitude()));
                editor.apply();
            }

        }
    };


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not Yet Implemented");

    }

    private void startLocationService() {

        LocationRequest locationRequest =  LocationRequest.create();
        locationRequest.setInterval(100)
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Please grant location access to this app.", Toast.LENGTH_LONG).show();
        } else
            LocationServices.getFusedLocationProviderClient(this).
                    requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("TAG", "onStartCommand: started in location service");
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                startLocationService();
            }
        }
        return super.onStartCommand(intent, flags, startId);

    }
}
