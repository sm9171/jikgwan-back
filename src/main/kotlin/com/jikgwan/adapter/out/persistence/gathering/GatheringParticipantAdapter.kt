package com.jikgwan.adapter.out.persistence.gathering

import com.jikgwan.application.gathering.port.out.GatheringParticipantPort
import com.jikgwan.domain.gathering.GatheringId
import com.jikgwan.domain.gathering.GatheringParticipant
import com.jikgwan.domain.user.UserId
import org.springframework.stereotype.Component

@Component
class GatheringParticipantAdapter(
    private val gatheringParticipantJpaRepository: GatheringParticipantJpaRepository
) : GatheringParticipantPort {

    override fun save(participant: GatheringParticipant): GatheringParticipant {
        val entity = GatheringParticipantEntity.from(participant)
        val saved = gatheringParticipantJpaRepository.save(entity)
        return saved.toDomain()
    }

    override fun findByGatheringIdAndUserId(
        gatheringId: GatheringId,
        userId: UserId
    ): GatheringParticipant? {
        return gatheringParticipantJpaRepository.findByGatheringIdAndUserId(
            gatheringId.value,
            userId.value
        )?.toDomain()
    }

    override fun countByGatheringId(gatheringId: GatheringId): Int {
        return gatheringParticipantJpaRepository.countByGatheringId(gatheringId.value).toInt()
    }

    override fun existsByGatheringIdAndUserId(
        gatheringId: GatheringId,
        userId: UserId
    ): Boolean {
        return gatheringParticipantJpaRepository.existsByGatheringIdAndUserId(
            gatheringId.value,
            userId.value
        )
    }

    override fun findAllByGatheringId(gatheringId: GatheringId): List<GatheringParticipant> {
        return gatheringParticipantJpaRepository.findByGatheringId(gatheringId.value)
            .map { it.toDomain() }
    }

    override fun findAllByUserId(userId: UserId): List<GatheringParticipant> {
        return gatheringParticipantJpaRepository.findByUserId(userId.value)
            .map { it.toDomain() }
    }

    override fun deleteByGatheringIdAndUserId(gatheringId: GatheringId, userId: UserId) {
        gatheringParticipantJpaRepository.deleteByGatheringIdAndUserId(
            gatheringId.value,
            userId.value
        )
    }
}
