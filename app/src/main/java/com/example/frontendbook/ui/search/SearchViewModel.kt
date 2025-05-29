package com.example.frontendbook.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontendbook.domain.model.Book
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

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
