package com.synergyyy.PurchaseOder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.material.tabs.TabLayout;
import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;
import com.synergyyy.General.LogoutClass;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;
import com.synergyyy.Search.SearchResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class SearchForPurchase extends MyBaseActivity {

    private String TAG;
    private String frId = "";
    private String workspaceId, token;
    private List<String> frIdList = new ArrayList<>();
    private ListView listView;
    private ArrayList<SearchResponse> contacts = new ArrayList<>();
    private SearchAdapterForPurchase searchAdapterForPurchase;
    private String role, username;

    private FusedLocationProviderClient client;
    private double latitude, longitude;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.search_purchase_order, null, false);
        drawer.addView(viewLayout, 0);
        //  setContentView(R.layout.search_purchase_order);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");
        username = sharedPreferences.getString("username", "");
        activityNameTv.setText("Search Purchase Order");
        setSupportActionBar(toolbar);
        username = sharedPreferences.getString("username", "");
        Intent intent = getIntent();
        workspaceId = intent.getStringExtra("workspace");

        ScrollView view = findViewById(R.id.scrollViewSearch);
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


        SearchView searchView = findViewById(R.id.search_view);
        searchView.setIconifiedByDefault(false);
        listView = findViewById(R.id.list_view);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String onTextChangeQueryCall) {
                if (onTextChangeQueryCall.isEmpty()) {
                    contacts.clear();
                    //   searchResponseAdapter.notifyDataSetChanged();
                } else {
                    contacts.clear();
                    loadSearch(onTextChangeQueryCall);
                    listView.setFilterText(onTextChangeQueryCall);
                }

                return false;
            }
        });
    }

    private void loadSearch(String callQueryDependent) {

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.setTitle("Searching Purchase Orders");
        progressDialog.show();

        Call<List<SearchResponse>> call = APIClient.getUserServices().
                getSearchForPurchase(workspaceId, callQueryDependent, token, role);
        call.enqueue(new Callback<List<SearchResponse>>() {
            @Override
            public void onResponse(Call<List<SearchResponse>> call, Response<List<SearchResponse>> response) {

                progressDialog.dismiss();
                if (response.code() == 200) {
                    List<SearchResponse> list = response.body();
                    if (list.isEmpty()) {
                        Toast.makeText(SearchForPurchase.this, "No Purchase Orders Available", Toast.LENGTH_SHORT).show();
                    } else {

                        for (SearchResponse searchResponse : list) {

                            SearchResponse searchResp = new SearchResponse();
                            frId = searchResponse.getFrId();
                            String rtdate = searchResponse.getReportedDate();
                            String status = searchResponse.getStatus();
                            String buildingg = searchResponse.getBuildingName();
                            String locationn = searchResponse.getLocationName();

                            String localDateTime = searchResponse.getActivationTime();

                            searchResp.setFrId(frId);
                            searchResp.setReportedDate(rtdate);
                            searchResp.setStatus(status);
                            searchResp.setBuilding(buildingg);
                            searchResp.setLocation(locationn);
                            searchResp.setActivationTime(localDateTime);
                            searchResp.setWorkspaceId(workspaceId);
                            searchResp.setLatitude(latitude);
                            searchResp.setLongitude(longitude);
                            //  frIdList.add(frId);
                            contacts.add(searchResp);
                        }
                    }
                    Collections.sort(frIdList);
                    searchAdapterForPurchase = new SearchAdapterForPurchase(SearchForPurchase.this,
                            contacts, workspaceId, latitude, longitude);
                    listView.setAdapter(searchAdapterForPurchase);
                    searchAdapterForPurchase.notifyDataSetChanged();


                } else if (response.code() == 401) {
                    Toast.makeText(SearchForPurchase.this, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                    LogoutClass logoutClass = new LogoutClass();
                    logoutClass.logout(SearchForPurchase.this);
                } else if (response.code() == 500) {
                    Toast.makeText(SearchForPurchase.this, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(SearchForPurchase.this, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<List<SearchResponse>> call, Throwable t) {
                Toast.makeText(SearchForPurchase.this, "Failed to load  purchase orders", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getCause());
                Log.d(TAG, "onFailure: " + t.getMessage());

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