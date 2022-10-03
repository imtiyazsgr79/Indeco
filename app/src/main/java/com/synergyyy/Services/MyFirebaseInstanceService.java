package com.synergyyy.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.synergyyy.General.Constants;
import com.synergyyy.EquipmentSearch.PmTaskActivity;
import com.synergyyy.PurchaseOder.UploadPurchasePdf;
import com.synergyyy.R;
import com.synergyyy.Search.EditFaultOnSearchActivity;
import com.synergyyy.UploadPdf.UploadPdf;

import androidx.core.app.NotificationCompat;

import org.jetbrains.annotations.NotNull;

import java.util.Random;

import static com.synergyyy.General.MainActivityLogin.SHARED_PREFS;

public class MyFirebaseInstanceService extends FirebaseMessagingService {
    private FusedLocationProviderClient client;
    double longitude, latitude;
    String workspace, id;
    Intent intent;
    int count;
    PendingIntent pendingIntent;
    ProgressDialog progressDialog;


    @Override
    public void onNewToken(@NotNull String token) {
        super.onNewToken(token);
        getSharedPreferences("_", MODE_PRIVATE).edit().putString("fcm_token", token).apply();
    }

    @Override
    public void onDeletedMessages() {
        super.onDeletedMessages();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        count=count+1;
        Random rand = new Random();
        rand.nextInt(10000);

        Log.d("TEST123", "onMessageReceived: " + remoteMessage.getData());
        String title = remoteMessage.getData().get("title");
        String body = remoteMessage.getData().get("message");
        String click_action = remoteMessage.getData().get("click_action");
        workspace = remoteMessage.getData().get("workspace");
        id = remoteMessage.getData().get("id");
        String msgId = remoteMessage.getData().get("msgId");
        String equipCode = "";
        String taskNumber = "";
        String afterImage = "";
        String beforeImage = "";
        String source = "search";
        SharedPreferences sharedPreferences = this.getApplicationContext().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        sharedPreferences.edit().putString("workspace", workspace).apply();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        intent = null;
        if (click_action.equals(Constants.UPLOAD_QUOTATION_ACTIVITY)) {
            intent = new Intent(MyFirebaseInstanceService.this, UploadPdf.class);
            intent.putExtra("frId", id);
            if ((remoteMessage.getData().get("remark") != null)) {
                intent.putExtra("remarks", remoteMessage.getData().get("remark").toString());
            }
            intent.putExtra("title", title);
            intent.putExtra("msgId", msgId);
            intent.putExtra("workspace", workspace);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(MyFirebaseInstanceService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        if (click_action.equals(Constants.PURCHASE_ORDER_ACTIVITY)) {
            intent = new Intent(MyFirebaseInstanceService.this, UploadPurchasePdf.class);
            intent.putExtra("frId", id);
            intent.putExtra("workspace", workspace);
            intent.putExtra("msgId", msgId);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(MyFirebaseInstanceService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }

        if (click_action.equals(Constants.EDIT_FAULT_REPORT_ACTIVITY_NOTIFICATION)) {
            intent = new Intent(MyFirebaseInstanceService.this, EditFaultOnSearchActivity.class);
            intent.putExtra("frId", id);
            intent.putExtra("msgId", msgId);
            intent.putExtra("workspace", workspace);
            intent.putExtra("longitude", longitude);
            intent.putExtra("latitude", latitude);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(MyFirebaseInstanceService.this, rand.nextInt(10000), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }
        if (click_action.equals(Constants.PM_TASK_ACTIVITY_NOTIFICATION)) {
            intent = new Intent(this, PmTaskActivity.class);
            intent.putExtra("taskId", Integer.parseInt(id));
            intent.putExtra("taskNumber", taskNumber);
            intent.putExtra("afterImage", afterImage);
            intent.putExtra("beforeImage", beforeImage);
            intent.putExtra("source", source);
            intent.putExtra("msgId", msgId);
            intent.putExtra("workspace", workspace);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }


        String channelId = "my_channel_id";
        CharSequence channelName = "My Channel";
        int importance = 0;
        NotificationChannel notificationChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            importance = NotificationManager.IMPORTANCE_MAX;
            notificationChannel = new NotificationChannel(channelId, channelName, importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(body)
                .setSmallIcon(R.drawable.i_cmms)
                .setNumber(count)
                .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                .setContentIntent(pendingIntent)
                .setChannelId(channelId)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(rand.nextInt(10000), notification);
    }


}

