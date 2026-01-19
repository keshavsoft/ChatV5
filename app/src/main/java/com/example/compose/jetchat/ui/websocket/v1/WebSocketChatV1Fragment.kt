package com.example.compose.jetchat.ui.websocket.v1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.compose.jetchat.conversation.ConversationUiState
import com.example.compose.jetchat.conversation.Message
import com.example.compose.jetchat.feature.websocketcode.ChatWebSocketViewModel
import com.example.compose.jetchat.theme.JetchatTheme

class WebSocketChatV1Fragment : Fragment() {

    private val viewModel: ChatWebSocketViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                JetchatTheme {

                    // collect websocket messages
                    val wsMessages =
                        viewModel.messages.collectAsStateWithLifecycle().value

                    // build ConversationUiState (JetChat way)
                    val uiState = ConversationUiState(
                        channelName = "WebSocket V1",
                        channelMembers = 1,
                        initialMessages = wsMessages.map {
                            Message(
                                author = "WS",
                                content = it,
                                timestamp = "now"
                            )
                        }
                    )

                    // show V1 screen
                    ConversationScreenV1(
                        uiState = uiState,
                        navigateToProfile = {},
                        onNavIconPressed = {
                            requireActivity()
                                .onBackPressedDispatcher
                                .onBackPressed()
                        }
                    )
                }
            }
        }
    }
}
