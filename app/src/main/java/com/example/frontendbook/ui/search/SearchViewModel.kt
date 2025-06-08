package com.example.frontendbook.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendbook.data.remote.AiRepository
import com.example.frontendbook.data.remote.BookRepository
import com.example.frontendbook.domain.model.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    private val aiRepository = AiRepository()
    private val bookRepository = BookRepository()

    private val _state = MutableLiveData<SearchUiState>()
    val state: LiveData<SearchUiState> = _state

    init {
        _state.value = SearchUiState()
    }

    fun sortBooks(option: SortOption) {
        _state.value = _state.value?.copy(isLoading = true, selectedSortOption = option)

        viewModelScope.launch {
            try {
                val sortedBooks = bookRepository.fetchSortedBooks(option)

                _state.value = _state.value?.copy(
                    isLoading = false,
                    books = sortedBooks,
                    isEmptyResult = sortedBooks.isEmpty(),
                    successMessage = "Books sorted by ${option.name.replace("_", " ").lowercase()}"
                )
            } catch (e: Exception) {
                _state.value = _state.value?.copy(
                    isLoading = false,
                    errorMessage = "Failed to fetch sorted books: ${e.message}"
                )
            }
        }
    }
    fun searchBooks(query: String) {
        _state.value = _state.value?.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val result = bookRepository.searchBooks(query)

                _state.value = _state.value?.copy(
                    isLoading = false,
                    books = result,
                    isEmptyResult = result.isEmpty(),
                    successMessage = "${result.size} book(s) found"
                )
            } catch (e: Exception) {
                _state.value = _state.value?.copy(
                    isLoading = false,
                    errorMessage = "Search failed: ${e.message}"
                )
            }
        }
    }


    fun getAiRecommendedBooks() {
        _state.value = _state.value?.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val jsonString = aiRepository.fetchRecommendations(
                    userPrompt = "Suggest 3 books as JSON with title, author, year, genre, country, language, imageUrl"
                )

                val books: List<Book> = Gson().fromJson(
                    jsonString,
                    object : TypeToken<List<Book>>() {}.type
                )

                _state.value = _state.value?.copy(
                    books = books,
                    isLoading = false,
                    successMessage = "AI recommendations loaded",
                    isEmptyResult = books.isEmpty()
                )
            } catch (e: Exception) {
                _state.value = _state.value?.copy(
                    isLoading = false,
                    errorMessage = "Failed to fetch AI recommendations: ${e.message}"
                )
            }
        }
    }

    // Filtre fonksiyonları hâlâ sahte veriyle çalışıyorsa burada kalabilir.
    // Gerçek API ile çalışacaksa BookRepository'ye taşınmalıdır.
    fun filterByGenre(genre: String) = applyFilter { it.genre.equals(genre, ignoreCase = true) }
    fun filterByCountry(country: String) = applyFilter { it.country.equals(country, ignoreCase = true) }
    fun filterByAuthor(author: String) = applyFilter { it.author.equals(author, ignoreCase = true) }
    fun filterByLanguage(language: String) = applyFilter { it.language.equals(language, ignoreCase = true) }

    private fun applyFilter(predicate: (Book) -> Boolean) {
        val currentBooks = _state.value?.books ?: return
        val filtered = currentBooks.filter(predicate)

        _state.value = _state.value?.copy(
            books = filtered,
            isEmptyResult = filtered.isEmpty()
        )
    }
}
