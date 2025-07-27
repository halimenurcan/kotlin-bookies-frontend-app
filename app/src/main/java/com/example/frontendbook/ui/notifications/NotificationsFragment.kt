package com.example.frontendbook.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.frontendbook.R
import com.example.frontendbook.data.model.Notification
import com.example.frontendbook.data.model.NotificationType
import com.example.frontendbook.databinding.FragmentNotificationsBinding
import com.example.frontendbook.ui.base.adapter.NotificationAdapter

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: NotificationsViewModel
    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, NotificationsViewModelFactory(requireContext()))
            .get(NotificationsViewModel::class.java)

        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = NotificationAdapter(
            items = mutableListOf(),
            onClick = { clickedNotification ->
                Log.d("NOTIFICATIONS", "Clicked notification: $clickedNotification")
                viewModel.markAsRead(clickedNotification)
                handleNotificationClick(clickedNotification)
            },
            onDeleteClick = { notification ->
                Log.d("NOTIFICATIONS", "Deleted notification: $notification")
                viewModel.deleteNotification(notification)
            }
        )

        binding.notificationsRecyclerView.adapter = adapter

        viewModel.notifications.observe(viewLifecycleOwner) { notifications ->
            Log.d("NOTIFICATIONS", "Notifications loaded: ${notifications.size}")
            adapter.updateItems(notifications)
        }

        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Log.e("NOTIFICATIONS", "Error: $it")
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.loadNotifications()
    }

    private fun handleNotificationClick(notification: Notification) {
        when (notification.type) {
            NotificationType.FOLLOW -> {
                val userId = notification.relatedId.toLongOrNull() ?: return
                Log.d("NOTIFICATIONS", "Navigating to OtherUserProfileFragment with userId=$userId")
                val bundle = Bundle().apply { putLong("userId", userId) }
                findNavController().navigate(R.id.otherUserProfileFragment, bundle)
            }

            NotificationType.LIKE_COMMENT -> {
                val reviewId = notification.relatedId.toLongOrNull() ?: return
                Log.d("NOTIFICATIONS", "Navigating to ReviewsFragment with reviewId=$reviewId")
                val bundle = Bundle().apply { putLong("reviewId", reviewId) }
                findNavController().navigate(R.id.reviewsFragment, bundle)
            }

            NotificationType.FOLLOW_LIST -> {
                val userId = notification.relatedId.toLongOrNull() ?: return
                Log.d("NOTIFICATIONS", "Navigating to ListsFragment with userId=$userId")

                val listId = notification.relatedId.toLongOrNull() ?: return
                Log.d("NOTIFICATIONS", "Navigating to ListsFragment with listId=$listId")

                val direction = NotificationsFragmentDirections
                    .actionNotificationsFragmentToListsFragment(
                        argUserId = userId,
                        argTitle = "Followed lists",
                        argType = "FOLLOWED",
                        argListId = -1L,
                        profileUserId = userId,
                        showUserLists = false
                    )

                findNavController().navigate(direction)

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}