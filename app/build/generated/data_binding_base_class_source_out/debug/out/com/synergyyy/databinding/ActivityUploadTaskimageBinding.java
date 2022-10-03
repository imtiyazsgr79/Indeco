// Generated by view binder compiler. Do not edit!
package com.synergyyy.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.synergyyy.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityUploadTaskimageBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final TabItem afterimageTab;

  @NonNull
  public final TabItem beforeimageTab;

  @NonNull
  public final TabLayout tabLayout1;

  @NonNull
  public final ViewPager taskImageVP;

  private ActivityUploadTaskimageBinding(@NonNull LinearLayout rootView,
      @NonNull TabItem afterimageTab, @NonNull TabItem beforeimageTab,
      @NonNull TabLayout tabLayout1, @NonNull ViewPager taskImageVP) {
    this.rootView = rootView;
    this.afterimageTab = afterimageTab;
    this.beforeimageTab = beforeimageTab;
    this.tabLayout1 = tabLayout1;
    this.taskImageVP = taskImageVP;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityUploadTaskimageBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityUploadTaskimageBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_upload_taskimage, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityUploadTaskimageBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.afterimage_tab;
      TabItem afterimageTab = rootView.findViewById(id);
      if (afterimageTab == null) {
        break missingId;
      }

      id = R.id.beforeimage_tab;
      TabItem beforeimageTab = rootView.findViewById(id);
      if (beforeimageTab == null) {
        break missingId;
      }

      id = R.id.tab_layout1;
      TabLayout tabLayout1 = rootView.findViewById(id);
      if (tabLayout1 == null) {
        break missingId;
      }

      id = R.id.taskImageVP;
      ViewPager taskImageVP = rootView.findViewById(id);
      if (taskImageVP == null) {
        break missingId;
      }

      return new ActivityUploadTaskimageBinding((LinearLayout) rootView, afterimageTab,
          beforeimageTab, tabLayout1, taskImageVP);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}