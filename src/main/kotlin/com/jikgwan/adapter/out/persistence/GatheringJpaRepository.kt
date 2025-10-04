package com.jikgwan.adapter.out.persistence

import com.jikgwan.adapter.out.persistence.entity.GatheringEntity
import org.springframework.data.jpa.repository.JpaRepository

interface GatheringJpaRepository : JpaRepository<GatheringEntity, Long>
