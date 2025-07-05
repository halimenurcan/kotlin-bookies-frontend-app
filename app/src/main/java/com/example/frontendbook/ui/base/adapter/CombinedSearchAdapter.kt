package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.ItemBookResultBinding
import com.example.frontendbook.databinding.ItemUserResultBinding
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.domain.model.User
import com.example.frontendbook.domain.model.CombinedSearchResult

class  CombinedSearchAdapter(
    private val onBookClick: (Book) -> Unit,
    private val onUserClick: (User) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

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
            BookViewHolder(binding, onBookClick)
        } else {
            val binding = ItemUserResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            UserViewHolder(binding, onUserClick)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is CombinedSearchResult.BookResult -> (holder as BookViewHolder).bind(item.book)
            is CombinedSearchResult.UserResult -> (holder as UserViewHolder).bind(item.user)
        }
    }

    class BookViewHolder( private val binding: ItemBookResultBinding,
                          private val onClick: (Book) -> Unit
    ) : RecyclerView.ViewHolder(binding.root){
        fun bind(book: Book) {
            binding.title.text = book.title
            binding.author.text = book.author
            Glide.with(binding.root.context)
                .load(book.coverImageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error_image)
                .into(binding.bookImageView)

            binding.root.setOnClickListener {
                onClick(book)
            }
        }
    }

    class UserViewHolder(private val binding: ItemUserResultBinding,
                         private val onClick: (User) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.username.text = user.username
            binding.email.text = user.id
            binding.root.setOnClickListener {
                onClick(user)
            }
        }
    }
}
