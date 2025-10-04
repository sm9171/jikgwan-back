package com.jikgwan.domain.user

data class Email(val value: String) {
    init {
        require(EMAIL_PATTERN.matches(value)) {
            "올바른 이메일 형식이 아닙니다"
        }
    }

    companion object {
        private val EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+@(.+)$".toRegex()
    }
}
