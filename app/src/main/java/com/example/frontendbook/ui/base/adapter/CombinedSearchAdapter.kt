package com.example.frontendbook.ui.base.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.ItemBookResultBinding
import com.example.frontendbook.databinding.ItemUserRowBinding
import com.example.frontendbook.domain.model.CombinedSearchResult
import com.example.frontendbook.domain.model.CombinedSearchResult.BookResult
import com.example.frontendbook.domain.model.CombinedSearchResult.UserResult
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.domain.model.User

class CombinedSearchAdapter(
    private val onBookClick: (Book) -> Unit,
    private val onUserClick: (User) -> Unit
) : ListAdapter<CombinedSearchResult, RecyclerView.ViewHolder>(DiffCallback) {

    companion object {
        private const val TYPE_BOOK = 1
        private const val TYPE_USER = 2

        private val DiffCallback = object : DiffUtil.ItemCallback<CombinedSearchResult>() {
            override fun areItemsTheSame(oldItem: CombinedSearchResult, newItem: CombinedSearchResult): Boolean {
                return when {
                    oldItem is BookResult && newItem is BookResult -> oldItem.book.id == newItem.book.id
                    oldItem is UserResult && newItem is UserResult -> oldItem.user.id == newItem.user.id
                    else -> false
                }
            }
            override fun areContentsTheSame(oldItem: CombinedSearchResult, newItem: CombinedSearchResult): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is BookResult -> TYPE_BOOK
            is UserResult -> TYPE_USER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_BOOK -> {
                val binding = ItemBookResultBinding.inflate(inflater, parent, false)
                BookViewHolder(binding, onBookClick)
            }
            TYPE_USER -> {
                val binding = ItemUserRowBinding.inflate(inflater, parent, false)
                UserViewHolder(binding, onUserClick)
            }
            else -> throw IllegalArgumentException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is BookResult -> (holder as BookViewHolder).bind(item.book)
            is UserResult -> (holder as UserViewHolder).bind(item.user)
        }
    }

    class BookViewHolder(
        private val binding: ItemBookResultBinding,
        private val onClick: (Book) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("CheckResult")
        fun bind(book: Book) {
            binding.title.text = book.title
            binding.author.text = book.author
            Glide.with(binding.bookImageView.context)
                .load(book.coverImageUrl) //
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(binding.bookImageView)
            binding.root.setOnClickListener { onClick(book) }
        }
    }

    class UserViewHolder(
        private val binding: ItemUserRowBinding,
        private val onClick: (User) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.userName.text = user.username
            Glide.with(itemView.context)
                .load(
                    when (user.profileImageUrl) {
                        "bookworms.png"     -> R.drawable.bookworms
                        "bookfriends.png"   -> R.drawable.bookfriends
                        "bookbibliofil.png" -> R.drawable.bookbibliofil
                        "bookcat.png"       -> R.drawable.bookcat
                        else                -> R.drawable.avatar
                    }
                )
                .circleCrop()
                .into(binding.userImage)
            binding.root.setOnClickListener { onClick(user) }
        }
    }
}
