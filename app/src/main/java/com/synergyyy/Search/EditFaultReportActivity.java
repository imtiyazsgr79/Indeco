package com.synergyyy.Search;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.kyanogen.signatureview.SignatureView;
import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;
import com.synergyyy.General.LogoutClass;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.PurchaseOder.ViewPurchaseOnly;
import com.synergyyy.R;
import com.synergyyy.FaultReport.list;
import com.synergyyy.SearchTasks.ImagesActivity;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Intent.ACTION_GET_CONTENT;
import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class EditFaultReportActivity extends MyBaseActivity {
    //test

    private FloatingActionButton fab_main, fab_before, fab_after, fab_upload_pdf, fabQuoteMain, fabQuoteUpload, fabPurchaseOrder;
    private Boolean isMenuOpen = false;
    private Boolean isMenuOpenQuote = false;
    private final OvershootInterpolator interpolator = new OvershootInterpolator();
    private Float translationY = 600f;
    private static final String TAG = "EditFault";
    private String timeS, remarksString, techName;
    private String frId, eot;
    TextView uploadFileTv;
    private int idStatus;
    String statuscomingFrid;
    private String statusComing = "";
    private String statusNameCurrent;
    Button uploadFileBtn;
    Intent uploadFileIntent;
    private String token, role;
    private String faultDetailString, username;
    private TextView equipmentIdTv;
    private Button plusbtn, deletebtn;
    private MaterialButton updateFaultReportButton, requestPauseButton,
            acceptButton, rejectButton, scanEquipmentBtn;
    private LinearLayout mLayout;
    private TextInputLayout acknowledgeEdtTxt;
    private String workSpaceid, equipCode;
    private int remarksId;
    private String fridFromFrDetail;
    //  private EditText editText;
    private TextWatcher textWatcher;
    private List<String> remarksList = new ArrayList<>();
    private List<TextInputEditText> editTextList = new ArrayList<TextInputEditText>();
    private int listRemarksNo;
    List<String> genralStatusList = new ArrayList<String>();
    List<list> genralTechnicalList = new ArrayList<list>();

    private ImageView techSignImgView;
    int depId, divId, priId, buildId, locId, faultId, maintId;
    private Integer costId = null;

    private ArrayAdapter<String> statusListAdapter;
    ProgressDialog progressDialog;
    LinearLayout linearLayoutdisable;
    boolean[] checkedItems;
    List<String> stockList = new ArrayList<>();
    String[] listItems;
    List attendedByIdsList;
    private LinearLayout fabLayoutQuotation;
    List<fmm> listFmm;

    private double latitude, longitude, latOfSearch, longOfSearch;
    private AutoCompleteTextView autoCompleteSpinner, fmmAutocompleteSpinner;
    private TextInputEditText frIdEditText, deptSpinner, reqNameEditText, activationDate,
            activationTime, faultDetailsEditText, locDescEditText, faultCategorySpinner,
            divisionSpinner, locationSpinner, buildingSpinner, prioritySpinner,
            mainGrpSpinner, observationEditText,
            requestorNumberEditText, actionTakenEditText, eotTypeTextView,
            equipmentTextView, editTextRemarks, techTv, acknowledgeedittextfield, responseTime,
            acknowledgeTime, downTime, eotTime, fmmCaseId;

    private RadioButton radioSelectedButton;
    private List<StatusItem> items = new ArrayList<StatusItem>();
    String tech = "Technician";
    String managingAgent = "ManagingAgent";
    private double latitudeEquipment, longitudeEquipment;

    private PDFView pdfView;
    private FloatingActionButton getpdfAuth, uploadAuth;
    private boolean uploadedFile = false;
    private androidx.appcompat.app.AlertDialog alertAuthPdf;
    private String nameAck = null;
    private String rankStringAck = null;
    private String signAck = null;
    //  private String techSignString = null;
    private ImageView ackImageViewSign;
    private TextInputEditText ackNameEt, ackRankEt;
    private LinearLayout ackMainLayout, fmmCaseIdLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_edit_fault_report, null, false);
        drawer.addView(viewLayout, 0);

        activityNameTv.setText("Edit Fault Report");

        progressDialog = new ProgressDialog(EditFaultReportActivity.this);
        progressDialog.setTitle("Loading...");

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        role = sharedPreferences.getString("role", "");
        token = sharedPreferences.getString("token", "");
        username = sharedPreferences.getString("username", "");


        Intent i = getIntent();
        fridFromFrDetail = i.getStringExtra("frId");
        longitude = i.getDoubleExtra("longitude", 0);
        latitude = i.getDoubleExtra("latitude", 0);
        latOfSearch = i.getDoubleExtra("latOfSearch", 0);
        longOfSearch = i.getDoubleExtra("longOfSearch", 0);
        workSpaceid = i.getStringExtra("workspace");
        workSpaceid=workspace;

        equipCode = i.getStringExtra("equipcode");
        frId = i.getStringExtra("frIdOnScan");
        autoCompleteSpinner = findViewById(R.id.statusSpinner);
        fmmAutocompleteSpinner = findViewById(R.id.fmmAutocompleteSpinner_xml);


        AutoCompleteTextAdaptar adapter = new AutoCompleteTextAdaptar(EditFaultReportActivity.this, items, role);
        autoCompleteSpinner.setAdapter(adapter);


        linearLayoutdisable = findViewById(R.id.layout_disable);
        if (role.equals(Constants.ROLE_MANAGING_AGENT)) {

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
            if (fridFromFrDetail != null) {
                acceptButton.setVisibility(View.GONE);
                rejectButton.setVisibility(View.GONE);
                updateFaultReportButton.setVisibility(View.GONE);
                requestPauseButton.setVisibility(View.GONE);
                initviewsAndGetInitialData(fridFromFrDetail);
            } else {
                requestPauseButton.setVisibility(View.GONE);
                initviewsAndGetInitialDataOnEquip(equipCode);
            }

        } else {
            items.add(new StatusItem("Select status"));
            items.add(new StatusItem("Open"));
            items.add(new StatusItem("Pause"));
            //      items.add(new StatusItem("Closed"));
            items.add(new StatusItem("Completed"));
            //     items.add(new StatusItem("Pause Requested"));
            initViews();
            acceptButton.setVisibility(View.GONE);
            rejectButton.setVisibility(View.GONE);
            initializeFab();
            calltech();
            if (fridFromFrDetail != null) {
                //  updateFaultReportButton.setVisibility(View.GONE);
                // requestPauseButton.setVisibility(View.GONE);
                initviewsAndGetInitialData(fridFromFrDetail);
            } else {
                initviewsAndGetInitialDataOnEquip(equipCode);

            }
        }
        updateFaultReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(fmmAutocompleteSpinner.getText())) {
                    Toast.makeText(EditFaultReportActivity.this, "Please Select FMM", Toast.LENGTH_SHORT).show();
                } else if (updateFaultReportButton.getText().toString().equals("Acknowledge") && !TextUtils.isEmpty(fmmAutocompleteSpinner.getText())) {
                    authorizeAcknowledge();
                } else {
                    String techSignString = null;
                    updateFaultReport(signAck, nameAck, rankStringAck, techSignString);
                }
            }
        });

        getAllFmmList();

    }

    private void getAllFmmList() {
        Call<List<fmm>> call = APIClient.getUserServices().getAllFmm(token, workSpaceid);
        call.enqueue(new Callback<List<fmm>>() {
            @Override
            public void onResponse(Call<List<fmm>> call, Response<List<fmm>> response) {
                if (response.code() == 200) {
                    listFmm = response.body();
                    AutoCompleteFmmAdapter adapter = new AutoCompleteFmmAdapter(EditFaultReportActivity.this, listFmm);
                    fmmAutocompleteSpinner.setAdapter(adapter);
                } else
                    Toast.makeText(EditFaultReportActivity.this, "Failed :" + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<fmm>> call, Throwable t) {
                Toast.makeText(EditFaultReportActivity.this, "Fmm to load Fmm list", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void authorizeAcknowledge() {

        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);

        final View dialogView = getLayoutInflater().inflate(R.layout.custom_acknowledger_oncomplete_status_dialog_box, null);
        final TextInputEditText nameEt = dialogView.findViewById(R.id.nameauth);
        final TextInputEditText rank = dialogView.findViewById(R.id.rank_alert_b0x_edittext);
        final TextInputLayout nameLayout = dialogView.findViewById(R.id.nricname_layout);
        final TextInputLayout rankLayout = dialogView.findViewById(R.id.rank_alert_b0x);
        final SignatureView achSignatureView = dialogView.findViewById(R.id.signatureEdit);
        final SignatureView technicianSign = dialogView.findViewById(R.id.techniciansignatureEdit);
        final TextView techSignTv = dialogView.findViewById(R.id.texhsigntv);
        final TextView signTv = dialogView.findViewById(R.id.signtv);

        builder.setTitle("Acknowledger Details")
                .setView(dialogView)
                .setCancelable(false)
                .setIcon(R.drawable.ic_error)
                .setPositiveButton("Update Fault Report", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        androidx.appcompat.app.AlertDialog alert = builder.create();

        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button b = alert.getButton(android.app.AlertDialog.BUTTON_POSITIVE);
                b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name, rankString;
                        if (!TextUtils.isEmpty(nameEt.getText())
                                && !TextUtils.isEmpty(rank.getText())
                                && !achSignatureView.isBitmapEmpty()
                                && !technicianSign.isBitmapEmpty()) {
                            name = nameEt.getText().toString();
                            rankString = rank.getText().toString();
                            compressImageMethod(achSignatureView, name, rankString, technicianSign);
                            alert.dismiss();
                        } else if (achSignatureView.isBitmapEmpty()) {
                            signTv.setText("Please sign Here ");
                            signTv.setTextColor(Color.parseColor("#FA0C0C"));
                        } else if (TextUtils.isEmpty(nameEt.getText())) {
                            nameLayout.setError("Required");
                        } else if (TextUtils.isEmpty(rank.getText())) {
                            rankLayout.setError("Required");
                        } else if (technicianSign.isBitmapEmpty()) {
                            techSignTv.setText("Please Sign Here");
                            techSignTv.setTextColor(Color.parseColor("#FA0C0C"));
                        }
                    }
                });
            }
        });
        alert.show();
    }

    private void compressImageMethod(SignatureView signatureView, String name, String rank, SignatureView technicianSign) {
        String acknowledgerSignString, techSignString = null;
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            signatureView.getSignatureBitmap().compress(Bitmap.CompressFormat.PNG, 50, baos); // bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();
            acknowledgerSignString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            ByteArrayOutputStream baosT = new ByteArrayOutputStream();
            technicianSign.getSignatureBitmap().compress(Bitmap.CompressFormat.PNG, 50, baosT); // bm is the bitmap object
            byte[] imageBytesT = baosT.toByteArray();
            techSignString = Base64.encodeToString(imageBytesT, Base64.DEFAULT);


            updateFaultReport(acknowledgerSignString, name, rank, techSignString);

        } catch (Exception e) {
            e.printStackTrace();
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
        // plusbtn.setEnabled(false);
        //deletebtn.setEnabled(false);
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
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(EditFaultReportActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: tech" + t.getCause());
                Log.d(TAG, "onFailure: tech" + t.getMessage());
                Toast.makeText(EditFaultReportActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void initViews() {
        fmmCaseIdLayout = findViewById(R.id.fmmcaseIdlayoutedit);
        fmmCaseId = findViewById(R.id.fmmcaseidedit);
        techSignImgView = findViewById(R.id.techSignImageView);
        ackNameEt = findViewById(R.id.AckNameEt);
        ackRankEt = findViewById(R.id.AckRankEt);
        ackImageViewSign = findViewById(R.id.AckImageView);

        ackMainLayout = findViewById(R.id.AckMainLayout);

        acknowledgeedittextfield = findViewById(R.id.acknowledgeedittextfield);
        eotTime = findViewById(R.id.eottimetextView);
        responseTime = findViewById(R.id.response_time);
        downTime = findViewById(R.id.downtime_time);
        acknowledgeTime = findViewById(R.id.acknowlegde_time);
        fabLayoutQuotation = findViewById(R.id.lay_fab_quote);
        fabQuoteMain = findViewById(R.id.quotation_fab_main);
        fabQuoteUpload = findViewById(R.id.pdf_fab_quotation_fab);
        fabPurchaseOrder = findViewById(R.id.pdf_fab_purchase_orde_fab);
        eotTypeTextView = findViewById(R.id.eottypetextView);
        acknowledgeEdtTxt = findViewById(R.id.acknowledgeedittext);
        acceptButton = findViewById(R.id.acceptbttn);
        rejectButton = findViewById(R.id.rejectbtn);
        scanEquipmentBtn = findViewById(R.id.equipmentScanButton);
        requestPauseButton = findViewById(R.id.requestPauseButton);
        techTv = findViewById(R.id.techtv);
        equipmentIdTv = findViewById(R.id.eq_id_send);
        updateFaultReportButton = findViewById(R.id.updateFaultReportButton);
        actionTakenEditText = findViewById(R.id.actionTaken);
        mainGrpSpinner = findViewById(R.id.mainGrp);
        //selectEquipmentButton = findViewById(R.id.selectEquipmentButton);
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
        mLayout = findViewById(R.id.layout_remarks);
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


/*
        selectTech.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditFaultReportActivity.this, "dialog", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder mbuilder = new AlertDialog.Builder(EditFaultReportActivity.this);
                mbuilder.setTitle("Select Technicians");
                mbuilder.setMultiChoiceItems(listItems, checkedItems, (dialog, position, isChecked) -> {
                    if (isChecked) {
                        if (!mUserItems.contains(position)) {
                            mUserItems.add(position);
                        }
                    } else if (mUserItems.contains(position)) {
                        mUserItems.remove(position);

                    }
                });
                mbuilder.setCancelable(false);
                mbuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < mUserItems.size(); i++) {
                            item = item + listItems[mUserItems.get(i)];
                            if (i != mUserItems.size() - 1) {
                                item = item + ", ";
                            }
                        }
                        techTv.setText(item);
                    }
                });
                mbuilder.show();
                mbuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });


            }

        });
*/

        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                checkFieldsForEmptyValues();
            }

            @Override
            public void afterTextChanged(Editable editable) {

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

        plusbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLayout.getChildCount() < 9) {
                    remarksId++;

                    mLayout.addView(createNewEditText(remarksString, remarksId));
                }
            }
        });
        requestPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fmmAutocompleteSpinner.getText().toString().isEmpty()) {
                    Toast.makeText(EditFaultReportActivity.this, "Please select FMM", Toast.LENGTH_SHORT).show();
                } else {

                    final AlertDialog.Builder builder = new AlertDialog.Builder(EditFaultReportActivity.this);
                    final View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_box, null);
                    final RadioGroup group = dialogView.findViewById(R.id.radioPersonGroup);
                    final EditText eotDialoText = dialogView.findViewById(R.id.requiredeotdialog);
                    builder.setTitle("Fault Cost")
                            .setView(dialogView)
                            .setCancelable(false)
                            .setPositiveButton("ok", null)

                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
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
                                        Toast.makeText(EditFaultReportActivity.this, "Please Select Cost", Toast.LENGTH_LONG).show();
                                        // no radio buttons are checked
                                    } else if (!TextUtils.isEmpty(eotDialoText.getText())) {
                                        // one of the radio buttons is checked
                                        int selectedId = group.getCheckedRadioButtonId();
                                        radioSelectedButton = (RadioButton) dialogView.findViewById(selectedId);

                                        if (radioSelectedButton.getText().toString().equals("Greater than S$1000")) {
                                            pauseMethodCall("Greater Than $1000", eotDialoText.getText().toString());
                                        }
                                        if (radioSelectedButton.getText().toString().equals("Less than S$1000")) {
                                            pauseMethodCall("Less Than $1000", eotDialoText.getText().toString());
                                        }
                                        alert.dismiss();

                                    } else {
                                        eotDialoText.setError("Required Field");
                                        Toast.makeText(EditFaultReportActivity.this, "Please Fill Required Eot Time",
                                                Toast.LENGTH_LONG).show();

                                    }


                                }
                            });
                        }
                    });
                    alert.show();
                }

            }
        });

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadpdfAuthorizationDialog();

                //    acceptMethod();
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

    }

    private void rejectMethod() {
        String file = null;
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
                observation, actionTaken, file);
        Call<Void> call = APIClient.getUserServices().getReject(token, workSpaceid, acceptRejectBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultReportActivity.this, "Rejected ", Toast.LENGTH_SHORT).show();
                    goToSeach();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditFaultReportActivity.this, "Failed to Reject", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage() + t.getCause());
                progressDialog.dismiss();
            }
        });
    }

    private void acceptMethod(String pdfFile) {
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
        AcceptRejectBody acceptRejectBody = new AcceptRejectBody(remarksList, frIdEditText.getText().toString(), observation, actionTaken, "jdjdbhcjds");
        Call<Void> call = APIClient.getUserServices().getAccept(token, workSpaceid, acceptRejectBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    uploadAuth.setEnabled(false);
                    uploadedFile = true;
                    Toast.makeText(EditFaultReportActivity.this, "Accepted", Toast.LENGTH_SHORT).show();
                    alertAuthPdf.dismiss();
                    goToSeach();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditFaultReportActivity.this, "Failed to Accepted", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage() + t.getCause());
                progressDialog.dismiss();
            }
        });


    }

    private void pauseMethodCall(String value, String eotTime) {
        progressDialog.show();
        String username = fmmAutocompleteSpinner.getText().toString();
        int id = 0;
        for (int i = 0; i < listFmm.size(); i++) {
            if (listFmm.get(i).getUsername().equals(username)) {
                id = listFmm.get(i).getId();
            }
        }
        fmm fmm = new fmm(username, id);

        String remarks1String;
        if (!editTextList.isEmpty()) {
            for (int iRem = listRemarksNo; iRem < editTextList.size(); iRem++) {
                remarks1String = editTextList.get(iRem).getText().toString();
                remarksList.add(remarks1String);
            }
        } else remarksList = null;
        String observation = observationEditText.getText().toString();
        String actionTaken = actionTakenEditText.getText().toString();
        PauseRequestBody pauseRequestBody = new PauseRequestBody(value, remarksList, frIdEditText.getText().toString(), observation, actionTaken, eotTime, fmm);
        Call<Void> call = APIClient.getUserServices().getRequestPause(token, workSpaceid, pauseRequestBody);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

                if (response.code() == 200) {
                    progressDialog.dismiss();
                    Toast.makeText(EditFaultReportActivity.this, "Pause Request sent", Toast.LENGTH_SHORT).show();
                    goToSeach();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(EditFaultReportActivity.this, "Failed to Pause", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onFailure: " + t.getMessage() + t.getCause());
                progressDialog.dismiss();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    private void callUploadsFileMethods() {
        uploadFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        uploadFileIntent.setType("*/*");
        startActivityForResult(Intent.createChooser(uploadFileIntent, "Select file"), 10);


    }

    private void initviewsAndGetInitialDataOnEquip(String equipmentCode) {
        progressDialog.show();
        EquipmentGeoLocationClass geoLocationClass = new EquipmentGeoLocationClass(equipmentCode, frId);
        Call<JsonObject> call = APIClient.getUserServices().getEquipmentDetailsOnGeolocation
                (workSpaceid, token, role, geoLocationClass);
        call.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                progressDialog.dismiss();
                if (response.code() == 216) {
                    Toast.makeText(EditFaultReportActivity.this, "No Fault Reports found on this code", Toast.LENGTH_SHORT).show();
                    goToSeach();
                } else if (response.code() == 214) {
                    Toast.makeText(EditFaultReportActivity.this, "You have not scanned the location for this fault report", Toast.LENGTH_LONG).show();
                    goToSeach();
                } else if (response.code() == 215) {
                    Toast.makeText(EditFaultReportActivity.this, "Equipment scanned is not of the viewed fault report", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(EditFaultReportActivity.this, EditFaultOnSearchActivity.class);
                    intent.putExtra("workspace", workSpaceid);
                    intent.putExtra("frId", frId);
                    startActivity(intent);
                    finish();
                } else if (response.code() == 200) {
                    JsonObject jsonObject = response.body();
                    Log.d(TAG, "onResponse: json on Qr search" + jsonObject);
                    assert jsonObject != null;

                    if (!jsonObject.get("technicianSignature").isJsonNull()) {
                        techSignImgView.setVisibility(View.VISIBLE);
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
                    if (!(jsonObject.get("fmm").isJsonNull())) {
                        fmmAutocompleteSpinner.setText(jsonObject.get("fmm").getAsJsonObject().get("username").getAsString());
                        fmmAutocompleteSpinner.setEnabled(false);
                        fmmAutocompleteSpinner.setDropDownHeight(0);
                    }
                    if (!jsonObject.get("eotType").isJsonNull()) {
                        eot = jsonObject.get("eotType").getAsString();
                        eotTypeTextView.setText(eot);
                    }
                    frIdEditText.setText(jsonObject.get("frId").getAsString());
                    reqNameEditText.setText(jsonObject.get("requestorName").getAsString());
                    requestorNumberEditText.setText(jsonObject.get("requestorContactNo").getAsString());
                    if (!(jsonObject.get("equipment").isJsonNull())) {
                        equipmentIdTv.setText(jsonObject.get("equipment").getAsJsonObject().get("id").getAsString());
                        equipmentTextView.setText(jsonObject.get("equipment").getAsJsonObject().get("name").getAsString());
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String buildingname = jsonObject.get("building").getAsJsonObject().get("name").getAsString();
                        buildId = jsonObject.get("building").getAsJsonObject().get("id").getAsInt();
                        // genralBuildingList.add(new list(buildingname, id));
                        buildingSpinner.setText(buildingname);

                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        if (!jsonObject.get("eotTime").isJsonNull()) {
                            String eottimeVale = jsonObject.get("eotTime").getAsString();
                            eotTime.setText(eottimeVale);
                        }
                    }
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        String buildingname = jsonObject.get("location").getAsJsonObject().get("name").getAsString();
                        locId = jsonObject.get("location").getAsJsonObject().get("id").getAsInt();
                        locationSpinner.setText(buildingname);
                    }

                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        if (!(jsonObject.get("division").isJsonNull())) {
                            String divisionname = jsonObject.get("division").getAsJsonObject().get("name").getAsString();
                            divId = jsonObject.get("division").getAsJsonObject().get("id").getAsInt();
                            divisionSpinner.setText(divisionname);
                        }
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
                    if (!(jsonObject.get("attendedBy").isJsonNull())) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                            JsonArray ja = jsonObject.get("attendedBy").getAsJsonArray();
                            for (int j = 0; j < ja.size(); j++) {
                                JsonObject jsonObject1 = ja.get(j).getAsJsonObject();
                                if (!(jsonObject1.isJsonNull())) {
                                    String techname = ja.get(j).getAsJsonObject().get("name").getAsString();
                                    techTv.setText(techname);
                                    OptionalInt index = IntStream.range(0, genralTechnicalList.size())
                                            .filter(i -> genralTechnicalList.get(i).name.equals(techname))
                                            .findFirst();
                                    if (index.isPresent()) {
                                        int realid = index.getAsInt();
                                    }
                                }
                            }
                        }
                    }

                    if (!(jsonObject.get("locationDesc").isJsonNull())) {
                        locDescEditText.setText(jsonObject.get("locationDesc").getAsString());
                    }
                    if (!(jsonObject.get("faultCategoryDesc").isJsonNull())) {
                        faultDetailsEditText.setText(jsonObject.get("faultCategoryDesc").getAsString());
                    }

                    if (!(jsonObject.get("observation").isJsonNull())) {
                        observationEditText.setText(jsonObject.get("observation").getAsString());
                    }
                    if (!(jsonObject.get("remarks").isJsonNull())) {
                        for (int i = 0; i < jsonObject.get("remarks").getAsJsonArray().size(); i++) {
                            if (!jsonObject.get("remarks").getAsJsonArray().get(i).isJsonNull()) {
                                listRemarksNo = i + 1;
                                String remString = jsonObject.get("remarks").getAsJsonArray()
                                        .get(i).getAsString();
                                mLayout.addView(createNewEditText(remString, i));
                            }
                        }
                    }

                    if (!(jsonObject.get("actionTaken").isJsonNull())) {
                        actionTakenEditText.setText(jsonObject.get("actionTaken").getAsString());
                    }

                    if (!(jsonObject.get("status").isJsonNull())) {
                        statusComing = jsonObject.get("status").getAsString();
                        if (genralStatusList.contains(statusComing)) {
                            autoCompleteSpinner.setText(statusComing, false);
                        }
                    }
                    if (!(jsonObject.get("activationTime").isJsonNull())) {
                        activationDate.setText(new MainFragment().returnTimeString(jsonObject.get("activationTime").getAsString()));
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

                    if (!(jsonObject.get("arrivalTime").isJsonNull())) {
                        activationTime.setText(new MainFragment().returnTimeString(jsonObject.get("arrivalTime").getAsString()));

                    }
                    String editableVariable = jsonObject.get("editable").getAsString();

                    if (role.equals(Constants.ROLE_MANAGING_AGENT) && editableVariable.equals("false")) {
                        if (role.equals(Constants.ROLE_MANAGING_AGENT) && statusComing.equals("Closed")) {
                            updateFaultReportButton.setVisibility(View.GONE);
                            acceptButton.setVisibility(View.GONE);
                            rejectButton.setVisibility(View.GONE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                }
                            });

                        }


                    } else if ((role.equals(Constants.ROLE_MANAGING_AGENT) && editableVariable.equals("true"))) {
                        if (role.equals(Constants.ROLE_MANAGING_AGENT) && statusComing.equals("Pause Requested")) {
                            acceptButton.setVisibility(View.VISIBLE);
                            autoCompleteSpinner.setDropDownWidth(0);
                            rejectButton.setVisibility(View.VISIBLE);
                            observationEditText.setEnabled(true);
                            acknowledgeedittextfield.setEnabled(false);
                            actionTakenEditText.setEnabled(true);
                            updateFaultReportButton.setVisibility(View.GONE);
                        } else if (role.equals(Constants.ROLE_MANAGING_AGENT) && statusComing.equals("Completed")) {
                            updateFaultReportButton.setVisibility(View.VISIBLE);
                            acceptButton.setVisibility(View.GONE);
                            acknowledgeedittextfield.setEnabled(false);
                            rejectButton.setVisibility(View.GONE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause Requested")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                }
                            });

                        } else if (role.equals(Constants.ROLE_MANAGING_AGENT) && statusComing.equals("Pause")) {
                            updateFaultReportButton.setVisibility(View.GONE);
                            acceptButton.setVisibility(View.GONE);
                            acknowledgeedittextfield.setEnabled(false);
                            rejectButton.setVisibility(View.GONE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                }
                            });

                        } else if (role.equals(Constants.ROLE_MANAGING_AGENT) && statusComing.equals("Closed")) {
                            updateFaultReportButton.setVisibility(View.GONE);
                            acceptButton.setVisibility(View.GONE);
                            rejectButton.setVisibility(View.GONE);
                            acknowledgeedittextfield.setEnabled(false);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                }
                            });

                        }
                    }
                    if (editableVariable.equals("false")) {
                        Toast.makeText(EditFaultReportActivity.this,
                                "Location not scanned", Toast.LENGTH_LONG).show();
                        scanEquipmentBtn.setVisibility(View.GONE);
                        updateFaultReportButton.setVisibility(View.GONE);
                        requestPauseButton.setVisibility(View.GONE);
                        acceptButton.setVisibility(View.GONE);
                        rejectButton.setVisibility(View.GONE);

                    } else if (editableVariable.equals("true")) {
                        if (role.equals(Constants.ROLE_TECHNICIAN) && statusComing.equals("Open")) {
                            requestPauseButton.setVisibility(View.VISIBLE);
                            updateFaultReportButton.setVisibility(View.VISIBLE);
                            acceptButton.setVisibility(View.GONE);
                            rejectButton.setVisibility(View.GONE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                        requestPauseButton.setEnabled(true);
                                        updateFaultReportButton.setText("Update");
                                        acknowledgeEdtTxt.setVisibility(View.GONE);

                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        requestPauseButton.setEnabled(false);


                                        updateFaultReportButton.setText("Acknowledge");
                                        //   acknowledgeEdtTxt.setVisibility(View.VISIBLE);
                                    }

                                    if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                        requestPauseButton.setEnabled(true);
                                        updateFaultReportButton.setText("Update");
                                        acknowledgeEdtTxt.setVisibility(View.GONE);


                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                        requestPauseButton.setEnabled(true);
                                        updateFaultReportButton.setText("Update");
                                        acknowledgeEdtTxt.setVisibility(View.GONE);


                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause Requested")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                        requestPauseButton.setEnabled(true);
                                        updateFaultReportButton.setText("Update");
                                        acknowledgeEdtTxt.setVisibility(View.GONE);


                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Open")) {
                                        requestPauseButton.setEnabled(true);
                                        updateFaultReportButton.setText("Update");
                                        acknowledgeEdtTxt.setVisibility(View.GONE);

                                    }
                                }
                            });
                        } else if (role.equals(Constants.ROLE_TECHNICIAN) && statusComing.equals("Pause")) {
                            requestPauseButton.setVisibility(View.GONE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                        updateFaultReportButton.setText("Update");
                                        acknowledgeEdtTxt.setVisibility(View.GONE);

                                        requestPauseButton.setEnabled(true);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        requestPauseButton.setEnabled(false);

                                        updateFaultReportButton.setText("Acknowledge");
                                        //    acknowledgeEdtTxt.setVisibility(View.VISIBLE);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Open")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                        updateFaultReportButton.setText("Update");
                                        acknowledgeEdtTxt.setVisibility(View.GONE);

                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                        updateFaultReportButton.setText("Update");
                                        acknowledgeEdtTxt.setVisibility(View.GONE);

                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                        updateFaultReportButton.setText("Update");
                                        acknowledgeEdtTxt.setVisibility(View.GONE);

                                    }
                                }
                            });
                        } else if (role.equals(Constants.ROLE_TECHNICIAN) && statusComing.equals("Closed")) {
                            requestPauseButton.setVisibility(View.VISIBLE);
                            updateFaultReportButton.setVisibility(View.VISIBLE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Open")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }


                                }
                            });

                        } else if (role.equals(Constants.ROLE_TECHNICIAN) && statusComing.equals("Completed")) {
                            updateFaultReportButton.setVisibility(View.VISIBLE);
                            updateFaultReportButton.setEnabled(true);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Open")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statusComing, false);
                                    }

                                }
                            });
                        }
                    }
                    if (!(jsonObject.get("acknowledgerCode").isJsonNull())) {
                        acknowledgeEdtTxt.setVisibility(View.VISIBLE);
                        acknowledgeedittextfield.setVisibility(View.VISIBLE);
                        acknowledgeedittextfield.setText(jsonObject.get("acknowledgerCode").getAsString());

                    }

                } else if (response.code() == 401) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(EditFaultReportActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                requestPauseButton.setEnabled(false);
                Toast.makeText(EditFaultReportActivity.this, "No Data Available", Toast.LENGTH_LONG).show();
                Log.d("TAG", "onFailure:equip call " + t.getMessage());
                Log.d(TAG, "onFailure: equip call" + t.getCause());

            }
        });
    }


    private void initviewsAndGetInitialData(String data) {
        // data from frid
        progressDialog.show();
        GeoLocation geolocation = new GeoLocation(latOfSearch, longOfSearch);
        SearchResposeWithLatLon respose = new SearchResposeWithLatLon(geolocation, data);
        Call<JsonObject> call = APIClient.getUserServices().getFindOne(workSpaceid, token, role, respose);
        call.enqueue(new Callback<JsonObject>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    JsonObject jsonObject = response.body();
                    Log.d(TAG, "onResponse: json on frid search" + jsonObject);

                    if (!jsonObject.get("technicianSignature").isJsonNull()) {
                        techSignImgView.setVisibility(View.VISIBLE);
                        getTechSign(jsonObject.get("technicianSignature").getAsString());
                    }
                    if (!jsonObject.get("fmmCaseId").isJsonNull()) {
                        fmmCaseId.setText(jsonObject.get("fmmCaseId").getAsString());
                    }
                    if (!(jsonObject.get("fmm").isJsonNull())) {
                        fmmAutocompleteSpinner.setText(jsonObject.get("fmm").getAsJsonObject().get("username").getAsString().toString());
                        fmmAutocompleteSpinner.setEnabled(false);
                        fmmAutocompleteSpinner.setDropDownHeight(0);
                    }
                    if (!(jsonObject.get("acknowledgedBy").isJsonNull())) {
                        ackMainLayout.setVisibility(View.VISIBLE);
                        ackNameEt.setText(jsonObject.get("acknowledgedBy").getAsJsonObject().get("name").getAsString());
                        ackRankEt.setText(jsonObject.get("acknowledgedBy").getAsJsonObject().get("rank").getAsString());
                        getSignatureMethod(jsonObject.get("frId").getAsString());

                    }

                    if (!jsonObject.get("eotType").isJsonNull()) {
                        eot = jsonObject.get("eotType").getAsString();
                        eotTypeTextView.setText(eot);
                    }
                    String editablevariable = jsonObject.get("editable").getAsString();
                    statuscomingFrid = jsonObject.get("status").getAsString();
                    //31 dec

                    if (role.equals(Constants.ROLE_TECHNICIAN) && statuscomingFrid.equals("Open")) {
                        acceptButton.setVisibility(View.GONE);
                        rejectButton.setVisibility(View.GONE);
                        requestPauseButton.setVisibility(View.VISIBLE);
                        updateFaultReportButton.setVisibility(View.VISIBLE);
                        autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                    requestPauseButton.setEnabled(true);
                                    updateFaultReportButton.setText("Update");
                                    acknowledgeEdtTxt.setVisibility(View.GONE);
                                }
                                if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                    requestPauseButton.setEnabled(false);
                                    updateFaultReportButton.setText("Acknowledge");
                                    //   acknowledgeEdtTxt.setVisibility(View.VISIBLE);
                                }

                                if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                    requestPauseButton.setEnabled(true);
                                    updateFaultReportButton.setText("Update");
                                    acknowledgeEdtTxt.setVisibility(View.GONE);

                                }
                                if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                    requestPauseButton.setEnabled(true);
                                    updateFaultReportButton.setText("Update");
                                    acknowledgeEdtTxt.setVisibility(View.GONE);

                                }
                                if (autoCompleteSpinner.getText().toString().equals("Pause Requested")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                    requestPauseButton.setEnabled(true);
                                    updateFaultReportButton.setText("Update");
                                    acknowledgeEdtTxt.setVisibility(View.GONE);


                                }
                                if (autoCompleteSpinner.getText().toString().equals("Open")) {
                                    requestPauseButton.setEnabled(true);
                                    updateFaultReportButton.setText("Update");
                                    acknowledgeEdtTxt.setVisibility(View.GONE);

                                }
                            }
                        });
                    }


                    if (role.equals(Constants.ROLE_TECHNICIAN) && statusComing.equals("Completed")) {
                        updateFaultReportButton.setVisibility(View.VISIBLE);
                        updateFaultReportButton.setEnabled(true);
                        autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                }
                                if (autoCompleteSpinner.getText().toString().equals("Open")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                }
                                if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                }
                                if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                }

                            }
                        });
                    }
                    if (role.equals(Constants.ROLE_TECHNICIAN) && statuscomingFrid.equals("Pause")) {
                        acceptButton.setVisibility(View.GONE);
                        rejectButton.setVisibility(View.GONE);
                        requestPauseButton.setVisibility(View.GONE);
                        scanEquipmentBtn.setVisibility(View.GONE);
                        updateFaultReportButton.setVisibility(View.VISIBLE);
                        autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                    updateFaultReportButton.setText("Update");
                                    acknowledgeEdtTxt.setVisibility(View.GONE);
                                    requestPauseButton.setEnabled(true);
                                }
                                if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                    requestPauseButton.setEnabled(false);

                                    updateFaultReportButton.setText("Acknowledge");
                                    //     acknowledgeEdtTxt.setVisibility(View.VISIBLE);
                                }
                                if (autoCompleteSpinner.getText().toString().equals("Open")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                    updateFaultReportButton.setText("Update");
                                    acknowledgeEdtTxt.setVisibility(View.GONE);

                                }
                                if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                    updateFaultReportButton.setText("Update");
                                    acknowledgeEdtTxt.setVisibility(View.GONE);

                                }
                                if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                    updateFaultReportButton.setText("Update");
                                    acknowledgeEdtTxt.setVisibility(View.GONE);

                                }

                            }
                        });
                    }
                    //2feb

                    if (role.equals(Constants.ROLE_MANAGING_AGENT) && statuscomingFrid.equals("Closed")) {
                        updateFaultReportButton.setVisibility(View.GONE);
                        acceptButton.setVisibility(View.GONE);
                        rejectButton.setVisibility(View.GONE);
                        autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                }
                                if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                }
                                if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                    autoCompleteSpinner.setText(statuscomingFrid, false);
                                }

                            }
                        });

                    } else if ((role.equals(Constants.ROLE_MANAGING_AGENT) && editablevariable.equals("true"))) {
                        Log.d(TAG, "onResponse: in2");
                        if (role.equals(Constants.ROLE_MANAGING_AGENT) && statuscomingFrid.equals("Pause Requested")) {
                            acceptButton.setVisibility(View.VISIBLE);
                            autoCompleteSpinner.setDropDownWidth(0);
                            rejectButton.setVisibility(View.VISIBLE);
                            observationEditText.setEnabled(true);
                            acknowledgeedittextfield.setEnabled(false);
                            actionTakenEditText.setEnabled(true);
                            updateFaultReportButton.setVisibility(View.GONE);
                        } else if (role.equals(Constants.ROLE_MANAGING_AGENT) && statuscomingFrid.equals("Completed")) {
                            updateFaultReportButton.setVisibility(View.VISIBLE);
                            Log.d(TAG, "onResponse: in3");
                            acceptButton.setVisibility(View.GONE);
                            acknowledgeedittextfield.setEnabled(false);
                            rejectButton.setVisibility(View.GONE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statuscomingFrid, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statuscomingFrid, false);
                                    }

                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statuscomingFrid, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause Requested")) {
                                        autoCompleteSpinner.setText(statuscomingFrid, false);
                                    }

                                }
                            });

                        } else if (role.equals(Constants.ROLE_MANAGING_AGENT) && statuscomingFrid.equals("Pause")) {
                            updateFaultReportButton.setVisibility(View.GONE);
                            acceptButton.setVisibility(View.GONE);
                            acknowledgeedittextfield.setEnabled(false);
                            rejectButton.setVisibility(View.GONE);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statuscomingFrid, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statuscomingFrid, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Closed")) {
                                        autoCompleteSpinner.setText(statuscomingFrid, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statuscomingFrid, false);
                                    }

                                }
                            });

                        } else if (role.equals(Constants.ROLE_MANAGING_AGENT) && statuscomingFrid.equals("Closed")) {
                            updateFaultReportButton.setVisibility(View.GONE);
                            acceptButton.setVisibility(View.GONE);
                            rejectButton.setVisibility(View.GONE);
                            acknowledgeedittextfield.setEnabled(false);
                            autoCompleteSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    if (autoCompleteSpinner.getText().toString().equals("Select status")) {
                                        autoCompleteSpinner.setText(statuscomingFrid, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Completed")) {
                                        autoCompleteSpinner.setText(statuscomingFrid, false);
                                    }
                                    if (autoCompleteSpinner.getText().toString().equals("Pause")) {
                                        autoCompleteSpinner.setText(statuscomingFrid, false);
                                    }

                                }
                            });

                        }
                    }

                    assert jsonObject != null;

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
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                        if (!jsonObject.get("eotTime").isJsonNull()) {
                            String eottimeVale = jsonObject.get("eotTime").getAsString();
                            eotTime.setText(eottimeVale);
                        }
                    }

                    if (!(jsonObject.get("observation").isJsonNull())) {
                        observationEditText.setText(jsonObject.get("observation").getAsString());
                    }
                    if (!(jsonObject.get("actionTaken").isJsonNull())) {
                        actionTakenEditText.setText(jsonObject.get("actionTaken").getAsString());
                    }

                    if (!(jsonObject.get("status").isJsonNull())) {
                        statuscomingFrid = jsonObject.get("status").getAsString();
                        if (genralStatusList.contains(statuscomingFrid)) {
                            autoCompleteSpinner.setText(statuscomingFrid, false);
                        }
                    }
                    if (!(jsonObject.get("activationTime").isJsonNull())) {
                        activationDate.setText(new MainFragment().returnTimeString(jsonObject.get("activationTime").getAsString()));
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
                    if (!(jsonObject.get("arrivalTime").isJsonNull())) {
                        activationTime.setText(new MainFragment().returnTimeString(jsonObject.get("arrivalTime").getAsString()));
                    }
                    if (jsonObject.get("remarks") != null) {
                        for (int i = 0; i < jsonObject.get("remarks").getAsJsonArray().size(); i++) {
                            if (!jsonObject.get("remarks").getAsJsonArray().get(i).isJsonNull()) {
                                String remString = jsonObject.get("remarks").getAsJsonArray()
                                        .get(i).getAsString();
                                listRemarksNo = i + 1;
                                mLayout.addView(createNewEditText(remString, i));
                            }
                        }
                    }
                    if (!(jsonObject.get("acknowledgerCode").isJsonNull())) {
                        acknowledgeEdtTxt.setVisibility(View.VISIBLE);
                        acknowledgeedittextfield.setVisibility(View.VISIBLE);
                        acknowledgeedittextfield.setText(jsonObject.get("acknowledgerCode").getAsString());
                    }

                } else if (response.code() == 401) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(EditFaultReportActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(EditFaultReportActivity.this, "No Data Available", Toast.LENGTH_SHORT).show();
                Log.d("TAG", "onFailure: init frid " + t.getMessage());
                Log.d(TAG, "onFailure: init frid" + t.getCause());
            }
        });
    }

    private void deleteRemarks(View view) {
        if (!editTextList.isEmpty()) {
            if (mLayout.getChildCount() > 1) {
                if (autoCompleteSpinner.getText().equals("Closed")) {
                    if (mLayout.getChildCount() > 2) {
                        mLayout.removeViewAt(mLayout.getChildCount() - 1);
                        int index = editTextList.size() - 1;
                        editTextList.remove(index);
                    }
                } else {
                    mLayout.removeViewAt(mLayout.getChildCount() - 1);
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
        editTextRemarks = new TextInputEditText(this);
        editTextRemarks.setId(remarksId);
        editTextRemarks.setBackgroundResource(R.drawable.mybutton);
        editTextRemarks.setText(remarksString);
        editTextRemarks.setLayoutParams(lparams);
        editTextRemarks.setPadding(35, 30, 30, 30);
        editTextRemarks.setHint("   add remarks");
        editTextRemarks.setMinHeight(60);
        editTextRemarks.setMaxWidth(mLayout.getWidth());
        editTextList.add(editTextRemarks);
        return editTextRemarks;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            }
        }
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

    private void checkFieldsForEmptyValues() {
        if (role.equals(Constants.ROLE_TECHNICIAN) && autoCompleteSpinner.getText().toString().equals("Completed")) {
            requestPauseButton.setEnabled(false);
        }
    }

    public void updateFaultReport(String encodedStringBuilder, String name, String rank, String techSignString) {
        String username = fmmAutocompleteSpinner.getText().toString();
        int id = 0;
        for (int i = 0; i < listFmm.size(); i++) {
            if (listFmm.get(i).getUsername().equals(username)) {
                id = listFmm.get(i).getId();
            }
        }
        fmm fmm = new fmm(username, id);
        String acknowledgeText = acknowledgeedittextfield.getText().toString();
        String contactNumber = requestorNumberEditText.getText().toString();
        if (username.isEmpty()) {
            Toast.makeText(this, "Please Select FMM", Toast.LENGTH_SHORT).show();
        } else if ((TextUtils.isEmpty(requestorNumberEditText.getText())) || ((contactNumber.length() < 8))) {
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
            faultDetailString = faultDetailsEditText.getText().toString();
            String observerString = observationEditText.getText().toString();
            String actionTakenString = actionTakenEditText.getText().toString();
            String statusString = autoCompleteSpinner.getText().toString();
            String diagnosesString = null;
            if (!editTextList.isEmpty()) {
                for (int iRem = 0; iRem < editTextList.size(); iRem++) {
                    String remarks1String = editTextList.get(iRem).getText().toString();
                    if (!remarks1String.trim().isEmpty()) {
                        remarksList.add(remarks1String);
                    }
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
            if (name != null && rank != null && encodedStringBuilder != null) {
                acknowledgedBy = new AcknowledgedBy(name, rank, encodedStringBuilder);
            }


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
                        Toast.makeText(EditFaultReportActivity.this, "Fault Report Updated", Toast.LENGTH_LONG).show();
                        goToSeach();
                    } else if (response.code() == 401) {
                        Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                        LogoutClass logoutClass = new LogoutClass();
                        logoutClass.logout(EditFaultReportActivity.this);
                    } else if (response.code() == 500) {
                        Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(EditFaultReportActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 500) {
                        AlertDialog.Builder emptyDailog = new AlertDialog.Builder(EditFaultReportActivity.this);
                        emptyDailog.setTitle("Error: " + response.code() + ". Please fill all the required fields!");
                        emptyDailog.setIcon(R.drawable.ic_error);
                        emptyDailog.setPositiveButton("Ok", null);
                        emptyDailog.show();
                    } else {
                        AlertDialog.Builder dailog = new AlertDialog.Builder(EditFaultReportActivity.this);
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
                    Toast.makeText(EditFaultReportActivity.this, "Failed to update" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            });
        }

    }


    private void initializeFab() {

        fab_before.setAlpha(0f);
        fab_after.setAlpha(0f);

        fabPurchaseOrder.setAlpha(0f);
        fabQuoteUpload.setAlpha(0f);

        fabQuoteUpload.setTranslationY(translationY);
        fabPurchaseOrder.setTranslationY(translationY);

        fab_before.setTranslationY(translationY);
        fab_after.setTranslationY(translationY);

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
        fabPurchaseOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultReportActivity.this, ViewPurchaseOnly.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "Before");
                intent.putExtra("checkForFrid", fridFromFrDetail);
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
                Intent intent = new Intent(EditFaultReportActivity.this, UploadFile.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "Before");
                intent.putExtra("checkForFrid", fridFromFrDetail);
                intent.putExtra("role", role);
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("frId", frIdEditText.getText().toString());
                intent.putExtra("status", autoCompleteSpinner.getText().toString());
                startActivity(intent);


            }
        });


        fab_before.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultReportActivity.this, ImagesActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "Before");
                intent.putExtra("role", role);
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("fromFrDetail", "EditFaultPAge");
                intent.putExtra("frId", frIdEditText.getText().toString());
                intent.putExtra("status", autoCompleteSpinner.getText().toString());
                startActivity(intent);


            }
        });
        fab_after.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditFaultReportActivity.this, ImagesActivity.class);
                intent.putExtra("token", token);
                intent.putExtra("value", "After");
                intent.putExtra("workspace", workSpaceid);
                intent.putExtra("role", role);
                intent.putExtra("fromFrDetail", "EditFaultPAge");
                intent.putExtra("status", autoCompleteSpinner.getText().toString());
                intent.putExtra("frId", frIdEditText.getText().toString());
                startActivity(intent);


            }
        });
    }

    private void openMenu() {
        isMenuOpen = !isMenuOpen;
        fab_main.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fab_before.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fab_after.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

    }

    private void openQuote() {
        isMenuOpenQuote = !isMenuOpenQuote;
        fabQuoteMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fabPurchaseOrder.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabQuoteUpload.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closeQuote() {
        isMenuOpenQuote = !isMenuOpenQuote;
        fabQuoteMain.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fabQuoteUpload.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
        fabPurchaseOrder.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();

    }

    private void closeMenu() {
        isMenuOpen = !isMenuOpen;
        fab_main.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        fab_before.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
        fab_after.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();

    }

    private String prependZero(String text) {
        if (text.length() == 1) {
            return '0' + text;
        }
        return text;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(EditFaultReportActivity.this, Search.class);
            intent.putExtra("workspace", workSpaceid);
            Log.d(TAG, "onBackPressed: " + workSpaceid);
            startActivity(intent);
            finish();
        }
    }

    public void goToSeach() {
        Intent intent = new Intent(EditFaultReportActivity.this, Search.class);
        intent.putExtra("workspace", workSpaceid);
        startActivity(intent);
        finish();
    }

    private void uploadpdfAuthorizationDialog() {
        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(EditFaultReportActivity.this);
        final View dialogView = getLayoutInflater().inflate(R.layout.custom_dialog_accept_requestmethod, null);
        getpdfAuth = dialogView.findViewById(R.id.getpdfautherozation);
        final ImageView crossImg = dialogView.findViewById(R.id.crosscancel);
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
                getpdfAuth.setOnClickListener(new View.OnClickListener() {
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

    private void getSignatureMethod(String frid) {

        Call<ResponseBody> call = APIClient.getUserServices().getSignatureAckFr(frid, workSpaceid, token, role);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                   /* byte[] decodedString = Base64.decode(response.body(), Base64.URL_SAFE );
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    ackImageViewSign.setImageBitmap(decodedByte);*/
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

    private void getTechSign(String frId) {
        Call<ResponseBody> call = APIClient.getUserServices().getTechnicianSign(frId, workSpaceid, token, role);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    techSignImgView.setImageBitmap(bmp);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


}