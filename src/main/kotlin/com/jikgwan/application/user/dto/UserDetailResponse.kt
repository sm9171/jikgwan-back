package com.jikgwan.application.user.dto

import com.jikgwan.domain.user.User

data class UserDetailResponse(
    val id: Long,
    val nickname: String,
    val profile: ProfileDetailResponse?
) {
    companion object {
        fun from(user: User) = UserDetailResponse(
            id = user.id.value,
            nickname = user.nickname.value,
            profile = user.profile?.let { ProfileDetailResponse.from(it) }
        )
    }
}

data class ProfileDetailResponse(
    val profileImageUrl: String?,
    val gender: String,
    val ageRange: String,
    val supportingTeams: Set<String>
) {
    companion object {
        fun from(profile: com.jikgwan.domain.user.Profile) = ProfileDetailResponse(
            profileImageUrl = profile.profileImage?.value,
            gender = profile.gender.name,
            ageRange = profile.ageRange.name,
            supportingTeams = profile.supportingTeams.map { it.name }.toSet()
        )
    }
}
