package com.synergyyy.General;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.synergyyy.EquipmentSearch.EquipmentScanActivity;
import com.synergyyy.FaultReport.FaultReportActivity;
import com.synergyyy.Messages.MessageObject;
import com.synergyyy.Messages.MessagesActivity;
import com.synergyyy.PurchaseOder.SearchForPurchase;
import com.synergyyy.R;
import com.synergyyy.Search.Search;
import com.synergyyy.SearchTasks.SearchTaskActivity;
import com.synergyyy.SearchTasks.TaskSearchActivity;
import com.synergyyy.Setting.SettingActivity;
import com.synergyyy.UploadPdf.SearchForQuote;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class MyBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public DrawerLayout drawer;
    public Toolbar toolbar;
    public String token;
    public String username;
    public NavigationView navigationView;
    public ActionBarDrawerToggle toggle;
    public String role, workspace;
    private final CheckInternet checkInternet = new CheckInternet();
    public TextView activityNameTv, messageCountTv;
    public ImageView logoutIcon, messageIcon;
    public CardView cardCountTv;
    private Handler h = new Handler();

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
        setContentView(R.layout.activity_my_base);


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");
        username = sharedPreferences.getString("username", "").toUpperCase();
        workspace = sharedPreferences.getString("workspace", "");
        drawer = findViewById(R.id.drawer);
        toolbar = findViewById(R.id.toolbar_mybase);

        activityNameTv = findViewById(R.id.toolbar_tv_name);
        messageCountTv = findViewById(R.id.message_count);
        logoutIcon = findViewById(R.id.logout_icon);
        messageIcon = findViewById(R.id.message_icon);
        navigationView = findViewById(R.id.navView);
        cardCountTv = findViewById(R.id.card_count_tv);


        View headerView = navigationView.getHeaderView(0);
        TextView usernameTv = (TextView) headerView.findViewById(R.id.headerName);
        usernameTv.setText(username);
        setSupportActionBar(toolbar);

        navigationView.setNavigationItemSelectedListener(this);
        toggle = new ActionBarDrawerToggle(this, drawer, toolbar, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setItemIconTintList(null);
        Menu nav_Menu = navigationView.getMenu();

        if (role.equals(Constants.ROLE_TECHNICIAN) || role.equals(Constants.ROLE_MANAGING_AGENT)) {
            nav_Menu.findItem(R.id.createFNav).setVisible(false);
            nav_Menu.findItem(R.id.setNav).setVisible(false);
        }
        if (role.equals(Constants.ROLE_MANAGING_AGENT)) {
            nav_Menu.findItem(R.id.upload_qoutation_nav).setTitle("Search Quotation");
            nav_Menu.findItem(R.id.upload_po_nav).setTitle("Search Purchase Order");
            nav_Menu.findItem(R.id.ScanTasks).setVisible(false);
        }
        if (role.equals(Constants.Coordinator)) {
            nav_Menu.findItem(R.id.createFNav).setVisible(false);
            nav_Menu.findItem(R.id.setNav).setVisible(false);
            nav_Menu.findItem(R.id.ScanFaultReport).setVisible(false);
            nav_Menu.findItem(R.id.ScanTasks).setVisible(false);
        }
        logoutIcon.setOnClickListener(v -> {
            LogoutClass logoutClass = new LogoutClass();
            logoutClass.logout(MyBaseActivity.this);
        });
        messageIcon.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), MessagesActivity.class);
            startActivity(intent);
            finish();
        });
        checkMessages.run();
    }


    public void intentMethod(Class cla, String value) {

        if (!(getClass().equals(cla))) {
            Intent intent = new Intent(getApplicationContext(), cla);
            intent.putExtra("workspace", workspace);
            intent.putExtra("value", value);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.messageNav:
                intentMethod(MessagesActivity.class, "");
                break;
            case R.id.createFNav:
                intentMethod(FaultReportActivity.class, "");
                break;
            case R.id.searchFNav:
                intentMethod(Search.class, "");
                break;
            case R.id.ScanFaultReport:
                intentMethod(EquipmentScanActivity.class, "Fault");
                break;
            case R.id.ScanTasks:

                intentMethod(EquipmentScanActivity.class, "Task");
                break;
            case R.id.tSearchNav:
                if (role.equals(Constants.ROLE_MANAGING_AGENT)) {
                    intentMethod(SearchTaskActivity.class, "");
                } else
                    intentMethod(TaskSearchActivity.class, "");
                break;
            case R.id.setNav:
                intentMethod(SettingActivity.class, "");
                break;

            case R.id.upload_qoutation_nav:

                intentMethod(SearchForQuote.class, "");
                break;
            case R.id.upload_po_nav:
                intentMethod(SearchForPurchase.class, "");
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }




    private final Runnable checkMessages = new Runnable() {
        @Override
        public void run() {
            {
                Call<MessageObject> getMessageList = APIClient.getUserServices().getListOfMessages(token, username);
                getMessageList.enqueue(new Callback<MessageObject>() {
                    @Override
                    public void onResponse(Call<MessageObject> call, Response<MessageObject> response) {

                        if (response.code() == 200) {
//                            h.postDelayed(checkMessages,6000);

                            MessageObject messageObject = response.body();
                            int count = messageObject.getCount();

                            if (count > 0) {
                                cardCountTv.setVisibility(View.VISIBLE);
                                messageCountTv.setText(String.valueOf(count));
                            }

                        }

                    }

                    @Override
                    public void onFailure(Call<MessageObject> call, Throwable t) {
//                        h.postDelayed(checkMessages,6000);

                    }
                });

            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        h.removeCallbacks(checkMessages);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        h.removeCallbacks(checkMessages);

    }



}


