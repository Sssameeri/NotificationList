package com.android.sssameeri.notificationlist.di;

import com.android.sssameeri.notificationlist.data.dao.NotificationDao;
import com.android.sssameeri.notificationlist.data.database.Database;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DaoModule {

    @Provides
    @Singleton
    public NotificationDao provideNotificationDao(Database database) {
        return database.getNotificationDao();
    }

}
