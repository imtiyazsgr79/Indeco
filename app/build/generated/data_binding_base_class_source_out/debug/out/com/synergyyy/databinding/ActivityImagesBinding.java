// Generated by view binder compiler. Do not edit!
package com.synergyyy.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.synergyyy.R;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;
import com.trex.lib.ThumbIndicator;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityImagesBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView authorizename;

  @NonNull
  public final TextView authorizerno;

  @NonNull
  public final ImageView cameraButton;

  @NonNull
  public final DotsIndicator dot;

  @NonNull
  public final FloatingActionButton fabDelete;

  @NonNull
  public final ImageView galleryButton;

  @NonNull
  public final ThumbIndicator indicator;

  @NonNull
  public final ConstraintLayout linearLayout;

  @NonNull
  public final ViewPager vpMain;

  private ActivityImagesBinding(@NonNull ConstraintLayout rootView, @NonNull TextView authorizename,
      @NonNull TextView authorizerno, @NonNull ImageView cameraButton, @NonNull DotsIndicator dot,
      @NonNull FloatingActionButton fabDelete, @NonNull ImageView galleryButton,
      @NonNull ThumbIndicator indicator, @NonNull ConstraintLayout linearLayout,
      @NonNull ViewPager vpMain) {
    this.rootView = rootView;
    this.authorizename = authorizename;
    this.authorizerno = authorizerno;
    this.cameraButton = cameraButton;
    this.dot = dot;
    this.fabDelete = fabDelete;
    this.galleryButton = galleryButton;
    this.indicator = indicator;
    this.linearLayout = linearLayout;
    this.vpMain = vpMain;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityImagesBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityImagesBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_images, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityImagesBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.authorizename;
      TextView authorizename = rootView.findViewById(id);
      if (authorizename == null) {
        break missingId;
      }

      id = R.id.authorizerno;
      TextView authorizerno = rootView.findViewById(id);
      if (authorizerno == null) {
        break missingId;
      }

      id = R.id.cameraButton;
      ImageView cameraButton = rootView.findViewById(id);
      if (cameraButton == null) {
        break missingId;
      }

      id = R.id.dot;
      DotsIndicator dot = rootView.findViewById(id);
      if (dot == null) {
        break missingId;
      }

      id = R.id.fabDelete;
      FloatingActionButton fabDelete = rootView.findViewById(id);
      if (fabDelete == null) {
        break missingId;
      }

      id = R.id.galleryButton;
      ImageView galleryButton = rootView.findViewById(id);
      if (galleryButton == null) {
        break missingId;
      }

      id = R.id.indicator;
      ThumbIndicator indicator = rootView.findViewById(id);
      if (indicator == null) {
        break missingId;
      }

      id = R.id.linearLayout;
      ConstraintLayout linearLayout = rootView.findViewById(id);
      if (linearLayout == null) {
        break missingId;
      }

      id = R.id.vpMain;
      ViewPager vpMain = rootView.findViewById(id);
      if (vpMain == null) {
        break missingId;
      }

      return new ActivityImagesBinding((ConstraintLayout) rootView, authorizename, authorizerno,
          cameraButton, dot, fabDelete, galleryButton, indicator, linearLayout, vpMain);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
