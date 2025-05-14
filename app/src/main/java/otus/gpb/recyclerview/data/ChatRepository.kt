package otus.gpb.recyclerview.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ChatRepository {
    private val _allChats = MutableStateFlow(emptyList<Chat>())

    private val _page = MutableStateFlow(0)

    fun getChatsFilteredAndSorted(): StateFlow<List<Chat>> {
        return _allChats.map { chats ->
            chats.asSequence()
                .filter { !it.isArchived }
                .toList()
        }.stateIn(CoroutineScope(Dispatchers.Default), SharingStarted.Eagerly, emptyList())
    }

    fun toggleArchiveChat(chatId: Int) {
        _allChats.update { chats ->
            chats.map { chat ->
                if (chat.id == chatId) chat.copy(isArchived = !chat.isArchived) else chat
            }
        }
    }

    fun loadMoreChats() {
        if (_allChats.value.size < getChatsCount()) {
            _page.update { it + 1 }
            _allChats.update { chats -> chats + loadMoreChatsFromDB(_page.value) }
        }
    }
}

