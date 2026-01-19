package com.example.compose.jetchat.feature.webSocketCode

import android.os.Bundle
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.compose.jetchat.feature.chat.ChatScreen
import com.example.compose.jetchat.feature.websocketcode.ChatWebSocketViewModel
import com.example.compose.jetchat.theme.JetchatTheme

class WebSocketChatFragment : Fragment() {

    private val viewModel: ChatWebSocketViewModel by viewModels()

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                JetchatTheme {
                    ChatScreen(viewModel = viewModel)
                }
            }
        }
    }
}
