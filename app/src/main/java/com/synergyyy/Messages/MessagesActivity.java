package com.synergyyy.Messages;

import androidx.core.view.GravityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.synergyyy.General.APIClient;
import com.synergyyy.General.MyBaseActivity;
import com.synergyyy.R;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessagesActivity extends MyBaseActivity {

    private static final String TAG = "";
    private RecyclerView recyclerView;
    private MessageAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String type;
    public String dateToday = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View viewLayout = layoutInflater.inflate(R.layout.activity_messages, null, false);
        drawer.addView(viewLayout, 0);
        activityNameTv.setText("Messages");

        recyclerView = findViewById(R.id.rv_messages);

        ArrayList<MessageCardDetails> messagesList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(MessagesActivity.this);
        recyclerView.setHasFixedSize(true);

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.show();

        Call<MessageObject> getMessageList = APIClient.getUserServices().getListOfMessages(token, username);
        getMessageList.enqueue(new Callback<MessageObject>() {
            @Override
            public void onResponse(Call<MessageObject> call, Response<MessageObject> response) {

                if (response.code() == 200) {
                    MessageObject messageObject = response.body();

                    List<MessageResponse> messageResponseList = messageObject.messages;
                    for (int i = 0; i < messageResponseList.size(); i++) {

                        String dateTimeLong = messageResponseList.get(i).createdDate;
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:MM aa");
                        String myDate = returnTimeString(dateTimeLong);

                        //  String date = dateFormat.format(dateTimeLong);
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
                            time = myDate;/* dateFormat.format(dateTimeLong);*/
                        } else
                            time = returnTimeFormateString(dateTimeLong);/*timeFormat.format(dateTimeLong);
                             */
                        type = messageResponseList.get(i).getType();
                        messagesList.add(new MessageCardDetails(messageResponseList.get(i).title, messageResponseList.get(i).getText(),
                                time, type, messageResponseList.get(i).getId(), messageResponseList.get(i).getSeen(), token));
                        mAdapter = new MessageAdapter(messagesList, MessagesActivity.this);
                    }
                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setAdapter(mAdapter);
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<MessageObject> call, Throwable t) {
                Toast.makeText(MessagesActivity.this, "Error: " + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String returnTimeString(String time) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date date = null;
        try {
            date = format.parse(time);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        String dateTime = sdf.format(date);
        return dateTime;
    }


    public String returnTimeFormateString(String time) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        Date date = null;
        try {
            date = format.parse(time);
            System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String dateTime = sdf.format(date);
        return dateTime;
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else super.onBackPressed();
    }
}