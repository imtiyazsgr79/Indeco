// Generated by view binder compiler. Do not edit!
package com.synergyyy.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.synergyyy.R;
import java.lang.NullPointerException;
import java.lang.Override;

public final class TvspinnertemplateBinding implements ViewBinding {
  @NonNull
  private final Spinner rootView;

  private TvspinnertemplateBinding(@NonNull Spinner rootView) {
    this.rootView = rootView;
  }

  @Override
  @NonNull
  public Spinner getRoot() {
    return rootView;
  }

  @NonNull
  public static TvspinnertemplateBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static TvspinnertemplateBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.tvspinnertemplate, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static TvspinnertemplateBinding bind(@NonNull View rootView) {
    if (rootView == null) {
      throw new NullPointerException("rootView");
    }

    return new TvspinnertemplateBinding((Spinner) rootView);
  }
}
