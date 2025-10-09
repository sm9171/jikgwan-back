package com.jikgwan.adapter.out.persistence.chat

import com.jikgwan.domain.chat.ChatRoom
import com.jikgwan.domain.chat.ChatRoomId
import com.jikgwan.domain.chat.ChatRoomStatus
import com.jikgwan.domain.gathering.GatheringId
import com.jikgwan.domain.user.UserId
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "chat_rooms",
    indexes = [
        Index(name = "idx_gathering_id", columnList = "gatheringId"),
        Index(name = "idx_host_id", columnList = "hostId"),
        Index(name = "idx_applicant_id", columnList = "applicantId")
    ]
)
class ChatRoomEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val gatheringId: Long,

    @Column(nullable = false)
    val hostId: Long,

    @Column(nullable = false)
    val applicantId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val status: ChatRoomStatus = ChatRoomStatus.ACTIVE,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): ChatRoom = ChatRoom(
        id = ChatRoomId(id),
        gatheringId = GatheringId(gatheringId),
        hostId = UserId(hostId),
        applicantId = UserId(applicantId),
        status = status,
        createdAt = createdAt
    )

    companion object {
        fun from(chatRoom: ChatRoom): ChatRoomEntity = ChatRoomEntity(
            id = chatRoom.id.value,
            gatheringId = chatRoom.gatheringId.value,
            hostId = chatRoom.hostId.value,
            applicantId = chatRoom.applicantId.value,
            status = chatRoom.status,
            createdAt = chatRoom.createdAt
        )
    }
}
