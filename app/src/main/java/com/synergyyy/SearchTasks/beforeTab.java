package com.synergyyy.SearchTasks;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.synergyyy.R;


public class beforeTab extends Fragment {

    ImageView imageView;
    String imageName, imageN;

    public beforeTab(ImageView imageView, String imageName,  String imageN) {
        this.imageView = imageView;
        this.imageName = imageName;

        this.imageN = imageN;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_before_tab, container, false);

        imageView = v.findViewById(R.id.beforeImageTask1);

        if (imageN!=null) {
            new UploadImageTask().retrieveImage(imageName,imageView, imageN, v, getContext());
        }
        return v;
    }
}