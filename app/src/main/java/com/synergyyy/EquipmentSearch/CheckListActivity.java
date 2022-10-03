package com.synergyyy.EquipmentSearch;

import androidx.annotation.RequiresApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;
import com.synergyyy.SearchTasks.GetCheckListResponse;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;
import com.synergyyy.SearchTasks.TaskSearchActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class CheckListActivity extends MyBaseActivity {

    private String descType;
    private LinearLayout fullLinearLayout;
    private Button saveButton;
    private final List<EditText> remarksEditTextList = new ArrayList<>();
    private final List<TextView> textViewList = new ArrayList<>();
    private final List<Object> objectList = new ArrayList<>();
    private final List<String> descTypeList = new ArrayList<>();
    private final List<Integer> idList = new ArrayList<>();
    private String source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_check_list, null, false);
        drawer.addView(viewLayout, 0);
        activityNameTv.setText("CheckLists");
        setSupportActionBar(toolbar);
        fullLinearLayout = findViewById(R.id.linearLayoutCheck);
        saveButton = findViewById(R.id.saveButtonCheckList);
        saveButton.setEnabled(false);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String role = sharedPreferences.getString("role", "");

        Intent intent = getIntent();
        int taskId = intent.getIntExtra("taskId", 0);
        source = intent.getStringExtra("source");

        getCheckList(taskId, token);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChecklist(taskId, token);
            }
        });
    }

    private void saveChecklist(int taskId, String token) {

        ProgressDialog mProgress = new ProgressDialog(this);
        mProgress.setIndeterminate(true);
        mProgress.setCancelable(false);
        mProgress.setMessage("Saving....");
        mProgress.show();
        List<CheckListAddRequest> checkListAddRequestList = new ArrayList<>();
        for (int i = 0; i < textViewList.size(); i++) {
            AutoCompleteTextView objectSpinner = (AutoCompleteTextView) objectList.get(i);
            String objectName = objectSpinner.getText().toString();
            EditText remarksEditText = remarksEditTextList.get(i);
            String remarks = remarksEditText.getText().toString();

            String desc = descTypeList.get(i);
            int id = idList.get(i);

            CheckListAddRequest checkListAddRequest = new CheckListAddRequest(id, taskId, objectName, remarks);
            checkListAddRequestList.add(checkListAddRequest);
        }
        Call<List<CheckListAddRequest>> saveCall = APIClient.getUserServices().postCheckList(checkListAddRequestList, token);
        saveCall.enqueue(new Callback<List<CheckListAddRequest>>() {
            @Override
            public void onResponse(Call<List<CheckListAddRequest>> call, Response<List<CheckListAddRequest>> response) {
                if (response.code() == 200) {
                    Toast.makeText(CheckListActivity.this, "CheckList Updated", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), TaskSearchActivity.class);
                    intent.putExtra("workspace", workspace);
                    intent.putExtra("value", "Task");
                    startActivity(intent);
                    finish();
                } else
                    Toast.makeText(CheckListActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();

                mProgress.dismiss();
            }

            @Override
            public void onFailure(Call<List<CheckListAddRequest>> call, Throwable t) {
                Toast.makeText(CheckListActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                mProgress.dismiss();
            }
        });
    }

    private void getCheckList(int taskId, String token) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Getting Checklist");
        progressDialog.show();
        Call<List<GetCheckListResponse>> checkListResponseCall = APIClient.getUserServices().getChecklist(String.valueOf(taskId), token);
        checkListResponseCall.enqueue(new Callback<List<GetCheckListResponse>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call<List<GetCheckListResponse>> call, Response<List<GetCheckListResponse>> response) {
                if (response.code() == 200) {

                    List<GetCheckListResponse> listResponse = response.body();
                    for (int i = 0; i < listResponse.size(); i++) {
                        descType = listResponse.get(i).getDescriptionType();
                        descTypeList.add(descType);
                        int id = listResponse.get(i).getId();
                        idList.add(id);
                        String desc = listResponse.get(i).getDescription();
                        String status = listResponse.get(i).getStatus();
                        String remarks = listResponse.get(i).getRemarks();

                        createLayout(desc, remarks, status);
                        saveButton.setEnabled(true);
                    }

                } else
                    Toast.makeText(CheckListActivity.this, "Error: " + response.code(), Toast.LENGTH_LONG).show();

                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<GetCheckListResponse>> call, Throwable t) {
                Toast.makeText(CheckListActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    void createLayout(String desc, String remarks, String status) {
        LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.checklist_template, null, false);

        AutoCompleteTextView spinner = linearLayout.findViewById(R.id.checklistspinner);
        TextView textView = linearLayout.findViewById(R.id.checklisttv);
        EditText editText = linearLayout.findViewById(R.id.checklistremarks);

        textView.setText(desc);
        List<String> statusList = new ArrayList<>();
        statusList.add("Yes");
        statusList.add("no");
        statusList.add("na");
        List<String> removedDupList = statusList.stream().distinct().collect(Collectors.toList());

        ArrayAdapter<String> statusAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, removedDupList);
        spinner.setAdapter(statusAdapter);
        if (!status.equals("Yes")) {
            statusList.add(status);
            spinner.setText(status, false);
        } else spinner.setText("Yes", false);
        editText.setText(remarks);

        if (role.equals(Constants.ROLE_MANAGING_AGENT) || source.equals("search")) {
            saveButton.setVisibility(View.GONE);
            editText.setEnabled(false);
            spinner.setText(status, true);
        }

        textViewList.add(textView);
        objectList.add(spinner);
        remarksEditTextList.add(editText);
        fullLinearLayout.addView(linearLayout);

    }
}