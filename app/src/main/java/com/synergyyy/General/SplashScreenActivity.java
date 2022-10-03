package com.synergyyy.General;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.JsonArray;
import com.synergyyy.EquipmentSearch.PmTaskActivity;
import com.synergyyy.PurchaseOder.UploadPurchasePdf;
import com.synergyyy.R;
import com.synergyyy.Search.EditFaultOnSearchActivity;
import com.synergyyy.UploadPdf.UploadPdf;
import com.synergyyy.Workspace.WorkspaceActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class SplashScreenActivity extends AppCompatActivity {
    private FusedLocationProviderClient client;
    private double latitude, longitude;
    String token;
    String remarks;
    String workspace, id;
    Intent intent;
    SharedPreferences sharedPreferences;
    public static Context context;

    //splash works for background notifications
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        SplashScreenActivity.context = getApplicationContext();
        sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        networkCall();

    }

    public static Context getAppContext() {
        return SplashScreenActivity.context;
    }

    private void networkCall() {

        Call<JsonArray> call = APIClient.getUserServices().getWorkspace(token);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {
                    Bundle bundle = new Bundle();
                    bundle = getIntent().getExtras();
                    Log.d(TAG, "onResponse:  bundle" + bundle);
                    if (bundle != null) {
                        if (bundle.get("id") != null) {
                            workspace = bundle.get("workspace").toString();
                            String click_action = bundle.get("click_action").toString();
                            id = bundle.get("id").toString();
                            String equipCode = "";
                            String taskNumber = "";
                            String afterImage = "";
                            String beforeImage = "";
                            String source = "search";
                            intent = null;

                            sharedPreferences.edit().putString("workspace", workspace).apply();
                            startActivity(new Intent(SplashScreenActivity.this, WorkspaceActivity.class));
                            finish();

                            if (click_action.equals(Constants.EDIT_FAULT_REPORT_ACTIVITY_NOTIFICATION)) {
                                //locationMethodPriority();
                                intent = new Intent(getApplicationContext(), EditFaultOnSearchActivity.class);
                                intent.putExtra("frId", id);
                                intent.putExtra("latitude", latitude);
                                intent.putExtra("longitude", longitude);
                                intent.putExtra("workspace", workspace);
                                intent.putExtra("workspace", workspace);
                                startActivity(intent);
                                bundle.clear();
                                finish();

                            }
                            if (click_action.equals(Constants.PM_TASK_ACTIVITY_NOTIFICATION)) {
                                intent = new Intent(getApplicationContext(), PmTaskActivity.class);
                                intent.putExtra("taskId", Integer.parseInt(id));
                                intent.putExtra("taskNumber", taskNumber);
                                intent.putExtra("afterImage", afterImage);
                                intent.putExtra("beforeImage", beforeImage);
                                intent.putExtra("source", source);
                                bundle.clear();
                                intent.putExtra("workspace", workspace);
                                startActivity(intent);
                                finish();
                            }
                            if (click_action.equals(Constants.UPLOAD_QUOTATION_ACTIVITY)) {
                                intent = new Intent(getApplicationContext(), UploadPdf.class);
                                intent.putExtra("frId", id);
                                intent.putExtra("workspace", workspace);
                                if ((bundle.get("remark") != null)) {
                                    intent.putExtra("remarks", bundle.get("remark").toString());
                                }
                                bundle.clear();
                                startActivity(intent);
                                finish();
                            }
                            if (click_action.equals(Constants.PURCHASE_ORDER_ACTIVITY)) {
                                intent = new Intent(getApplicationContext(), UploadPurchasePdf.class);
                                intent.putExtra("frId", id);
                                intent.putExtra("workspace", workspace);
                                bundle.clear();
                                startActivity(intent);
                                finish();
                            }

                        } else {
                            startActivity(new Intent(SplashScreenActivity.this, WorkspaceActivity.class));
                            finish();
                        }
                    } else {
                        startActivity(new Intent(SplashScreenActivity.this, WorkspaceActivity.class));
                        finish();
                    }
                } else if (response.code() == 401) {
                    startActivity(new Intent(SplashScreenActivity.this, MainActivityLogin.class));
                    finish();
                } else {
                    Toast.makeText(SplashScreenActivity.this, "Please Login Again", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(SplashScreenActivity.this, MainActivityLogin.class));
                    finish();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                startActivity(new Intent(SplashScreenActivity.this, MainActivityLogin.class));
                finish();
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void locationMethodPriority() {
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);

        client.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();
                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                    Log.d(TAG, "onComplete: slassh loc" + location.getLongitude());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 108) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationMethodPriority();
                networkCall();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

}
