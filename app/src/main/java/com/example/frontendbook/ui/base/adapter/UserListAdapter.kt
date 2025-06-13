package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.domain.model.UserSimple

/**
 * Kullanıcı listesini göstermek için RecyclerView.Adapter
 * @param users Gösterilecek kullanıcıların listesi
 * @param onClick Her bir kullanıcıya tıklandığında çalışacak fonksiyon (örneğin profiline gitmek)
 */
class UserListAdapter(
    private val users: List<UserSimple>,
    private val onClick: (UserSimple) -> Unit
) : RecyclerView.Adapter<UserListAdapter.UserViewHolder>() {

    /**
     * Her bir kullanıcı kartını temsil eden ViewHolder
     */
    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatar: ImageView = view.findViewById(R.id.avatarImageView)
        val username: TextView = view.findViewById(R.id.usernameTextView)

        /**
         * Kullanıcı bilgilerini ilgili görsellere yükler
         */
        fun bind(user: UserSimple) {
            username.text = user.username

            // Glide ile profil fotoğrafını yükle, yoksa default avatar kullan
            Glide.with(itemView.context)
                .load(user.avatarUrl ?: R.drawable.avatar)
                .into(avatar)

            // Kullanıcıya tıklanınca verilen fonksiyonu çalıştır
            itemView.setOnClickListener { onClick(user) }
        }
    }

    /**
     * Yeni ViewHolder oluşturur
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_card, parent, false)
        return UserViewHolder(view)
    }

    /**
     * ViewHolder'a kullanıcı verilerini bağlar
     */
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(users[position])
    }

    /**
     * Liste boyutunu döndürür
     */
    override fun getItemCount(): Int = users.size
}
