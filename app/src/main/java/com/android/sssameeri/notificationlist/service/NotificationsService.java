package com.android.sssameeri.notificationlist.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.renderscript.RenderScript;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.android.sssameeri.notificationlist.R;
import com.android.sssameeri.notificationlist.data.dao.NotificationDao;
import com.android.sssameeri.notificationlist.data.database.Database;
import com.android.sssameeri.notificationlist.model.NotificationModel;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;


/*
* Данный сервис запускается постоянно, как только приходит уведомление.
* При запуске создается уведомление в статус баре, НО убить данный сервис нельзя.
* Для остановки даного сервиса можно вызывать Intent ACTION_NOTIFICATION_LISTENER_SETTINGS
* И убирать разрешение на прослушку уведомлений
* */
public class NotificationsService extends NotificationListenerService {

    public static final String CHANNEL_ID = "com.android.sssameeri.notificationlist";
    public static final int notificationId = 13;

    private NotificationModel notificationModel;
    private NotificationDao notificationDao;

    private NotificationCompat.Builder notification;

    private CompositeDisposable compositeDisposable;

    @Override
    public void onCreate() {
        super.onCreate();
        compositeDisposable = new CompositeDisposable();
        notificationModel = new NotificationModel();

        Database database = Database.getInstance(this);
        notificationDao = database.getNotificationDao();

        notification = createNotification();
        notification
                .setContentTitle(getResources().getString(R.string.notification_description))
                .setContentText(getResources().getString(R.string.notification_description))
                .setSmallIcon(R.drawable.ic_baseline_sort_24);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startForeground(notificationId, notification.build());
        return START_STICKY;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentDate = simpleDateFormat.format(new Date());

        notificationModel.setNotificationText(notification.extras.getCharSequence(Notification.EXTRA_TEXT).toString());
        notificationModel.setApplicationName(notification.extras.getCharSequence(Notification.EXTRA_TITLE).toString());
        notificationModel.setApplicationPackage(sbn.getPackageName());
        notificationModel.setDate(currentDate);

        compositeDisposable.add(notificationDao.insertNotification(notificationModel)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(
                () -> {
                    Log.d("TAG", "Inserted");
                }, throwable -> {
                    Toast.makeText(this, R.string.service_error, Toast.LENGTH_SHORT).show();
                }
        ));
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    private NotificationCompat.Builder createNotification() {
        createNotificationChannel();
       return new NotificationCompat.Builder(this, CHANNEL_ID);
    }

    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.notification_channel_name);
            String description = getString(R.string.notification_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}
