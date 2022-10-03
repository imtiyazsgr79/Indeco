// Generated by view binder compiler. Do not edit!
package com.synergyyy.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.synergyyy.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySettingBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final SwitchMaterial adminSswitch;

  @NonNull
  public final SwitchMaterial notificationSwitch;

  @NonNull
  public final SwitchMaterial permissionSwitch;

  @NonNull
  public final Toolbar toolbarearch;

  private ActivitySettingBinding(@NonNull LinearLayout rootView,
      @NonNull SwitchMaterial adminSswitch, @NonNull SwitchMaterial notificationSwitch,
      @NonNull SwitchMaterial permissionSwitch, @NonNull Toolbar toolbarearch) {
    this.rootView = rootView;
    this.adminSswitch = adminSswitch;
    this.notificationSwitch = notificationSwitch;
    this.permissionSwitch = permissionSwitch;
    this.toolbarearch = toolbarearch;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySettingBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySettingBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_setting, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySettingBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.admin_sswitch;
      SwitchMaterial adminSswitch = rootView.findViewById(id);
      if (adminSswitch == null) {
        break missingId;
      }

      id = R.id.notification_switch;
      SwitchMaterial notificationSwitch = rootView.findViewById(id);
      if (notificationSwitch == null) {
        break missingId;
      }

      id = R.id.permission_switch;
      SwitchMaterial permissionSwitch = rootView.findViewById(id);
      if (permissionSwitch == null) {
        break missingId;
      }

      id = R.id.toolbarearch;
      Toolbar toolbarearch = rootView.findViewById(id);
      if (toolbarearch == null) {
        break missingId;
      }

      return new ActivitySettingBinding((LinearLayout) rootView, adminSswitch, notificationSwitch,
          permissionSwitch, toolbarearch);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
