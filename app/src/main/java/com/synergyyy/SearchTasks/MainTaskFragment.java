package com.synergyyy.SearchTasks;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;
import com.synergyyy.EquipmentSearch.EquipmentSearchAdapter;
import com.synergyyy.EquipmentSearch.EquipmentSearchCard;
import com.synergyyy.General.LogoutClass;
import com.synergyyy.R;
import com.synergyyy.Search.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class MainTaskFragment extends Fragment {

    private String workspace, token;
    private ListView listView;
    private ArrayList<SearchResponse> contacts = new ArrayList<>();
    String role, username;
    private TextView textView;
    private String title;
    private final ArrayList<EquipmentSearchCard> equipmentSearchCardArrayList = new ArrayList<>();
    private final EquipmentSearchAdapter mAdapter = new EquipmentSearchAdapter(equipmentSearchCardArrayList);
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private RecyclerView recyclerView;
    private String mParam2;

    public MainTaskFragment() {
    }

    public static MainTaskFragment newInstance(String param1, String param2) {
        MainTaskFragment fragment = new MainTaskFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");
        username = sharedPreferences.getString("username", "");
        workspace = this.getArguments().getString("workspace");

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main_task, container, false);
        textView = view.findViewById(R.id.textviewfrag);
        title = this.getArguments().getString("title");
        textView.setText(title);
        ScrollView scrollView = view.findViewById(R.id.scrollViewSearch);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
        recyclerView = view.findViewById(R.id.recycler_view_Task);
        scrollView.setFocusable(true);
        scrollView.setFocusableInTouchMode(true);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocusFromTouch();
                return false;
            }
        });
        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String onTextChangeQueryCall) {
                if (onTextChangeQueryCall.isEmpty()) {
                    equipmentSearchCardArrayList.clear();
                    recyclerView.setAdapter(null);
                } else {
                    equipmentSearchCardArrayList.clear();
                    loadSearch(onTextChangeQueryCall, token, role, workspace, textView.getText().toString());
                }
                return false;
            }
        });
        return view;

    }

    private void loadSearch(String newText, String token, String role, String workspace, String type) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();

        Call<List<TaskSearchResponse>> callTaskSearch = APIClient.getUserServices().taskSearch(newText, type, token, role, workspace);
        callTaskSearch.enqueue(new Callback<List<TaskSearchResponse>>() {
            @Override
            public void onResponse(Call<List<TaskSearchResponse>> call, Response<List<TaskSearchResponse>> response) {
                if (response.code() == 200) {

                    String source = "search";
                    List<TaskSearchResponse> equipmentSearchResponse = response.body();

                    if (equipmentSearchResponse.isEmpty()) {
                        Toast.makeText(getActivity(), "No tasks available!", Toast.LENGTH_SHORT).show();
                        //listView.setAdapter(null);
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

                            EquipmentSearchCard equipmentSearchCard = new EquipmentSearchCard((long) taskId, taskNumber, workspace,
                                    status, buildingName, locationName, scheduleDate,
                                    afterImage, beforeImage, source, equipCode);
                            equipmentSearchCardArrayList.add(equipmentSearchCard);

                        }

                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                        recyclerView.setAdapter(mAdapter);

                    }
                } else if (response.code() == 401) {
                    Toast.makeText(getActivity(), Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(getActivity());
                } else if (response.code() == 500) {
                    Toast.makeText(getActivity(), Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getActivity(), Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<TaskSearchResponse>> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

}
