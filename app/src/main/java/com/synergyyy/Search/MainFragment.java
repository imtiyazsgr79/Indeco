package com.synergyyy.Search;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;
import com.synergyyy.General.LogoutClass;
import com.synergyyy.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class MainFragment extends Fragment {

    private String TAG;
    private String frId = "";
    private String workspaceId, token;
    private List<String> frIdList = new ArrayList<>();
    private ListView listView;
    private ArrayList<SearchResponse> contacts = new ArrayList<>();
    private SearchResponseAdapter searchResponseAdapter;
    Toolbar toolbar;
    String role, username;
    private double latitude, longitude;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String title;
    TextView textView;
    private String mParam1;
    private String mParam2;

    public MainFragment() {
    }

    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");
        username = sharedPreferences.getString("username", "");
        workspaceId = this.getArguments().getString("workspace");
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        textView = view.findViewById(R.id.textviewfrag);
        title = this.getArguments().getString("title");
        textView.setText(title);
        ScrollView scrollView = view.findViewById(R.id.scrollViewSearch);
        scrollView.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
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
        listView = view.findViewById(R.id.list_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String onTextChangeQueryCall) {
                if (onTextChangeQueryCall.isEmpty()) {
                    contacts.clear();
                } else {
                    contacts.clear();
                    loadSearch(onTextChangeQueryCall, textView.getText().toString());
                    listView.setFilterText(onTextChangeQueryCall);
                }
                return false;
            }
        });
        return view;
    }

    private void loadSearch(String callQueryDependent, String type) {

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Loading...");
        progressDialog.show();
        Call<List<SearchResponse>> call = APIClient.getUserServices().getSearchResult(workspaceId,
                callQueryDependent, type, token, role);
        call.enqueue(new Callback<List<SearchResponse>>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<List<SearchResponse>> call, Response<List<SearchResponse>> response) {

                progressDialog.dismiss();
                if (response.code() == 200) {
                    progressDialog.dismiss();
                    List<SearchResponse> list = response.body();
                    if (list.isEmpty()) {
                        Toast.makeText(getContext(), "No Fault Reports Here", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();

                        for (SearchResponse searchResponse : list) {

                            SearchResponse searchResp = new SearchResponse();
                            frId = searchResponse.getFrId();
                            String rtdate = searchResponse.getReportedDate();
                            String status = searchResponse.getStatus();
                            String buildingg = searchResponse.getBuildingName();
                            String locationn = searchResponse.getLocationName();
                            String activationTime = searchResponse.getActivationTime();


                            searchResp.setFrId(frId);
                            searchResp.setReportedDate(rtdate);
                            searchResp.setStatus(status);
                            searchResp.setBuilding(buildingg);
                            searchResp.setLocation(locationn);

                            searchResp.setActivationTime(activationTime);
                            searchResp.setWorkspaceId(workspaceId);
                            searchResp.setLatitude(latitude);
                            searchResp.setLongitude(longitude);
                            contacts.add(searchResp);
                        }
                    }
                    Collections.sort(frIdList);
                    searchResponseAdapter = new SearchResponseAdapter(getActivity(),
                            contacts, workspaceId, latitude, longitude);
                    listView.setAdapter(searchResponseAdapter);
                    searchResponseAdapter.notifyDataSetChanged();


                } else if (response.code() == 401) {
                    Toast.makeText(getActivity(), Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(getActivity());
                } else if (response.code() == 500) {
                    Toast.makeText(getActivity(), Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(getActivity(), Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<SearchResponse>> call, Throwable t) {
                Toast.makeText(getActivity(), "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    public String returnTimeString(String time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(time);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        String dateTime = sdf.format(date);
        return dateTime;
    }
}