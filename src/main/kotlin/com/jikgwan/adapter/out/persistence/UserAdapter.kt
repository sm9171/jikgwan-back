package com.jikgwan.adapter.out.persistence

import com.jikgwan.adapter.out.persistence.entity.UserEntity
import com.jikgwan.application.user.port.out.UserPort
import com.jikgwan.domain.user.User
import com.jikgwan.domain.user.UserId
import org.springframework.stereotype.Component

@Component
class UserAdapter(
    private val userJpaRepository: UserJpaRepository
) : UserPort {

    override fun save(user: User): User {
        val entity = UserEntity.from(user)
        val saved = userJpaRepository.save(entity)
        return saved.toDomain()
    }

    override fun findById(id: UserId): User? {
        return userJpaRepository.findById(id.value)
            .map { it.toDomain() }
            .orElse(null)
    }

    override fun findByEmail(email: String): User? {
        return userJpaRepository.findByEmail(email)?.toDomain()
    }

    override fun existsByEmail(email: String): Boolean {
        return userJpaRepository.existsByEmail(email)
    }
}
