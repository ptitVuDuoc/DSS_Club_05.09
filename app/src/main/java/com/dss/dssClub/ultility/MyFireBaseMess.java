package com.dss.dssClub.ultility;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;

import com.dss.dssClub.R;
import com.dss.dssClub.activity.DetailNotifiActivity;
import com.dss.dssClub.activity.NotifiActivity;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

public class MyFireBaseMess extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Map data = remoteMessage.getData();
        String id = (String) data.get("id");
        sendNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody(), id);

        updateNotifiNew();

//        FirebaseInstanceId.getInstance().getToken();

        super.onMessageReceived(remoteMessage);
    }

    private void updateNotifiNew() {
        Intent intent = new Intent(Statistic.ACTION_BROAD_CAST);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    private void sendNotification(String title, String messageBody, String id) {

        Intent intent = new Intent(this, NotifiActivity.class);

        intent.putExtra(KeyConst.KEY_ID_NOTIFI, id);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("0", "Dss_Club", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "0")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.mipmap.ic_launcher)
//                .setContentTitle("DSS - Club")
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
