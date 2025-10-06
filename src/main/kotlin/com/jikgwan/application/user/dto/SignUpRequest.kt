package com.jikgwan.application.user.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size
import org.springframework.web.multipart.MultipartFile

data class SignUpRequest(
    @field:Email(message = "올바른 이메일 형식이 아닙니다")
    val email: String,

    @field:Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다")
    val password: String,

    @field:Size(min = 2, max = 20, message = "닉네임은 2-20자 사이여야 합니다")
    val nickname: String,

    @field:NotBlank(message = "성별을 선택해주세요")
    val gender: String,

    @field:NotBlank(message = "연령대를 선택해주세요")
    val ageRange: String,

    @field:NotEmpty(message = "응원하는 팀을 최소 1개 이상 선택해주세요")
    val supportingTeams: Set<String>,

    val profileImage: MultipartFile? = null
)
