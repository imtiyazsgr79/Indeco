// Generated by view binder compiler. Do not edit!
package com.synergyyy.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import com.synergyyy.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class RecylerCardBinding implements ViewBinding {
  @NonNull
  private final CardView rootView;

  @NonNull
  public final TextView descTv;

  @NonNull
  public final TextView worskspaceTv;

  private RecylerCardBinding(@NonNull CardView rootView, @NonNull TextView descTv,
      @NonNull TextView worskspaceTv) {
    this.rootView = rootView;
    this.descTv = descTv;
    this.worskspaceTv = worskspaceTv;
  }

  @Override
  @NonNull
  public CardView getRoot() {
    return rootView;
  }

  @NonNull
  public static RecylerCardBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static RecylerCardBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.recyler_card, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static RecylerCardBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.desc_tv;
      TextView descTv = rootView.findViewById(id);
      if (descTv == null) {
        break missingId;
      }

      id = R.id.worskspace_tv;
      TextView worskspaceTv = rootView.findViewById(id);
      if (worskspaceTv == null) {
        break missingId;
      }

      return new RecylerCardBinding((CardView) rootView, descTv, worskspaceTv);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
