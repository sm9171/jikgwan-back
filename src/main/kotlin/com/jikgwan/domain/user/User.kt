package com.jikgwan.domain.user

import java.time.LocalDateTime

data class User(
    val id: UserId,
    val email: Email,
    val password: Password,
    val nickname: Nickname,
    val profile: Profile? = null,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun canCreateGathering(): Boolean =
        profile?.isComplete() ?: false

    fun canApplyToGathering(): Boolean =
        profile?.isComplete() ?: false

    fun updateProfile(newProfile: Profile): User {
        validateProfileUpdate(newProfile)
        return copy(
            profile = newProfile,
            updatedAt = LocalDateTime.now()
        )
    }

    private fun validateProfileUpdate(newProfile: Profile) {
        require(newProfile.isComplete()) {
            "프로필의 모든 필수 정보를 입력해주세요"
        }
    }
}
