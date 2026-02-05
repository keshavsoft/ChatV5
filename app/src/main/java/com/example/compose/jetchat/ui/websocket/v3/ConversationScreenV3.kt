@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.compose.jetchat.ui.websocket.v3

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.compose.jetchat.R
import com.example.compose.jetchat.components.JetchatAppBar
import com.example.compose.jetchat.conversation.*
import com.example.compose.jetchat.feature.websocketcode.connectToServer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ConversationScreenV3(
    uiState: ConversationUiState,
    navigateToProfile: (String) -> Unit,
    modifier: Modifier = Modifier,
    onNavIconPressed: () -> Unit = {},
) {
    val authorMe = stringResource(R.string.author_me)
    val timeNow = stringResource(id = R.string.now)

    val scrollState = rememberLazyListState()
    val topBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(topBarState)
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            JetchatAppBar(
                title = {
                    Column {
                        Text(uiState.channelName, style = MaterialTheme.typography.titleMedium)
                        Text(
                            stringResource(R.string.members, uiState.channelMembers),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                onNavIconPressed = onNavIconPressed,
                scrollBehavior = scrollBehavior,
            )
        },
        contentWindowInsets = ScaffoldDefaults
            .contentWindowInsets
            .exclude(WindowInsets.navigationBars)
            .exclude(WindowInsets.ime),
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
        ) {
            Messages(
                messages = uiState.messages,
                navigateToProfile = navigateToProfile,
                modifier = Modifier.weight(1f),
                scrollState = scrollState,
            )

            UserInput(
                onMessageSent = { content ->
                    // 1️⃣ Local UI update
                    uiState.addMessage(
                        Message(authorMe, content, timeNow),
                    )

                    // 2️⃣ WebSocket send (V3 ONLY)
                    connectToServer.send(content)
                },
                resetScroll = {
                    scope.launch {
                        scrollState.scrollToItem(0)
                    }
                },
                modifier = Modifier
                    .navigationBarsPadding()
                    .imePadding(),
            )
        }
    }
}