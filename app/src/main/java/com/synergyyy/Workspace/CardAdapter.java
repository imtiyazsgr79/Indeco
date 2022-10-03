package com.synergyyy.Workspace;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.synergyyy.R;
import com.synergyyy.Search.Search;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;
import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ViewHolder> {
    public CardAdapter(ArrayList<CardDetails> mCardDetails, Context context) {
        this.mCardDetails = mCardDetails;
        this.context = context;
    }

    private String variable;
    private ArrayList<CardDetails> mCardDetails;
    private static final String TAG = "CardAdapter";
    private Context context;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyler_card, parent, false);
        ViewHolder farmsViewHolder = new ViewHolder(v);
        return farmsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final CardDetails currentItem = mCardDetails.get(position);


        holder.tvWorkID.setText(currentItem.getWorkspaceId());
        holder.tvDesc.setText(currentItem.getBuildingDescription().toUpperCase());

        SharedPreferences sharedPreferences = this.context.getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView workspaceTextView = holder.itemView.findViewById(R.id.worskspace_tv);
                String id = workspaceTextView.getText().toString();
                sharedPreferences.edit().putString("workspace", id).apply();
                if(id=="CMMS-SPORTSHUB-072022-001"){
                    Intent intent = new Intent(v.getContext(), SearchSportsHub.class);
                }
                Intent intent = new Intent(v.getContext(), Search.class);
                intent.putExtra("workspace", id);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {

        return mCardDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvDesc;
        public TextView tvWorkID;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDesc = itemView.findViewById(R.id.worskspace_tv);
            tvWorkID = itemView.findViewById(R.id.desc_tv);
        }
    }

}
