package com.synergyyy.SearchTasks;


import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;
import com.synergyyy.Search.MainAdapter;

import java.util.ArrayList;

public class TaskSearchActivity extends MyBaseActivity {

    ArrayList<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_task_search, null, false);
        drawer.addView(viewLayout, 0);


        activityNameTv.setText("Search Tasks");

        Intent intent = getIntent();
        String workspace = intent.getStringExtra("workspace");

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
        MainTaskFragment fragment = new MainTaskFragment();
        for (int i = 0; i < list.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString("title", list.get(i));
            bundle.putString("workspace", workspace);
            fragment.setArguments(bundle);
            adapter.addFragment(fragment, list.get(i));
            fragment = new MainTaskFragment();
        }
        viewPager.setAdapter(adapter);
    }

}