package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.R
import com.example.frontendbook.data.model.Notification

// Bildirimleri göstermek için RecyclerView adapter sınıfı
class NotificationAdapter(
    private val notificationList: List<Notification>, // Gösterilecek bildirim listesi
    private val onNotificationClick: (Notification) -> Unit // Tıklanınca yapılacak işlem
) : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    // RecyclerView her bir kart (satır) için bu ViewHolder sınıfını kullanır
    inner class NotificationViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        // Bildirim kartındaki görsel ve metin bileşenleri
        val icon: ImageView = view.findViewById(R.id.notificationIcon)
        val message: TextView = view.findViewById(R.id.notificationMessage)
        val time: TextView = view.findViewById(R.id.notificationTime)

        // Her bir bildirimi view'a bağlayan metod
        fun bind(notification: Notification) {
            // Verileri ilgili UI öğelerine yerleştir
            icon.setImageResource(notification.iconResId)
            message.text = notification.message
            time.text = notification.time

            // Kart tıklanınca dışarıdan verilen callback çalıştırılır
            itemView.setOnClickListener {
                onNotificationClick(notification)
            }
        }
    }

    // Yeni bir ViewHolder oluşturulur (her yeni satır için çağrılır)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_notification, parent, false)
        return NotificationViewHolder(view)
    }

    // Belirli bir pozisyondaki veriyi ilgili ViewHolder ile bağlar
    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notificationList[position])
    }

    // Toplam bildirim sayısı
    override fun getItemCount(): Int = notificationList.size
}
