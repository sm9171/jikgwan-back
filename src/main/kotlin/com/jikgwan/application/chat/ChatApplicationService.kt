package com.jikgwan.application.chat

import com.jikgwan.application.chat.dto.ChatRoomDetailResponse
import com.jikgwan.application.chat.dto.ChatRoomResponse
import com.jikgwan.application.chat.dto.MessageResponse
import com.jikgwan.application.chat.port.out.ChatRoomPort
import com.jikgwan.application.chat.port.out.MessagePort
import com.jikgwan.application.gathering.port.out.GatheringPort
import com.jikgwan.application.user.port.out.UserPort
import com.jikgwan.common.exception.BusinessException
import com.jikgwan.common.exception.ErrorCode
import com.jikgwan.domain.chat.*
import com.jikgwan.domain.gathering.GatheringId
import com.jikgwan.domain.user.UserId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ChatApplicationService(
    private val chatRoomPort: ChatRoomPort,
    private val messagePort: MessagePort,
    private val gatheringPort: GatheringPort,
    private val userPort: UserPort,
    private val gatheringParticipantPort: com.jikgwan.application.gathering.port.out.GatheringParticipantPort
) {

    fun createChatRoom(
        userId: Long,
        gatheringId: Long
    ): ChatRoomResponse {
        // 1. 모임 조회
        val gathering = gatheringPort.findById(GatheringId(gatheringId))
            ?: throw BusinessException(ErrorCode.GATHERING_NOT_FOUND)

        // 2. 본인 모임 체크
        require(!gathering.isHostedBy(UserId(userId))) {
            "본인이 만든 모임에는 참여할 수 없습니다"
        }

        // 3. 기존 채팅방 체크
        chatRoomPort.findByGatheringAndApplicant(
            GatheringId(gatheringId),
            UserId(userId)
        )?.let { return ChatRoomResponse.from(it) }

        // 4. 새 채팅방 생성
        val chatRoom = ChatRoom.create(
            gatheringId = GatheringId(gatheringId),
            hostId = gathering.hostId,
            applicantId = UserId(userId)
        )

        val saved = chatRoomPort.save(chatRoom)

        return ChatRoomResponse.from(saved)
    }

    fun sendMessage(
        chatRoomId: Long,
        senderId: Long,
        content: String
    ): Message {
        // 1. 채팅방 조회 및 권한 검증
        val chatRoom = chatRoomPort.findById(ChatRoomId(chatRoomId))
            ?: throw BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND)

        chatRoom.validateParticipant(UserId(senderId))

        // 2. 메시지 생성
        val message = Message(
            id = MessageId(0),
            chatRoomId = ChatRoomId(chatRoomId),
            senderId = UserId(senderId),
            content = content
        )

        // 3. 저장
        return messagePort.save(message)
    }

    @Transactional(readOnly = true)
    fun getChatRooms(userId: Long): List<ChatRoomDetailResponse> {
        val chatRooms = chatRoomPort.findByParticipant(UserId(userId))

        return chatRooms.map { chatRoom ->
            // 모임 정보 조회
            val gathering = gatheringPort.findById(chatRoom.gatheringId)
                ?: throw BusinessException(ErrorCode.GATHERING_NOT_FOUND)

            // 참여자 정보 조회
            val host = userPort.findById(chatRoom.hostId)
                ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)
            val applicant = userPort.findById(chatRoom.applicantId)
                ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

            // 마지막 메시지 조회
            val lastMessage = messagePort.findLastMessageByChatRoomId(chatRoom.id)
                ?.let { MessageResponse.from(it) }

            // 읽지 않은 메시지 수 조회
            val unreadCount = messagePort.countUnreadMessages(chatRoom.id, UserId(userId))

            // 참여 확정 여부 확인
            val isConfirmed = gatheringParticipantPort.existsByGatheringIdAndUserId(
                chatRoom.gatheringId,
                chatRoom.applicantId
            )

            ChatRoomDetailResponse.from(
                chatRoom = chatRoom,
                gathering = gathering,
                host = host,
                applicant = applicant,
                lastMessage = lastMessage,
                unreadCount = unreadCount,
                isConfirmed = isConfirmed
            )
        }
    }

    @Transactional(readOnly = true)
    fun getChatRoom(chatRoomId: Long, userId: Long): ChatRoomDetailResponse {
        val chatRoom = chatRoomPort.findById(ChatRoomId(chatRoomId))
            ?: throw BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND)

        // 권한 검증
        chatRoom.validateParticipant(UserId(userId))

        // 모임 정보 조회
        val gathering = gatheringPort.findById(chatRoom.gatheringId)
            ?: throw BusinessException(ErrorCode.GATHERING_NOT_FOUND)

        // 참여자 정보 조회
        val host = userPort.findById(chatRoom.hostId)
            ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)
        val applicant = userPort.findById(chatRoom.applicantId)
            ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        // 마지막 메시지 조회
        val lastMessage = messagePort.findLastMessageByChatRoomId(ChatRoomId(chatRoomId))
            ?.let { MessageResponse.from(it) }

        // 읽지 않은 메시지 수 조회
        val unreadCount = messagePort.countUnreadMessages(ChatRoomId(chatRoomId), UserId(userId))

        // 참여 확정 여부 확인
        val isConfirmed = gatheringParticipantPort.existsByGatheringIdAndUserId(
            chatRoom.gatheringId,
            chatRoom.applicantId
        )

        return ChatRoomDetailResponse.from(
            chatRoom = chatRoom,
            gathering = gathering,
            host = host,
            applicant = applicant,
            lastMessage = lastMessage,
            unreadCount = unreadCount,
            isConfirmed = isConfirmed
        )
    }

    @Transactional(readOnly = true)
    fun getMessages(
        chatRoomId: Long,
        userId: Long,
        pageable: Pageable
    ): Page<MessageResponse> {
        // 권한 검증
        val chatRoom = chatRoomPort.findById(ChatRoomId(chatRoomId))
            ?: throw BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND)

        chatRoom.validateParticipant(UserId(userId))

        // 메시지 조회
        return messagePort.findByChatRoomId(ChatRoomId(chatRoomId), pageable)
            .map { MessageResponse.from(it) }
    }

    fun markMessagesAsRead(
        chatRoomId: Long,
        userId: Long
    ) {
        // 권한 검증
        val chatRoom = chatRoomPort.findById(ChatRoomId(chatRoomId))
            ?: throw BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND)

        chatRoom.validateParticipant(UserId(userId))

        // 메시지 읽음 처리
        messagePort.markMessagesAsRead(ChatRoomId(chatRoomId), UserId(userId))
    }

    @Transactional(readOnly = true)
    fun getUnreadMessageCount(
        chatRoomId: Long,
        userId: Long
    ): Long {
        // 권한 검증
        val chatRoom = chatRoomPort.findById(ChatRoomId(chatRoomId))
            ?: throw BusinessException(ErrorCode.CHAT_ROOM_NOT_FOUND)

        chatRoom.validateParticipant(UserId(userId))

        return messagePort.countUnreadMessages(ChatRoomId(chatRoomId), UserId(userId))
    }
}
