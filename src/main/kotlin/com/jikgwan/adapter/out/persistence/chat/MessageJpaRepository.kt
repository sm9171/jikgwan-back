package com.jikgwan.adapter.out.persistence.chat

import com.jikgwan.domain.chat.MessageStatus
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface MessageJpaRepository : JpaRepository<MessageEntity, Long> {
    fun findByChatRoomIdOrderBySentAtDesc(chatRoomId: Long, pageable: Pageable): Page<MessageEntity>

    fun findFirstByChatRoomIdOrderBySentAtDesc(chatRoomId: Long): MessageEntity?

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE MessageEntity m SET m.status = 'READ' WHERE m.chatRoomId = :chatRoomId AND m.senderId != :userId AND m.status = 'SENT'")
    fun markMessagesAsRead(chatRoomId: Long, userId: Long)

    @Query("SELECT COUNT(m) FROM MessageEntity m WHERE m.chatRoomId = :chatRoomId AND m.senderId != :userId AND m.status = 'SENT'")
    fun countUnreadMessages(chatRoomId: Long, userId: Long): Long
}
