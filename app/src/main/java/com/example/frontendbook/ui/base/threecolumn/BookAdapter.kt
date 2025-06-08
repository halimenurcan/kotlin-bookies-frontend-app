package com.example.frontendbook.ui.base.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.databinding.ItemBookGridBinding
import com.example.frontendbook.domain.model.Book

class BookAdapter : RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    private var books: List<String> = emptyList()

    inner class BookViewHolder(val binding: ItemBookGridBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.binding.bookTitle.text = books[position]
    }

    override fun getItemCount(): Int = books.size

    fun submitList(newBooks: List<Book>) {
        books = newBooks
        notifyDataSetChanged()
    }
}
