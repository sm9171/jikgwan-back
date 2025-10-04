package com.jikgwan.adapter.`in`.web

import com.jikgwan.adapter.`in`.web.common.ApiResponse
import com.jikgwan.application.gathering.GatheringApplicationService
import com.jikgwan.application.gathering.dto.CreateGatheringRequest
import com.jikgwan.application.gathering.dto.GatheringResponse
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
    @Operation(summary = "모임 목록 조회", description = "모임 목록을 조회합니다")
    fun getGatherings(
        pageable: Pageable
    ): ResponseEntity<ApiResponse<Page<GatheringResponse>>> {
        val gatherings = gatheringApplicationService.getGatherings(pageable)
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
}
