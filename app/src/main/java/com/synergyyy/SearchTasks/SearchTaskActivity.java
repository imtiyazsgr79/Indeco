package com.synergyyy.SearchTasks;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;
import com.synergyyy.EquipmentSearch.EquipmentSearchAdapter;
import com.synergyyy.EquipmentSearch.EquipmentSearchCard;
import com.synergyyy.General.LogoutClass;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class SearchTaskActivity extends MyBaseActivity {

    private final List<String> taskIdList = new ArrayList<>();
    private String role;
    private final ArrayList<EquipmentSearchCard> equipmentSearchCardArrayList = new ArrayList<>();
    private final EquipmentSearchAdapter mAdapter = new EquipmentSearchAdapter(equipmentSearchCardArrayList);
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_search_task, null, false);
        drawer.addView(viewLayout, 0);

        recyclerView = findViewById(R.id.recycler_view_Task);

        activityNameTv.setText("Search Tasks");

        Intent intent = getIntent();
        String workspace = intent.getStringExtra("workspace");

        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        role = preferences.getString("role", "");
        String token = preferences.getString("token", "");

        SearchView searchView = findViewById(R.id.search_viewTask);
        searchView.setIconifiedByDefault(false);

        ScrollView view = (ScrollView) findViewById(R.id.scrollViewTaskSearch);
        view.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    equipmentSearchCardArrayList.clear();
                    recyclerView.setAdapter(null);
                } else {
                    equipmentSearchCardArrayList.clear();
                    loadSearch(newText, token, role, workspace);
                }

                return false;
            }
        });

    }

    private void loadSearch(String newText, String token, String role, String workspace) {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        Call<List<TaskSearchResponse>> callTaskSearch = APIClient.getUserServices().taskSearch(newText, "Inactive", token, role, workspace);
        callTaskSearch.enqueue(new Callback<List<TaskSearchResponse>>() {
            @Override
            public void onResponse(Call<List<TaskSearchResponse>> call, Response<List<TaskSearchResponse>> response) {
                if (response.code() == 200) {

                    String source = "search";
                    List<TaskSearchResponse> equipmentSearchResponse = response.body();

                    if (equipmentSearchResponse.isEmpty()) {
                        Toast.makeText(SearchTaskActivity.this, "No tasks available!", Toast.LENGTH_SHORT).show();
                        recyclerView.setAdapter(null);
                    } else {


                        for (int i = 0; i < equipmentSearchResponse.size(); i++) {
                            String taskNumber = equipmentSearchResponse.get(i).getTaskNumber();
                            int taskId = equipmentSearchResponse.get(i).getTaskId();
                            String buildingName = equipmentSearchResponse.get(i).getBuildingName();
                            String locationName = equipmentSearchResponse.get(i).getLocationName();
                            String equipCode = equipmentSearchResponse.get(i).getEquipmentName();
                            long scheduleDate = equipmentSearchResponse.get(i).getScheduleDate();
                            String status = equipmentSearchResponse.get(i).getStatus();
                            String afterImage = equipmentSearchResponse.get(i).getAfterImage();
                            String beforeImage = equipmentSearchResponse.get(i).getBeforeImage();

                            equipmentSearchCardArrayList.add(new EquipmentSearchCard((long) taskId, taskNumber, workspace,
                                    status, buildingName, locationName, scheduleDate,
                                    afterImage, beforeImage, source, equipCode));
                        }

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(SearchTaskActivity.this));
                        recyclerView.setAdapter(mAdapter);
                    }
                } else if (response.code() == 401) {
                    Toast.makeText(SearchTaskActivity.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(SearchTaskActivity.this);
                } else if (response.code() == 500) {
                    Toast.makeText(SearchTaskActivity.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(SearchTaskActivity.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<TaskSearchResponse>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}