package com.jikgwan.application.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class SignUpRequest(
    @field:Email(message = "올바른 이메일 형식이 아닙니다")
    val email: String,

    @field:Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    val password: String,

    @field:Size(min = 2, max = 20, message = "닉네임은 2-20자 사이여야 합니다")
    val nickname: String
)
