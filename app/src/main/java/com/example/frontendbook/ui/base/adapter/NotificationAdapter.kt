package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.R
import com.example.frontendbook.data.model.Notification
import com.example.frontendbook.data.model.NotificationType

class NotificationAdapter(
    private val items: List<Notification>,
    private val onClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val icon: ImageView = view.findViewById(R.id.notificationIcon)
        private val message: TextView = view.findViewById(R.id.notificationMessage)
        private val time: TextView = view.findViewById(R.id.notificationTime)

        fun bind(item: Notification) {
            // 1. Tür bazlı ikon belirleme
            val iconRes = when (item.type) {
                NotificationType.FOLLOW -> R.drawable.notificationsfollow
                NotificationType.LIKE_COMMENT -> R.drawable.notificationslike
                NotificationType.FOLLOW_LIST -> R.drawable.notificationslikelists
            }
            icon.setImageResource(iconRes)

            // 2. Tür bazlı mesaj ayarlama
            val titleText = when (item.type) {
                NotificationType.FOLLOW -> "Yeni bir takipçin var!"
                NotificationType.LIKE_COMMENT -> "Yorumun beğenildi!"
                NotificationType.FOLLOW_LIST -> "Listene biri bayıldı!"
            }
            message.text = "$titleText\n${item.message}" // mesaj altına açıklama

            // 3. Zaman gösterimi
            time.text = item.time

            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
