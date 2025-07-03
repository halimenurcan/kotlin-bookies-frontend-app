// app/src/main/java/com/example/frontendbook/ui/base/adapter/NotificationAdapter.kt
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

/**
 * Domain model Notification (data.model.Notification) ile çalışacak şekilde güncellendi.
 */
class NotificationAdapter(
    private val items: List<Notification>,
    private val onClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val icon: ImageView    = view.findViewById(R.id.notificationIcon)
        private val message: TextView  = view.findViewById(R.id.notificationMessage)
        private val time: TextView     = view.findViewById(R.id.notificationTime)

        fun bind(item: Notification) {
            // Türüne göre ikon seçilebilir
            val iconRes = when(item.type) {
                NotificationType.FOLLOW      -> R.drawable.add
                NotificationType.LIKE_COMMENT-> R.drawable.add
                NotificationType.FOLLOW_LIST -> R.drawable.add
            }
            icon.setImageResource(iconRes)

            message.text = item.message
            time.text    = item.time

            itemView.setOnClickListener { onClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(v)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
