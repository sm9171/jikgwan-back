package com.jikgwan.application.chat.dto

import com.jikgwan.domain.chat.ChatRoom
import java.time.LocalDateTime

data class ChatRoomResponse(
    val id: Long,
    val gatheringId: Long,
    val hostId: Long,
    val applicantId: Long,
    val status: String,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(chatRoom: ChatRoom) = ChatRoomResponse(
            id = chatRoom.id.value,
            gatheringId = chatRoom.gatheringId.value,
            hostId = chatRoom.hostId.value,
            applicantId = chatRoom.applicantId.value,
            status = chatRoom.status.name,
            createdAt = chatRoom.createdAt
        )
    }
}
