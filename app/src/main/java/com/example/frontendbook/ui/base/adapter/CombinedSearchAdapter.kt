package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.databinding.ItemBookResultBinding
import com.example.frontendbook.databinding.ItemUserResultBinding
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.domain.model.User
import com.example.frontendbook.domain.model.CombinedSearchResult

class CombinedSearchAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<CombinedSearchResult>()

    fun submitList(list: List<CombinedSearchResult>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CombinedSearchResult.BookResult -> 0
            is CombinedSearchResult.UserResult -> 1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0) {
            val binding = ItemBookResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            BookViewHolder(binding)
        } else {
            val binding = ItemUserResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            UserViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is CombinedSearchResult.BookResult -> (holder as BookViewHolder).bind(item.book)
            is CombinedSearchResult.UserResult -> (holder as UserViewHolder).bind(item.user)
        }
    }

    class BookViewHolder(private val binding: ItemBookResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(book: Book) {
            binding.title.text = book.title
            binding.author.text = book.author
            // TODO: image yükleme (Glide/Picasso), vs.
        }
    }

    class UserViewHolder(private val binding: ItemUserResultBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.username.text = user.username
            binding.email.text = user.id
        }
    }
}
