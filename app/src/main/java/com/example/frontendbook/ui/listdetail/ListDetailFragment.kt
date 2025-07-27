package com.example.frontendbook.ui.listdetail

import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.frontendbook.R
import com.example.frontendbook.data.api.dto.BookDto
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.ListFollowsRepository
import com.example.frontendbook.domain.model.Book
import com.example.frontendbook.ui.addBook.AddBookBottomSheet
import com.example.frontendbook.ui.base.adapter.BookAdapter
import com.example.frontendbook.ui.homePage.ListsViewModel
import com.example.frontendbook.ui.homePage.ListsViewModelFactory
import android.widget.LinearLayout


class ListDetailFragment : Fragment() {

    private lateinit var viewModel: ListsViewModel
    private var listId: Long = -1L
    private lateinit var adapter: BookAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_lists, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listId = arguments?.getLong("listId") ?: -1L

        viewModel = ViewModelProvider(
            this,
            ListsViewModelFactory(requireContext())
        )[ListsViewModel::class.java]

        viewModel.loadListWithBooks(listId)

        viewModel.listWithBooks.observe(viewLifecycleOwner) { list ->
            val books = list.books
            val container = view.findViewById<LinearLayout>(R.id.bookContainer)
            container.removeAllViews()


            val addToListView = LayoutInflater.from(requireContext())
                .inflate(R.layout.add_to_list_card, container, false)
            container.addView(addToListView)


            books.forEach { book ->
                val bookView = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_book_grid, container, false)

                val imageView = bookView.findViewById<ImageView>(R.id.bookImage)
                val titleView = bookView.findViewById<TextView>(R.id.bookTitle)

                titleView.text = book.title
                Glide.with(requireContext())
                    .load(book.coverImageUrl)
                    .placeholder(R.drawable.placeholder)
                    .into(imageView)

                bookView.setOnClickListener {
                    Toast.makeText(requireContext(), "${book.title} clicked", Toast.LENGTH_SHORT).show()
                }

                container.addView(bookView)
            }

            val seeMoreView = LayoutInflater.from(requireContext())
                .inflate(R.layout.see_more_card, container, false)
            container.addView(seeMoreView)
        }

        viewModel.error.observe(viewLifecycleOwner) {
            Toast.makeText(requireContext(), it ?: "Error", Toast.LENGTH_SHORT).show()
        }
    }

}
