package com.example.frontendbook.ui.homePage

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import com.example.frontendbook.data.api.dto.ListDto
import com.example.frontendbook.data.repository.ListsRepository
import kotlinx.coroutines.launch

class ListsViewModel(
    private val context: Context,
    private val repo: ListsRepository
) : ViewModel() {

    val deleteResult = MutableLiveData<Boolean>()
    private val _lists = MutableLiveData<List<ListDto>>()
    val lists: LiveData<List<ListDto>> = _lists

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    private val _otherLists = MutableLiveData<List<ListDto>>()
    val otherLists: LiveData<List<ListDto>> = _otherLists

    fun loadOtherLists() {
        viewModelScope.launch {
            try {
                val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
                val currentUserId = prefs.getLong("user_id", -1L)
                val allLists = repo.getAllLists()
                Log.d("LISTS_VM", "All lists from Repo: $allLists")

                val filtered = allLists.filter { it.owner.id != currentUserId }
                Log.d("LISTS_VM", "Other filtered lists: $filtered")

                _otherLists.value = filtered
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                Log.e("LISTS_VM", "Error: ${e.message}")
            }
        }
    }

    fun loadSingleList(listId: Long) {
        viewModelScope.launch {
            try {
                val list = repo.getListById(listId)
                _lists.value = listOf(list)
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun loadOtherListsForUser(userId: Long) {
        viewModelScope.launch {
            try {
                _otherLists.value = repo.getUserLists(userId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }


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


    fun refreshLists() {
        viewModelScope.launch {
            val prefs = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
            val userId = prefs.getLong("user_id", -1L)
            if (userId != -1L) {
                loadUserLists(userId)
            }
        }
    }


    fun deleteList(listId: Long) {
        viewModelScope.launch {
            val success = repo.deleteListById(listId)
            deleteResult.postValue(success)
            if (success) {
                refreshLists()
            }
        }
    }


    fun createList(userId: Long, title: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                val success = repo.createList(userId, title)
                if (success) {
                    loadUserLists(userId)
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


    private val _listWithBooks = MutableLiveData<ListDto>()
    val listWithBooks: LiveData<ListDto> = _listWithBooks

    fun loadListWithBooks(listId: Long) {
        viewModelScope.launch {
            try {
                val list = repo.getListWithBooks(listId)
                _listWithBooks.value = list
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }


    fun loadExploreLists() {
        viewModelScope.launch {
            try {
                _lists.value = repo.getAllLists()
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}
