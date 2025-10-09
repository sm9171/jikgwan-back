package com.jikgwan.domain.gathering

import com.jikgwan.domain.user.Team
import com.jikgwan.domain.user.User
import com.jikgwan.domain.user.UserId
import java.time.LocalDateTime

data class Gathering(
    val id: GatheringId,
    val hostId: UserId,
    val gameInfo: GameInfo,
    val meetingPlace: MeetingPlace,
    val maxParticipants: Int,
    val description: String,
    val status: GatheringStatus = GatheringStatus.ACTIVE,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun validateCreation(host: User) {
        require(host.canCreateGathering()) {
            "프로필을 완성해주세요"
        }
        require(!gameInfo.isPast()) {
            "과거 경기에 대한 모임은 생성할 수 없습니다"
        }
        require(maxParticipants in 1..10) {
            "모집 인원은 1-10명 사이여야 합니다"
        }
    }

    fun isHostedBy(userId: UserId): Boolean =
        hostId == userId

    fun matchesTeam(team: Team): Boolean =
        gameInfo.hasTeam(team)

    fun update(
        meetingPlace: MeetingPlace? = null,
        maxParticipants: Int? = null,
        description: String? = null
    ): Gathering = copy(
        meetingPlace = meetingPlace ?: this.meetingPlace,
        maxParticipants = maxParticipants ?: this.maxParticipants,
        description = description ?: this.description,
        updatedAt = LocalDateTime.now()
    )

    fun canAddParticipant(currentParticipantCount: Int): Boolean =
        currentParticipantCount < maxParticipants

    fun validateParticipantAddition(
        requesterId: UserId,
        applicantId: UserId,
        currentParticipantCount: Int
    ) {
        require(isHostedBy(requesterId)) {
            "모임 호스트만 참여자를 추가할 수 있습니다"
        }
        require(hostId != applicantId) {
            "호스트는 참여자로 추가할 수 없습니다"
        }
        require(canAddParticipant(currentParticipantCount)) {
            "최대 참여 인원을 초과했습니다"
        }
        require(status == GatheringStatus.ACTIVE) {
            "활성 상태의 모임만 참여자를 추가할 수 있습니다"
        }
    }

    fun validateParticipantCancellation(
        requesterId: UserId,
        participantId: UserId
    ) {
        require(isHostedBy(requesterId)) {
            "모임 호스트만 참여자를 취소할 수 있습니다"
        }
        require(hostId != participantId) {
            "호스트는 취소할 수 없습니다"
        }
        require(status == GatheringStatus.ACTIVE) {
            "활성 상태의 모임만 참여자를 취소할 수 있습니다"
        }
    }
}
