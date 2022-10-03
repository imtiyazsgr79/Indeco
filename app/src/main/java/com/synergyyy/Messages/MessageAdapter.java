package com.synergyyy.Messages;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.synergyyy.General.APIClient;
import com.synergyyy.R;
import com.synergyyy.Search.EditFaultOnSearchActivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    private ArrayList<MessageCardDetails> messageList = new ArrayList<>();
    private Context context;

    public MessageAdapter(ArrayList<MessageCardDetails> messageList, Context context) {
        this.messageList = messageList;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_card, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        MessageCardDetails currentDetails = messageList.get(position);
        Boolean seen = messageList.get(position).getSeen();

        if (seen.equals(false)) {
            holder.cardViewMessages.setCardBackgroundColor(Color.parseColor("#FF0000"));
        }
        holder.mBody.setText(currentDetails.getMessageText());
        holder.mTitle.setText(currentDetails.getMessageTitle());
        holder.mTime.setText(currentDetails.getMessageTime());
        holder.mImage.setImageResource(R.drawable.user_icon);
        holder.seenTv.setText(String.valueOf(currentDetails.seen));
        String token = currentDetails.getToken();
        Integer id = currentDetails.getId();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callRead(String.valueOf(id), token);
            }
        });
    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }

    @Override
    public int getItemViewType(int position) {

        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mBody;
        public ImageView mImage;
        public TextView mTime;
        public TextView seenTv;
        public CardView cardViewMessages;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardViewMessages = itemView.findViewById(R.id.messages_card);
            mTitle = itemView.findViewById(R.id.message_title);
            mBody = itemView.findViewById(R.id.message_body);
            mImage = itemView.findViewById(R.id.imageView_message);
            mTime = itemView.findViewById(R.id.message_time);
            seenTv = itemView.findViewById(R.id.seenTv);
        }
    }

    private void callRead(String id, String token) {

        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.show();
        Call<SingleMessageResponse> callChat = APIClient.getUserServices().checkReadMessage(token, id);
        callChat.enqueue(new Callback<SingleMessageResponse>() {
            @Override
            public void onResponse(Call<SingleMessageResponse> call, Response<SingleMessageResponse> response) {

                if (response.code() == 200) {
                    SingleMessageResponse singleMessageResponse = response.body();
                    assert singleMessageResponse != null;
                    Log.d("TAG", "onResponse:ff " + singleMessageResponse.getType());
                    if (singleMessageResponse.getType().equals("GENERAL_NOTIFICATION")) {
                        Toast.makeText(context, "Please check playstore for app update", Toast.LENGTH_SHORT).show();
                    } else {
                        String frid = singleMessageResponse.getExtras().getId();
                        Intent intent = new Intent(context, EditFaultOnSearchActivity.class);
                        intent.putExtra("frId", frid);
                        context.startActivity(intent);
                        ((Activity) context).finish();

                    }
                }
                progressDialog.dismiss();


            }

            @Override
            public void onFailure(Call<SingleMessageResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

}
