package com.synergyyy.SearchTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.synergyyy.General.APIClient;
import com.synergyyy.General.Constants;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadImageTask extends MyBaseActivity {

    private TabLayout tabLayout;
    private TabItem beforeTab, afterTab;
    private PageAdapter pageAdapter;
    private ViewPager viewPager;
    private ImageView beforeImage, afterImage;
    private String workspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_upload_taskimage, null, false);
        drawer.addView(viewLayout, 0);
        toolbar.setTitle("Upload Image");
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String taskId = intent.getStringExtra("taskId");
        workspace = intent.getStringExtra("workspace");
        String afterImageName = intent.getStringExtra("afterImage");
        String beforeImageName = intent.getStringExtra("beforeImage");

        tabLayout = findViewById(R.id.tab_layout1);
        beforeTab = findViewById(R.id.beforeimage_tab);
        afterTab = findViewById(R.id.afterimage_tab);
        viewPager = findViewById(R.id.taskImageVP);
        beforeImage = findViewById(R.id.beforeImageTask1);
        afterImage = findViewById(R.id.afterImageTask1);

        pageAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),
                afterImage, afterImageName, this, null);
        viewPager.setAdapter(pageAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

                if (tab.getPosition() == 0) {
                    if (beforeImageName != null) {
                        pageAdapter = new PageAdapter(getSupportFragmentManager(),
                                tabLayout.getTabCount(), afterImage, afterImageName, UploadImageTask.this, "before");
                        viewPager.setAdapter(pageAdapter);
                    }
                    pageAdapter.notifyDataSetChanged();
                } else if (tab.getPosition() == 1) {
                    if (afterImageName != null) {
                        pageAdapter = new PageAdapter(getSupportFragmentManager(),
                                tabLayout.getTabCount(), afterImage, afterImageName, UploadImageTask.this, "after");
                        viewPager.setAdapter(pageAdapter);
                    }
                    pageAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    public void retrieveImage(String imageName, ImageView imageView, String imageN, View view, Context context) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Please Wait....");
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<ResponseBody> callTaskImage = APIClient.getUserServices().getTaskImage(imageName, role, token, workspace);
        ImageView finalImageView = imageView;
        callTaskImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                if (response.code() == 200) {
                    Bitmap bmp = BitmapFactory.decodeStream(response.body().byteStream());
                    finalImageView.setBackground(null);
                    finalImageView.setImageBitmap(bmp);
                } else if (response.code() == 401) {
                    Toast.makeText(context, Constants.ERROR_CODE_401_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 500) {
                    Toast.makeText(context, Constants.ERROR_CODE_500_MESSAGE, Toast.LENGTH_SHORT).show();
                } else if (response.code() == 404) {
                    Toast.makeText(context, Constants.ERROR_CODE_404_MESSAGE, Toast.LENGTH_SHORT).show();
                } else
                    Toast.makeText(context, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(context, t.getMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });

    }

}
