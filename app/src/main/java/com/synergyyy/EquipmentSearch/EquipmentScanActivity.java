package com.synergyyy.EquipmentSearch;

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
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScannerView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;
import com.synergyyy.General.LogoutClass;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;
import com.synergyyy.Search.EditFaultReportActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static android.content.ContentValues.TAG;
import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class EquipmentScanActivity extends MyBaseActivity {

    private CodeScannerView codeScannerView;
    private TextView scanTextView;
    private ProgressDialog mProgress;
    private String role, token, frId;

    private ListenableFuture cameraProviderFuture;
    private ExecutorService cameraExecutorService;
    private PreviewView previewView;
    private MyImageAnalyzer analyser;
    private Preview preview;
    private String frid, value;
    private Double latFromSharedRef, longFromSharedRef;
    private boolean alertShowed = false;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

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

        scanTextView = findViewById(R.id.scan_tv);
        codeScannerView = findViewById(R.id.qr_btn);

        mProgress = new ProgressDialog(EquipmentScanActivity.this);
        mProgress.setTitle("Searching Equipment...");
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        Intent intent = getIntent();
        workspace = intent.getStringExtra("workspace");
        value = intent.getStringExtra("value");
        frId = intent.getStringExtra("frId");

        if (value.equals("Fault")) {
            activityNameTv.setText("Scan Fault Report");
        } else activityNameTv.setText("Scan Tasks");
        setSupportActionBar(toolbar);

        newScanner();

    }

    private void newScanner() {
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
                                                     if (!(ContextCompat.checkSelfPermission(EquipmentScanActivity.this,
                                                             Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

                                                     )) {
                                                         new AlertDialog.Builder(EquipmentScanActivity.this)
                                                                 .setTitle("Permission needed for Camera")
                                                                 .setMessage("This permission is needed for the application to run properly")
                                                                 .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                                                     @RequiresApi(api = Build.VERSION_CODES.M)
                                                                     @Override
                                                                     public void onClick(DialogInterface dialog, int which) {
                                                                         ActivityCompat.requestPermissions(EquipmentScanActivity.this,
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

    private void alertMethodForDueOrOverdue(String result, String open) {
        alertShowed = true;
        final AlertDialog.Builder builder = new AlertDialog.Builder(EquipmentScanActivity.this);
        final View dialogView = getLayoutInflater().inflate(R.layout.due_overdue_dialog, null);
        final RadioGroup group = dialogView.findViewById(R.id.radioPersonGroup);
        builder.setView(dialogView)
                .setCancelable(false)
                .setPositiveButton("ok", null)

                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        startActivity(getIntent());
                        dialogInterface.cancel();
                    }
                });

        AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                Button b = alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        if (group.getCheckedRadioButtonId() == -1) {
                            Toast.makeText(EquipmentScanActivity.this, "Please Select Type", Toast.LENGTH_LONG).show();
                        } else {
                            int selectedId = group.getCheckedRadioButtonId();
                            RadioButton radioSelectedButton = (RadioButton) dialogView.findViewById(selectedId);

                            if (radioSelectedButton.getText().toString().equals("Due Tasks")) {
                                callQrCodeSearch(result, "Open", "Due");
                            } else if (radioSelectedButton.getText().toString().equals("Overdue Tasks")) {
                                callQrCodeSearch(result, "Open", "Overdue");
                            }
                            alert.dismiss();

                        }
                    }
                });
            }
        });
        alert.show();
    }


    private void callQrCodeSearch(String result, String status, String dueOrOverdue) {
        mProgress.show();

        Call<List<GetPmTaskItemsResponse>> callEquipment = APIClient.getUserServices().getEquipmentTask(result, dueOrOverdue, role, token, workspace);
        callEquipment.enqueue(new Callback<List<GetPmTaskItemsResponse>>() {
            @Override
            public void onResponse(Call<List<GetPmTaskItemsResponse>> call, Response<List<GetPmTaskItemsResponse>> response) {
                if (response.code() == 200) {

                    scanTextView.setVisibility(View.GONE);
                    List<GetPmTaskItemsResponse> equipmentSearchResponse = response.body();
                    if (equipmentSearchResponse.isEmpty()) {
                        Toast.makeText(EquipmentScanActivity.this, "No tasks available!", Toast.LENGTH_LONG).show();
                        finish();
                        startActivity(getIntent());
                    }
                    RecyclerView recyclerView = findViewById(R.id.recycler_view_equip);

                    ArrayList<EquipmentSearchCard> equipmentSearchCardArrayList = new ArrayList<>();
                    String source = "scan";
                    for (int i = 0; i < equipmentSearchResponse.size(); i++) {
                        String taskNumber = equipmentSearchResponse.get(i).getTaskNumber();
                        Long taskId = equipmentSearchResponse.get(i).getId();
                        String buildingName = null, locationName = null;
                        if (equipmentSearchResponse.get(i).getEquipment() != null) {
                            buildingName = equipmentSearchResponse.get(i).getEquipment().getBuilding().getName();
                            locationName = equipmentSearchResponse.get(i).getEquipment().getLocation().getName();
                        }

                        long scheduleDate = equipmentSearchResponse.get(i).getScheduleDate();
                        String status = equipmentSearchResponse.get(i).getStatus();
                        String beforeImage = null, afterImage = null, equipName = null;
                        if (equipmentSearchResponse.get(i).getAfterImage() != null) {
                            afterImage = equipmentSearchResponse.get(i).getAfterImage().getImage();
                        }
                        if (equipmentSearchResponse.get(i).getBeforeImage() != null) {
                            beforeImage = equipmentSearchResponse.get(i).getBeforeImage().getImage();
                        }
                        if (equipmentSearchResponse.get(i).getEquipment() != null) {
                            equipName = equipmentSearchResponse.get(i).getEquipment().getName();
                        }
                        equipmentSearchCardArrayList.add(new EquipmentSearchCard(taskId, taskNumber, workspace, status, buildingName,
                                locationName, scheduleDate, afterImage, beforeImage, source, equipName));
                    }
                    recyclerView.setHasFixedSize(true);
                    EquipmentSearchAdapter mAdapter = new EquipmentSearchAdapter(equipmentSearchCardArrayList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(EquipmentScanActivity.this));
                    recyclerView.setAdapter(mAdapter);
                } else if (response.code() == 401) {
                    Toast.makeText(EquipmentScanActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(EquipmentScanActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(EquipmentScanActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                } else if (response.code() == 404) {
                    Toast.makeText(EquipmentScanActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());
                } else {
                    finish();
                    startActivity(getIntent());
                    Toast.makeText(EquipmentScanActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();
                }
                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<List<GetPmTaskItemsResponse>> call, Throwable t) {
                Toast.makeText(EquipmentScanActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                mProgress.dismiss();
                finish();
                startActivity(getIntent());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void dialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(EquipmentScanActivity.this).create(); //Read Update
        LayoutInflater layoutInflater = this.getLayoutInflater();
        View radioLayoutView = layoutInflater.inflate(R.layout.custom_dialog_radio_layout_qr, null);
        RadioGroup radioGroup = radioLayoutView.findViewById(R.id.radio_grp_id);

        alertDialog.setView(radioLayoutView);
        alertDialog.setTitle("Select Type of Tasks");

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioSelectedButton = radioGroup.findViewById(selectedId);

                String status = radioSelectedButton.getText().toString();
                status = status.substring(0, status.length() - 6);
                //callQrCodeSearch(String.valueOf(scanTextView.getText()), status);
            }
        });
        alertDialog.setButton(Dialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
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
                        Toast.makeText(EquipmentScanActivity.this, "Failed to Read ;" + e.getMessage(), Toast.LENGTH_SHORT).show();
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

                    CoordinatorLayout coordinatorLayout = findViewById(R.id.codinatorlayout);
                    coordinatorLayout.setVisibility(View.GONE);

                    if (value.equals("Fault")) {
                        Intent intent1 = new Intent(EquipmentScanActivity.this,
                                EditFaultReportActivity.class);
                        intent1.putExtra("equipcode", barcode.getRawValue());
                        intent1.putExtra("workspace", workspace);
                        intent1.putExtra("frIdOnScan", frId);
                        startActivity(intent1);
                        finish();
                    } else {

                        if (!alertShowed) {
                            alertMethodForDueOrOverdue(barcode.getRawValue(), "Open");
                        }
                    }
                    //make change
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
}