package com.jikgwan.domain.chat

import com.jikgwan.domain.user.UserId
import java.time.LocalDateTime

data class Message(
    val id: MessageId,
    val chatRoomId: ChatRoomId,
    val senderId: UserId,
    val content: String,
    val status: MessageStatus = MessageStatus.SENT,
    val sentAt: LocalDateTime = LocalDateTime.now()
) {
    init {
        require(content.isNotBlank()) {
            "메시지 내용을 입력해주세요"
        }
        require(content.length <= 1000) {
            "메시지는 1000자 이하여야 합니다"
        }
    }
}
