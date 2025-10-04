package com.jikgwan.application.gathering.dto

import jakarta.validation.constraints.*
import java.time.LocalDateTime

data class CreateGatheringRequest(
    @field:Future(message = "과거 경기에 대한 모임은 생성할 수 없습니다")
    val gameDateTime: LocalDateTime,

    val homeTeam: String,
    val awayTeam: String,
    val stadium: String,

    @field:NotBlank(message = "만날 장소를 입력해주세요")
    val meetingPlace: String,

    @field:Min(value = 1, message = "최소 1명 이상이어야 합니다")
    @field:Max(value = 10, message = "최대 10명까지 가능합니다")
    val maxParticipants: Int,

    @field:NotBlank(message = "소개글을 입력해주세요")
    @field:Size(max = 500, message = "소개글은 500자 이하여야 합니다")
    val description: String
)
