package com.jikgwan.adapter.`in`.web

import com.jikgwan.adapter.`in`.web.common.ApiResponse
import com.jikgwan.application.user.UserApplicationService
import com.jikgwan.application.user.dto.UserResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users")
@Tag(name = "User", description = "사용자 API")
class UserController(
    private val userApplicationService: UserApplicationService
) {

    @GetMapping("/me")
    @Operation(
        summary = "내 정보 조회",
        description = "현재 로그인한 사용자의 정보를 조회합니다",
        security = [SecurityRequirement(name = "Bearer Authentication")]
    )
    fun getMyInfo(
        authentication: Authentication
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val userId = authentication.name.toLong()
        val user = userApplicationService.getMyInfo(userId)
        return ResponseEntity.ok(ApiResponse.success(user))
    }
}
