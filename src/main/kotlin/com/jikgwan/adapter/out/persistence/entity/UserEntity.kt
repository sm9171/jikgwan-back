package com.jikgwan.adapter.out.persistence.entity

import com.jikgwan.domain.user.*
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "users")
class UserEntity(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val email: String,

    @Column(nullable = false)
    val password: String,

    @Column(nullable = false, length = 50)
    val nickname: String,

    @Embedded
    val profile: ProfileEmbeddable? = null,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(nullable = false)
    val updatedAt: LocalDateTime = LocalDateTime.now()
) {
    fun toDomain(): User = User(
        id = UserId(id),
        email = Email(email),
        password = Password(password),
        nickname = Nickname(nickname),
        profile = profile?.toDomain(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    companion object {
        fun from(user: User): UserEntity = UserEntity(
            id = user.id.value,
            email = user.email.value,
            password = user.password.value,
            nickname = user.nickname.value,
            profile = user.profile?.let { ProfileEmbeddable.from(it) },
            createdAt = user.createdAt,
            updatedAt = user.updatedAt
        )
    }
}

@Embeddable
class ProfileEmbeddable(
    val profileImageUrl: String? = null,

    @Enumerated(EnumType.STRING)
    val gender: Gender,

    @Enumerated(EnumType.STRING)
    val ageRange: AgeRange,

    @ElementCollection
    @CollectionTable(
        name = "user_teams",
        joinColumns = [JoinColumn(name = "user_id")]
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "team")
    val supportingTeams: Set<Team> = emptySet()
) {
    fun toDomain(): Profile = Profile(
        profileImage = profileImageUrl?.let { ProfileImage(it) },
        gender = gender,
        ageRange = ageRange,
        supportingTeams = supportingTeams
    )

    companion object {
        fun from(profile: Profile): ProfileEmbeddable = ProfileEmbeddable(
            profileImageUrl = profile.profileImage?.value,
            gender = profile.gender,
            ageRange = profile.ageRange,
            supportingTeams = profile.supportingTeams
        )
    }
}
