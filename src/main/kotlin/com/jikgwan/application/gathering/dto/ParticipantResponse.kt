package com.jikgwan.application.gathering.dto

import com.jikgwan.domain.gathering.GatheringParticipant
import com.jikgwan.domain.user.User
import java.time.LocalDateTime

data class ParticipantResponse(
    val userId: Long,
    val nickname: String,
    val profileImageUrl: String?,
    val gender: String?,
    val ageRange: String?,
    val teams: Set<String>,
    val joinedAt: LocalDateTime
) {
    companion object {
        fun from(participant: GatheringParticipant, user: User) = ParticipantResponse(
            userId = participant.userId.value,
            nickname = user.nickname.value,
            profileImageUrl = user.profile?.profileImage?.value,
            gender = user.profile?.gender?.name,
            ageRange = user.profile?.ageRange?.name,
            teams = user.profile?.supportingTeams?.map { it.name }?.toSet() ?: emptySet(),
            joinedAt = participant.joinedAt
        )
    }
}
