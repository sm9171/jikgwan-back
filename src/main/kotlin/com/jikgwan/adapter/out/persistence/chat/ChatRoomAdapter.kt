package com.jikgwan.adapter.out.persistence.chat

import com.jikgwan.application.chat.port.out.ChatRoomPort
import com.jikgwan.domain.chat.ChatRoom
import com.jikgwan.domain.chat.ChatRoomId
import com.jikgwan.domain.gathering.GatheringId
import com.jikgwan.domain.user.UserId
import org.springframework.stereotype.Component

@Component
class ChatRoomAdapter(
    private val chatRoomJpaRepository: ChatRoomJpaRepository
) : ChatRoomPort {

    override fun save(chatRoom: ChatRoom): ChatRoom {
        val entity = ChatRoomEntity.from(chatRoom)
        val saved = chatRoomJpaRepository.save(entity)
        return saved.toDomain()
    }

    override fun findById(id: ChatRoomId): ChatRoom? {
        return chatRoomJpaRepository.findById(id.value)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findByGatheringAndApplicant(gatheringId: GatheringId, applicantId: UserId): ChatRoom? {
        return chatRoomJpaRepository.findByGatheringIdAndApplicantId(
            gatheringId.value,
            applicantId.value
        )?.toDomain()
    }

    override fun findByParticipant(userId: UserId): List<ChatRoom> {
        return chatRoomJpaRepository.findByParticipant(userId.value)
            .map { it.toDomain() }
    }
}
