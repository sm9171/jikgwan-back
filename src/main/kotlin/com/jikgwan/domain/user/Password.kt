package com.jikgwan.domain.user

import org.springframework.security.crypto.password.PasswordEncoder

data class Password(val value: String) {

    fun matches(rawPassword: String, encoder: PasswordEncoder): Boolean =
        encoder.matches(rawPassword, value)

    companion object {
        fun encode(rawPassword: String, encoder: PasswordEncoder): Password {
            validateRawPassword(rawPassword)
            return Password(encoder.encode(rawPassword))
        }

        private fun validateRawPassword(password: String) {
            require(password.length >= 8) {
                "비밀번호는 최소 8자 이상이어야 합니다"
            }
        }
    }
}
