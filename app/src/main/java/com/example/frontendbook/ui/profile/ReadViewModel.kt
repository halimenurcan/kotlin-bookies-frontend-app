package com.example.frontendbook.ui.profile

import androidx.lifecycle.*
import com.example.frontendbook.data.model.ReadEntry
import com.example.frontendbook.data.repository.ReadRepository
import kotlinx.coroutines.launch

class ReadViewModel(
    private val repository: ReadRepository
) : ViewModel() {

    private val _readList = MutableLiveData<List<ReadEntry>>()
    val readList: LiveData<List<ReadEntry>> = _readList

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error

    /** Belirli bir kullanıcının read list'ini getirir */
    fun loadReadList(userId: Long) {
        viewModelScope.launch {
            try {
                val list = repository.getReadList(userId)
                _readList.value = list
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Liste yüklenemedi: ${e.message}"
            }
        }
    }

    /** Yeni kitap ekle */
    fun addReadEntry(entry: ReadEntry) {
        viewModelScope.launch {
            try {
                repository.addToReadList(entry)
                _error.value = null
                loadReadList(entry.userId) // Listeyi güncelle
            } catch (e: Exception) {
                _error.value = "Ekleme başarısız: ${e.message}"
            }
        }
    }

    /** Kitabı listeden sil */
    fun removeReadEntry(entryId: Long, userId: Long) {
        viewModelScope.launch {
            try {
                repository.removeFromReadList(entryId)
                _error.value = null
                loadReadList(userId) // Listeyi güncelle
            } catch (e: Exception) {
                _error.value = "Silme başarısız: ${e.message}"
            }
        }
    }
}