package com.jikgwan.application.chat.dto

import com.jikgwan.domain.chat.Message
import java.time.LocalDateTime

data class MessageResponse(
    val id: Long,
    val chatRoomId: Long,
    val senderId: Long,
    val content: String,
    val status: String,
    val sentAt: LocalDateTime
) {
    companion object {
        fun from(message: Message) = MessageResponse(
            id = message.id.value,
            chatRoomId = message.chatRoomId.value,
            senderId = message.senderId.value,
            content = message.content,
            status = message.status.name,
            sentAt = message.sentAt
        )
    }
}
