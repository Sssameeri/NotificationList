package com.android.sssameeri.notificationlist.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "notification")
public class NotificationModel {

    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "application_name")
    private String applicationName;
    @ColumnInfo(name = "notification_text")
    private String notificationText;
    @ColumnInfo(name = "notification_date")
    private String date;
    @ColumnInfo(name = "application_package")
    private String applicationPackage;


    public String getApplicationPackage() {
        return applicationPackage;
    }

    public void setApplicationPackage(String applicationPackage) {
        this.applicationPackage = applicationPackage;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
