package com.jikgwan.application.chat.port.out

import com.jikgwan.domain.chat.ChatRoom
import com.jikgwan.domain.chat.ChatRoomId
import com.jikgwan.domain.gathering.GatheringId
import com.jikgwan.domain.user.UserId

interface ChatRoomPort {
    fun save(chatRoom: ChatRoom): ChatRoom
    fun findById(id: ChatRoomId): ChatRoom?
    fun findByGatheringAndApplicant(gatheringId: GatheringId, applicantId: UserId): ChatRoom?
    fun findByParticipant(userId: UserId): List<ChatRoom>
}
