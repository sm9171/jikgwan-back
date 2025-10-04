package com.jikgwan.domain.user

@JvmInline
value class Nickname(val value: String) {
    init {
        require(value.length in 2..20) {
            "닉네임은 2-20자 사이여야 합니다"
        }
    }
}
