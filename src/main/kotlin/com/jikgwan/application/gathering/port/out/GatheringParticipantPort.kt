package com.jikgwan.application.gathering.port.out

import com.jikgwan.domain.gathering.GatheringId
import com.jikgwan.domain.gathering.GatheringParticipant
import com.jikgwan.domain.user.UserId

interface GatheringParticipantPort {
    fun save(participant: GatheringParticipant): GatheringParticipant
    fun findByGatheringIdAndUserId(gatheringId: GatheringId, userId: UserId): GatheringParticipant?
    fun countByGatheringId(gatheringId: GatheringId): Int
    fun existsByGatheringIdAndUserId(gatheringId: GatheringId, userId: UserId): Boolean
    fun findAllByGatheringId(gatheringId: GatheringId): List<GatheringParticipant>
    fun findAllByUserId(userId: UserId): List<GatheringParticipant>
    fun deleteByGatheringIdAndUserId(gatheringId: GatheringId, userId: UserId)
}