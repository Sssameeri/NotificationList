package com.android.sssameeri.notificationlist.di;

import android.app.Application;

import com.android.sssameeri.notificationlist.NotificationApplication;
import com.android.sssameeri.notificationlist.data.dao.NotificationDao;
import com.android.sssameeri.notificationlist.data.repository.Repository;
import com.android.sssameeri.notificationlist.service.NotificationsService;
import com.android.sssameeri.notificationlist.ui.MainActivity;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {RepositoryModule.class, ApplicationModule.class, DatabaseModule.class, DaoModule.class})
@Singleton
public interface AppComponent {

     //Activity
     void inject(MainActivity mainActivity);

     //Application
     void inject(NotificationApplication notificationApplication);

     //Service
     void inject(NotificationsService notificationsService);

     Application getApplication();
}
