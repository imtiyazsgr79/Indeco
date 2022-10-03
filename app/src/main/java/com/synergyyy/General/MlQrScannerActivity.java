package com.synergyyy.General;

import static android.content.ContentValues.TAG;
import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.common.InputImage;
import com.synergyyy.R;
import com.synergyyy.Search.GeoLocation;
import com.synergyyy.Search.LocationScanModel;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MlQrScannerActivity extends MyBaseActivity {


    private ListenableFuture cameraProviderFuture;
    private ExecutorService cameraExecutorService;
    private PreviewView previewView;
    private MyImageAnalyzer analyser;
    private Preview preview;
    private String frid;
     private Double latFromSharedRef, longFromSharedRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.ml_qr_scanner, null, false);
        drawer.addView(viewLayout, 0);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");
        username = sharedPreferences.getString("username", "");
        Intent intent = getIntent();
        frid = intent.getStringExtra("frId");


        latFromSharedRef = Double.parseDouble(sharedPreferences.getString("lat", ""));
        longFromSharedRef = Double.parseDouble(sharedPreferences.getString("long", ""));


        workspace = intent.getStringExtra("workspace");
        String value = intent.getStringExtra("value");
        activityNameTv.setText("Scan Location");
        setSupportActionBar(toolbar);


        previewView = findViewById(R.id.preview);
        this.getWindow().setFlags(1024, 1024);
        cameraExecutorService = Executors.newSingleThreadExecutor();
        cameraProviderFuture = ProcessCameraProvider.getInstance(this);
        analyser = new MyImageAnalyzer(getSupportFragmentManager());

        cameraProviderFuture.addListener(new Runnable() {
                                             @Override
                                             public void run() {
                                                 try {
                                                     //permission
                                                     if (!(ContextCompat.checkSelfPermission(MlQrScannerActivity.this,
                                                             Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

                                                     )) {
                                                         new AlertDialog.Builder(MlQrScannerActivity.this)
                                                                 .setTitle("Permission needed for Camera")
                                                                 .setMessage("This permission is needed for the application to run properly")
                                                                 .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                     @RequiresApi(api = Build.VERSION_CODES.M)
                                                                     @Override
                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                         ActivityCompat.requestPermissions(MlQrScannerActivity.this,
                                                                                 new String[]{Manifest.permission.CAMERA}, 101);
                                                                     }
                                                                 })
                                                                 .create().show();

                                                     }

                                                     ProcessCameraProvider processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
                                                     bindpreview(processCameraProvider);


                                                 } catch (ExecutionException e) {
                                                     e.printStackTrace();
                                                 } catch (InterruptedException e) {
                                                     e.printStackTrace();
                                                 }
                                             }
                                         }, ContextCompat.getMainExecutor(this)
        );
    }

    private void bindpreview(ProcessCameraProvider processCameraProvider) {
        preview = new Preview.Builder().build();
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK).build();
        preview.setSurfaceProvider(previewView.getSurfaceProvider());

        ImageCapture imageCapture = new ImageCapture.Builder().build();
        ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                .setTargetResolution(new Size(1200, 720))
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)

                .build();
        imageAnalysis.setAnalyzer(cameraExecutorService, analyser);

        processCameraProvider.unbindAll();
        processCameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture, imageAnalysis);


    }

    public class MyImageAnalyzer implements ImageAnalysis.Analyzer {
        private FragmentManager fragmentManager;

        public MyImageAnalyzer(FragmentManager fragmentManager) {
            this.fragmentManager = fragmentManager;
        }

        @Override
        public void analyze(@NonNull ImageProxy image) {
            scanbarCode(image);

        }
    }

    private void scanbarCode(ImageProxy image) {

        @SuppressLint("UnsafeOptInUsageError") Image image1 = image.getImage();

        assert image1 != null;
        InputImage inputImage = InputImage.fromMediaImage(image1, image.getImageInfo().getRotationDegrees());
        BarcodeScannerOptions options =
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(
                                Barcode.FORMAT_QR_CODE)
                        .build();

        BarcodeScanner scanner = BarcodeScanning.getClient();

        Task<List<Barcode>> result = scanner.process(inputImage)
                .addOnSuccessListener(new OnSuccessListener<List<Barcode>>() {
                    @Override
                    public void onSuccess(List<Barcode> barcodes) {
                        readBarCodeData(barcodes, image);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MlQrScannerActivity.this, "Failed to Read"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }).addOnCompleteListener(new OnCompleteListener<List<Barcode>>() {
                    @Override
                    public void onComplete(@NonNull Task<List<Barcode>> task) {
                        image.close();
                    }
                });

    }

    @SuppressLint("RestrictedApi")
    private void readBarCodeData(List<Barcode> barcodes, ImageProxy image) {

        if (barcodes != null && barcodes.size() > 0) {
            for (Barcode barcode : barcodes) {
                int scannedFormat = barcode.getFormat();
                if (scannedFormat == Barcode.FORMAT_QR_CODE) {
                    Objects.requireNonNull(preview.getCamera()).close();
                    callToChechLocatuion(barcode.getRawValue());
                    Log.d(TAG, "readBarCodeData:wah " + barcode.getRawValue());
                    break;

                } else {
                    Toast.makeText(this, "Unknown Qr Format", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && permissions.length > 0) {
            ProcessCameraProvider processCameraProvider = null;
            try {
                processCameraProvider = (ProcessCameraProvider) cameraProviderFuture.get();
            } catch (ExecutionException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            } catch (InterruptedException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            bindpreview(processCameraProvider);
        }

    }

    private void callToChechLocatuion(String locationCode) {
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
                Toast.makeText(MlQrScannerActivity.this, "Failed to Scan Location : " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

}