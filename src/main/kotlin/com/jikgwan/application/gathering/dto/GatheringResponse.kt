package com.jikgwan.application.gathering.dto

import com.jikgwan.domain.gathering.GameInfo
import com.jikgwan.domain.gathering.Gathering
import com.jikgwan.domain.user.User
import java.time.LocalDateTime

data class GatheringResponse(
    val id: Long,
    val gameInfo: GameInfoResponse,
    val meetingPlace: String,
    val maxParticipants: Int,
    val description: String,
    val host: HostInfoResponse,
    val createdAt: LocalDateTime
) {
    companion object {
        fun from(gathering: Gathering, host: User) = GatheringResponse(
            id = gathering.id.value,
            gameInfo = GameInfoResponse.from(gathering.gameInfo),
            meetingPlace = gathering.meetingPlace.value,
            maxParticipants = gathering.maxParticipants,
            description = gathering.description,
            host = HostInfoResponse.from(host),
            createdAt = gathering.createdAt
        )
    }
}

data class GameInfoResponse(
    val gameDateTime: LocalDateTime,
    val homeTeam: String,
    val awayTeam: String,
    val stadium: String
) {
    companion object {
        fun from(gameInfo: GameInfo) = GameInfoResponse(
            gameDateTime = gameInfo.gameDateTime,
            homeTeam = gameInfo.homeTeam.name,
            awayTeam = gameInfo.awayTeam.name,
            stadium = gameInfo.stadium.name
        )
    }
}

data class HostInfoResponse(
    val id: Long,
    val nickname: String,
    val profileImageUrl: String?,
    val gender: String?,
    val ageRange: String?
) {
    companion object {
        fun from(user: User) = HostInfoResponse(
            id = user.id.value,
            nickname = user.nickname.value,
            profileImageUrl = user.profile?.profileImage?.value,
            gender = user.profile?.gender?.name,
            ageRange = user.profile?.ageRange?.name
        )
    }
}
