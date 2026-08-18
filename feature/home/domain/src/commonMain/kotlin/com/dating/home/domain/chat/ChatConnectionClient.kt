package com.dating.home.domain.chat

import com.dating.home.domain.models.ChatMessage
import com.dating.home.domain.models.ConnectionState
import com.dating.home.domain.models.MessagesReadEvent
import com.dating.home.domain.models.TypingIndicator
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ChatConnectionClient {
    val chatMessages: Flow<ChatMessage>
    val connectionState: StateFlow<ConnectionState>
    val typingIndicators: Flow<TypingIndicator>
    val messagesRead: Flow<MessagesReadEvent>
    // Módulo 5 (RN-5.6) — deltas de presencia de radar en tiempo real (entrar/salir de zona).
    val radarPresence: Flow<RadarPresenceEvent>
    suspend fun sendTyping(chatId: String)
    suspend fun sendReadReceipt(chatId: String, messageIds: List<String>)
    suspend fun sendReaction(messageId: String, chatId: String, emoji: String)
}

/** Un usuario entró (present=true) o salió (present=false) de la zona de radar del usuario actual. */
data class RadarPresenceEvent(
    val userId: String,
    val present: Boolean
)