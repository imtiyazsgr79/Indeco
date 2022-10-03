package com.synergyyy.Messages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.synergyyy.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    ArrayList<ChatCardDetails> chatList = new ArrayList<>();

    public ChatAdapter(ArrayList<ChatCardDetails> chatList) {
        this.chatList = chatList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_recyclerview, parent, false);
        ChatAdapter.ViewHolder farmsViewHolder = new ChatAdapter.ViewHolder(v);
        return farmsViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final ChatCardDetails currentDetails = chatList.get(position);
        holder.chatTime.setText(currentDetails.getChatTime());
        holder.chatBody.setText(currentDetails.getChatBody());
        holder.chatTitle.setText(currentDetails.getChatTitle());
//        holder.chatCard.setCardBackgroundColor(Color.parseColor("#ff0000"));

    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView chatTitle, chatBody, chatTime;
        public CardView chatCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            chatCard=itemView.findViewById(R.id.chat_card);
            chatTitle = itemView.findViewById(R.id.title_chat);
            chatBody = itemView.findViewById(R.id.body_chat);
            chatTime = itemView.findViewById(R.id.time_chat);
        }
    }
}
