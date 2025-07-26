package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.R
import com.example.frontendbook.data.model.Notification
import com.example.frontendbook.data.model.NotificationType

class NotificationAdapter(
    private var items: MutableList<Notification>,
    private val onClick: (Notification) -> Unit,
    private val onDeleteClick: (Notification) -> Unit
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val icon: ImageView = view.findViewById(R.id.notificationIcon)
        private val message: TextView = view.findViewById(R.id.notificationMessage)
        private val senderUsername: TextView = view.findViewById(R.id.notificationSenderUsername)
        private val time: TextView = view.findViewById(R.id.notificationTime)
        private val deleteButton: ImageButton = view.findViewById(R.id.notDeleteButton)

        fun bind(item: Notification) {
            icon.setImageResource(item.type.getIconRes())
            message.text = "${item.type.getTitle()}\n${item.message}"
            senderUsername.text = "from @${item.senderUsername}"
            time.text = item.time
            deleteButton.setOnClickListener { onDeleteClick(item) }
            itemView.setOnClickListener { onClick(item) }
        }
    }

    fun updateItems(newItems: List<Notification>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
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
