package com.android.sssameeri.notificationlist.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.sssameeri.notificationlist.R;
import com.android.sssameeri.notificationlist.model.NotificationModel;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private List<NotificationModel> notificationModelList;
    private Context context;

    public NotificationAdapter(Context context) {
        this.context = context;
    }

    public void setNotificationModelList(List<NotificationModel> notificationModelList) {
        this.notificationModelList = notificationModelList;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        NotificationModel notificationModel = notificationModelList.get(position);

        try {
            Drawable icon = context.getPackageManager().getApplicationIcon(notificationModel.getApplicationPackage());
            holder.iconImgView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
           e.printStackTrace();
        }


        //Split date(2020-06-05 10:53) from database to date(ex. 2020-06-05) and time (ex. 10:53)
        String dateTime = notificationModel.getDate();
        String[] dateArray = dateTime.split(" ");

        holder.appNameTxtView.setText(notificationModel.getApplicationName());
        holder.notificationTextTxtView.setText(notificationModel.getNotificationText());
        holder.dateTxtView.setText(dateArray[0]);
        holder.timeTxtView.setText(dateArray[1]);

    }

    @Override
    public int getItemCount() {
        return notificationModelList.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{

        private ImageView iconImgView;
        private TextView appNameTxtView;
        private TextView notificationTextTxtView;
        private TextView timeTxtView;
        private TextView dateTxtView;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);

            iconImgView = itemView.findViewById(R.id.appIconImgView);
            appNameTxtView = itemView.findViewById(R.id.appNameTxtView);
            notificationTextTxtView = itemView.findViewById(R.id.notificationDescriptionTxtView);
            timeTxtView = itemView.findViewById(R.id.timeTxtView);
            dateTxtView = itemView.findViewById(R.id.dateTxtView);

        }
    }
}
