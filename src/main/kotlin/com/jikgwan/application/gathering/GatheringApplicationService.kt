package com.jikgwan.application.gathering

import com.jikgwan.application.gathering.dto.CreateGatheringRequest
import com.jikgwan.application.gathering.dto.GatheringResponse
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
    private val userPort: UserPort
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
    fun getGatherings(pageable: Pageable): Page<GatheringResponse> {
        val gatherings = gatheringPort.findAll(pageable)

        return gatherings.map { gathering ->
            val host = userPort.findById(gathering.hostId)
                ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)
            GatheringResponse.from(gathering, host)
        }
    }

    @Transactional(readOnly = true)
    fun getGathering(id: Long): GatheringResponse {
        val gathering = gatheringPort.findById(GatheringId(id))
            ?: throw BusinessException(ErrorCode.GATHERING_NOT_FOUND)

        val host = userPort.findById(gathering.hostId)
            ?: throw BusinessException(ErrorCode.USER_NOT_FOUND)

        return GatheringResponse.from(gathering, host)
    }
}
