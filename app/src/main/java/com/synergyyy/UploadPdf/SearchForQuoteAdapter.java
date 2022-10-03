package com.synergyyy.UploadPdf;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.synergyyy.R;
import com.synergyyy.Search.ActivationTime;
import com.synergyyy.Search.MainFragment;
import com.synergyyy.Search.SearchResponse;

import java.util.ArrayList;

public class SearchForQuoteAdapter extends BaseAdapter {

    private Context context; //context
    private ArrayList<SearchResponse> contacts; //data source of the list adapter
    private static final String TAG = "SearchResponseAdapter";
    private String workspaceId;
    private double latitude, longitude;


    public SearchForQuoteAdapter(Context context, ArrayList<SearchResponse> items, String workspaceId, double latitude, double longitude) {
        this.context = context;
        this.workspaceId = workspaceId;
        this.contacts = items;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Object getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).
                    inflate(R.layout.search_row, parent, false);
        }
        SearchResponse currentItem = (SearchResponse) getItem(position);

        TextView frIdTextView = (TextView) convertView.findViewById(R.id.textView_frid);
        TextView activationDAte = (TextView) convertView.findViewById(R.id.textView_reporteddatete);
        TextView cr = (TextView) convertView.findViewById(R.id.textView_createddate);
        TextView status = (TextView) convertView.findViewById(R.id.textView_status);
        TextView building = (TextView) convertView.findViewById(R.id.textView_building);
        TextView location = (TextView) convertView.findViewById(R.id.textView_location);
        TextView to = convertView.findViewById(R.id.tokenGen);
        TextView workspaceSearchTextView = convertView.findViewById(R.id.workspace_search);


        frIdTextView.setText(currentItem.getFrId().trim());
        activationDAte.setText(new MainFragment().returnTimeString(currentItem.getActivationTime()));
        status.setText(currentItem.getStatus());
        building.setText(currentItem.getBuilding());
        location.setText(currentItem.getLocation());
        workspaceId = currentItem.getWorkspaceId();
        longitude = currentItem.getLongitude();
        latitude = currentItem.getLatitude();

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String frid = ((TextView) view.findViewById(R.id.textView_frid)).getText().toString();//.substring(6);
                Intent intent = new Intent(context.getApplicationContext(), UploadPdf.class);
                intent.putExtra("frId", frid);
                intent.putExtra("longitude", currentItem.getLongitude());
                intent.putExtra("latitude", currentItem.getLatitude());
                intent.putExtra("workspace", workspaceId);
                context.startActivity(intent);
                ((Activity)context).finish();
            }
        });

        return convertView;
    }

    private String convertObjectToTime(ActivationTime activationTime) {
        String day = activationTime.getDayOfMonth();
        String month = activationTime.getMonthValue();
        String year = activationTime.getYear();
        String hour = activationTime.getHour();
        String minute = activationTime.getMinute();
        return prependZero(day) + "-" + prependZero(month) + "-" + year + " " + prependZero(hour) + ":" + prependZero(minute);
    }

    private String prependZero(String text) {
        if (text.length() == 1) {
            return '0' + text;
        }
        return text;
    }
}

