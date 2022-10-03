// Generated by view binder compiler. Do not edit!
package com.synergyyy.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import com.google.android.material.card.MaterialCardView;
import com.synergyyy.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class EquipmentSearchRowBinding implements ViewBinding {
  @NonNull
  private final MaterialCardView rootView;

  @NonNull
  public final TextView textviewEqBuilding;

  @NonNull
  public final TextView textviewEqEqcode;

  @NonNull
  public final TextView textviewEqId;

  @NonNull
  public final TextView textviewEqName;

  @NonNull
  public final TextView textviewEqSubLocation;

  @NonNull
  public final TextView textviewEqSubType;

  @NonNull
  public final TextView textviewEqType;

  private EquipmentSearchRowBinding(@NonNull MaterialCardView rootView,
      @NonNull TextView textviewEqBuilding, @NonNull TextView textviewEqEqcode,
      @NonNull TextView textviewEqId, @NonNull TextView textviewEqName,
      @NonNull TextView textviewEqSubLocation, @NonNull TextView textviewEqSubType,
      @NonNull TextView textviewEqType) {
    this.rootView = rootView;
    this.textviewEqBuilding = textviewEqBuilding;
    this.textviewEqEqcode = textviewEqEqcode;
    this.textviewEqId = textviewEqId;
    this.textviewEqName = textviewEqName;
    this.textviewEqSubLocation = textviewEqSubLocation;
    this.textviewEqSubType = textviewEqSubType;
    this.textviewEqType = textviewEqType;
  }

  @Override
  @NonNull
  public MaterialCardView getRoot() {
    return rootView;
  }

  @NonNull
  public static EquipmentSearchRowBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static EquipmentSearchRowBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.equipment_search_row, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static EquipmentSearchRowBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.textview_eq_building;
      TextView textviewEqBuilding = rootView.findViewById(id);
      if (textviewEqBuilding == null) {
        break missingId;
      }

      id = R.id.textview_eq_eqcode;
      TextView textviewEqEqcode = rootView.findViewById(id);
      if (textviewEqEqcode == null) {
        break missingId;
      }

      id = R.id.textview_eq_id;
      TextView textviewEqId = rootView.findViewById(id);
      if (textviewEqId == null) {
        break missingId;
      }

      id = R.id.textview_eq_name;
      TextView textviewEqName = rootView.findViewById(id);
      if (textviewEqName == null) {
        break missingId;
      }

      id = R.id.textview_eq_sub_location;
      TextView textviewEqSubLocation = rootView.findViewById(id);
      if (textviewEqSubLocation == null) {
        break missingId;
      }

      id = R.id.textview_eq_sub_type;
      TextView textviewEqSubType = rootView.findViewById(id);
      if (textviewEqSubType == null) {
        break missingId;
      }

      id = R.id.textview_eq_type;
      TextView textviewEqType = rootView.findViewById(id);
      if (textviewEqType == null) {
        break missingId;
      }

      return new EquipmentSearchRowBinding((MaterialCardView) rootView, textviewEqBuilding,
          textviewEqEqcode, textviewEqId, textviewEqName, textviewEqSubLocation, textviewEqSubType,
          textviewEqType);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
