package com.jikgwan.adapter.out.persistence.chat

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ChatRoomJpaRepository : JpaRepository<ChatRoomEntity, Long> {
    fun findByGatheringIdAndApplicantId(gatheringId: Long, applicantId: Long): ChatRoomEntity?

    @Query("SELECT c FROM ChatRoomEntity c WHERE c.hostId = :userId OR c.applicantId = :userId ORDER BY c.createdAt DESC")
    fun findByParticipant(userId: Long): List<ChatRoomEntity>
}
