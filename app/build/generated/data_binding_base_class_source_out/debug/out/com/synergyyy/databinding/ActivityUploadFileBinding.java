// Generated by view binder compiler. Do not edit!
package com.synergyyy.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import com.synergyyy.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityUploadFileBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Toolbar toolbarEditFault;

  @NonNull
  public final TextView uploadFile;

  @NonNull
  public final Button uploadFileBtn;

  private ActivityUploadFileBinding(@NonNull LinearLayout rootView,
      @NonNull Toolbar toolbarEditFault, @NonNull TextView uploadFile,
      @NonNull Button uploadFileBtn) {
    this.rootView = rootView;
    this.toolbarEditFault = toolbarEditFault;
    this.uploadFile = uploadFile;
    this.uploadFileBtn = uploadFileBtn;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityUploadFileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityUploadFileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_upload_file, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityUploadFileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.toolbar_edit_fault;
      Toolbar toolbarEditFault = rootView.findViewById(id);
      if (toolbarEditFault == null) {
        break missingId;
      }

      id = R.id.upload_file;
      TextView uploadFile = rootView.findViewById(id);
      if (uploadFile == null) {
        break missingId;
      }

      id = R.id.upload_file_btn;
      Button uploadFileBtn = rootView.findViewById(id);
      if (uploadFileBtn == null) {
        break missingId;
      }

      return new ActivityUploadFileBinding((LinearLayout) rootView, toolbarEditFault, uploadFile,
          uploadFileBtn);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
