package com.jikgwan.application.user.dto

import com.jikgwan.domain.user.*

data class UserResponse(
    val id: Long,
    val email: String,
    val nickname: String,
    val profile: ProfileResponse?
) {
    companion object {
        fun from(user: User) = UserResponse(
            id = user.id.value,
            email = user.email.value,
            nickname = user.nickname.value,
            profile = user.profile?.let { ProfileResponse.from(it) }
        )
    }
}

data class ProfileResponse(
    val profileImageUrl: String?,
    val gender: String,
    val ageRange: String,
    val teams: Set<String>
) {
    companion object {
        fun from(profile: Profile) = ProfileResponse(
            profileImageUrl = profile.profileImage?.value,
            gender = profile.gender.name,
            ageRange = profile.ageRange.name,
            teams = profile.supportingTeams.map { it.name }.toSet()
        )
    }
}
