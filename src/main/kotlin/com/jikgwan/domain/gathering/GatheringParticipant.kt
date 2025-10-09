package com.jikgwan.domain.gathering

import com.jikgwan.domain.user.UserId
import java.time.LocalDateTime

@JvmInline
value class GatheringParticipantId(val value: Long)

data class GatheringParticipant(
    val id: GatheringParticipantId,
    val gatheringId: GatheringId,
    val userId: UserId,
    val status: ParticipantStatus = ParticipantStatus.CONFIRMED,
    val joinedAt: LocalDateTime = LocalDateTime.now()
)

enum class ParticipantStatus {
    CONFIRMED,  // 확정됨
    CANCELLED   // 취소됨
}