package com.synergyyy.EquipmentSearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.synergyyy.R;

import java.util.ArrayList;

public class EquipmentSearchAdapter extends RecyclerView.Adapter<EquipmentSearchAdapter.MyViewHolder> {

    public ArrayList<EquipmentSearchCard> equipmentSearchCards;
    Context context;

    public EquipmentSearchAdapter(ArrayList<EquipmentSearchCard> equipmentSearchCards) {
        this.equipmentSearchCards = equipmentSearchCards;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_search_adapter, parent, false);
        return new MyViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        EquipmentSearchCard currentPosition = equipmentSearchCards.get(position);
        holder.taskIdTV.setText(String.valueOf(currentPosition.getTaskId()));
        holder.statusTV.setText(String.valueOf(currentPosition.getStatus()));
        holder.buildingTV.setText(String.valueOf(currentPosition.getBuildingName()));
        holder.locationTV.setText(String.valueOf(currentPosition.getLocationName()));
        String currentSchedule = null;
        if (currentPosition.getScheduleDate() != 0) {
            currentSchedule = new PmTaskActivity().getDate(currentPosition.getScheduleDate(), "dd-MM-yyyy");
        }
        holder.scheduleTV.setText(currentSchedule);
        holder.taskNumberTV.setText(currentPosition.getTaskNumber());
        String workspace = currentPosition.getWorkspace();
        String afterImage = currentPosition.getAfterImage();
        String beforeImage = currentPosition.getBeforeImage();
        String source = currentPosition.getSource();
        String equipName = currentPosition.getEquipName();

        int taskId1 = Math.toIntExact(currentPosition.getTaskId());

        holder.equipNameTV.setText(equipName);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PmTaskActivity.class);
                intent.putExtra("taskNumber", currentPosition.getTaskNumber());
                intent.putExtra("taskId", taskId1);
                intent.putExtra("workspace", workspace);
                intent.putExtra("afterImage", afterImage);
                intent.putExtra("source", source);
                intent.putExtra("beforeImage", beforeImage);
                v.getContext().startActivity(intent);
                ((Activity) v.getContext()).finish();
            }
        });
    }

    @Override
    public int getItemCount() {
        return equipmentSearchCards.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView taskNumberTV, taskIdTV, statusTV, buildingTV, locationTV, scheduleTV, equipNameTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            taskNumberTV = itemView.findViewById(R.id.taskNumberRV);
            taskIdTV = itemView.findViewById(R.id.taskIdRV);
            statusTV = itemView.findViewById(R.id.statusRV);
            buildingTV = itemView.findViewById(R.id.buildingRV);
            locationTV = itemView.findViewById(R.id.locationRV);
            scheduleTV = itemView.findViewById(R.id.schduleDateRV);
            equipNameTV = itemView.findViewById(R.id.equipNameSearch);
        }
    }
}
