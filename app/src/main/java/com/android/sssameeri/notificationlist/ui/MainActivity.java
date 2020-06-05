package com.android.sssameeri.notificationlist.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.sssameeri.notificationlist.NotificationApplication;
import com.android.sssameeri.notificationlist.R;
import com.android.sssameeri.notificationlist.data.repository.Repository;
import com.android.sssameeri.notificationlist.di.AppComponent;
import com.android.sssameeri.notificationlist.service.NotificationsService;
import com.google.android.material.button.MaterialButton;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private MaterialButton startService;
    private RecyclerView notificationRecyclerView;

    private NotificationAdapter notificationAdapter;
    private View view;
    private boolean isStarted = false;

    @Inject
    Repository repository;

    private CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((NotificationApplication) getApplication()).getComponent().inject(this);

        //Set toolbar
        Toolbar toolbar = findViewById(R.id.appBar);
        setSupportActionBar(toolbar);

        compositeDisposable = new CompositeDisposable();

        //FindViews
        view = findViewById(R.id.noItemsLayout);
        notificationRecyclerView = findViewById(R.id.notificationsRecyclerView);
        startService = findViewById(R.id.startService);

        //Setup RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        notificationRecyclerView.setLayoutManager(linearLayoutManager);
        notificationAdapter = new NotificationAdapter(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Get notifications list
        getAllNotifications();

        //Service Intent
        Intent intent = new Intent(this, NotificationsService.class);

        //Start service button
        startService.setOnClickListener(v -> {
            //ask for permission
            if (!NotificationManagerCompat.getEnabledListenerPackages(this).contains(getPackageName())) {
                Toast.makeText(this, R.string.need_permission, Toast.LENGTH_LONG).show();
                Intent permissionIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
                startActivity(permissionIntent);
            } else {
                if(isStarted) {
                    stopServiceInSettings();
                    stopService(intent);
                    startService.setText(R.string.start);
                    startService.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    isStarted = false;
                } else {
                    startService(intent);
                    startService.setText(R.string.stop);
                    startService.setBackgroundColor(getResources().getColor(R.color.buttonStop));
                    isStarted = true;
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    //Костыль, который убивает сервис, но после запуска этих комманд система добавляет сервис в блеклист.
    private void stopService() {
        PackageManager pm  = getApplicationContext().getPackageManager();
        ComponentName componentName = new ComponentName(getApplicationContext(),
                NotificationsService.class);
        pm.setComponentEnabledSetting(componentName,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    //Stop service in settings
    private void stopServiceInSettings() {
        Toast.makeText(this, R.string.stop_service_here, Toast.LENGTH_LONG).show();
        Intent permissionIntent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
        startActivity(permissionIntent);
    }

    //Get all notifications from database
    private void getAllNotifications() {
        compositeDisposable.add(repository.getNotificationList().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            if(result.isEmpty()) {
                                view.setVisibility(View.VISIBLE);
                            } else {
                                view.setVisibility(View.GONE);
                                notificationAdapter.setNotificationModelList(result);
                                notificationRecyclerView.setAdapter(notificationAdapter);
                            }
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        }
                )
        );
    }

    //Get hour notifications from database
    private void getHourNotifications() {
        compositeDisposable.add(repository.getNotificationsByHour()
                .subscribe(
                        result -> {
                            notificationAdapter.setNotificationModelList(result);
                            notificationRecyclerView.setAdapter(notificationAdapter);
                        },
                        throwable -> {
                            Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                        }
                ));
    }

    //Get day notifications from database
    private void getDayNotifications() {
        compositeDisposable.add(
                repository.getNotificationsByDay()
                        .subscribe(
                                result -> {
                                    notificationAdapter.setNotificationModelList(result);
                                    notificationRecyclerView.setAdapter(notificationAdapter);
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    //Get month notifications from database
    private void getMonthNotifications() {
        compositeDisposable.add(
                repository.getNotificationsByMonth()
                        .subscribe(
                                result -> {
                                    notificationAdapter.setNotificationModelList(result);
                                    notificationRecyclerView.setAdapter(notificationAdapter);
                                },
                                throwable -> {
                                    Toast.makeText(getApplicationContext(), R.string.error, Toast.LENGTH_SHORT).show();
                                }
                        )
        );
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.all: {
                getAllNotifications();
                item.setChecked(true);
                break;
            } case R.id.perHour: {
                getHourNotifications();
                item.setChecked(true);
                break;
            } case R.id.perDay: {
                getDayNotifications();
                item.setChecked(true);
                break;
            } case R.id.perMonth: {
                getMonthNotifications();
                item.setChecked(true);
                break;
            }
        }
        return true;
    }


    //Inflate menu from app_bar_menu resource
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.app_bar_menu, menu);
        return true;
    }
}