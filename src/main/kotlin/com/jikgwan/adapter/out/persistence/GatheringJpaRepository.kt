package com.jikgwan.adapter.out.persistence

import com.jikgwan.adapter.out.persistence.entity.GatheringEntity
import com.jikgwan.domain.user.Team
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface GatheringJpaRepository : JpaRepository<GatheringEntity, Long> {
    fun findByGameInfoHomeTeamOrGameInfoAwayTeam(homeTeam: Team, awayTeam: Team, pageable: Pageable): Page<GatheringEntity>
    fun findByHostId(hostId: Long): List<GatheringEntity>
}
