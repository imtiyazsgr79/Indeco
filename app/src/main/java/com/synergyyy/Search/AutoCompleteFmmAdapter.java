package com.synergyyy.Search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.synergyyy.R;

import java.util.List;

public class AutoCompleteFmmAdapter extends ArrayAdapter<fmm> {

    private static final String TAG = "FmmAdapter";
    private List<fmm> items;
    private int mLastClicked;
    private String currentStatus;

    public void setLastClicked(int lastClicked) {
        mLastClicked = lastClicked;
    }

    public AutoCompleteFmmAdapter(@NonNull Context context, @NonNull List<fmm> items) {
        super(context, 0,items);
        this.items = items;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.autocomplete_fmm_layout, parent, false);
        }
        TextView fmmTextView = convertView.findViewById(R.id.fmm_text);

        fmm selectedFmm = getItem(position);
        if (selectedFmm != null) {
            fmmTextView.setText(selectedFmm.getUsername());
        }

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {

        return true;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
}
