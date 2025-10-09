package com.jikgwan.adapter.out.persistence.chat

import com.jikgwan.domain.chat.ChatRoomId
import com.jikgwan.domain.chat.Message
import com.jikgwan.domain.chat.MessageId
import com.jikgwan.domain.chat.MessageStatus
import com.jikgwan.domain.user.UserId
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "messages",
    indexes = [
        Index(name = "idx_chat_room_id", columnList = "chatRoomId"),
        Index(name = "idx_sender_id", columnList = "senderId"),
        Index(name = "idx_sent_at", columnList = "sentAt")
    ]
)
class MessageEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val chatRoomId: Long,

    @Column(nullable = false)
    val senderId: Long,

    @Column(nullable = false, columnDefinition = "TEXT")
    val content: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val status: MessageStatus = MessageStatus.SENT,

    @Column(nullable = false)
    val sentAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): Message = Message(
        id = MessageId(id),
        chatRoomId = ChatRoomId(chatRoomId),
        senderId = UserId(senderId),
        content = content,
        status = status,
        sentAt = sentAt
    )

    companion object {
        fun from(message: Message): MessageEntity = MessageEntity(
            id = message.id.value,
            chatRoomId = message.chatRoomId.value,
            senderId = message.senderId.value,
            content = message.content,
            status = message.status,
            sentAt = message.sentAt
        )
    }
}
