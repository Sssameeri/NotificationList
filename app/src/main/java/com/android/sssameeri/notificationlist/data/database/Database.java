package com.android.sssameeri.notificationlist.data.database;


import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.android.sssameeri.notificationlist.data.dao.NotificationDao;
import com.android.sssameeri.notificationlist.model.NotificationModel;

import io.reactivex.Notification;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@androidx.room.Database(version = 1, entities = {NotificationModel.class})
public abstract class Database extends RoomDatabase {

    public abstract NotificationDao getNotificationDao();

    private static Database instance;

    public static Database getInstance(Context context) {
        if(instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class,
                    "notification_database")
                    .build();
        }
        return instance;
    }
}
