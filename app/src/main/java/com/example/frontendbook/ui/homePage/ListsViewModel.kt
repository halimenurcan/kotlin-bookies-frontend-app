package com.example.frontendbook.ui.homePage

import androidx.lifecycle.*
import com.example.frontendbook.data.model.ListDto
import com.example.frontendbook.data.repository.ListsRepository
import kotlinx.coroutines.launch

class ListsViewModel(
    private val repo: ListsRepository
) : ViewModel() {

    private val _lists = MutableLiveData<List<ListDto>>()
    val lists: LiveData<List<ListDto>> = _lists

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadUserLists(userId: Long) {
        viewModelScope.launch {
            try {
                _lists.value = repo.getUserLists(userId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun createList(userId: Long, title: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val success = repo.createList(userId, title)

                if (success) {
                    loadUserLists(userId) // Listeyi güncelle
                }
                onResult(success)
            } catch (e: Exception) {
                _error.value = e.message
                onResult(false)
            }
        }
    }

    fun addBookToList(listId: Long, bookId: Long, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val success = repo.addBookToList(listId, bookId)
                onResult(success)
            } catch (e: Exception) {
                _error.value = e.message
                onResult(false)
            }
        }
    }


}
