package com.jikgwan.application.chat.port.out

import com.jikgwan.domain.chat.ChatRoomId
import com.jikgwan.domain.chat.Message
import com.jikgwan.domain.chat.MessageId
import com.jikgwan.domain.user.UserId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface MessagePort {
    fun save(message: Message): Message
    fun findById(id: MessageId): Message?
    fun findByChatRoomId(chatRoomId: ChatRoomId, pageable: Pageable): Page<Message>
    fun findLastMessageByChatRoomId(chatRoomId: ChatRoomId): Message?
    fun markMessagesAsRead(chatRoomId: ChatRoomId, userId: UserId)
    fun countUnreadMessages(chatRoomId: ChatRoomId, userId: UserId): Long
}
