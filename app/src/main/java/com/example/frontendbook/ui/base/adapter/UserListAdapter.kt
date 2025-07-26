package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.R
import com.example.frontendbook.data.model.UserSimple

class UserListAdapter(
    private var users: List<UserSimple> = emptyList(),
    private val onClick: (UserSimple) -> Unit
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val avatar: ImageView    = view.findViewById(R.id.avatarImageView)
        private val username: TextView   = view.findViewById(R.id.usernameTextView)

        fun bind(user: UserSimple) {
            username.text = user.username
            avatar.setImageResource(R.drawable.avatar)
            itemView.setOnClickListener { onClick(user) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_card, parent, false)
        return UserViewHolder(v)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    override fun getItemCount(): Int = users.size

    fun submitList(newUsers: List<UserSimple>) {
        users = newUsers
        notifyDataSetChanged()
    }
}
