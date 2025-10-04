package com.jikgwan.application.gathering.port.out

import com.jikgwan.domain.gathering.Gathering
import com.jikgwan.domain.gathering.GatheringId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface GatheringPort {
    fun save(gathering: Gathering): Gathering
    fun findById(id: GatheringId): Gathering?
    fun findAll(pageable: Pageable): Page<Gathering>
}
