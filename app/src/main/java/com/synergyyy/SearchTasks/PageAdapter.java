package com.synergyyy.SearchTasks;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {

    private final int numberOfTabs;
    private ImageView imageView;
    private String imageName;
    private Context context;
    private String imageN;

    public PageAdapter(@NonNull FragmentManager fm, int numberOfTabs,
                       ImageView imageView, String imageName, Context context, String ImageN) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        this.imageView = imageView;
        this.imageName = imageName;
        this.context = context;
        this.imageN = imageN;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        if (position == 0) {
            return new beforeTab(imageView, imageName, imageN);
        } else {
            return new afterTab(imageView, imageName, imageN);
        }


    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);

    }


}
