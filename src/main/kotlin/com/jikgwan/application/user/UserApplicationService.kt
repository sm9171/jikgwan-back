package com.jikgwan.application.user

import com.jikgwan.application.user.dto.*
import com.jikgwan.application.user.port.out.UserPort
import com.jikgwan.common.exception.BusinessException
import com.jikgwan.common.exception.ErrorCode
import com.jikgwan.common.security.JwtTokenProvider
import com.jikgwan.domain.user.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserApplicationService(
    private val userPort: UserPort,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider
) {

    fun signUp(request: SignUpRequest): UserResponse {
        require(!userPort.existsByEmail(request.email)) {
            throw BusinessException(ErrorCode.DUPLICATE_EMAIL)
        }

        val user = User(
            id = UserId(0),
            email = Email(request.email),
            password = Password.encode(request.password, passwordEncoder),
            nickname = Nickname(request.nickname)
        )

        val savedUser = userPort.save(user)
        return UserResponse.from(savedUser)
    }

    fun login(request: LoginRequest): TokenResponse {
        val user = userPort.findByEmail(request.email)
            ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        require(user.password.matches(request.password, passwordEncoder)) {
            throw BusinessException(ErrorCode.INVALID_PASSWORD)
        }

        val accessToken = jwtTokenProvider.createAccessToken(
            userId = user.id.value,
            email = user.email.value
        )
        val refreshToken = jwtTokenProvider.createRefreshToken(
            userId = user.id.value
        )

        return TokenResponse(
            accessToken = accessToken,
            refreshToken = refreshToken
        )
    }
}
