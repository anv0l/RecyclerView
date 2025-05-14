package otus.gpb.recyclerview.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatViewModel(private val repository: ChatRepository) : ViewModel() {
    val chats = repository.getChatsFilteredAndSorted()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    init {
        _isLoading.value = true
        viewModelScope.launch {
            repository.loadMoreChats()
            _isLoading.value = false
        }
    }

    fun toggleArchiveChat(chatId: Int) {
        repository.toggleArchiveChat(chatId)
    }

    fun loadMoreChats() {
        if (_isLoading.value) return

        _isLoading.value = true
        viewModelScope.launch {
            repository.loadMoreChats()
            _isLoading.value = false
        }
    }
}