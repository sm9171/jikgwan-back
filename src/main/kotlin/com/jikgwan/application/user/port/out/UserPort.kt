package com.jikgwan.application.user.port.out

import com.jikgwan.domain.user.User
import com.jikgwan.domain.user.UserId

interface UserPort {
    fun save(user: User): User
    fun findById(id: UserId): User?
    fun findByEmail(email: String): User?
    fun existsByEmail(email: String): Boolean
}
