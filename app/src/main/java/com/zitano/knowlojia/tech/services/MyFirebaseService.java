package com.zitano.knowlojia.tech.services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.TaskStackBuilder;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zitano.knowlojia.tech.MainActivity;
import com.zitano.knowlojia.tech.R;

import java.util.Random;

public class MyFirebaseService extends FirebaseMessagingService {

    @Override
    public void onNewToken (String s) {
        super.onNewToken(s);
        Log.d("TOKEN_UPDATE", s);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getNotification() != null){
            final String title = remoteMessage.getNotification().getTitle();
            final String body = remoteMessage.getNotification().getBody();
            String url ="";
            if (remoteMessage.getData() != null)
                url = remoteMessage.getData().get("image"); //Image url will be sent with the data payload key = image
            if (!TextUtils.isEmpty(url)){
                final String finalUrl = url;
                //create thread to load picture from url
                new Handler(Looper.getMainLooper())
                        .post(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.get()
                                        .load(finalUrl)
                                        .into(new Target() {
                                            @Override
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                showNotification(MyFirebaseService.this,
                                                        title,body,null,bitmap);
                                            }

                                            @Override
                                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                                            }
                                        });
                            }
                        });
            }
            else
                showNotification(MyFirebaseService.this,title,body,null,null);
        }
        else {
            final String title = remoteMessage.getData().get("title");
            final String body = remoteMessage.getData().get("body");
            String url = remoteMessage.getData().get("image"); //Image url will be sent with the data payload key = image
            if (!TextUtils.isEmpty(url)){
                final String finalUrl = url;
                //create thread to load picture from url
                new Handler(Looper.getMainLooper())
                        .post(new Runnable() {
                            @Override
                            public void run() {
                                Picasso.get()
                                        .load(finalUrl)
                                        .into(new Target() {
                                            @Override
                                            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                                showNotification(MyFirebaseService.this,
                                                        title,body,null,bitmap);
                                            }

                                            @Override
                                            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

                                            }

                                            @Override
                                            public void onPrepareLoad(Drawable placeHolderDrawable) {

                                            }
                                        });
                            }
                        });
            }
            else
                showNotification(MyFirebaseService.this,title,body,null,null);

        }

    }

    private  void showNotification(Context context,
                                   String title,
                                   String body,
                                   Intent pendingIntent,
                                   Bitmap bitmap){
        //NotificationManager notificationManager = (NotificationManager)context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        NotificationCompat.BigPictureStyle style = new NotificationCompat.BigPictureStyle();
        style.bigPicture(bitmap);
        //FirebaseMessaging.getInstance().subscribeToTopic("all");

        int notificationId = new Random().nextInt();
        String channelId = "knowlojia-911";
        String channelName = "knowlojia";

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O)
        {
            NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
        NotificationCompat.Builder builder;
        if (bitmap != null)
            builder = new NotificationCompat.Builder(this,channelId)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setLargeIcon(bitmap)
                    .setStyle(style)
                    .setContentText(body);
        else
            builder = new NotificationCompat.Builder(this,channelId)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(body))
                    .setContentText(body);
        if (pendingIntent != null)
        {
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addNextIntent(pendingIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            builder.setAutoCancel(true);
        }
        else {
            Intent notifIntent = new Intent(this, MainActivity.class);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0, notifIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(resultPendingIntent);
            builder.setAutoCancel(true);

        }


        notificationManager.notify(notificationId,builder.build());
    }
}

