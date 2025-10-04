package com.jikgwan.domain.gathering

import com.jikgwan.domain.user.Team
import java.time.LocalDateTime

data class GameInfo(
    val gameDateTime: LocalDateTime,
    val homeTeam: Team,
    val awayTeam: Team,
    val stadium: Stadium
) {
    init {
        require(homeTeam != awayTeam) {
            "같은 팀끼리 경기할 수 없습니다"
        }
    }

    fun isPast(): Boolean =
        gameDateTime.isBefore(LocalDateTime.now())

    fun hasTeam(team: Team): Boolean =
        homeTeam == team || awayTeam == team
}
