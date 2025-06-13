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
import com.example.frontendbook.data.model.NotificationType
import com.example.frontendbook.ui.base.adapter.NotificationAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

class NotificationsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notifications, container, false)

        recyclerView = view.findViewById(R.id.notificationrecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Bildirimleri sunucudan al
        fetchNotifications()

        return view
    }

    private fun fetchNotifications() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://senin-api-adresin.com/") // <- BURAYI KENDİ API URL’İN İLE DEĞİŞTİR
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(NotificationApi::class.java)

        api.getNotifications().enqueue(object : Callback<List<Notification>> {
            override fun onResponse(
                call: Call<List<Notification>>,
                response: Response<List<Notification>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val notifications = response.body()!!
                    adapter = NotificationAdapter(notifications) { notification ->
                        handleNotificationClick(notification)
                    }
                    recyclerView.adapter = adapter
                    recyclerView.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<Notification>>, t: Throwable) {
                // Hata durumu
                t.printStackTrace()
            }
        })
    }

    private fun handleNotificationClick(notification: Notification) {
        when (notification.type) {
            NotificationType.FOLLOW -> {
                val userId = notification.relatedId
                // TODO: user profil sayfasına git
            }

            NotificationType.LIKE_COMMENT -> {
                val commentId = notification.relatedId
                // TODO: yorum detay sayfasına git
            }

            NotificationType.FOLLOW_LIST -> {
                val listId = notification.relatedId
                // TODO: liste detay sayfasına git
            }
        }
    }


    interface NotificationApi {
        @GET("notifications") // → örnek endpoint
        fun getNotifications(): Call<List<Notification>>
    }
}
