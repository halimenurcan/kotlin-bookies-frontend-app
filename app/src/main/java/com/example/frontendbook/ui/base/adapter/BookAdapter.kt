package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frontendbook.databinding.ItemBookGridBinding
import com.example.frontendbook.domain.model.Book

class BookAdapter(
    private val onBookClick: (Book) -> Unit
) : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var books: List<Book> = emptyList()

    inner class BookViewHolder(val binding: ItemBookGridBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]

        holder.binding.bookTitle.text = book.title
        Glide.with(holder.binding.root.context)
            .load(book.coverImageUrl)
            .placeholder(com.example.frontendbook.R.drawable.placeholder)
            .error(com.example.frontendbook.R.drawable.bookk)
            .into(holder.binding.bookImage)

        holder.itemView.setOnClickListener {
            onBookClick(book)
        }

    }

    override fun getItemCount(): Int = books.size

    fun submitList(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
