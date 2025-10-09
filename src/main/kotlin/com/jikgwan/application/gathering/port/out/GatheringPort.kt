package com.jikgwan.application.gathering.port.out

import com.jikgwan.domain.gathering.Gathering
import com.jikgwan.domain.gathering.GatheringId
import com.jikgwan.domain.user.Team
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface GatheringPort {
    fun save(gathering: Gathering): Gathering
    fun findById(id: GatheringId): Gathering?
    fun findAll(pageable: Pageable): Page<Gathering>
    fun findByTeam(team: Team, pageable: Pageable): Page<Gathering>
    fun findByHostId(hostId: com.jikgwan.domain.user.UserId): List<Gathering>
}
