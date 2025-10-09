package com.jikgwan.adapter.out.persistence.gathering

import com.jikgwan.domain.gathering.GatheringId
import com.jikgwan.domain.gathering.GatheringParticipant
import com.jikgwan.domain.gathering.GatheringParticipantId
import com.jikgwan.domain.gathering.ParticipantStatus
import com.jikgwan.domain.user.UserId
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "gathering_participants",
    uniqueConstraints = [
        UniqueConstraint(name = "uk_gathering_user", columnNames = ["gatheringId", "userId"])
    ],
    indexes = [
        Index(name = "idx_gathering_id", columnList = "gatheringId"),
        Index(name = "idx_user_id", columnList = "userId")
    ]
)
class GatheringParticipantEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val gatheringId: Long,

    @Column(nullable = false)
    val userId: Long,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val status: ParticipantStatus = ParticipantStatus.CONFIRMED,

    @Column(nullable = false)
    val joinedAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): GatheringParticipant = GatheringParticipant(
        id = GatheringParticipantId(id),
        gatheringId = GatheringId(gatheringId),
        userId = UserId(userId),
        status = status,
        joinedAt = joinedAt
    )

    companion object {
        fun from(participant: GatheringParticipant): GatheringParticipantEntity =
            GatheringParticipantEntity(
                id = participant.id.value,
                gatheringId = participant.gatheringId.value,
                userId = participant.userId.value,
                status = participant.status,
                joinedAt = participant.joinedAt
            )
    }
}
