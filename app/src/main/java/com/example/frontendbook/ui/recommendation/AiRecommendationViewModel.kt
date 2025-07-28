package com.example.frontendbook.ui.base.threecolumn

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.frontendbook.data.remote.AiRepository
import com.example.frontendbook.data.remote.RetrofitClient
import com.example.frontendbook.data.repository.BookRepository
import com.example.frontendbook.data.repository.LikedBooksRepository
import com.example.frontendbook.domain.model.Book
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class AiRecommendationViewModel(
    private val context: Context,
    private val aiRepo: AiRepository
) : ViewModel() {

    private val likedBooksRepo = LikedBooksRepository(
        RetrofitClient.likedBooksApiService(context)
    )
    private val allBooksRepo = BookRepository(context)

    private val _recommendedBooks = MutableLiveData<List<Book>>()
    val recommendedBooks: LiveData<List<Book>> = _recommendedBooks

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun fetchRecommendationsFromLikedBooks(userId: Long) {
        viewModelScope.launch {
            try {
                val likedBooks = likedBooksRepo.getAllLikedBooks(userId)
                val allBooks = allBooksRepo.fetchAllBooks()

                Log.d("AI_VM", "Liked books count: ${likedBooks.size}")
                Log.d("AI_VM", "All books count: ${allBooks.size}")

                val prompt = buildPrompt(likedBooks, allBooks)
                Log.d("AI_VM", "Prompt sent to OpenAI:\n$prompt")

                val aiResponse = aiRepo.fetchRecommendations(prompt)
                Log.d("AI_VM", "Raw response from OpenAI:\n$aiResponse")

                val books = parseAiResponseToBooks(aiResponse)
                Log.d("AI_VM", "Parsed AI recommendations: ${books.map { it.title }}")

                _recommendedBooks.value = books

            } catch (e: Exception) {
                Log.e("AI_VM", "Error: ${e.message}", e)
                _error.value = e.message
            }
        }
    }


    private fun buildPrompt(liked: List<Book>, allBooks: List<Book>): String {
        return buildString {
            append("User liked these books:\n")
            liked.forEach {
                append("- ${it.title} by ${it.author}\n")
            }
            append("\nAvailable books in database:\n")
            allBooks.forEach {
                append("- ${it.title} by ${it.author}: ${it.description}\n")
            }
            append("\nFrom the available books, suggest 5 that match the user's preferences based on previous likes. Respond ONLY in JSON format as an array of objects with 'title', 'author', 'description'.")
        }
    }

    private fun parseAiResponseToBooks(json: String): List<Book> {
        return try {
            val array = JSONArray(json)
            val results = mutableListOf<Book>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                results.add(
                    Book(
                        id = (1000 + i).toLong(), // dummy id
                        title = obj.optString("title", "Unknown"),
                        author = obj.optString("author", "Unknown"),
                        description = obj.optString("description", ""),
                        isbn = "",
                        publisher = "",
                        publishedYear = 0,
                        rating = 0,
                        coverImageUrl = "",
                        pageCount = 0
                    )
                )
            }
            results
        } catch (e: Exception) {
            _error.postValue("AI response parse error: ${e.message}")
            emptyList()
        }
    }
}
