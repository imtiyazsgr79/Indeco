package com.synergyyy.EquipmentSearch;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.kyanogen.signatureview.SignatureView;
import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;
import com.synergyyy.General.LogoutClass;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;
import com.synergyyy.Search.AutoCompleteFmmAdapter;
import com.synergyyy.Search.EditFaultOnSearchActivity;
import com.synergyyy.Search.fmm;
import com.synergyyy.SearchTasks.SearchTaskActivity;
import com.synergyyy.SearchTasks.TaskSearchActivity;
import com.synergyyy.SearchTasks.UploadImagesTaskActivity;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class PmTaskActivity extends MyBaseActivity implements DatePickerDialog.OnDateSetListener {

    private TextInputEditText taskNumberTextView;
    private TextInputEditText scheduleNumberTextView;
    private TextInputEditText buildingNameTextView;
    private TextInputEditText locationNameTextView;
    private TextInputEditText equipmentNameTextView;
    private TextInputEditText briefDescTextView;
    private TextInputEditText acknowledgedBy;
    private TextInputEditText rankAckEd;
    private TextInputLayout ackTaskTV;
    private TextView tv_sign;
    private LinearLayout ackTaskLL;
    private TextInputLayout rankAckTV;
    private ImageView signAckPmTask;
    private TextInputEditText scheduleDateTextView;
    private TextInputEditText nameTextView;
    private AutoCompleteTextView statusSpinner;
    private Button buttonUpdate;
    private TextInputEditText datePickerEdit;
    private final OvershootInterpolator interpolator = new OvershootInterpolator();
    private final Float translationY = 600f;
    private TextInputEditText timePickerEdit;
    private int tHour, tMinute;
    private final List<TextInputEditText> editTextList = new ArrayList<TextInputEditText>();
    private ProgressDialog mProgress;
    private ArrayAdapter<String> statusSpinnerAdapter;
    private final List<String> statusList = new ArrayList<>();
    private ProgressDialog updateProgress;
    private String roleTask, statusComing;
    private int taskId, imageBeforeId, imageAfterId;
    private LinearLayout mLayout;
    private String workspace;
    private Long scheduleDate;
    private String afterImage, beforeImage;
    private String source = null;
    private FloatingActionButton moreFab, imageUploadFabBefore, checkListFab, imageUploadFabAfter;
    private boolean isOpen;
    private String authNameBefore, authContactBefore;
    private String authNameAfter, authContactAfter;
    private String currentDateTimeString;
    private SimpleDateFormat sdf1;
    private MaterialButton plusBtn, deleteBtn;
    private ImageView techSignImgView;
    private TextView techSignTv;
    private AutoCompleteTextView fmmAutocompleteSpinnerPm;
    private List<fmm> listFmm;
    private TextInputLayout fmmautopmtsklayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_pm_task, null, false);
        drawer.addView(viewLayout, 0);
        activityNameTv.setText("Pm Task");
        setSupportActionBar(toolbar);
        fmmautopmtsklayout = findViewById(R.id.fmmautopmtsklayout);
        fmmAutocompleteSpinnerPm = findViewById(R.id.fmmAutocompleteSpinner_xml_pm);
        techSignTv = findViewById(R.id.techSignTv);
        techSignImgView = findViewById(R.id.techSignIView);
        taskNumberTextView = findViewById(R.id.textViewTaskNumberPm);
        scheduleNumberTextView = findViewById(R.id.textViewScheduleNumberPm);
        scheduleNumberTextView.setMovementMethod(new ScrollingMovementMethod());
        nameTextView = findViewById(R.id.namePmTasks);
        mLayout = findViewById(R.id.layout_remarksTasks);

        plusBtn = findViewById(R.id.plusButton);
        deleteBtn = findViewById(R.id.deleteButton);
        buildingNameTextView = findViewById(R.id.textViewBuildingNumberPm);
        locationNameTextView = findViewById(R.id.textViewLocationNumberPm);
        equipmentNameTextView = findViewById(R.id.textViewEquipmentPm);
        briefDescTextView = findViewById(R.id.textViewBriefDescriptionPm);
        scheduleDateTextView = findViewById(R.id.textViewScheduleDatePm);
        datePickerEdit = findViewById(R.id.date_picker_pmtasksPm);
        timePickerEdit = findViewById(R.id.time_picker_pmtasks);
        buttonUpdate = findViewById(R.id.buttonUpdateTaskPm);
        acknowledgedBy = findViewById(R.id.acknowledgeByTask);
        ackTaskTV = findViewById(R.id.acknowledgeByTaskTV);
        tv_sign = findViewById(R.id.tv_sign);
        ackTaskLL = findViewById(R.id.ackTaskLL);
        rankAckEd = findViewById(R.id.rankEDPmTask);
        rankAckTV = findViewById(R.id.rankTVPmTask);
        signAckPmTask = findViewById(R.id.signImagePmTask);
        buttonUpdate.setEnabled(false);
        statusSpinner = findViewById(R.id.spinner_status_pmtasks);
        moreFab = findViewById(R.id.more_pmtask);
        checkListFab = findViewById(R.id.checklist_pmtask);
        imageUploadFabBefore = findViewById(R.id.image_pmtaskbefore);
        imageUploadFabAfter = findViewById(R.id.image_pmtaskafter);

        isOpen = false;

        checkListFab.setAlpha(0f);
        imageUploadFabBefore.setAlpha(0f);
        imageUploadFabAfter.setAlpha(0f);
        checkListFab.setTranslationY(translationY);
        imageUploadFabBefore.setTranslationY(translationY);
        imageUploadFabAfter.setTranslationY(translationY);


        taskNumberTextView.setEnabled(false);
        scheduleNumberTextView.setEnabled(false);
        buildingNameTextView.setEnabled(false);
        locationNameTextView.setEnabled(false);
        equipmentNameTextView.setEnabled(false);
        briefDescTextView.setEnabled(false);
        scheduleDateTextView.setEnabled(false);
        datePickerEdit.setEnabled(false);
        timePickerEdit.setEnabled(false);
        nameTextView.setEnabled(false);


        mProgress = new ProgressDialog(PmTaskActivity.this);
        mProgress.setMessage("Please wait...");
        mProgress.setCancelable(false);
        mProgress.setIndeterminate(true);

        updateProgress = new ProgressDialog(this);
        updateProgress.setTitle("Updating...");
        updateProgress.setCancelable(false);
        updateProgress.setIndeterminate(true);

        statusSpinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, statusList);
        statusSpinner.setAdapter(statusSpinnerAdapter);

        Intent intent = getIntent();
        String taskNumberString = intent.getStringExtra("taskNumber");
        taskId = intent.getIntExtra("taskId", 0);
        workspace = intent.getStringExtra("workspace");
        source = intent.getStringExtra("source");
        String msgId = intent.getStringExtra("msgId");
        if (msgId != null) {
            EditFaultOnSearchActivity editFaultOnSearchActivity = new EditFaultOnSearchActivity();
            editFaultOnSearchActivity.callReadMessage(msgId);
        }

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        roleTask = sharedPreferences.getString("role", "");

        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        currentDateTimeString = sdf.format(d);
        sdf1 = new SimpleDateFormat("dd-MM-yyyy");
        getPmTask(taskNumberString, taskId, token);
        getAllFmmList();

        if (role.equals(Constants.ROLE_MANAGING_AGENT)) {
            fmmAutocompleteSpinnerPm.setVisibility(View.GONE);
            fmmautopmtsklayout.setVisibility(View.GONE);

        }
        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((TextUtils.isEmpty(fmmAutocompleteSpinnerPm.getText()))) {
                    Toast.makeText(PmTaskActivity.this, "Please Select FMM", Toast.LENGTH_SHORT).show();

                } else if (!statusSpinner.getText().toString().equals("Open") && !(TextUtils.isEmpty(fmmAutocompleteSpinnerPm.getText()))) {
                    createAlertDialog();
                } else
                    Toast.makeText(PmTaskActivity.this, "Please update the status!", Toast.LENGTH_SHORT).show();
            }
        });

        moreFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen) {
                    closeMenu();
                } else {
                    openMenu();
                }
            }
        });

        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLayout.getChildCount() < 11) {
                    int remarksId = 0;
                    remarksId++;
                    mLayout.addView(createNewEditText("", remarksId));
                }
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteRemarks(view);
            }
        });

        nameTextView.setText(username.toLowerCase());
        if (role.equals(Constants.Coordinator)) {
            deleteBtn.setEnabled(false);
            plusBtn.setEnabled(false);
            buttonUpdate.setVisibility(View.GONE);
        }
        if (source.equals("scan")) {
            checkListFab.setVisibility(View.GONE);
        }
    }

    private void deleteRemarks(View view) {
        if (!editTextList.isEmpty()) {
            if (mLayout.getChildCount() > 0) {
                mLayout.removeViewAt(mLayout.getChildCount() - 1);
                int index = editTextList.size() - 1;
                editTextList.remove(index);

            }
        }
    }


    private void updateTaskMethod(int taskId, String token, String workspace, String base64String,
                                  String authName, String authRank, String tectSignString) {

        String username = fmmAutocompleteSpinnerPm.getText().toString();
        int id = 0;
        for (int i = 0; i < listFmm.size(); i++) {
            if (listFmm.get(i).getUsername().equals(username)) {
                id = listFmm.get(i).getId();
            }
        }
        fmm fmm = new fmm(username, id);

        String timeString = timePickerEdit.getText().toString();
        String dateString = datePickerEdit.getText().toString();
        String status = statusSpinner.getText().toString();
        String remString = null;
        List<String> remarksList = new ArrayList<>();

        for (int i = 0; i < editTextList.size(); i++) {
            remString = editTextList.get(i).getText().toString();
            remarksList.add(remString);
        }

        Acknowledger acknowledger = null;
        if (statusSpinner.getText().toString().equals("Closed")) {
            acknowledger = new Acknowledger(authRank, base64String, authName);
        }

        String dateTimeString = dateString + timeString;

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyyHH:mm");
        Date date = null;
        try {
            date = (Date) formatter.parse(dateTimeString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Long completedDateTime = null;
        if (statusSpinner.getText().toString().equals("Closed")) {
            completedDateTime = date.getTime();
        }
        GetUpdatePmTaskRequest getUpdatePmTaskRequest = new GetUpdatePmTaskRequest(tectSignString, status, remarksList, completedDateTime,
                completedDateTime, taskId, acknowledger, fmm);

        updatePmTaskService(getUpdatePmTaskRequest, token, workspace);

    }

    private void createAlertDialog() {

        final androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        final View dialogView = getLayoutInflater().inflate(R.layout.ack_dialog_tasks, null);
        final TextInputLayout nameTVAck = dialogView.findViewById(R.id.nricname_layout);
        final TextInputLayout rankTVAck = dialogView.findViewById(R.id.rank_alert_b0x);
        final TextView signTVAck = dialogView.findViewById(R.id.signtv);
        final SignatureView signatureView = dialogView.findViewById(R.id.signatureEditT);
        final SignatureView techTaskSignView = dialogView.findViewById(R.id.techSignTAskSignatureView);
        final TextView techSignTv = dialogView.findViewById(R.id.techSignTAskTv);
        final TextInputEditText nameEt = dialogView.findViewById(R.id.nameauth);
        final TextInputEditText rankEt = dialogView.findViewById(R.id.rank_alert_b0x_edittext);

        builder.setTitle("Acknowledge Update")
                .setView(dialogView)
                .setCancelable(false)
                .setIcon(R.drawable.ic_error)
                .setPositiveButton("Acknowledge", null)
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
                        String name = null;
                        String rankString = null;


                        if (!TextUtils.isEmpty(nameEt.getText())
                                && !TextUtils.isEmpty(rankEt.getText()) && !(signatureView.isBitmapEmpty())
                                && !techTaskSignView.isBitmapEmpty()) {
                            name = nameEt.getText().toString();
                            rankString = rankEt.getText().toString();

                            String base64Sign = convertSignMethod(signatureView);
                            String tectSignString = convertSignMethod(techTaskSignView);
                            updateTaskMethod(taskId, token, workspace, base64Sign, name, rankString, tectSignString);
                            alert.dismiss();
                        } else if (signatureView.isBitmapEmpty()) {
                            signTVAck.setText(R.string.pleaseSignHere);
                            signTVAck.setTextColor(Color.parseColor("#FA0C0C"));
                        } else if (TextUtils.isEmpty(nameEt.getText())) {
                            nameTVAck.setError("Required");
                        } else if (TextUtils.isEmpty(rankEt.getText())) {
                            rankTVAck.setError("Required");
                        } else if (techTaskSignView.isBitmapEmpty()) {
                            techSignTv.setText(R.string.pleaseSignHere);
                            techSignTv.setTextColor(Color.parseColor("#FA0C0C"));
                        }
                    }
                });
            }
        });
        alert.show();

    }

    private String convertSignMethod(SignatureView signatureView) {

        String encodedString = null;

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            signatureView.getSignatureBitmap().compress(Bitmap.CompressFormat.PNG, 50, baos); // bm is the bitmap object
            byte[] imageBytes = baos.toByteArray();

            encodedString = Base64.encodeToString(imageBytes, Base64.DEFAULT);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodedString;
    }

    private void getPmTask(String taskNumberString, int taskId, String token) {
        mProgress.show();
        taskNumberTextView.setText(taskNumberString);

        Call<GetPmTaskItemsResponse> callPmTask = APIClient.getUserServices().getCallPmTask(String.valueOf(taskId), token, role);
        callPmTask.enqueue(new Callback<GetPmTaskItemsResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<GetPmTaskItemsResponse> call, Response<GetPmTaskItemsResponse> response) {
                mProgress.dismiss();
                if (response.code() == 200) {
                    GetPmTaskItemsResponse getPmTaskItemsResponse = response.body();

                    if (getPmTaskItemsResponse.getFmm() != null) {
                        fmmAutocompleteSpinnerPm.setText(getPmTaskItemsResponse.getFmm().getUsername());
                        fmmAutocompleteSpinnerPm.setDropDownHeight(0);
                        fmmAutocompleteSpinnerPm.setEnabled(false);

                    }
                    if (getPmTaskItemsResponse.getTaskNumber() != null) {
                        taskNumberTextView.setText(getPmTaskItemsResponse.getTaskNumber());
                    }
                    if (getPmTaskItemsResponse.getSchedule() != null) {
                        scheduleNumberTextView.setText(getPmTaskItemsResponse.getSchedule().getScheduleNumber());
                    }
                    if (getPmTaskItemsResponse.getSchedule().getBriefDescription() != null) {
                        briefDescTextView.setText(getPmTaskItemsResponse.getSchedule().getBriefDescription());
                    }
                    if (getPmTaskItemsResponse.getEquipment().getBuilding() != null) {
                        buildingNameTextView.setText(getPmTaskItemsResponse.getEquipment().getBuilding().getName());
                    }
                    if (getPmTaskItemsResponse.getStatus() != null) {
                        statusComing = getPmTaskItemsResponse.getStatus();
                        statusList.add(getPmTaskItemsResponse.getStatus());
                        if (getPmTaskItemsResponse.getStatus().equals("Open")) {
                            statusList.add("Closed");
                        } else statusList.add("Open");

                        if (statusComing.equalsIgnoreCase("Closed")) {
                            buttonUpdate.setVisibility(View.GONE);
                            statusSpinner.setText(Constants.CLOSED_STATUS, true);
                            plusBtn.setEnabled(false);
                            deleteBtn.setEnabled(false);
                            ackTaskTV.setVisibility(View.VISIBLE);
                            acknowledgedBy.setVisibility(View.VISIBLE);
                            rankAckEd.setVisibility(View.VISIBLE);
                            rankAckTV.setVisibility(View.VISIBLE);
                            ackTaskLL.setVisibility(View.VISIBLE);
                            tv_sign.setVisibility(View.VISIBLE);
                            signAckPmTask.setVisibility(View.VISIBLE);
                        }

                        TreeSet<String> seen = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
                        statusList.removeIf(s -> !seen.add(s));

                        statusSpinner.setAdapter(statusSpinnerAdapter);
                        statusSpinner.setText(getPmTaskItemsResponse.getStatus(), false);
                    }
                    if (getPmTaskItemsResponse.getCompletedDate() != null && getPmTaskItemsResponse.getCompletedTime() != null) {
                        datePickerEdit.setText(getDate(getPmTaskItemsResponse.getCompletedDate(), "dd-MM-yyyy"));
                        timePickerEdit.setText(getPmTaskItemsResponse.getCompletedTime());
                    } else {
                        timePickerEdit.setEnabled(true);
                        datePickerEdit.setEnabled(true);
                        timePickerEdit.setText(currentDateTimeString);
                        datePickerEdit.setText(sdf1.format(new Date()));
                        if (statusComing.equalsIgnoreCase("Open")) {
                            getTimeAndDatePicker();
                        }
                    }

                    if (getPmTaskItemsResponse.getEquipment() != null) {
                        equipmentNameTextView.setText(getPmTaskItemsResponse.getEquipment().getName());
                    }
                    if (getPmTaskItemsResponse.getEquipment().getLocation() != null) {
                        locationNameTextView.setText(getPmTaskItemsResponse.getEquipment().getLocation().getName());
                    }
                    if (getPmTaskItemsResponse.getScheduleDate() != 0) {
                        scheduleDate = getPmTaskItemsResponse.getScheduleDate();
                        String currentSchedule = getDate(getPmTaskItemsResponse.getScheduleDate(), "dd-MM-yyyy");
                        scheduleDateTextView.setText(currentSchedule);
                    }

                    if (getPmTaskItemsResponse.getBeforeImage() != null) {
                        beforeImage = getPmTaskItemsResponse.getBeforeImage().getImage();
                        imageBeforeId = getPmTaskItemsResponse.getBeforeImage().getId();
                        authNameBefore = getPmTaskItemsResponse.getBeforeImage().getName();
                        authContactBefore = getPmTaskItemsResponse.getBeforeImage().getContactNo();
                    }
                    if (getPmTaskItemsResponse.getAfterImage() != null) {
                        afterImage = getPmTaskItemsResponse.getAfterImage().getImage();
                        imageAfterId = getPmTaskItemsResponse.getAfterImage().getId();
                        authNameAfter = getPmTaskItemsResponse.getAfterImage().getName();
                        authContactAfter = getPmTaskItemsResponse.getAfterImage().getContactNo();
                    }
                    if (getPmTaskItemsResponse.getTech_signature() != null) {
                        techSignImgView.setVisibility(View.VISIBLE);
                        techSignTv.setVisibility(View.VISIBLE);
                        getTechSign(getPmTaskItemsResponse.getTech_signature());
                    }

                    if (getPmTaskItemsResponse.getAcknowledger() != null) {
                        if (getPmTaskItemsResponse.getAcknowledger().getName() != null) {
                            acknowledgedBy.setText(getPmTaskItemsResponse.getAcknowledger().getName());
                        }
                        if (getPmTaskItemsResponse.getAcknowledger().getRank() != null) {
                            rankAckEd.setText(getPmTaskItemsResponse.getAcknowledger().getRank());
                        }
                        if (getPmTaskItemsResponse.getAcknowledger().getSignature() != null) {
                            retrieveSignatureMethod(getPmTaskItemsResponse.getAcknowledger().getSignature());
                        }
                    }

                    if (getPmTaskItemsResponse.getRemarks() != null) {
                        for (int i = 0; i < getPmTaskItemsResponse.getRemarks().size(); i++) {
                            mLayout.addView(createNewEditText(getPmTaskItemsResponse.getRemarks().get(i).toString(), i));
                        }
                    }

                    if (getPmTaskItemsResponse.getCompletedBy() != null) {
                        nameTextView.setText(getPmTaskItemsResponse.getCompletedBy());
                    }
                    if (source.equals("scan")) {
                        buttonUpdate.setEnabled(true);
                    }

                    if (role.equals(Constants.ROLE_TECHNICIAN) && statusComing.equals("Open")) {
                        buttonUpdate.setVisibility(View.GONE);
                        statusSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                if (statusSpinner.getText().toString().equals("Closed")) {
                                    buttonUpdate.setText(Constants.ACKNOWLEDGE_BUTTON);
                                    buttonUpdate.setVisibility(View.VISIBLE);
                                } else if (statusSpinner.getText().toString().equals("Open")) {
                                    buttonUpdate.setText("Update");
                                    buttonUpdate.setVisibility(View.GONE);
                                }
                            }
                        });
                    } else if (role.equals(Constants.ROLE_MANAGING_AGENT) && statusComing.equals("Completed")) {
                        buttonUpdate.setEnabled(false);
                        statusSpinner.setEnabled(false);
                    }

                } else if (response.code() == 401) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(PmTaskActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(PmTaskActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetPmTaskItemsResponse> call, Throwable t) {
                mProgress.dismiss();
                LogoutClass logoutClass = new LogoutClass();
                logoutClass.alertDialog("Error: " + t.getLocalizedMessage(), PmTaskActivity.this);
            }
        });
    }

    private void getTechSign(String tech_signature) {

        Call<ResponseBody> call = APIClient.getUserServices().getTechSignTask(tech_signature, workspace, token, role);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    techSignImgView.setImageBitmap(bitmap);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void retrieveSignatureMethod(String signatureName) {

        ProgressDialog pr = new ProgressDialog(this);
        pr.setIndeterminate(true);
        pr.setMessage("Loading Acknowledger Information");
        pr.setCancelable(true);
        pr.show();

        Call<ResponseBody> getSignatureCall = APIClient.getUserServices().getSignature(signatureName, role, token, workspace);
        getSignatureCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    signAckPmTask.setImageBitmap(bitmap);
                } else if (response.code() == 401) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 500) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(PmTaskActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();

                pr.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(PmTaskActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                pr.dismiss();
            }
        });

    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (DatePickerDialog.OnDateSetListener) this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DATE)
        );
        if (scheduleDate != null) {
            datePickerDialog.getDatePicker().setMinDate(scheduleDate);
        }
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        Date date1 = null;
        String date = i2 + "-" + (i1 + 1) + "-" + i;
        try {
            date1 = new SimpleDateFormat("dd-MM-yyyy").parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        datePickerEdit.setText(sdf1.format(date1));
    }

    private void getTimeAndDatePicker() {
        timePickerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                TimePickerDialog timePickerDialog = new TimePickerDialog(
                        PmTaskActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                                tHour = i;
                                tMinute = i1;

                                Calendar calendar = Calendar.getInstance();
                                calendar.set(0, 0, 0, tHour, tMinute);

                                timePickerEdit.setText(DateFormat.format("HH:mm:ss", calendar));
                            }
                        }, 12, 0, true
                );
                timePickerDialog.updateTime(tHour, tMinute);
                timePickerDialog.show();
            }
        });

        datePickerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();

            }
        });

    }

    private void updatePmTaskService(GetUpdatePmTaskRequest getUpdatePmTaskRequest, String
            token, String workspace) {
        updateProgress.show();

        Call<GetUpdatePmTaskResponse> callTaskUpdate = APIClient.getUserServices().postPmTaskUpdate(getUpdatePmTaskRequest, token, roleTask, workspace);
        callTaskUpdate.enqueue(new Callback<GetUpdatePmTaskResponse>() {
            @Override
            public void onResponse(Call<GetUpdatePmTaskResponse> call, Response<GetUpdatePmTaskResponse> response) {
                updateProgress.dismiss();
                if (response.code() == 200) {
                    Intent setIntent = new Intent(getApplicationContext(), CheckListActivity.class);
                    setIntent.putExtra("taskId", taskId);
                    setIntent.putExtra("source", source);
                    setIntent.putExtra("value", "Task");
                    startActivity(setIntent);
                    finish();
                   /* Toast.makeText(PmTaskActivity.this, "Task Updated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), TaskSearchActivity.class);
                    intent.putExtra("workspace", workspace);
                    intent.putExtra("value", "Task");
                    startActivity(intent);
                    finish();*/
                } else if (response.code() == 401) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(PmTaskActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(PmTaskActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(PmTaskActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GetUpdatePmTaskResponse> call, Throwable t) {
                Toast.makeText(PmTaskActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                updateProgress.dismiss();
            }
        });
    }

    private void openMenu() {
        isOpen = !isOpen;
        moreFab.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        imageUploadFabBefore.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        imageUploadFabAfter.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        checkListFab.animate().translationY(0f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();

        imageUploadFabBefore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadImagesTaskActivity.class);
                intent.putExtra("workspace", workspace);
                intent.putExtra("taskNumber", taskNumberTextView.getText().toString());
                intent.putExtra("taskId", String.valueOf(taskId));
                intent.putExtra("afterImage", afterImage);
                intent.putExtra("beforeImage", beforeImage);
                intent.putExtra("imageId", imageBeforeId);
                intent.putExtra("imageName", beforeImage);
                intent.putExtra("authName", authNameBefore);
                intent.putExtra("source", source);
                intent.putExtra("authContact", authContactBefore);
                intent.putExtra("sourceValue", "Before");
                startActivity(intent);
                finish();
            }
        });

        imageUploadFabAfter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadImagesTaskActivity.class);
                intent.putExtra("workspace", workspace);
                intent.putExtra("taskNumber", taskNumberTextView.getText().toString());
                intent.putExtra("taskId", String.valueOf(taskId));
                intent.putExtra("afterImage", afterImage);
                intent.putExtra("beforeImage", beforeImage);
                intent.putExtra("imageId", imageAfterId);
                intent.putExtra("imageName", afterImage);
                intent.putExtra("source", source);
                intent.putExtra("authName", authNameAfter);
                intent.putExtra("authContact", authContactAfter);
                intent.putExtra("sourceValue", "After");
                startActivity(intent);
                finish();
            }
        });
        checkListFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setIntent = new Intent(getApplicationContext(), CheckListActivity.class);
                setIntent.putExtra("taskId", taskId);
                setIntent.putExtra("source", source);
                startActivity(setIntent);
            }
        });
    }

    private void closeMenu() {
        isOpen = !isOpen;
        moreFab.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();
        imageUploadFabBefore.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
        imageUploadFabAfter.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
        checkListFab.animate().translationY(translationY).setInterpolator(interpolator).setDuration(300).start();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (role.equals(Constants.ROLE_TECHNICIAN)) {
            if (source.equals("search")) {
                Intent intent = new Intent(getApplicationContext(), TaskSearchActivity.class);
                intent.putExtra("workspace", workspace);
                startActivity(intent);
            } else if (source.equals("scan")) {
                Intent intent = new Intent(getApplicationContext(), EquipmentScanActivity.class);
                intent.putExtra("workspace", workspace);
                intent.putExtra("value", "task");
                startActivity(intent);
            }
        } else if (role.equals(Constants.ROLE_MANAGING_AGENT)) {
            Intent intent = new Intent(getApplicationContext(), SearchTaskActivity.class);
            intent.putExtra("workspace", workspace);
            startActivity(intent);
        }
        finish();
    }

    public String returnTimeStringPmTask(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateTime = sdf.format(date);
        return dateTime;
    }

    public String getDate(long milliSeconds, String dateFormat) {
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String millsToLocalDateTime(long millis) {
        Instant instant = Instant.ofEpochMilli(millis);
        LocalDateTime date = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
        return String.valueOf(date);
    }

    @NotNull
    private TextView createNewEditText(String remarksString, int remarksId) {
        LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(LinearLayout
                .LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lparams.setMargins(20, 8, 20, 8);
        TextInputEditText editTextRemarks = new TextInputEditText(this);
        editTextRemarks.setId(remarksId);
        editTextRemarks.setBackgroundResource(R.drawable.mybutton);
        editTextRemarks.setText(remarksString);
        editTextRemarks.setLayoutParams(lparams);
        editTextRemarks.setPadding(35, 40, 30, 40);
        editTextRemarks.setHint("Add remarks");
        if (statusComing.equals(Constants.CLOSED_STATUS) || statusComing.equals("CLOSED")) {
            editTextRemarks.setEnabled(false);
        }
        editTextRemarks.setMinHeight(60);
        editTextRemarks.setMaxWidth(mLayout.getWidth());
        editTextList.add(editTextRemarks);
        return editTextRemarks;
    }

    private void getAllFmmList() {
        Call<List<fmm>> call = APIClient.getUserServices().getAllFmmPMTask(token, workspace);
        call.enqueue(new Callback<List<fmm>>() {
            @Override
            public void onResponse(Call<List<fmm>> call, Response<List<fmm>> response) {
                if (response.code() == 200) {
                    listFmm = response.body();
                    AutoCompleteFmmAdapter adapterm = new AutoCompleteFmmAdapter(PmTaskActivity.this, listFmm);
                    fmmAutocompleteSpinnerPm.setAdapter(adapterm);
                } else
                    Toast.makeText(PmTaskActivity.this, "Failed :" + response.code(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<fmm>> call, Throwable t) {
                Toast.makeText(PmTaskActivity.this, "Fmm to load Fmm list", Toast.LENGTH_SHORT).show();
            }
        });

    }

}