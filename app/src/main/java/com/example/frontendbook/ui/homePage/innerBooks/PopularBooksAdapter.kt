package com.example.frontendbook.ui.homePage.innerBooks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.frontendbook.databinding.ItemBookGridBinding

class PopularBooksAdapter(
    private val books: List<String> // Dummy string verisi, ileride model eklenebilir
) : RecyclerView.Adapter<PopularBooksAdapter.BookViewHolder>() {

    inner class BookViewHolder(val binding: ItemBookGridBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val binding = ItemBookGridBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val title = books[position]
        holder.binding.bookTitle.text = title
    }

    override fun getItemCount(): Int = books.size
}
