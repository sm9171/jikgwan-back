package com.jikgwan.adapter.`in`.web

import com.jikgwan.adapter.`in`.web.common.ApiResponse
import com.jikgwan.application.user.UserApplicationService
import com.jikgwan.application.user.dto.*
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "인증 API")
class AuthController(
    private val userApplicationService: UserApplicationService
) {

    @PostMapping("/signup", consumes = ["multipart/form-data"])
    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다")
    fun signUp(
        @Valid @ModelAttribute request: SignUpRequest
    ): ResponseEntity<ApiResponse<UserResponse>> {
        val user = userApplicationService.signUp(request)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(user))
    }

    @PostMapping("/login")
    @Operation(summary = "로그인", description = "JWT 토큰을 발급받습니다")
    fun login(
        @Valid @RequestBody request: LoginRequest
    ): ResponseEntity<ApiResponse<TokenResponse>> {
        val token = userApplicationService.login(request)
        return ResponseEntity.ok(ApiResponse.success(token))
    }

    @PostMapping("/logout")
    @Operation(summary = "로그아웃", description = "JWT 토큰을 무효화합니다")
    fun logout(
        @RequestHeader("Authorization") authorization: String
    ): ResponseEntity<ApiResponse<Unit>> {
        val token = authorization.removePrefix("Bearer ")
        userApplicationService.logout(token)
        return ResponseEntity.ok(ApiResponse.success(Unit))
    }
}
