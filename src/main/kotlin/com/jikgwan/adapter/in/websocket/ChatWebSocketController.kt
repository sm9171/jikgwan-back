package com.jikgwan.adapter.`in`.websocket

import com.jikgwan.application.chat.ChatApplicationService
import com.jikgwan.application.chat.dto.MessageResponse
import com.jikgwan.application.chat.dto.SendMessageRequest
import org.springframework.messaging.handler.annotation.DestinationVariable
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.security.Principal

@Controller
class ChatWebSocketController(
    private val chatService: ChatApplicationService,
    private val messagingTemplate: SimpMessagingTemplate
) {

    /**
     * 클라이언트에서 /app/chat/{chatRoomId}로 메시지 전송
     * 예: stompClient.send("/app/chat/1", {}, JSON.stringify({content: "안녕하세요"}))
     */
    @MessageMapping("/chat/{chatRoomId}")
    fun sendMessage(
        @DestinationVariable chatRoomId: Long,
        request: SendMessageRequest,
        principal: Principal
    ) {
        val userId = principal.name.toLong()

        // 1. 메시지 생성 및 저장
        val message = chatService.sendMessage(
            chatRoomId = chatRoomId,
            senderId = userId,
            content = request.content
        )

        // 2. 메시지 응답 생성
        val response = MessageResponse.from(message)

        // 3. 채팅방의 모든 참여자에게 전송
        // 클라이언트는 /topic/chat/{chatRoomId}를 구독해야 함
        messagingTemplate.convertAndSend(
            "/topic/chat/$chatRoomId",
            response
        )
    }

    /**
     * 메시지 읽음 처리
     * 예: stompClient.send("/app/chat/{chatRoomId}/read", {}, {})
     */
    @MessageMapping("/chat/{chatRoomId}/read")
    fun markAsRead(
        @DestinationVariable chatRoomId: Long,
        principal: Principal
    ) {
        val userId = principal.name.toLong()

        chatService.markMessagesAsRead(
            chatRoomId = chatRoomId,
            userId = userId
        )

        // 읽음 상태 변경을 상대방에게 알림
        messagingTemplate.convertAndSend(
            "/topic/chat/$chatRoomId/read",
            mapOf("userId" to userId, "chatRoomId" to chatRoomId)
        )
    }
}
