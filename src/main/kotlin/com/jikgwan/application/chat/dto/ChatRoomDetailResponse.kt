package com.jikgwan.application.chat.dto

import com.jikgwan.application.gathering.dto.GameInfoResponse
import com.jikgwan.domain.chat.ChatRoom
import com.jikgwan.domain.gathering.Gathering
import com.jikgwan.domain.user.User
import java.time.LocalDateTime

data class ChatRoomDetailResponse(
    val id: Long,
    val hostId: Long,
    val meetingInfo: MeetingInfoResponse,
    val participants: List<ParticipantResponse>,
    val lastMessage: MessageResponse?,
    val unreadCount: Long,
    val isConfirmed: Boolean,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(
            chatRoom: ChatRoom,
            gathering: Gathering,
            host: User,
            applicant: User,
            lastMessage: MessageResponse?,
            unreadCount: Long,
            isConfirmed: Boolean
        ) = ChatRoomDetailResponse(
            id = chatRoom.id.value,
            hostId = chatRoom.hostId.value,
            meetingInfo = MeetingInfoResponse.from(gathering),
            participants = listOf(
                ParticipantResponse.from(host),
                ParticipantResponse.from(applicant)
            ),
            lastMessage = lastMessage,
            unreadCount = unreadCount,
            isConfirmed = isConfirmed,
            createdAt = chatRoom.createdAt
        )
    }
}

data class MeetingInfoResponse(
    val id: Long,
    val gameInfo: GameInfoResponse
) {
    companion object {
        fun from(gathering: Gathering) = MeetingInfoResponse(
            id = gathering.id.value,
            gameInfo = GameInfoResponse.from(gathering.gameInfo)
        )
    }
}

data class ParticipantResponse(
    val id: Long,
    val nickname: String,
    val profileImage: String?,
    val gender: String?,
    val ageRange: String?
) {
    companion object {
        fun from(user: User) = ParticipantResponse(
            id = user.id.value,
            nickname = user.nickname.value,
            profileImage = user.profile?.profileImage?.value,
            gender = user.profile?.gender?.name,
            ageRange = user.profile?.ageRange?.name
        )
    }
}
