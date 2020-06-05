package com.android.sssameeri.notificationlist.di;

import android.app.Application;

import com.android.sssameeri.notificationlist.data.dao.NotificationDao;
import com.android.sssameeri.notificationlist.data.database.Database;
import com.android.sssameeri.notificationlist.data.repository.Repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Singleton
    @Provides
    public Repository provideRepository(Database database, NotificationDao notificationDao) {
        return new Repository(database, notificationDao);
    }

}
