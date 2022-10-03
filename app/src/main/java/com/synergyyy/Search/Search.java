package com.synergyyy.Search;

import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;

import java.util.ArrayList;

import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class Search extends MyBaseActivity {
    private String workspaceId;
    ArrayList<String> list;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_search, null, false);
        drawer.addView(viewLayout, 0);

        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        role = sharedPreferences.getString("role", "");
        username = sharedPreferences.getString("username", "");
        activityNameTv.setText("Search Fault Report");
        username = sharedPreferences.getString("username", "");
        Intent intent = getIntent();

        workspaceId = intent.getStringExtra("workspace");
        ViewPager viewPager = findViewById(R.id.viewpager);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        list = new ArrayList<String>();
        list.add("Active");
        list.add("Inactive");

        prepareViewPager(viewPager, list);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> list) {
        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        MainFragment fragment = new MainFragment();
        for (int i = 0; i < list.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString("title", list.get(i));
            bundle.putString("workspace", workspaceId);
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, list.get(i));
            fragment = new MainFragment();
        }
        viewPager.setAdapter(adapter);
    }

}