package com.example.frontendbook.ui.notifications

import com.example.frontendbook.R

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.databinding.FragmentNotificationsBinding
import com.example.frontendbook.ui.base.adapter.NotificationAdapter
import com.example.frontendbook.ui.homePage.ReviewsFragment
import com.example.frontendbook.ui.listdetail.ListDetailFragment
import com.example.frontendbook.ui.profile.OtherUserProfileFragment

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NotificationsViewModel
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel’i Factory üzerinden al
        viewModel = ViewModelProvider(this, NotificationsViewModelFactory(requireContext()))
            .get(NotificationsViewModel::class.java)

        // RecyclerView ayarları
        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Bildirimler LiveData olarak gözlemleniyor
        viewModel.notifications.observe(viewLifecycleOwner) { notifications ->
            adapter = NotificationAdapter(notifications) { clickedNotification ->
                viewModel.markAsRead(clickedNotification) // Bildirimi okundu olarak işaretle
                handleNotificationClick(clickedNotification) // Türüne göre yönlendir
            }
            binding.notificationsRecyclerView.adapter = adapter
        }

        // Hata durumunu gözlemle
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        // Veriyi yükle
        viewModel.loadNotifications()
    }

    private fun handleNotificationClick(notification: com.example.frontendbook.data.model.Notification) {
        when (notification.type) {
            com.example.frontendbook.data.model.NotificationType.FOLLOW -> {
                val userId = notification.relatedId
                val fragment = OtherUserProfileFragment().apply {
                    arguments = Bundle().apply {
                        putString("user_id", userId)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.notificationsRecyclerView, fragment)
                    .addToBackStack(null)
                    .commit()
            }
            com.example.frontendbook.data.model.NotificationType.LIKE_COMMENT -> {
                val commentId = notification.relatedId.toLongOrNull() ?: return
                val fragment = ReviewsFragment().apply {
                    arguments = Bundle().apply {
                        putLong("review_id", commentId)
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, fragment) // container ID'yi doğru gir
                    .addToBackStack(null)
                    .commit()
            }
            com.example.frontendbook.data.model.NotificationType.FOLLOW_LIST -> {
                val listId = notification.relatedId
                val fragment = ListDetailFragment().apply {
                    arguments = Bundle().apply {
                        putLong("listId", listId.toLong())
                    }
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.notificationsRecyclerView, fragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
