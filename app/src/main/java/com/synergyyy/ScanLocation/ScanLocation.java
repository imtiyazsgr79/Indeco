package com.synergyyy.ScanLocation;

import androidx.annotation.NonNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.synergyyy.General.APIClient;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;
import com.synergyyy.Search.GeoLocation;
import com.synergyyy.Search.LocationScanModel;

import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class ScanLocation extends MyBaseActivity {

    private CodeScannerView codeScannerView;
    private TextView scanTextView;
    private ProgressDialog mProgress;
    private String workspace, role, token, username;

    String frid, locationCode;
    double latFromSharedRef,longFromSharedRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_equipment_search, null, false);
        drawer.addView(viewLayout, 0);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");
        username = sharedPreferences.getString("username", "");
        Intent intent = getIntent();
        frid = intent.getStringExtra("frId");

        scanTextView = findViewById(R.id.scan_tv);
        codeScannerView = findViewById(R.id.qr_btn);
        latFromSharedRef= Double.parseDouble(sharedPreferences.getString("lat",""));
        longFromSharedRef= Double.parseDouble(sharedPreferences.getString("long",""));
        Log.d("TAG", "onCreate: "+longFromSharedRef+"      "+latFromSharedRef);
        mProgress = new ProgressDialog(ScanLocation.this);
        mProgress.setTitle("Searching Equipment...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);


        workspace = intent.getStringExtra("workspace");
        String value = intent.getStringExtra("value");
        activityNameTv.setText("Scan Location");
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 1);
            }

            codeScannerView.setVisibility(View.VISIBLE);

            CodeScanner codeScanner = new CodeScanner(ScanLocation.this, codeScannerView);
            codeScanner.startPreview();
            codeScanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            codeScannerView.setVisibility(View.GONE);
                            scanTextView.setVisibility(View.VISIBLE);
                            scanTextView.setText(result.getText() + "Latitude :" + latFromSharedRef + "longitude :" + longFromSharedRef);
                            locationCode = result.getText();
                            callToChechLocatuion();
                        }
                    });
                }
            });
        }
    }

    private void callToChechLocatuion() {
        GeoLocation geoLocation = new GeoLocation(latFromSharedRef, longFromSharedRef);
        LocationScanModel locationScanModel = new LocationScanModel(frid, locationCode, geoLocation);

        Call<Void> call = APIClient.getUserServices().checkLocation(token, workspace, role, locationScanModel);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                //let it edit
                Intent intent11 = new Intent();
                intent11.putExtra("response_code", response.code());
                setResult(Activity.RESULT_OK, intent11);
                finish();
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ScanLocation.this, "Failed to Scan Location : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    @Override
    protected void onResume()  {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}