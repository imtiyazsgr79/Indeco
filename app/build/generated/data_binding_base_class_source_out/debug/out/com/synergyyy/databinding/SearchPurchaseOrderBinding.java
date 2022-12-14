// Generated by view binder compiler. Do not edit!
package com.synergyyy.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.viewbinding.ViewBinding;
import com.synergyyy.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class SearchPurchaseOrderBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final ListView listView;

  @NonNull
  public final ScrollView scrollViewSearch;

  @NonNull
  public final SearchView searchView;

  @NonNull
  public final Toolbar toolSearch;

  private SearchPurchaseOrderBinding(@NonNull LinearLayout rootView, @NonNull ListView listView,
      @NonNull ScrollView scrollViewSearch, @NonNull SearchView searchView,
      @NonNull Toolbar toolSearch) {
    this.rootView = rootView;
    this.listView = listView;
    this.scrollViewSearch = scrollViewSearch;
    this.searchView = searchView;
    this.toolSearch = toolSearch;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static SearchPurchaseOrderBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static SearchPurchaseOrderBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.search_purchase_order, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static SearchPurchaseOrderBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.list_view;
      ListView listView = rootView.findViewById(id);
      if (listView == null) {
        break missingId;
      }

      id = R.id.scrollViewSearch;
      ScrollView scrollViewSearch = rootView.findViewById(id);
      if (scrollViewSearch == null) {
        break missingId;
      }

      id = R.id.search_view;
      SearchView searchView = rootView.findViewById(id);
      if (searchView == null) {
        break missingId;
      }

      id = R.id.tool_search;
      Toolbar toolSearch = rootView.findViewById(id);
      if (toolSearch == null) {
        break missingId;
      }

      return new SearchPurchaseOrderBinding((LinearLayout) rootView, listView, scrollViewSearch,
          searchView, toolSearch);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
