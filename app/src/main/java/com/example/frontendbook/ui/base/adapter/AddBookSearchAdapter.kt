package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.databinding.ItemBookSearchBinding
import com.example.frontendbook.domain.model.Book

class AddBookSearchAdapter(
    private val onBookClick: (Book) -> Unit
) : RecyclerView.Adapter<AddBookSearchAdapter.BookViewHolder>() {

    private val books = mutableListOf<Book>()

    inner class BookViewHolder(private val binding: ItemBookSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book) {
            binding.titleTextView.text = book.title
            binding.authorTextView.text = book.author

            Glide.with(binding.root.context)
                .load(book.coverImageUrl ?: R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.search)
                .into(binding.bookCoverImage)

            binding.root.setOnClickListener {
                onBookClick(book)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(books[position])
    }

    override fun getItemCount(): Int = books.size

    fun submitList(newBooks: List<Book>) {
        books.clear()
        books.addAll(newBooks)
        notifyDataSetChanged()
    }
}
