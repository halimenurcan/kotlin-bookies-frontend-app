package com.example.frontendbook.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.R
import com.example.frontendbook.data.model.Notification
import com.example.frontendbook.ui.base.adapter.NotificationAdapter

class NotificationsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        // RecyclerView referansı
        val recyclerView = view.findViewById<RecyclerView>(R.id.notificationrecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Dummy veriler
        val notifications = listOf(
            Notification(R.drawable.notifications_new, "Your profile image was removed", "32s ago"),
            Notification(R.drawable.notifications_new, "Tell us about your business", "1h ago"),
            Notification(R.drawable.notifications_new, "New feature released!", "Yesterday")
        )

        // Adapter bağlama
        val adapter = NotificationAdapter(notifications)
        recyclerView.adapter = adapter
        recyclerView.visibility = View.VISIBLE

        return view
    }
}
