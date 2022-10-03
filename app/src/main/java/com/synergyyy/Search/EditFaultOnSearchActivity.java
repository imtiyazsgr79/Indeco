package com.synergyyy.Search;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.synergyyy.General.APIClient;
import com.synergyyy.General.CheckInternet;
import com.synergyyy.General.Constants;
import com.synergyyy.EquipmentSearch.EquipmentScanActivity;
import com.synergyyy.FaultReport.list;
import com.synergyyy.General.LogoutClass;
import com.synergyyy.General.MainActivityLogin;
import com.synergyyy.General.MlQrScannerActivity;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.Messages.SingleMessageResponse;
import com.synergyyy.PurchaseOder.UploadPurchasePdf;
import com.synergyyy.PurchaseOder.ViewPurchaseOnly;
import com.synergyyy.R;
import com.synergyyy.SearchTasks.ImagesActivity;
import com.synergyyy.UploadPdf.UploadPdf;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.ACTION_GET_CONTENT;
import static com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY;
import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class EditFaultOnSearchActivity extends MyBaseActivity {


    private FloatingActionButton fab_main, fab_before, fab_after, fabQuoteMain, fabQuoteUpload, fabPurchaseOrder;
    private Boolean isMenuOpen = false;
    private Boolean isMenuOpenQuote = false;
    private final OvershootInterpolator interpolator = new OvershootInterpolator();
    private Float translationY = 600f;

    private static final String TAG = "EditFault";
    private String timeS, remarksString, techName;
    private String frId;
    private TextView uploadFileTv;
    private int idStatus;
    private String statusNameCurrent;
    private Button uploadFileBtn;
    private Intent uploadFileIntent;
    private String token, role;
    private String eotTypee;
    private TextView equipmentIdTv;
    private LinearLayout fabLayoutQuotation;
    private Button plusbtn, deletebtn;
    private MaterialButton scanLocationBtn, acceptButton, rejectButton, scanEquipmentBtn, closeFaultReportBtn;
    private LinearLayout mlayout;
    private String frid, workSpaceid, equipCode;
    private int remarksId;
    //  private EditText editText;
    private TextWatcher textWatcher;
    private List<String> remarksList = new ArrayList<String>();

    private List<TextInputEditText> editTextList = new ArrayList<TextInputEditText>();

    private List<String> genralStatusList = new ArrayList<String>();
    private List<list> genralTechnicalList = new ArrayList<list>();

    private JsonObject jsonObject;
    int depId, divId, priId, buildId, locId, faultId, maintId;
    private Integer costId = null;
    private Integer techId = null;
    private ArrayAdapter<String> statusListAdapter;
    private ProgressDialog progressDialog;
    private LinearLayout linearLayoutdisable;
    boolean[] checkedItems;
    private List<String> stockList = new ArrayList<>();
    private String[] listItems;
    private int listRemarksNo;

    private double latitude, longitude;
    private AutoCompleteTextView autoCompleteSpinner;
    private TextInputEditText frIdEditText, deptSpinner, eotTypeTextView, reqNameEditText, activationDate,
            activationTime, faultDetailsEditText, locDescEditText, faultCategorySpinner,
            divisionSpinner, locationSpinner, buildingSpinner, prioritySpinner,
            mainGrpSpinner, observationEditText,
            requestorNumberEditText, actionTakenEditText, equipmentTextView, editText, techTv,
            ackEditText, responseTime, acknowledgeTime, downTime, eotTime, fmmCaseId;
    private TextInputLayout textInputLayoutAck, fmmcaseIdLayout;

    private RadioButton radioSelectedButton;
    private List<StatusItem> items = new ArrayList<StatusItem>();
    private String tech = "Technician";
    private String managingAgent = "ManagingAgent";
    private final CheckInternet checkInternet = new CheckInternet();

    private PDFView pdfView;
    private FloatingActionButton getPdfAuth, uploadAuth;
    private boolean uploadedFile = false;
    private androidx.appcompat.app.AlertDialog alertAuthPdf;
    private ImageView ackImageViewSign, techSignImgView;
    private TextInputEditText ackNameEt, ackRankEt, fmmTextView;
    private LinearLayout ackMainLayout;

    List attendedByIdsList;
    private String fmmUsername;
    private int fmmId;
    private FusedLocationProviderClient fusedLocationProviderClient;
    public static final String SHARED_PREFS = "sharedPrefs";
    public SharedPreferences.Editor editor;
 /*   public void checkForGps() {
        final LocationManager manager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);



        if (!(manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) || !(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED)) {
            showSettingsAlert();
        } else {
            Toast.makeText(EditFaultOnSearchActivity.this, "GPS is enabled!", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 20 && grantResults.length > 0) {
            startLocationService();

        }

    }
    public void startLocationService() {
        Log.d("TAG", "startLocationService: location started in MybaseActivity");
        Intent i = new Intent(getApplicationContext(), LocationService.class);
        i.setAction("startLocationService");
        startService(i);
    }
    public void showSettingsAlert() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(EditFaultOnSearchActivity.this);
        alertDialog.setTitle("Background Location and GPS permission");
        alertDialog.setCancelable(false);
        alertDialog.setMessage("Background location is required for further process. Do you want to give access ?");
        alertDialog.setPositiveButton("yes",
                (dialog, which) -> {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            20);

                    Intent intent = new Intent(
                            Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivityForResult(intent, 9);

                });

        alertDialog.show();
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_edit_fault_on_search, null, false);
        drawer.addView(viewLayout, 0);
        activityNameTv.setText("Fr Detail");

//        checkForGps();
        progressDialog = new ProgressDialog(com.synergyyy.Search.EditFaultOnSearchActivity.this);
        progressDialog.setTitle("Loading...");
        linearLayoutdisable = findViewById(R.id.layout_disable);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");
        Intent i = getIntent();
        frid = i.getStringExtra("frId");
        longitude = i.getDoubleExtra("longitude", 0);
        latitude = i.getDoubleExtra("latitude", 0);
        workSpaceid = i.getStringExtra("workspace");
        String msgId = i.getStringExtra("msgId");
        workSpaceid = workspace;
        if (msgId != null) {
            callReadMessage(msgId);
        }

        initViews();
        callDisable();
        initializeFab();
        calltech();
        items.add(new StatusItem("Select status"));
        items.add(new StatusItem("Open"));
        items.add(new StatusItem("Pause"));
        items.add(new StatusItem("Closed"));
        items.add(new StatusItem("Completed"));
        items.add(new StatusItem("Pause Requested"));

        initviewsAndGetInitialData(frid);

        AutoCompleteTextAdaptar adapter = new AutoCompleteTextAdaptar(
                EditFaultOnSearchActivity.this, items, role);
        autoCompleteSpinner.setAdapter(adapter);
        if (role.equals(Constants.Coordinator)) {
            acceptButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
            scanLocationBtn.setVisibility(View.GONE);
            scanEquipmentBtn.setVisibility(View.GONE);

            //uploadFileBtn.setVisibility(View.GONE);
            plusbtn.setEnabled(false);
            deletebtn.setEnabled(false);
        }

    }


    private void callDisable() {
        techTv.setEnabled(false);
        frIdEditText.setEnabled(false);
        prioritySpinner.setEnabled(false);
        requestorNumberEditText.setEnabled(false);
        buildingSpinner.setEnabled(false);
        locationSpinner.setEnabled(false);
        divisionSpinner.setEnabled(false);
        locDescEditText.setEnabled(false);
        faultCategorySpinner.setEnabled(false);
        faultDetailsEditText.setEnabled(false);
        mainGrpSpinner.setEnabled(false);
        observationEditText.setEnabled(false);
        actionTakenEditText.setEnabled(false);
        plusbtn.setEnabled(false);
        deletebtn.setEnabled(false);
        if (role.equals(Constants.ROLE_MANAGING_AGENT)) {
            plusbtn.setEnabled(true);
            deletebtn.setEnabled(true);
        }
    }


    private void calltech() {

        Call<JsonArray> callTechAllGet = APIClient.getUserServices().getTechnicianCall(token, workSpaceid);
        callTechAllGet.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                if (response.code() == 200) {

                    JsonArray jsonarraytech = response.body();
                    for (int i = 0; i < jsonarraytech.size(); i++) {
                        String technicianName = jsonarraytech.get(i).getAsJsonObject().get("name").getAsString();
                        int id = jsonarraytech.get(i).getAsJsonObject().get("id").getAsInt();
                        genralTechnicalList.add(new list(technicianName, id));
                        stockList.add(technicianName);

                    }
                    listItems = new String[stockList.size()];
                    listItems = stockList.toArray(listItems);
                    checkedItems = new boolean[listItems.length];
                } else if (response.code() == 401) {
                    Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(EditFaultOnSearchActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: tech" + t.getCause());
                Log.d(TAG, "onFailure: tech" + t.getMessage());
                Toast.makeText(EditFaultOnSearchActivity.this, "Error while loading  Technician " +
                        ": " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    private void initViews() {
        fmmTextView = findViewById(R.id.fmmTextView);
        fmmcaseIdLayout = findViewById(R.id.fmmcaseIdlayout);
        fmmCaseId = findViewById(R.id.fmmcaseid);

        ackNameEt = findViewById(R.id.AckNameEt);
        ackRankEt = findViewById(R.id.AckRankEt);
        ackImageViewSign = findViewById(R.id.AckImageView);
        techSignImgView = findViewById(R.id.techSignImageView);
        ackMainLayout = findViewById(R.id.AckMainLayout);

        eotTime = findViewById(R.id.eotimetextView);
        responseTime = findViewById(R.id.response_time);
        downTime = findViewById(R.id.downtime_time);
        acknowledgeTime = findViewById(R.id.acknowlegde_time);

        fabLayoutQuotation = findViewById(R.id.lay_fab_quote);
        fabQuoteMain = findViewById(R.id.quotation_fab_main);
        fabQuoteUpload = findViewById(R.id.pdf_fab_quotation_fab);
        fabPurchaseOrder = findViewById(R.id.pdf_fab_purchase_orde_fab);

        textInputLayoutAck = findViewById(R.id.acknowledgelayout);
        ackEditText = findViewById(R.id.acknowledgeedittext);
        eotTypeTextView = findViewById(R.id.eottypetextView);

        scanLocationBtn = findViewById(R.id.scanlocationbttn);
        acceptButton = findViewById(R.id.acceptbttn);
        closeFaultReportBtn = findViewById(R.id.closeFaultreport);
        rejectButton = findViewById(R.id.rejectbtn);
        autoCompleteSpinner = findViewById(R.id.statusSpinner);
        scanEquipmentBtn = findViewById(R.id.equipmentScanButton);
        techTv = findViewById(R.id.techtv);
        equipmentIdTv = findViewById(R.id.eq_id_send);
        actionTakenEditText = findViewById(R.id.actionTaken);
        mainGrpSpinner = findViewById(R.id.mainGrp);
        faultCategorySpinner = findViewById(R.id.faultCategory);
        divisionSpinner = findViewById(R.id.divisionNumberSpinner);
        locationSpinner = findViewById(R.id.unitNumber);
        buildingSpinner = findViewById(R.id.buildingNumber);
        prioritySpinner = findViewById(R.id.priorityNumber);
        deptSpinner = findViewById(R.id.dept_number);
        requestorNumberEditText = findViewById(R.id.contactNumber);
        frIdEditText = findViewById(R.id.frIdEditText);
        locDescEditText = findViewById(R.id.locationDescrip);
        faultDetailsEditText = findViewById(R.id.faultDetails);
        reqNameEditText = findViewById(R.id.reqNameEditTextEditFault);
        equipmentTextView = findViewById(R.id.equipmentTextViewEditFault);
        activationDate = findViewById(R.id.activation_date);
        activationTime = findViewById(R.id.activation_time);
        plusbtn = findViewById(R.id.plusButton);
        deletebtn = findViewById(R.id.deleteButton);
        mlayout = findViewById(R.id.layout_remarks);
        TextView textView = new TextView(this);
        observationEditText = findViewById(R.id.observation);
        fab_main = findViewById(R.id.images_id);
        fab_before = findViewById(R.id.before_id);
        fab_after = findViewById(R.id.after_id);

        locationSpinner.setEnabled(false);
        buildingSpinner.setEnabled(false);
        requestorNumberEditText.setEnabled(false);
        prioritySpinner.setEnabled(false);
        divisionSpinner.setEnabled(false);
        locDescEditText.setEnabled(false);
        faultCategorySpinner.setEnabled(false);
        faultDetailsEditText.setEnabled(false);
        mainGrpSpinner.setEnabled(false);

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                //  checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                //    checkFieldsForEmptyValues();
            }
        };


        if (role.equals(tech)) {
            genralStatusList.add("Select status");
            genralStatusList.add("Open");
            genralStatusList.add("Pause");
            genralStatusList.add("Completed");

        } else if (role.equals(managingAgent)) {
            genralStatusList.add("Select status");
            genralStatusList.add("Open");
            genralStatusList.add("Pause");
            genralStatusList.add("Closed");
            genralStatusList.add("Completed");
            genralStatusList.add("Pause Requested");

        } else {
            genralStatusList.add("Select status");
            genralStatusList.add("Open");
            genralStatusList.add("Pause");
            genralStatusList.add("Closed");
            genralStatusList.add("Completed");
        }

        scanEquipmentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditFaultOnSearchActivity.this, EquipmentScanActivity.class);
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("value", "Fault");
                intent.putExtra("frId", frid);
                startActivity(intent);
                finish();
            }
        });

        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mlayout.getChildCount() < 9) {
                    remarksId++;
                    mlayout.addView(createNewEditText(remarksString, remarksId));
                }
            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadpdfAuthorizationDialog();

                //  acceptMethod();
            }
        });
        rejectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectMethod();
            }
        });

        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRemarks(view);
            }
        });
        closeFaultReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                closeFaultReportMethod();
            }
        });
    }

    private void closeFaultReportMethod() {
        Building building = new Building();
        building.setId(buildId);
        Location location = new Location();
        location.setId(locId);

        attendedByIdsList = new ArrayList();
        String attendedbyString = techTv.getText().toString();
        List<String> attendedbylist = Arrays.asList(attendedbyString.split(", "));
        for (int j = 0; j < attendedbylist.size(); j++) {
            String techincian = attendedbylist.get(j);
            for (list list : genralTechnicalList) {
                if (list.getName().equals(techincian)) {
                    Integer idd = list.getId();
                    attendedByIdsList.add(idd);
                }
            }
        }
        ArrayList<AttendedBy> attendedBy = new ArrayList<>();
        for (int j = 0; j < attendedByIdsList.size(); j++) {
            AttendedBy attendedbyObject = new AttendedBy();
            attendedbyObject.setId((Integer) attendedByIdsList.get(j));
            attendedBy.add(attendedbyObject);
        }
        if (!editTextList.isEmpty() && editTextList.size() > listRemarksNo) {
            for (int iRem = 0; iRem < editTextList.size(); iRem++) {
                String remarks1String = editTextList.get(iRem).getText().toString().trim();
                if (!TextUtils.isEmpty(editTextList.get(iRem).getText().toString().trim()) && !remarks1String.trim().isEmpty()) {
                    remarksList.add(remarks1String);
                } else {
                    remarksList.clear();
                    Toast.makeText(this, "Remarks field cannot be empty", Toast.LENGTH_SHORT).show();
                }
            }
            if (remarksList.size() > listRemarksNo) {
                CloseFaultReportBody closeFaultReportBody = new CloseFaultReportBody(remarksList, frid, "Closed", building, location, attendedBy, username);
                progressDialog.setTitle("Closing Fault Report");
                progressDialog.setMessage("Please Wait...");
                progressDialog.show();

                Call<Void> call = APIClient.getUserServices().closeFaultFReport(closeFaultReportBody, workSpaceid, token, role);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        progressDialog.dismiss();
                        if (response.code() == 200) {
                            Toast.makeText(EditFaultOnSearchActivity.this, "Fault Report Closed", Toast.LENGTH_SHORT).show();
                            goToSeach();
                        } else if (response.code() == 401) {
                            Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                            LogoutClass logoutClass = new LogoutClass();
                            logoutClass.logout(EditFaultOnSearchActivity.this);
                        } else if (response.code() == 500) {
                            Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                        } else if (response.code() == 404) {
                            Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(EditFaultOnSearchActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        progressDialog.dismiss();
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        Log.d(TAG, "onFailure close: " + t.getCause());
                        Toast.makeText(EditFaultOnSearchActivity.this, "Failed to Close due to :" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        } else {
            Toast.makeText(this, "Please add remarks", Toast.LENGTH_SHORT).show();
        }
    }

    private void rejectMethod() {
        String pdfFile = null;
        progressDialog.show();
        String remarks1String;
        if (!editTextList.isEmpty()) {
            for (int iRem = listRemarksNo; iRem < editTextList.size(); iRem++) {
                remarks1String = editTextList.get(iRem).getText().toString();
                remarksList.add(remarks1String);
            }
        } else remarksList = null;

        String observation = observationEditText.getText().toString();
        String actionTaken = actionTakenEditText.getText().toString();
        AcceptRejectBody acceptRejectBody = new AcceptRejectBody(remarksList, frIdEditText.getText().toString(), observation, actionTaken, pdfFile);
        Call<Void> call = APIClient.getUserServices().getReject(token, workSpaceid, acceptRejectBody);
        call.enqueue(new Callback<Void>() {

            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultOnSearchActivity.this, "Rejected ", Toast.LENGTH_SHORT).show();
                    goToSearch();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditFaultOnSearchActivity.this, "Failed to Reject", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage() + t.getCause());
                progressDialog.dismiss();
            }
        });
    }

    private void acceptMethod(String pdfStringFile) {
        progressDialog.show();
        String remarks1String;
        if (!editTextList.isEmpty()) {
            for (int iRem = listRemarksNo; iRem < editTextList.size(); iRem++) {
                remarks1String = editTextList.get(iRem).getText().toString();
                remarksList.add(remarks1String);
            }
        } else remarksList = null;
        String observation = observationEditText.getText().toString();
        String actionTaken = actionTakenEditText.getText().toString();

        AcceptRejectBody acceptRejectBody = new AcceptRejectBody(remarksList, frIdEditText.getText().toString(),
                observation, actionTaken, pdfStringFile);
        Call<Void> call = APIClient.getUserServices().getAccept(token, workSpaceid, acceptRejectBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    uploadAuth.setEnabled(false);
                    uploadedFile = true;
                    Toast.makeText(EditFaultOnSearchActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
                    alertAuthPdf.dismiss();

                    goToSearch();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditFaultOnSearchActivity.this, "Failed to Accepted", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage() + t.getCause());
                progressDialog.dismiss();
            }
        });


    }


    private void initviewsAndGetInitialData(String data) {
        progressDialog.show();
        GeoLocation geolocation = new GeoLocation(latitude, longitude);
        SearchResposeWithLatLon respose = new SearchResposeWithLatLon(geolocation, data);
        Call<JsonObject> call = APIClient.getUserServices().getFindOne(workSpaceid, token, role, respose);
        call.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    jsonObject = response.body();

                    Log.d(TAG, "onResponse: Json Obect on frid search : " + jsonObject);
                    assert jsonObject != null;
                    if (!jsonObject.get("technicianSignature").isJsonNull()) {
                        getTechSign(jsonObject.get("technicianSignature").getAsString());
                    }
                    if (!jsonObject.get("fmmCaseId").isJsonNull()) {
                        fmmCaseId.setText(jsonObject.get("fmmCaseId").getAsString());
                    }

                    if (!(jsonObject.get("acknowledgedBy").isJsonNull())) {
                        ackMainLayout.setVisibility(View.VISIBLE);
                        ackNameEt.setText(jsonObject.get("acknowledgedBy").getAsJsonObject().get("name").getAsString());
                        ackRankEt.setText(jsonObject.get("acknowledgedBy").getAsJsonObject().get("rank").getAsString());
                        getSignatureMethod(jsonObject.get("frId").getAsString());
                    }

                    if (!jsonObject.get("eotType").isJsonNull()) {
                        eotTypee = jsonObject.get("eotType").getAsString();
                        eotTypeTextView.setText(eotTypee);
                    }
                    if (!(jsonObject.get("fmm").isJsonNull())) {
                        fmmUsername = jsonObject.get("fmm").getAsJsonObject().get("username").getAsString().toString();
                        fmmId = jsonObject.get("fmm").getAsJsonObject().get("id").getAsInt();
                        fmmTextView.setText(jsonObject.get("fmm").getAsJsonObject().get("username").getAsString().toString());
                    }
                    if (!(jsonObject.get("acknowledgerCode").isJsonNull())) {
                        textInputLayoutAck.setVisibility(View.VISIBLE);
                        ackEditText.setVisibility(View.VISIBLE);
                        techSignImgView.setVisibility(View.VISIBLE);
                        ackEditText.setText(jsonObject.get("acknowledgerCode").getAsString());

                    }
                    String statusCommingFromCall = null;
                    String editableVariable = jsonObject.get("editable").getAsString();
                    if (!(jsonObject.get("status").isJsonNull())) {
                        statusCommingFromCall = jsonObject.get("status").getAsString();
                        autoCompleteSpinner.setText(statusCommingFromCall, false);
                    }
                    if (role.equals(Constants.ROLE_MANAGING_AGENT) && statusCommingFromCall.equals("Pause Requested")
                            && eotTypee.equals("Greater Than $1000")) {
                        acceptButton.setVisibility(View.VISIBLE);
                        rejectButton.setVisibility(View.VISIBLE);
                        closeFaultReportBtn.setVisibility(View.VISIBLE);
                        actionTakenEditText.setEnabled(true);
                        observationEditText.setEnabled(true);
                        autoCompleteSpinner.setDropDownHeight(0);
                    } else if (role.equals(Constants.ROLE_MANAGING_AGENT) && statusCommingFromCall.equals("Pause Requested")) {
                        acceptButton.setVisibility(View.VISIBLE);
                        rejectButton.setVisibility(View.VISIBLE);
                        closeFaultReportBtn.setVisibility(View.GONE);
                        actionTakenEditText.setEnabled(true);
                        observationEditText.setEnabled(true);
                        autoCompleteSpinner.setDropDownHeight(0);

                    } else if (role.equals(Constants.ROLE_MANAGING_AGENT) && statusCommingFromCall.equals("Pause")
                            || statusCommingFromCall.equals("Closed")) {
                        acceptButton.setVisibility(View.GONE);
                        autoCompleteSpinner.setDropDownHeight(0);
                        plusbtn.setEnabled(false);
                        deletebtn.setEnabled(false);
                        rejectButton.setVisibility(View.GONE);
                    } else if (role.equals(Constants.ROLE_MANAGING_AGENT) && statusCommingFromCall.equals("Completed")) {
                        autoCompleteSpinner.setDropDownHeight(0);
                        if (editableVariable.equals("true")) {
                            linearLayoutdisable.setVisibility(View.GONE);
                            Intent intent = new Intent(EditFaultOnSearchActivity.this, EditFaultReportActivity.class);
                            intent.putExtra("workspace", workSpaceid);
                            intent.putExtra("value", "Fault");
                            intent.putExtra("frId", jsonObject.get("frId").getAsString());
                            intent.putExtra("latOfSearch", latitude);
                            intent.putExtra("longOfSearch", longitude);
                            startActivity(intent);
                            finish();
                            //}

                        } else if (editableVariable.equals("false")) {
                            scanEquipmentBtn.setVisibility(View.GONE);
                            acceptButton.setVisibility(View.GONE);
                            rejectButton.setVisibility(View.GONE);
                            autoCompleteSpinner.setDropDownHeight(0);
                            scanLocationBtn.setVisibility(View.VISIBLE);
                            scanLocationBtn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    scanLocationMethod();
                                }
                            });

                        }
                    }//18jan changed
                    if (role.equals(Constants.ROLE_TECHNICIAN)) {
//2june 2021
                        if (statusCommingFromCall.equals("Completed")) {

                            callToCheckCompletedFridIsEditable(jsonObject.get("frId").getAsString());
                        } else {
                            if ((statusCommingFromCall.equals("Pause")) && eotTypee.equals("Greater Than $1000")
                                    && jsonObject.get("quotationStatus").isJsonNull()) {
                                scanLocationBtn.setVisibility(View.GONE);
                                scanEquipmentBtn.setVisibility(View.GONE);
                                alertDialog("Quotation");
                                Toast.makeText(EditFaultOnSearchActivity.this, "Please Upload Quotation For Further Action ", Toast.LENGTH_LONG).show();
                            } else if ((statusCommingFromCall.equals("Pause")) && eotTypee.equals("Greater Than $1000")
                                    && jsonObject.get("quotationStatus").getAsString().equals("Rejected")) {
                                scanLocationBtn.setVisibility(View.GONE);
                                scanEquipmentBtn.setVisibility(View.GONE);
                                alertDialog("Quotation");
                                Toast.makeText(EditFaultOnSearchActivity.this, "Please Upload Quotation For Further Action ", Toast.LENGTH_LONG).show();
                            } else if (statusCommingFromCall.equals("Pause") && eotTypee.equals("Greater Than $1000") &&
                                    jsonObject.get("quotationStatus").getAsString().equals("Accepted") &&
                                    jsonObject.get("purchaseOrder").isJsonNull()) {
                                scanLocationBtn.setVisibility(View.GONE);
                                scanEquipmentBtn.setVisibility(View.GONE);
                                alertDialog("Purchase");
                                Toast.makeText(EditFaultOnSearchActivity.this, "Please Upload Purchase Order For Further Action ", Toast.LENGTH_LONG).show();
                            } else if ((statusCommingFromCall.equals("Pause")) && eotTypee.equals("Greater Than $1000")
                                    && jsonObject.get("quotationStatus").getAsString().equals("Uploaded")) {
                                scanLocationBtn.setVisibility(View.GONE);
                                scanEquipmentBtn.setVisibility(View.GONE);
                                Toast.makeText(EditFaultOnSearchActivity.this, "You cannot take action until the Quotation has been Accepted/Rejected ", Toast.LENGTH_LONG).show();
                            } else if ((role.equals(Constants.ROLE_TECHNICIAN)) && statusCommingFromCall.equals("Closed")
                                    || statusCommingFromCall.equals("Pause Requested")) {
                                scanEquipmentBtn.setVisibility(View.GONE);
                                scanLocationBtn.setVisibility(View.GONE);
                            } else if (role.equals(Constants.ROLE_TECHNICIAN) && editableVariable.equals("true")) {
                                if (!(jsonObject.get("equipment").isJsonNull())) {
                                    scanEquipmentBtn.setVisibility(View.VISIBLE);
                                    acceptButton.setVisibility(View.GONE);
                                    rejectButton.setVisibility(View.GONE);
                                    autoCompleteSpinner.setDropDownHeight(0);
                                } else if (jsonObject.get("equipment").isJsonNull()) {
                                    linearLayoutdisable.setVisibility(View.GONE);
                                    Intent intent = new Intent(EditFaultOnSearchActivity.this, EditFaultReportActivity.class);
                                    intent.putExtra("workspace", workSpaceid);
                                    intent.putExtra("value", "Fault");
                                    intent.putExtra("frId", jsonObject.get("frId").getAsString());
                                    intent.putExtra("latOfSearch", latitude);
                                    intent.putExtra("longOfSearch", longitude);
                                    startActivity(intent);
                                    finish();
                                }
                            } else if (role.equals(Constants.ROLE_TECHNICIAN) && editableVariable.equals("false")) {
                                scanEquipmentBtn.setVisibility(View.GONE);
                                acceptButton.setVisibility(View.GONE);
                                rejectButton.setVisibility(View.GONE);
                                autoCompleteSpinner.setDropDownHeight(0);
                                scanLocationBtn.setVisibility(View.VISIBLE);
                                scanLocationBtn.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        scanLocationMethod();
                                    }
                                });
                            }
                        }

                    }
                    frIdEditText.setText(jsonObject.get("frId").getAsString());
                    reqNameEditText.setText(jsonObject.get("requestorName").getAsString());
                    requestorNumberEditText.setText(jsonObject.get("requestorContactNo").getAsString());
                    if (!(jsonObject.get("equipment").isJsonNull())) {
                        equipmentIdTv.setText(jsonObject.get("equipment").getAsJsonObject().get("id").getAsString());
                        equipmentTextView.setText(jsonObject.get("equipment").getAsJsonObject().get("name").getAsString());
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String locationname = jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                        locId = jsonObject.get("location").getAsJsonObject().get("id").getAsInt();
                        locationSpinner.setText(locationname);
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String buildingname = jsonObject.get("building").getAsJsonObject().get("name").getAsString();
                        buildId = jsonObject.get("building").getAsJsonObject().get("id").getAsInt();
                        buildingSpinner.setText(buildingname);
                    }
                    if (!(jsonObject.get("division").isJsonNull())) {
                        String divisionname = jsonObject.get("division").getAsJsonObject().get("name").getAsString();
                        divId = jsonObject.get("division").getAsJsonObject().get("id").getAsInt();
                        divisionSpinner.setText(divisionname);
                    }
                    if (!(jsonObject.get("department").isJsonNull())) {
                        String deptname = jsonObject.get("department").getAsJsonObject().get("name").getAsString();
                        depId = jsonObject.get("department").getAsJsonObject().get("id").getAsInt();
                        deptSpinner.setText(deptname);
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String prirityname = jsonObject.get("priority").getAsJsonObject().get("name").getAsString();
                        priId = jsonObject.get("priority").getAsJsonObject().get("id").getAsInt();
                        prioritySpinner.setText(prirityname);
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String maintname = jsonObject.get("maintGrp").getAsJsonObject().get("name").getAsString();
                        maintId = jsonObject.get("maintGrp").getAsJsonObject().get("id").getAsInt();
                        mainGrpSpinner.setText(maintname);
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String faulname = jsonObject.get("faultCategory").getAsJsonObject().get("name").getAsString();
                        faultId = jsonObject.get("faultCategory").getAsJsonObject().get("id").getAsInt();
                        faultCategorySpinner.setText(faulname);
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        if (!jsonObject.get("eotTime").isJsonNull()) {
                            String eottimeVale = jsonObject.get("eotTime").getAsString();
                            eotTime.setText(eottimeVale);
                        }
                    }

                    if (!(jsonObject.get("attendedBy").isJsonNull())) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            JsonArray ja = jsonObject.get("attendedBy").getAsJsonArray();
                            for (int j = 0; j < ja.size(); j++) {
                                JsonObject jsonObject1 = ja.get(j).getAsJsonObject();
                                if (!(jsonObject1.isJsonNull())) {
                                    String techname = ja.get(j).getAsJsonObject().get("name").getAsString();
                                    techTv.setText(techname);
                                }
                            }
                        }
                    }
                    if ((jsonObject.get("locationDesc").getAsString()) != null) {
                        locDescEditText.setText(jsonObject.get("locationDesc").getAsString());
                    }
                    if ((jsonObject.get("faultCategoryDesc").getAsString()) != null) {
                        faultDetailsEditText.setText(jsonObject.get("faultCategoryDesc").getAsString());
                    }
                    if (!(jsonObject.get("observation").isJsonNull())) {
                        observationEditText.setText(jsonObject.get("observation").getAsString());
                    }
                    if (!(jsonObject.get("actionTaken").isJsonNull())) {
                        actionTakenEditText.setText(jsonObject.get("actionTaken").getAsString());
                    }
                    if (!(jsonObject.get("activationTime").isJsonNull())) {
                        activationDate.setText(new MainFragment().returnTimeString(jsonObject.get("activationTime").getAsString()));
                    }
                    if (!(jsonObject.get("arrivalTime").isJsonNull())) {
                        activationTime.setText(new MainFragment().returnTimeString(jsonObject.get("arrivalTime").getAsString()));
                    }
                    if (!(jsonObject.get("responseTime").isJsonNull())) {
                        responseTime.setText(jsonObject.get("responseTime").getAsString());
                    }
                    if (!(jsonObject.get("acknowledgementTime").isJsonNull())) {
                        acknowledgeTime.setText(new MainFragment().returnTimeString(jsonObject.get("acknowledgementTime").getAsString()));
                    }
                    if (!(jsonObject.get("downTime").isJsonNull())) {
                        downTime.setText(jsonObject.get("downTime").getAsString());
                    }
                    if (jsonObject.get("remarks") != null) {
                        for (int i = 0; i < jsonObject.get("remarks").getAsJsonArray().size(); i++) {
                            if (!jsonObject.get("remarks").getAsJsonArray().get(i).isJsonNull()) {
                                Log.d(TAG, "onResponse: " + listRemarksNo);
                                listRemarksNo = i + 1;
                                Log.d(TAG, "onResponse: " + listRemarksNo);
                                String remString = jsonObject.get("remarks").getAsJsonArray()
                                        .get(i).getAsString();
                                mlayout.addView(createNewEditText(remString, i));
                            }
                        }
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(EditFaultOnSearchActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(EditFaultOnSearchActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditFaultOnSearchActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: init frid " + t.getMessage());
                Log.d(TAG, "onFailure: init frid" + t.getCause());
            }
        });
    }

    private void callToCheckCompletedFridIsEditable(String fr) {

        Call<ResponseBody> call = APIClient.getUserServices().checkCompletefFridIsEditable(fr, workSpaceid, token, role);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    String editableOnComplete = null;
                    try {
                        editableOnComplete = response.body().string();
                        Log.d(TAG, "onResponse: check for complete " + editableOnComplete);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (editableOnComplete.equals("true")) {

                        MaterialButton acknowledgerBtn = findViewById(R.id.updateAcknowledgerBtn);
                        acknowledgerBtn.setVisibility(View.VISIBLE);
                        observationEditText.setEnabled(true);
                        actionTakenEditText.setEnabled(true);
                        plusbtn.setEnabled(true);
                        deletebtn.setEnabled(true);
                        autoCompleteSpinner.setDropDownHeight(0);
                        acknowledgerBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                callUpdateFaultReport();
                            }
                        });
                    } else if (editableOnComplete.equals("false")) {
                        scanEquipmentBtn.setVisibility(View.GONE);
                        acceptButton.setVisibility(View.GONE);
                        rejectButton.setVisibility(View.GONE);
                        autoCompleteSpinner.setDropDownHeight(0);
                        scanLocationBtn.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: failed call");

            }
        });
    }

    private void callUpdateFaultReport() {
        fmm fmm = new fmm(fmmUsername, fmmId);
        String acknowledgeText = ackEditText.getText().toString();
        String contactNumber = requestorNumberEditText.getText().toString();
        if ((TextUtils.isEmpty(requestorNumberEditText.getText())) || ((contactNumber.length() < 8))) {
            Toast.makeText(this, "Contact not valid", Toast.LENGTH_SHORT).show();
        } else if (techTv.getText().toString().isEmpty()) {
            Toast.makeText(this, "Technician not Selected", Toast.LENGTH_SHORT).show();
        } else {
            ProgressDialog mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setTitle("Updating");
            mProgressDialog.show();

            frId = frIdEditText.getText().toString();
            String reqName = reqNameEditText.getText().toString();
            String locationDesc = locDescEditText.getText().toString();
            String faultDetailString = faultDetailsEditText.getText().toString();
            String observerString = observationEditText.getText().toString();
            String actionTakenString = actionTakenEditText.getText().toString();
            String statusString = autoCompleteSpinner.getText().toString();
            String diagnosesString = null;
            if (!editTextList.isEmpty()) {
                for (int iRem = 0; iRem < editTextList.size(); iRem++) {
                    String remarks1String = editTextList.get(iRem).getText().toString();
                    remarksList.add(remarks1String);
                }
            } else remarksList = null;
            Integer eqId = null;
            if (!(TextUtils.isEmpty(equipmentIdTv.getText()))) {
                eqId = Integer.parseInt(equipmentIdTv.getText().toString());
            }

            attendedByIdsList = new ArrayList();

            String attendedbyString = techTv.getText().toString();
            List<String> attendedbylist = Arrays.asList(attendedbyString.split(", "));
            for (int j = 0; j < attendedbylist.size(); j++) {
                String techincian = attendedbylist.get(j);
                Log.d(TAG, "updateFaultReport: technician" + genralTechnicalList);
                for (list list : genralTechnicalList) {
                    if (list.getName().equals(techincian)) {
                        Integer idd = list.getId();
                        attendedByIdsList.add(idd);
                    }
                }
            }
            Equipment equipment;
            if (eqId != null) {
                equipment = new Equipment(eqId);
            } else {
                equipment = null;

            }
            Location location = new Location();
            location.setId(locId);
            Building building = new Building();
            building.setId(buildId);
            Department department = new Department();
            department.setId(depId);
            Division division = new Division();
            division.setId(divId);
            FaultCategory faultCategory = new FaultCategory();
            faultCategory.setId(faultId);
            MaintGrp maintGrp = new MaintGrp();
            maintGrp.setId(maintId);
            Priority priority = new Priority();
            priority.setId(priId);
            CostCenter costCenter = null;

            ArrayList<AttendedBy> attendedBy = new ArrayList<>();
            for (int j = 0; j < attendedByIdsList.size(); j++) {
                AttendedBy attendedbyObject = new AttendedBy();
                attendedbyObject.setId((Integer) attendedByIdsList.get(j));
                attendedBy.add(attendedbyObject);

            }
            AcknowledgedBy acknowledgedBy = null;
/*
            if (name != null && rank != null && encodedStringBuilder != null) {
                acknowledgedBy = new AcknowledgedBy(name, rank, encodedStringBuilder);
            }
*/

            String techSignString = null;
            EditFaultReportRequest editFaultReportRequest = new EditFaultReportRequest(techSignString, acknowledgedBy,
                    acknowledgeText,
                    frId,
                    building,
                    location,
                    reqName,
                    department,
                    contactNumber,
                    locationDesc,
                    faultCategory,
                    faultDetailString,
                    priority,
                    maintGrp,
                    division,
                    observerString,
                    diagnosesString,
                    actionTakenString,
                    costCenter,
                    statusString,
                    equipment,
                    remarksList,
                    attendedBy, fmm);

            Call<Void> callEditFaultReport = APIClient.getUserServices().updateReport(editFaultReportRequest,
                    token, workSpaceid, role);
            callEditFaultReport.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() == 200) {
                        Toast.makeText(EditFaultOnSearchActivity.this, "Fault Report Updated", Toast.LENGTH_LONG).show();
                        goToSeach();

                    } else if (response.code() == 401) {
                        Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                        LogoutClass logoutClass = new LogoutClass();
                        logoutClass.logout(EditFaultOnSearchActivity.this);
                    } else if (response.code() == 500) {
                        Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(EditFaultOnSearchActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        AlertDialog.Builder emptyDailog = new AlertDialog.Builder(EditFaultOnSearchActivity.this);
                        emptyDailog.setTitle("Error: " + response.code() + ". Please fill all the required fields!");
                        emptyDailog.setIcon(R.drawable.ic_error);
                        emptyDailog.setPositiveButton("Ok", null);
                        emptyDailog.show();
                    } else {
                        AlertDialog.Builder dailog = new AlertDialog.Builder(EditFaultOnSearchActivity.this);
                        dailog.setTitle("Error: " + response.code());
                        dailog.setIcon(R.drawable.ic_error);
                        dailog.setPositiveButton("Ok", null);
                        dailog.show();
                    }
                    mProgressDialog.dismiss();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.d(TAG, "onFailure: " + t.getCause());
                    Log.d(TAG, "onFailure: " + t.getMessage());
                    Toast.makeText(EditFaultOnSearchActivity.this, "Failed to update" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });
        }


    }

    private void getTechSign(String frId) {
        Call<ResponseBody> call = APIClient.getUserServices().getTechnicianSign(frId, workSpaceid, token, role);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse:tech sign " + response.body().byteStream());
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    techSignImgView.setImageBitmap(bmp);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void getSignatureMethod(String frid) {

        Call<ResponseBody> call = APIClient.getUserServices().getSignatureAckFr(frid, workSpaceid, token, role);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Log.d(TAG, "onResponse: sign " + response.body().byteStream());
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    ackImageViewSign.setImageBitmap(bmp);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void deleteRemarks(View view) {
        if (!editTextList.isEmpty()) {
            if (mlayout.getChildCount() > 1) {
                if (autoCompleteSpinner.getText().equals("Closed")) {
                    if (mlayout.getChildCount() > 2) {
                        mlayout.removeViewAt(mlayout.getChildCount() - 1);
                        int index = editTextList.size() - 1;
                        editTextList.remove(index);
                    }
                } else {
                    mlayout.removeViewAt(mlayout.getChildCount() - 1);
                    int index = editTextList.size() - 1;
                    editTextList.remove(index);
                }
            }
        }
    }

    @NotNull
    private TextView createNewEditText(String remarksString, int remarksId) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(20, 8, 20, 8);
        editText = new TextInputEditText(this);
        editText.setId(remarksId);
        editText.setBackgroundResource(R.drawable.mybutton);
        editText.setText(remarksString);
        editText.setLayoutParams(lparams);
        editText.setPadding(35, 30, 30, 30);
        editText.setHint("   add remarks");
        editText.setMinHeight(60);
        editText.setMaxWidth(mlayout.getWidth());
        editTextList.add(editText);
        String remarkSt = editText.getText().toString();
        editText.addTextChangedListener(textWatcher);
        return editText;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        checkForGps();
        if (requestCode == 111 && resultCode == RESULT_OK) {
            getLoc();
        }
        if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                int recievedCode = data.getIntExtra("response_code", 0);
                if (recievedCode == 200) {
                    if (!(jsonObject.get("equipment").isJsonNull())) {
                        scanEquipmentBtn.setVisibility(View.VISIBLE);
                        scanLocationBtn.setVisibility(View.GONE);
                    } else if (jsonObject.get("equipment").isJsonNull()) {
                        Intent intent = new Intent(EditFaultOnSearchActivity.this, EditFaultReportActivity.class);
                        intent.putExtra("workspace", workSpaceid);
                        intent.putExtra("value", "Fault");
                        intent.putExtra("frId", frIdEditText.getText().toString());
                        startActivity(intent);
                        finish();
                    }
                }
                if (recievedCode == 204) {
                    Toast.makeText(this, "You are not at the current location", Toast.LENGTH_SHORT).show();
                    scanEquipmentBtn.setVisibility(View.GONE);
                    acceptButton.setVisibility(View.GONE);
                    rejectButton.setVisibility(View.GONE);
                    autoCompleteSpinner.setDropDownWidth(0);
                }
                if (recievedCode == 422) {
                    Toast.makeText(this, "Fault report does not match with the scanned location", Toast.LENGTH_SHORT).show();
                    scanEquipmentBtn.setVisibility(View.GONE);
                    acceptButton.setVisibility(View.GONE);
                    rejectButton.setVisibility(View.GONE);
                    autoCompleteSpinner.setDropDownWidth(0);
                }
                if (recievedCode == 400) {
                    Toast.makeText(this, "Geo-Location is not assigned to this fault report ", Toast.LENGTH_SHORT).show();
                    scanEquipmentBtn.setVisibility(View.GONE);
                    acceptButton.setVisibility(View.GONE);
                    rejectButton.setVisibility(View.GONE);
                    autoCompleteSpinner.setDropDownWidth(0);
                }

            }
        }
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                String eqip = data.getStringExtra("equipment_code");
                equipmentTextView.setText(eqip);
            }
        } else if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                assert data != null;
                uploadFileTv.setText(data.getData().getPath());
                uploadFileBtn.setVisibility(View.VISIBLE);
                Uri uri = data.getData();
            }
        }

        //1feb upload pdf for accept
        if (requestCode == 11 && resultCode == RESULT_OK && data != null) {
            assert data != null;
            uploadAuth.setEnabled(true);
            Uri uri = data.getData();

            try {

                InputStream inputStream = this.getContentResolver().openInputStream(uri);
                byte[] pdfInBytes = new byte[inputStream.available()];

                pdfView.recycle();
                pdfView.fromBytes(pdfInBytes).load();

                inputStream.read(pdfInBytes);
                String path = android.util.Base64.encodeToString(pdfInBytes, android.util.Base64.DEFAULT);

                uploadAuth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        acceptMethod(path);
                        //  uploadFileMethod(path, frid);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void initializeFab() {
        fab_before.setAlpha(0f);
        fab_after.setAlpha(0f);
        fab_before.setTranslationY(translationY);
        fab_after.setTranslationY(translationY);
        fabPurchaseOrder.setAlpha(0f);
        fabQuoteUpload.setAlpha(0f);
        fabQuoteUpload.setTranslationY(translationY);
        fabPurchaseOrder.setTranslationY(translationY);
        fabQuoteMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMenuOpenQuote) {
                    closeQuote();
                } else {
                    openQuote();
                }
            }
        });
        fabPurchaseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultOnSearchActivity.this, ViewPurchaseOnly.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "Before");
                intent.putExtra("checkForFrid", frid);
                intent.putExtra("role", role);
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("frId", frIdEditText.getText().toString());
                intent.putExtra("status", autoCompleteSpinner.getText().toString());
                startActivity(intent);
            }
        });
        fabQuoteUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultOnSearchActivity.this, UploadFile.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "Before");
                intent.putExtra("checkForFrid", frid);
                intent.putExtra("role", role);
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("frId", frIdEditText.getText().toString());
                intent.putExtra("status", autoCompleteSpinner.getText().toString());
                startActivity(intent);
            }
        });
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isMenuOpen) {
                    closeMenu();
                    fabLayoutQuotation.setVisibility(View.VISIBLE);
                } else {
                    openMenu();
                    fabLayoutQuotation.setVisibility(View.GONE);
                }
            }
        });
        fab_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultOnSearchActivity.this, ImagesActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "Before");
                intent.putExtra("checkForFrid", frid);
                intent.putExtra("role", role);
                intent.putExtra("fromFrDetail", "FrDetailPaGE");
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("frId", frIdEditText.getText().toString());
                intent.putExtra("status", autoCompleteSpinner.getText().toString());
                startActivity(intent);
            }
        });
        fab_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultOnSearchActivity.this, ImagesActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "After");
                intent.putExtra("checkForFrid", frid);
                intent.putExtra("fromFrDetail", "FrDetailPaGE");
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("role", role);
                intent.putExtra("status", autoCompleteSpinner.getText().toString());
                intent.putExtra("frId", frIdEditText.getText().toString());
                startActivity(intent);
            }
        });
    }

    private void closeQuote() {
        isMenuOpenQuote = !isMenuOpenQuote;
        fabQuoteMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fabQuoteUpload.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
        fabPurchaseOrder.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
    }

    private void openQuote() {
        isMenuOpenQuote = !isMenuOpenQuote;
        fabQuoteMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fabPurchaseOrder.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabQuoteUpload.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fab_main.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fab_before.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_after.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;
        fab_main.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fab_before.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
        fab_after.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(checkInternet, intentFilter);


    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(checkInternet);
    }

    private String prependZero(String text) {
        if (text.length() == 1) {
            return '0' + text;
        }
        return text;
    }

    private void scanLocationMethod() {

        getLoc();


    }

    private void getLoc() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            fusedLocationProviderClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, null).addOnCompleteListener(new OnCompleteListener<android.location.Location>() {
                @Override
                public void onComplete(@NonNull Task<android.location.Location> task) {
                    
                    if (task.getResult().getLongitude() != 0 && task.getResult().getLatitude() != 0) {

                        double lat = task.getResult().getLatitude();
                        double longg = task.getResult().getLongitude();

                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        editor = sharedPreferences.edit();
                        editor.putString("long", String.valueOf(longg));
                        editor.putString("lat", String.valueOf(lat));
                        editor.apply();
                        Log.d(TAG, "onComplete: got coordinates " + lat + "long" + longg);

                        Intent intent = new Intent(EditFaultOnSearchActivity.this,
                                MlQrScannerActivity.class);
                        intent.putExtra("token", token);
                        intent.putExtra("role", role);
                        intent.putExtra("frId", frIdEditText.getText().toString());
                        intent.putExtra("workspace", workSpaceid);
                        startActivityForResult(intent, 2);
                    }
                }
            });


        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
        }


    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(EditFaultOnSearchActivity.this, Search.class);
            intent.putExtra("workspace", workSpaceid);

            startActivity(intent);
            finish();
        }
    }

    public void goToSearch() {
        Intent intent = new Intent(EditFaultOnSearchActivity.this, Search.class);
        intent.putExtra("workspace", workSpaceid);
        startActivity(intent);
        finish();
    }

    public void alertDialog(String value) {
        String tilte = null;
        String message = null;
        if (value.equals("Quotation")) {
            tilte = "Upload Quotation";
            message = "Please Upload Quotation For Further Action";
        } else if (value.equals("Purchase")) {
            tilte = "Upload Purchase Order";
            message = "Please Upload Purchase order For Further Action";
        }

        new AlertDialog.Builder(this)
                .setTitle(tilte)
                .setMessage(message)
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (value.equals("Quotation")) {
                            Intent intent = new Intent(EditFaultOnSearchActivity.this, UploadPdf.class);
                            intent.putExtra("frId", frid);
                            intent.putExtra("workspace", workSpaceid);
                            startActivity(intent);
                            finish();
                        } else if (value.equals("Purchase")) {
                            Intent intent = new Intent(EditFaultOnSearchActivity.this, UploadPurchasePdf.class);
                            intent.putExtra("frId", frid);
                            intent.putExtra("workspace", workSpaceid);
                            startActivity(intent);
                            finish();
                        }

                    }
                })
                .create().show();

    }

    private void uploadpdfAuthorizationDialog() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(EditFaultOnSearchActivity.this);
        final View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_accept_requestmethod, null);
        final ImageView crossImg = dialogView.findViewById(R.id.crosscancel);
        getPdfAuth = dialogView.findViewById(R.id.getpdfautherozation);
        uploadAuth = dialogView.findViewById(R.id.uploadpdfauthorization);
        pdfView = dialogView.findViewById(R.id.pdfViewAuth);

        builder.setView(dialogView)
                .setCancelable(false);
        alertAuthPdf = builder.create();
        alertAuthPdf.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                crossImg.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertAuthPdf.dismiss();
                    }
                });
                getPdfAuth.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getPdfFromPhone();
                    }
                });
            }
        });
        alertAuthPdf.show();
    }

    private void getPdfFromPhone() {
        uploadFileIntent = new Intent(ACTION_GET_CONTENT);
        uploadFileIntent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(uploadFileIntent, "Select file"), 11);
    }

    public void goToSeach() {
        Intent intent = new Intent(EditFaultOnSearchActivity.this, Search.class);
        intent.putExtra("workspace", workSpaceid);
        startActivity(intent);
        finish();
    }

    public void callReadMessage(String idFromMessages) {

       /* ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        progressDialog.show();*/
        Call<SingleMessageResponse> callChat = APIClient.getUserServices().checkReadMessage(token, idFromMessages);
        callChat.enqueue(new Callback<SingleMessageResponse>() {
            @Override
            public void onResponse(Call<SingleMessageResponse> call, Response<SingleMessageResponse> response) {
//                progressDialog.dismiss();
                if (response.code() == 200) {

                }
            }

            @Override
            public void onFailure(Call<SingleMessageResponse> call, Throwable t) {
//                progressDialog.dismiss();
            }
        });
    }


}