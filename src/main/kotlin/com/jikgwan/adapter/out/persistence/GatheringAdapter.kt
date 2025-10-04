package com.jikgwan.adapter.out.persistence

import com.jikgwan.adapter.out.persistence.entity.GatheringEntity
import com.jikgwan.application.gathering.port.out.GatheringPort
import com.jikgwan.domain.gathering.Gathering
import com.jikgwan.domain.gathering.GatheringId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Component

@Component
class GatheringAdapter(
    private val gatheringJpaRepository: GatheringJpaRepository
) : GatheringPort {

    override fun save(gathering: Gathering): Gathering {
        val entity = GatheringEntity.from(gathering)
        val saved = gatheringJpaRepository.save(entity)
        return saved.toDomain()
    }

    override fun findById(id: GatheringId): Gathering? {
        return gatheringJpaRepository.findById(id.value)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findAll(pageable: Pageable): Page<Gathering> {
        return gatheringJpaRepository.findAll(pageable)
            .map { it.toDomain() }
    }
}
