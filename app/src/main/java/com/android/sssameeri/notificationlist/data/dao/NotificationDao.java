package com.android.sssameeri.notificationlist.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.android.sssameeri.notificationlist.model.NotificationModel;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

@Dao
public interface NotificationDao {

    @Insert
    Completable insertNotification(NotificationModel notificationModel);

    @Query("SELECT * FROM notification")
    Flowable<List<NotificationModel>> getAllNotifications();

    @Query("SELECT * FROM notification WHERE datetime(notification_date) >= datetime('now', '-1 days')")
    Flowable<List<NotificationModel>> getNotificationsByDay();

    @Query("SELECT * FROM notification WHERE datetime(notification_date) >= datetime('now', '-1 months')")
    Flowable<List<NotificationModel>> getNotificationsByMonth();

    @Query("SELECT * FROM notification WHERE datetime(notification_date) >= datetime('now', '-1 hours')")
    Flowable<List<NotificationModel>> getNotificationsByHour();
}
