package com.example.compose.jetchat.feature.websocketcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatWebSocketViewModel : ViewModel() {

    private val _messages = MutableStateFlow<List<String>>(emptyList())
    val messages: StateFlow<List<String>> = _messages.asStateFlow()

    init {
        connectToServer.connect()

        viewModelScope.launch {
            connectToServer.incomingMessages.collect { msg ->
                _messages.value = _messages.value + msg
            }
        }
    }

    fun sendMessage(text: String) {
        connectToServer.send(text)
    }

    override fun onCleared() {
        super.onCleared()
        connectToServer.close()
    }
}
