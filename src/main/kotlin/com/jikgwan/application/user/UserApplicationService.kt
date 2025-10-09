package com.jikgwan.application.user

import com.jikgwan.adapter.out.cache.TokenBlacklistService
import com.jikgwan.application.user.dto.*
import com.jikgwan.application.user.port.out.UserPort
import com.jikgwan.common.exception.BusinessException
import com.jikgwan.common.exception.ErrorCode
import com.jikgwan.common.security.JwtTokenProvider
import com.jikgwan.common.storage.FileStorage
import com.jikgwan.domain.user.*
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserApplicationService(
    private val userPort: UserPort,
    private val passwordEncoder: PasswordEncoder,
    private val jwtTokenProvider: JwtTokenProvider,
    private val fileStorage: FileStorage,
    private val tokenBlacklistService: TokenBlacklistService
) {

    fun signUp(request: SignUpRequest): UserResponse {
        require(!userPort.existsByEmail(request.email)) {
            throw BusinessException(ErrorCode.DUPLICATE_EMAIL)
        }

        // 프로필 이미지 업로드
        val profileImageUrl = request.profileImage?.let { image ->
            fileStorage.upload(image, "profiles")
        }

        val profile = Profile(
            profileImage = profileImageUrl?.let { ProfileImage(it) },
            gender = Gender.valueOf(request.gender),
            ageRange = AgeRange.valueOf(request.ageRange),
            supportingTeams = request.supportingTeams.map { Team.valueOf(it) }.toSet()
        )

        val user = User(
            id = UserId(0),
            email = Email(request.email),
            password = Password.encode(request.password, passwordEncoder),
            nickname = Nickname(request.nickname),
            profile = profile
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

    @Transactional(readOnly = true)
    fun getMyInfo(userId: Long): UserResponse {
        val user = userPort.findById(UserId(userId))
            ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        return UserResponse.from(user)
    }

    @Transactional(readOnly = true)
    fun getUserDetail(userId: Long): UserDetailResponse {
        val user = userPort.findById(UserId(userId))
            ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        return UserDetailResponse.from(user)
    }

    fun logout(token: String) {
        // 1. 토큰 유효성 검증
        require(jwtTokenProvider.validateToken(token)) {
            throw BusinessException(ErrorCode.INVALID_TOKEN)
        }

        // 2. 토큰 만료 시간 계산
        val expirationSeconds = jwtTokenProvider.getExpirationSeconds(token)

        // 3. Redis 블랙리스트에 추가
        tokenBlacklistService.addToBlacklist(token, expirationSeconds)
    }
}
