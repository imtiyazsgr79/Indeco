// Generated by view binder compiler. Do not edit!
package com.synergyyy.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import com.github.barteksc.pdfviewer.PDFView;
import com.synergyyy.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityViewPurchaseOnlyBinding implements ViewBinding {
  @NonNull
  private final ConstraintLayout rootView;

  @NonNull
  public final TextView errormsgtv;

  @NonNull
  public final ConstraintLayout innerLayout;

  @NonNull
  public final PDFView pdfView;

  @NonNull
  public final Toolbar toolbarUploadPdf;

  private ActivityViewPurchaseOnlyBinding(@NonNull ConstraintLayout rootView,
      @NonNull TextView errormsgtv, @NonNull ConstraintLayout innerLayout, @NonNull PDFView pdfView,
      @NonNull Toolbar toolbarUploadPdf) {
    this.rootView = rootView;
    this.errormsgtv = errormsgtv;
    this.innerLayout = innerLayout;
    this.pdfView = pdfView;
    this.toolbarUploadPdf = toolbarUploadPdf;
  }

  @Override
  @NonNull
  public ConstraintLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityViewPurchaseOnlyBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityViewPurchaseOnlyBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_view_purchase_only, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityViewPurchaseOnlyBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.errormsgtv;
      TextView errormsgtv = rootView.findViewById(id);
      if (errormsgtv == null) {
        break missingId;
      }

      id = R.id.inner_layout;
      ConstraintLayout innerLayout = rootView.findViewById(id);
      if (innerLayout == null) {
        break missingId;
      }

      id = R.id.pdfView;
      PDFView pdfView = rootView.findViewById(id);
      if (pdfView == null) {
        break missingId;
      }

      id = R.id.toolbar_upload_pdf;
      Toolbar toolbarUploadPdf = rootView.findViewById(id);
      if (toolbarUploadPdf == null) {
        break missingId;
      }

      return new ActivityViewPurchaseOnlyBinding((ConstraintLayout) rootView, errormsgtv,
          innerLayout, pdfView, toolbarUploadPdf);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
