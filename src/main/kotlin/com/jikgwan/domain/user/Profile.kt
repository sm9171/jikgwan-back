package com.jikgwan.domain.user

@JvmInline
value class ProfileImage(val value: String)

enum class Gender { MALE, FEMALE, OTHER }

enum class AgeRange { TEENS, TWENTIES, THIRTIES, FORTIES, FIFTIES_PLUS }

data class Profile(
    val profileImage: ProfileImage?,
    val gender: Gender,
    val ageRange: AgeRange,
    val supportingTeams: Set<Team>
) {
    fun isComplete(): Boolean =
        profileImage != null && supportingTeams.isNotEmpty()
}
