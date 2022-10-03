package com.synergyyy.Messages;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;

import java.util.ArrayList;

public class ChatActivity extends MyBaseActivity {

    private static final String TAG = "";
    private RecyclerView recyclerView;
    private ChatAdapter chatAdapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_chat, null, false);
        drawer.addView(viewLayout, 0);
        recyclerView = findViewById(R.id.rv_chat);
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        activityNameTv.setText("Chat Activity");
        setSupportActionBar(toolbar);

        layoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setHasFixedSize(true);

        ArrayList<ChatCardDetails> chatList = new ArrayList<>();

        ProgressDialog progressBar = new ProgressDialog(this);
        progressBar.setIndeterminate(true);
        progressBar.setCancelable(false);
        progressBar.show();

        String dateToday = new MessagesActivity().dateToday;

       /* Call<SingleMessageResponse> callChat = APIClient.getUserServices().checkReadMessage(token, id);
        callChat.enqueue(new Callback<SingleMessageResponse>() {
            @Override
            public void onResponse(Call<SingleMessageResponse> call, Response<SingleMessageResponse> response) {
                if (response.code() == 200) {
                    SingleMessageResponse messageResponseList = response.body();

                        String title = messageResponseList.getTitle();
                        String body = messageResponseList.getText();

                        String dateTimeLong = messageResponseList.getCreatedDate();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:MM");

                        String myDate = new MessagesActivity().returnTimeString(dateTimeLong);


                        String time;
                        Date today = new Date();
                        Date incomingToday = new Date();
                        try {
                            today = dateFormat.parse(dateToday);
                            incomingToday = dateFormat.parse(myDate);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if (incomingToday.before(today)) {
                            time = myDate;
                        } else time = new MessagesActivity().returnTimeFormateString(dateTimeLong);

                        chatList.add(new ChatCardDetails(title, body, time));

                    chatAdapter = new ChatAdapter(chatList);
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(chatAdapter);
                    chatAdapter.notifyDataSetChanged();

                } else
                    Toast.makeText(ChatActivity.this, "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }

            @Override
            public void onFailure(Call<SingleMessageResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }
        });*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}