package com.jikgwan.adapter.`in`.web

import com.jikgwan.adapter.`in`.web.common.ApiResponse
import com.jikgwan.application.gathering.GatheringApplicationService
import com.jikgwan.application.gathering.dto.ConfirmParticipantRequest
import com.jikgwan.application.gathering.dto.CreateGatheringRequest
import com.jikgwan.application.gathering.dto.GatheringResponse
import com.jikgwan.application.gathering.dto.ParticipantResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/gatherings")
@Tag(name = "Gathering", description = "모임 API")
class GatheringController(
    private val gatheringApplicationService: GatheringApplicationService
) {

    @PostMapping
    @Operation(summary = "모임 생성", description = "새로운 모임을 생성합니다")
    fun createGathering(
        @AuthenticationPrincipal userId: String,
        @Valid @RequestBody request: CreateGatheringRequest
    ): ResponseEntity<ApiResponse<GatheringResponse>> {
        val gathering = gatheringApplicationService.createGathering(
            userId = userId.toLong(),
            request = request
        )
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(gathering))
    }

    @GetMapping
    @Operation(summary = "모임 목록 조회", description = "모임 목록을 조회합니다. 팀 필터를 사용할 수 있습니다.")
    fun getGatherings(
        @RequestParam(required = false) team: String?,
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<GatheringResponse>>> {
        val gatherings = gatheringApplicationService.getGatherings(team, pageable)
        return ResponseEntity.ok(ApiResponse.success(gatherings))
    }

    @GetMapping("/{id}")
    @Operation(summary = "모임 상세 조회", description = "모임 상세 정보를 조회합니다")
    fun getGathering(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<GatheringResponse>> {
        val gathering = gatheringApplicationService.getGathering(id)
        return ResponseEntity.ok(ApiResponse.success(gathering))
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "모임 참가자 확정", description = "호스트가 특정 사용자를 모임 참가자로 확정합니다")
    fun confirmParticipant(
        @AuthenticationPrincipal hostUserId: String,
        @PathVariable id: Long,
        @Valid @RequestBody request: ConfirmParticipantRequest
    ): ResponseEntity<ApiResponse<Unit>> {
        gatheringApplicationService.confirmParticipant(
            gatheringId = id,
            participantUserId = request.participantUserId,
            hostUserId = hostUserId.toLong()
        )
        return ResponseEntity.ok(ApiResponse.success(Unit))
    }

    @GetMapping("/{id}/participants")
    @Operation(summary = "모임 참가자 목록", description = "확정된 참가자 목록을 조회합니다")
    fun getParticipants(
        @PathVariable id: Long
    ): ResponseEntity<ApiResponse<List<ParticipantResponse>>> {
        val participants = gatheringApplicationService.getParticipants(id)
        return ResponseEntity.ok(ApiResponse.success(participants))
    }

    @DeleteMapping("/{id}/participants/{participantUserId}")
    @Operation(summary = "모임 참가자 취소", description = "호스트가 확정된 참가자를 취소합니다")
    fun cancelParticipant(
        @AuthenticationPrincipal hostUserId: String,
        @PathVariable id: Long,
        @PathVariable participantUserId: Long
    ): ResponseEntity<ApiResponse<Unit>> {
        gatheringApplicationService.cancelParticipant(
            gatheringId = id,
            participantUserId = participantUserId,
            hostUserId = hostUserId.toLong()
        )
        return ResponseEntity.ok(ApiResponse.success(Unit))
    }

    @GetMapping("/my-participating")
    @Operation(summary = "내가 참여 중인 모임 목록", description = "현재 사용자가 참여 확정된 모임 목록을 조회합니다")
    fun getMyParticipatingGatherings(
        @AuthenticationPrincipal userId: String
    ): ResponseEntity<ApiResponse<List<GatheringResponse>>> {
        val gatherings = gatheringApplicationService.getMyParticipatingGatherings(userId.toLong())
        return ResponseEntity.ok(ApiResponse.success(gatherings))
    }
}
