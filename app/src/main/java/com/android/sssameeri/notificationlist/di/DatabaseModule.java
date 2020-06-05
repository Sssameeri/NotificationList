package com.android.sssameeri.notificationlist.di;

import android.app.Application;

import androidx.room.Room;

import com.android.sssameeri.notificationlist.data.dao.NotificationDao;
import com.android.sssameeri.notificationlist.data.database.Database;
import com.android.sssameeri.notificationlist.data.repository.Repository;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    @Inject
    Application application;


    public DatabaseModule(Application application) {
        this.application = application;
    }

    @Singleton
    @Provides
    Database provideDatabase() {
       return  Room.databaseBuilder(application,
                Database.class,
                "notification_database")
                .build();
    }
}
