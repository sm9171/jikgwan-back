package com.jikgwan.adapter.out.persistence.entity

import com.jikgwan.domain.gathering.*
import com.jikgwan.domain.user.Team
import com.jikgwan.domain.user.UserId
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "gatherings",
    indexes = [
        Index(name = "idx_game_date_status", columnList = "gameDateTime,status"),
        Index(name = "idx_home_team", columnList = "homeTeam"),
        Index(name = "idx_away_team", columnList = "awayTeam")
    ]
)
class GatheringEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val hostId: Long,

    @Embedded
    val gameInfo: GameInfoEmbeddable,

    @Column(nullable = false)
    val meetingPlace: String,

    @Column(nullable = false)
    val maxParticipants: Int,

    @Column(nullable = false, columnDefinition = "TEXT")
    val description: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val status: GatheringStatus = GatheringStatus.ACTIVE,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): Gathering = Gathering(
        id = GatheringId(id),
        hostId = UserId(hostId),
        gameInfo = gameInfo.toDomain(),
        meetingPlace = MeetingPlace(meetingPlace),
        maxParticipants = maxParticipants,
        description = description,
        status = status,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun from(gathering: Gathering): GatheringEntity = GatheringEntity(
            id = gathering.id.value,
            hostId = gathering.hostId.value,
            gameInfo = GameInfoEmbeddable.from(gathering.gameInfo),
            meetingPlace = gathering.meetingPlace.value,
            maxParticipants = gathering.maxParticipants,
            description = gathering.description,
            status = gathering.status,
            createdAt = gathering.createdAt,
            updatedAt = gathering.updatedAt
        )
    }
}

@Embeddable
class GameInfoEmbeddable(
    @Column(nullable = false)
    val gameDateTime: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val homeTeam: Team,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val awayTeam: Team,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    val stadium: Stadium
) {
    fun toDomain(): GameInfo = GameInfo(
        gameDateTime = gameDateTime,
        homeTeam = homeTeam,
        awayTeam = awayTeam,
        stadium = stadium
    )

    companion object {
        fun from(gameInfo: GameInfo): GameInfoEmbeddable = GameInfoEmbeddable(
            gameDateTime = gameInfo.gameDateTime,
            homeTeam = gameInfo.homeTeam,
            awayTeam = gameInfo.awayTeam,
            stadium = gameInfo.stadium
        )
    }
}
