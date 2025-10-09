package com.jikgwan.adapter.out.persistence.gathering

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface GatheringParticipantJpaRepository : JpaRepository<GatheringParticipantEntity, Long> {

    fun existsByGatheringIdAndUserId(gatheringId: Long, userId: Long): Boolean

    fun findByGatheringId(gatheringId: Long): List<GatheringParticipantEntity>

    fun findByGatheringIdAndUserId(gatheringId: Long, userId: Long): GatheringParticipantEntity?

    fun findByUserId(userId: Long): List<GatheringParticipantEntity>

    fun countByGatheringId(gatheringId: Long): Long

    fun deleteByGatheringIdAndUserId(gatheringId: Long, userId: Long)

    @Query("SELECT p.userId FROM GatheringParticipantEntity p WHERE p.gatheringId = :gatheringId")
    fun findUserIdsByGatheringId(gatheringId: Long): List<Long>
}
