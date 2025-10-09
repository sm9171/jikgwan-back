package com.jikgwan.adapter.out.persistence.chat

import com.jikgwan.application.chat.port.out.MessagePort
import com.jikgwan.domain.chat.ChatRoomId
import com.jikgwan.domain.chat.Message
import com.jikgwan.domain.chat.MessageId
import com.jikgwan.domain.user.UserId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class MessageAdapter(
    private val messageJpaRepository: MessageJpaRepository
) : MessagePort {

    override fun save(message: Message): Message {
        val entity = MessageEntity.from(message)
        val saved = messageJpaRepository.save(entity)
        return saved.toDomain()
    }

    override fun findById(id: MessageId): Message? {
        return messageJpaRepository.findById(id.value)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findByChatRoomId(chatRoomId: ChatRoomId, pageable: Pageable): Page<Message> {
        return messageJpaRepository.findByChatRoomIdOrderBySentAtDesc(chatRoomId.value, pageable)
            .map { it.toDomain() }
    }

    override fun findLastMessageByChatRoomId(chatRoomId: ChatRoomId): Message? {
        return messageJpaRepository.findFirstByChatRoomIdOrderBySentAtDesc(chatRoomId.value)
            ?.toDomain()
    }

    @Transactional
    override fun markMessagesAsRead(chatRoomId: ChatRoomId, userId: UserId) {
        messageJpaRepository.markMessagesAsRead(chatRoomId.value, userId.value)
    }

    override fun countUnreadMessages(chatRoomId: ChatRoomId, userId: UserId): Long {
        return messageJpaRepository.countUnreadMessages(chatRoomId.value, userId.value)
    }
}
