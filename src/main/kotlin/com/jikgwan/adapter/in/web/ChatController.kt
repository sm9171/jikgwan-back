package com.jikgwan.adapter.`in`.web

import com.jikgwan.adapter.`in`.web.common.ApiResponse
import com.jikgwan.application.chat.ChatApplicationService
import com.jikgwan.application.chat.dto.ChatRoomDetailResponse
import com.jikgwan.application.chat.dto.ChatRoomResponse
import com.jikgwan.application.chat.dto.MessageResponse
import com.jikgwan.application.chat.dto.SendMessageRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/chat")
@Tag(name = "Chat", description = "채팅 API")
class ChatController(
    private val chatApplicationService: ChatApplicationService
) {

    @PostMapping("/rooms")
    @Operation(summary = "채팅방 생성", description = "모임에 참여 신청하여 채팅방을 생성합니다")
    fun createChatRoom(
        @AuthenticationPrincipal userId: String,
        @RequestParam gatheringId: Long
    ): ResponseEntity<ApiResponse<ChatRoomResponse>> {
        val chatRoom = chatApplicationService.createChatRoom(
            userId = userId.toLong(),
            gatheringId = gatheringId
        )
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(chatRoom))
    }

    @GetMapping("/rooms")
    @Operation(summary = "내 채팅방 목록", description = "참여 중인 채팅방 목록을 상세 정보와 함께 조회합니다")
    fun getChatRooms(
        @AuthenticationPrincipal userId: String
    ): ResponseEntity<ApiResponse<List<ChatRoomDetailResponse>>> {
        val chatRooms = chatApplicationService.getChatRooms(userId.toLong())
        return ResponseEntity.ok(ApiResponse.success(chatRooms))
    }

    @GetMapping("/rooms/{chatRoomId}")
    @Operation(summary = "채팅방 단일 조회", description = "특정 채팅방의 상세 정보를 조회합니다")
    fun getChatRoom(
        @AuthenticationPrincipal userId: String,
        @PathVariable chatRoomId: Long
    ): ResponseEntity<ApiResponse<ChatRoomDetailResponse>> {
        val chatRoom = chatApplicationService.getChatRoom(
            chatRoomId = chatRoomId,
            userId = userId.toLong()
        )
        return ResponseEntity.ok(ApiResponse.success(chatRoom))
    }

    @GetMapping("/rooms/{chatRoomId}/messages")
    @Operation(summary = "채팅 메시지 조회", description = "채팅방의 메시지 목록을 조회합니다")
    fun getMessages(
        @AuthenticationPrincipal userId: String,
        @PathVariable chatRoomId: Long,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<MessageResponse>>> {
        val messages = chatApplicationService.getMessages(
            chatRoomId = chatRoomId,
            userId = userId.toLong(),
            pageable = pageable
        )
        return ResponseEntity.ok(ApiResponse.success(messages))
    }

    @PostMapping("/rooms/{chatRoomId}/read")
    @Operation(summary = "메시지 읽음 처리", description = "채팅방의 메시지를 읽음 처리합니다")
    fun markMessagesAsRead(
        @AuthenticationPrincipal userId: String,
        @PathVariable chatRoomId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        chatApplicationService.markMessagesAsRead(
            chatRoomId = chatRoomId,
            userId = userId.toLong()
        )
        return ResponseEntity.ok(ApiResponse.success(Unit))
    }

    @GetMapping("/rooms/{chatRoomId}/unread-count")
    @Operation(summary = "읽지 않은 메시지 수 조회", description = "채팅방의 읽지 않은 메시지 수를 조회합니다")
    fun getUnreadMessageCount(
        @AuthenticationPrincipal userId: String,
        @PathVariable chatRoomId: Long
    ): ResponseEntity<ApiResponse<Long>> {
        val count = chatApplicationService.getUnreadMessageCount(
            chatRoomId = chatRoomId,
            userId = userId.toLong()
        )
        return ResponseEntity.ok(ApiResponse.success(count))
    }

    @PostMapping("/rooms/{chatRoomId}/messages")
    @Operation(summary = "메시지 전송 (REST API)", description = "REST API로 메시지를 전송합니다 (WebSocket 없이도 가능)")
    fun sendMessage(
        @AuthenticationPrincipal userId: String,
        @PathVariable chatRoomId: Long,
        @Valid @RequestBody request: SendMessageRequest
    ): ResponseEntity<ApiResponse<MessageResponse>> {
        val message = chatApplicationService.sendMessage(
            chatRoomId = chatRoomId,
            senderId = userId.toLong(),
            content = request.content
        )
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(MessageResponse.from(message)))
    }
}
