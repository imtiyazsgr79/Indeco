// Generated by view binder compiler. Do not edit!
package com.synergyyy.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.synergyyy.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivitySearchTaskBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final RecyclerView recyclerViewTask;

  @NonNull
  public final ScrollView scrollViewTaskSearch;

  @NonNull
  public final SearchView searchViewTask;

  @NonNull
  public final Toolbar toolSearchTasks;

  private ActivitySearchTaskBinding(@NonNull LinearLayout rootView,
      @NonNull RecyclerView recyclerViewTask, @NonNull ScrollView scrollViewTaskSearch,
      @NonNull SearchView searchViewTask, @NonNull Toolbar toolSearchTasks) {
    this.rootView = rootView;
    this.recyclerViewTask = recyclerViewTask;
    this.scrollViewTaskSearch = scrollViewTaskSearch;
    this.searchViewTask = searchViewTask;
    this.toolSearchTasks = toolSearchTasks;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivitySearchTaskBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivitySearchTaskBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_search_task, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivitySearchTaskBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.recycler_view_Task;
      RecyclerView recyclerViewTask = rootView.findViewById(id);
      if (recyclerViewTask == null) {
        break missingId;
      }

      id = R.id.scrollViewTaskSearch;
      ScrollView scrollViewTaskSearch = rootView.findViewById(id);
      if (scrollViewTaskSearch == null) {
        break missingId;
      }

      id = R.id.search_viewTask;
      SearchView searchViewTask = rootView.findViewById(id);
      if (searchViewTask == null) {
        break missingId;
      }

      id = R.id.tool_searchTasks;
      Toolbar toolSearchTasks = rootView.findViewById(id);
      if (toolSearchTasks == null) {
        break missingId;
      }

      return new ActivitySearchTaskBinding((LinearLayout) rootView, recyclerViewTask,
          scrollViewTaskSearch, searchViewTask, toolSearchTasks);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
