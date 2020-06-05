package com.android.sssameeri.notificationlist;

import android.app.Application;

import com.android.sssameeri.notificationlist.di.AppComponent;
import com.android.sssameeri.notificationlist.di.ApplicationModule;
import com.android.sssameeri.notificationlist.di.DaggerAppComponent;
import com.android.sssameeri.notificationlist.di.DaoModule;
import com.android.sssameeri.notificationlist.di.DatabaseModule;
import com.android.sssameeri.notificationlist.di.RepositoryModule;

public class NotificationApplication extends Application {

    protected AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();


        appComponent = DaggerAppComponent
                .builder()
                .applicationModule(new ApplicationModule(this))
                .daoModule(new DaoModule())
                .databaseModule(new DatabaseModule(this))
                .repositoryModule(new RepositoryModule())
                .build();

        appComponent.inject(this);
    }

    public AppComponent getComponent() {
        return appComponent;
    }
}
