package com.jikgwan.application.gathering

import com.jikgwan.application.gathering.dto.CreateGatheringRequest
import com.jikgwan.application.gathering.dto.GatheringResponse
import com.jikgwan.application.gathering.dto.ParticipantResponse
import com.jikgwan.application.gathering.port.out.GatheringParticipantPort
import com.jikgwan.application.gathering.port.out.GatheringPort
import com.jikgwan.application.user.port.out.UserPort
import com.jikgwan.common.exception.BusinessException
import com.jikgwan.common.exception.ErrorCode
import com.jikgwan.domain.gathering.*
import com.jikgwan.domain.user.Team
import com.jikgwan.domain.user.UserId
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class GatheringApplicationService(
    private val gatheringPort: GatheringPort,
    private val userPort: UserPort,
    private val gatheringParticipantPort: GatheringParticipantPort
) {

    fun createGathering(
        userId: Long,
        request: CreateGatheringRequest
    ): GatheringResponse {
        val host = userPort.findById(UserId(userId))
            ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        val gathering = Gathering(
            id = GatheringId(0),
            hostId = host.id,
            gameInfo = GameInfo(
                gameDateTime = request.gameDateTime,
                homeTeam = Team.valueOf(request.homeTeam),
                awayTeam = Team.valueOf(request.awayTeam),
                stadium = Stadium.valueOf(request.stadium)
            ),
            meetingPlace = MeetingPlace(request.meetingPlace),
            maxParticipants = request.maxParticipants,
            description = request.description
        )

        gathering.validateCreation(host)

        val saved = gatheringPort.save(gathering)
        return GatheringResponse.from(saved, host)
    }

    @Transactional(readOnly = true)
    fun getGatherings(team: String?, pageable: Pageable): Page<GatheringResponse> {
        // 팀 필터링
        val gatherings = if (team != null) {
            val teamEnum = Team.valueOf(team.uppercase())
            gatheringPort.findByTeam(teamEnum, pageable)
        } else {
            gatheringPort.findAll(pageable)
        }

        return gatherings.map { gathering ->
            val host = userPort.findById(gathering.hostId)
                ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

            // 확정된 참여자 목록 조회
            val participants = gatheringParticipantPort.findAllByGatheringId(gathering.id)
                .mapNotNull { participant ->
                    userPort.findById(participant.userId)?.let { user ->
                        com.jikgwan.application.gathering.dto.GatheringParticipantInfo.from(user)
                    }
                }

            GatheringResponse.from(gathering, host, participants)
        }
    }

    @Transactional(readOnly = true)
    fun getGathering(id: Long): GatheringResponse {
        val gathering = gatheringPort.findById(GatheringId(id))
            ?: throw BusinessException(ErrorCode.GATHERING_NOT_FOUND)

        val host = userPort.findById(gathering.hostId)
            ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        // 확정된 참여자 목록 조회
        val participants = gatheringParticipantPort.findAllByGatheringId(GatheringId(id))
            .mapNotNull { participant ->
                userPort.findById(participant.userId)?.let { user ->
                    com.jikgwan.application.gathering.dto.GatheringParticipantInfo.from(user)
                }
            }

        return GatheringResponse.from(gathering, host, participants)
    }

    fun confirmParticipant(
        gatheringId: Long,
        participantUserId: Long,
        hostUserId: Long
    ) {
        // 1. 모임 조회
        val gathering = gatheringPort.findById(GatheringId(gatheringId))
            ?: throw BusinessException(ErrorCode.GATHERING_NOT_FOUND)

        // 2. 참여자 조회
        val participant = userPort.findById(UserId(participantUserId))
            ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        // 3. 중복 확인
        require(!gatheringParticipantPort.existsByGatheringIdAndUserId(
            GatheringId(gatheringId),
            UserId(participantUserId)
        )) {
            "이미 확정된 참가자입니다"
        }

        // 4. 현재 참가자 수 확인
        val currentCount = gatheringParticipantPort.countByGatheringId(GatheringId(gatheringId))

        // 5. 도메인 검증 로직 실행
        gathering.validateParticipantAddition(
            requesterId = UserId(hostUserId),
            applicantId = UserId(participantUserId),
            currentParticipantCount = currentCount
        )

        // 6. 참가자 추가
        val gatheringParticipant = GatheringParticipant(
            id = GatheringParticipantId(0),
            gatheringId = GatheringId(gatheringId),
            userId = UserId(participantUserId),
            status = ParticipantStatus.CONFIRMED
        )
        gatheringParticipantPort.save(gatheringParticipant)
    }

    @Transactional(readOnly = true)
    fun getParticipants(gatheringId: Long): List<ParticipantResponse> {
        val participants = gatheringParticipantPort.findAllByGatheringId(GatheringId(gatheringId))

        return participants.map { participant ->
            val user = userPort.findById(participant.userId)
                ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)
            ParticipantResponse.from(participant, user)
        }
    }

    fun cancelParticipant(
        gatheringId: Long,
        participantUserId: Long,
        hostUserId: Long
    ) {
        // 1. 모임 조회
        val gathering = gatheringPort.findById(GatheringId(gatheringId))
            ?: throw BusinessException(ErrorCode.GATHERING_NOT_FOUND)

        // 2. 참여자 존재 확인
        require(gatheringParticipantPort.existsByGatheringIdAndUserId(
            GatheringId(gatheringId),
            UserId(participantUserId)
        )) {
            "확정된 참가자가 아닙니다"
        }

        // 3. 도메인 검증 로직 실행
        gathering.validateParticipantCancellation(
            requesterId = UserId(hostUserId),
            participantId = UserId(participantUserId)
        )

        // 4. 참가자 삭제
        gatheringParticipantPort.deleteByGatheringIdAndUserId(
            GatheringId(gatheringId),
            UserId(participantUserId)
        )
    }

    @Transactional(readOnly = true)
    fun getMyParticipatingGatherings(userId: Long): List<GatheringResponse> {
        val currentUserId = UserId(userId)

        // 1. 사용자가 호스트인 모임 조회
        val hostedGatherings = gatheringPort.findByHostId(currentUserId)

        // 2. 사용자가 참여 확정된 모임 조회
        val participatingGatherings = gatheringParticipantPort.findAllByUserId(currentUserId)
            .mapNotNull { participant ->
                gatheringPort.findById(participant.gatheringId)
            }

        // 3. 두 목록 합치기 (중복 제거)
        val allGatherings = (hostedGatherings + participatingGatherings)
            .distinctBy { it.id.value }

        // 4. 응답 생성
        return allGatherings.map { gathering ->
            val host = userPort.findById(gathering.hostId)
                ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

            // 확정된 참여자 목록 조회
            val allParticipants = gatheringParticipantPort.findAllByGatheringId(gathering.id)
                .mapNotNull { p ->
                    userPort.findById(p.userId)?.let { user ->
                        com.jikgwan.application.gathering.dto.GatheringParticipantInfo.from(user)
                    }
                }

            GatheringResponse.from(gathering, host, allParticipants)
        }
    }
}
