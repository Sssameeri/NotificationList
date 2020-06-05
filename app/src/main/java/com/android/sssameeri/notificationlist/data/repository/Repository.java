package com.android.sssameeri.notificationlist.data.repository;

import android.app.Application;

import com.android.sssameeri.notificationlist.data.dao.NotificationDao;
import com.android.sssameeri.notificationlist.data.database.Database;
import com.android.sssameeri.notificationlist.model.NotificationModel;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class Repository {

    Database database;

    NotificationDao notificationDao;

    @Inject
    public Repository(Database database, NotificationDao notificationDao) {
        this.database = database;
        this.notificationDao = notificationDao;
    }

    public Flowable<List<NotificationModel>> getNotificationList() {
        return notificationDao.getAllNotifications().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<NotificationModel>> getNotificationsByHour() {
        return notificationDao.getNotificationsByHour().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<NotificationModel>> getNotificationsByDay() {
        return notificationDao.getNotificationsByDay().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

    public Flowable<List<NotificationModel>> getNotificationsByMonth() {
        return notificationDao.getNotificationsByMonth().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}
