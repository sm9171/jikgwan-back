package com.jikgwan.domain.chat

import com.jikgwan.domain.gathering.GatheringId
import com.jikgwan.domain.user.UserId
import java.time.LocalDateTime

data class ChatRoom(
    val id: ChatRoomId,
    val gatheringId: GatheringId,
    val hostId: UserId,
    val applicantId: UserId,
    val status: ChatRoomStatus = ChatRoomStatus.ACTIVE,
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun validateParticipant(userId: UserId) {
        require(isParticipant(userId)) {
            "채팅방 접근 권한이 없습니다"
        }
    }

    fun isParticipant(userId: UserId): Boolean =
        hostId == userId || applicantId == userId

    fun getOtherParticipant(userId: UserId): UserId =
        when (userId) {
            hostId -> applicantId
            applicantId -> hostId
            else -> throw IllegalArgumentException("채팅방 참여자가 아닙니다")
        }

    companion object {
        fun create(
            gatheringId: GatheringId,
            hostId: UserId,
            applicantId: UserId
        ): ChatRoom {
            require(hostId != applicantId) {
                "자신과 채팅할 수 없습니다"
            }
            return ChatRoom(
                id = ChatRoomId(0),
                gatheringId = gatheringId,
                hostId = hostId,
                applicantId = applicantId
            )
        }
    }
}
