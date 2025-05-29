package com.example.frontendbook.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendbook.data.remote.AiRepository
import com.example.frontendbook.domain.model.Book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val aiRepository = AiRepository()

    private val _state = MutableLiveData<SearchUiState>()
    val state: LiveData<SearchUiState> = _state

    init {
        // Sayfa açıldığında boş state
        _state.value = SearchUiState()
    }

    fun searchBooks(query: String) {
        _state.value = _state.value?.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                // Geçici simülasyon
                delay(1000)

                // Burada repository'den veri çekilecek
                val result: List<Book> = fakeBookSearch(query)

                _state.value = _state.value?.copy(
                    isLoading = false,
                    books = result,
                    isEmptyResult = result.isEmpty(),
                    successMessage = if (result.isNotEmpty()) "${result.size} kitap bulundu" else null
                )
            } catch (e: Exception) {
                _state.value = _state.value?.copy(
                    isLoading = false,
                    books = emptyList(),
                    errorMessage = "Bir hata oluştu: ${e.message}"
                )
            }
        }
    }

    fun filterByYear(year: Int) {
        _state.value = _state.value?.copy(isLoading = true, selectedYear = year)

        viewModelScope.launch {
            delay(800) // Simülasyon

            val result = fakeBookFilterByYear(year)

            _state.value = _state.value?.copy(
                isLoading = false,
                books = result,
                isEmptyResult = result.isEmpty()
            )
        }
    }
    fun getAiRecommendedBooks() {
        _state.value = _state.value?.copy(isLoading = true)

        viewModelScope.launch {
            try {
                val jsonString = aiRepository.fetchRecommendations(
                    userPrompt = "Suggest 3 books as JSON with title, author, year, imageUrl"
                )

                val books: List<Book> = Gson().fromJson(
                    jsonString,
                    object : TypeToken<List<Book>>() {}.type
                )

                _state.value = _state.value?.copy(
                    books = books,
                    isLoading = false,
                    successMessage = "OpenAI kitap önerileri yüklendi",
                    isEmptyResult = books.isEmpty()
                )

            } catch (e: Exception) {
                _state.value = _state.value?.copy(
                    isLoading = false,
                    errorMessage = "OpenAI verisi alınamadı: ${e.message}"
                )
            }
        }
    }



    // Geçici sahte veri (gerçek repository yerine)
    private fun fakeBookSearch(query: String): List<Book> {
        val books = listOf(
            Book("Sefiller", "Victor Hugo", 1862),
            Book("1984", "George Orwell", 1949),
            Book("Körlük", "José Saramago", 1995)
        )
        return books.filter { it.title.contains(query, ignoreCase = true) }
    }

    private fun fakeBookFilterByYear(year: Int): List<Book> {
        return listOf(
            Book("Örnek Kitap", "Yazar", year)
        )
    }
}
