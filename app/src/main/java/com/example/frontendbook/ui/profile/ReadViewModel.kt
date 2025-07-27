package com.example.frontendbook.ui.profile

import androidx.lifecycle.*
import com.example.frontendbook.data.model.ReadEntry
import com.example.frontendbook.data.repository.ReadRepository
import kotlinx.coroutines.launch

class ReadViewModel(
    private val repository: ReadRepository
) : ViewModel() {

    private val _toReadList = MutableLiveData<List<ReadEntry>>()
    val toReadList: LiveData<List<ReadEntry>> get() = _toReadList

    private val _readBooks = MutableLiveData<List<ReadEntry>>()
    val readBooks: LiveData<List<ReadEntry>> get() = _readBooks

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    fun loadToReadList(userId: Long) {
        viewModelScope.launch {
            try {
                _toReadList.value = repository.getToReadList(userId)
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Readlist could not be loaded: ${e.message}"
            }
        }
    }

    fun loadReadBooks(userId: Long) {
        viewModelScope.launch {
            try {
                val result = repository.getReadBooks(userId)


                result.forEach {
                    println(" Read Book -> id: ${it.bookId}, title: ${it.bookTitle}")
                }

                val validBooks = result.filter { it.bookId != 0L }

                _readBooks.value = validBooks
                _error.value = null
            } catch (e: Exception) {
                _error.value = e.message
                println(" Error: ${e.message}")
            }
        }
    }



    fun addToReadList(userId: Long, bookId: Long) {
        viewModelScope.launch {
            try {
                repository.addToReadList(userId, bookId)
                _error.value = null
                loadToReadList(userId)
            } catch (e: Exception) {
                _error.value = "Could not added to ReadList: ${e.message}"
            }
        }
    }

    fun addToReadBooks(userId: Long, bookId: Long) {
        viewModelScope.launch {
            try {
                repository.addToReadBooks(userId, bookId)
                _error.value = null
                loadReadBooks(userId)
            } catch (e: Exception) {
                _error.value = "Could not add to Read: ${e.message}"
            }
        }
    }


    fun removeFromReadList(userId: Long, bookId: Long) {
        viewModelScope.launch {
            try {
                repository.removeFromReadList(userId, bookId)
                _error.value = null
                loadToReadList(userId)
            } catch (e: Exception) {
                _error.value = "Could not be deleted from ReadList: ${e.message}"
            }
        }
    }


    fun removeFromReadBooks(userId: Long, bookId: Long) {
        viewModelScope.launch {
            try {
                repository.removeFromReadBooks(userId, bookId)
                _error.value = null
                loadReadBooks(userId)
            } catch (e: Exception) {
                _error.value = "Could not be deleted from Read: ${e.message}"
            }
        }
    }
}
