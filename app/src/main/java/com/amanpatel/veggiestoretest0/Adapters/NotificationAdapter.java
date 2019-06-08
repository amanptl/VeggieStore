package com.amanpatel.veggiestoretest0.Adapters;

import android.app.Notification;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amanpatel.veggiestoretest0.Models.Notifications;
import com.amanpatel.veggiestoretest0.Network.GlideApp;
import com.amanpatel.veggiestoretest0.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<Notifications> notificationsList;
    private Context mContext;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);

        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Notifications notification = notificationsList.get(position);
        holder.message.setText(notification.getMessage());
        GlideApp.with(mContext).load(notification.getImage()).centerCrop().into(holder.image);
    }

    @Override
    public int getItemCount() {
        return notificationsList.size();
    }

    public NotificationAdapter(Context mContext, List<Notifications> notificationsList) {
        this.notificationsList = notificationsList;
        this.mContext = mContext;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView message;

        ViewHolder(View v) {
            super(v);
            image = v.findViewById(R.id.notification_image);
            message = v.findViewById(R.id.notification_message);
        }
    }
}

