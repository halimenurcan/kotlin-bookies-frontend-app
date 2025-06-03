package com.example.frontendbook.ui.notifications

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.R
import com.example.frontendbook.data.model.Notification

class NotificationAdapter(private val notificationList: List<Notification>) :
    RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val icon: ImageView = view.findViewById(R.id.notificationIcon)
        val message: TextView = view.findViewById(R.id.notificationMessage)
        val time: TextView = view.findViewById(R.id.notificationTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notificationList[position]
        holder.icon.setImageResource(notification.iconResId)
        holder.message.text = notification.message
        holder.time.text = notification.time
    }

    override fun getItemCount(): Int = notificationList.size
}


