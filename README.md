# 직관 (Jikgwan) - 야구 팬 매칭 플랫폼

같은 야구팀을 응원하는 팬들이 특정 경기를 함께 관람할 동료를 찾을 수 있는 웹 기반 매칭 플랫폼입니다.

> "직관"은 "직접 관람"의 줄임말로, 야구 팬들이 경기장에 직접 가서 응원하는 문화를 의미합니다.

## 📋 프로젝트 구조

이 프로젝트는 **Hexagonal Architecture**와 **DDD(Domain-Driven Design)** 패턴을 적용한 Kotlin Spring Boot 프로젝트입니다.

```
src/main/kotlin/com/jikgwan/
├── domain/                          # 도메인 계층 (비즈니스 로직)
│   ├── user/
│   │   ├── UserId.kt               # Value Object (Inline)
│   │   ├── Email.kt                # Value Object
│   │   ├── Password.kt             # Value Object
│   │   ├── Nickname.kt             # Value Object
│   │   ├── Team.kt                 # Enum (10개 구단)
│   │   ├── Profile.kt              # Value Object
│   │   └── User.kt                 # Entity
│   ├── gathering/
│   │   ├── GatheringId.kt          # Value Object (Inline)
│   │   ├── Stadium.kt              # Enum
│   │   ├── GameInfo.kt             # Value Object
│   │   ├── MeetingPlace.kt         # Value Object
│   │   ├── GatheringStatus.kt      # Enum
│   │   └── Gathering.kt            # Entity
│   └── chat/
│       ├── ChatRoomId.kt           # Value Object (Inline)
│       ├── MessageId.kt            # Value Object (Inline)
│       ├── ChatRoomStatus.kt       # Enum
│       ├── MessageStatus.kt        # Enum
│       ├── ChatRoom.kt             # Entity
│       └── Message.kt              # Entity
│
├── application/                     # 애플리케이션 계층 (유스케이스)
│   ├── user/
│   │   ├── UserApplicationService.kt
│   │   ├── port/out/UserPort.kt
│   │   └── dto/
│   │       ├── SignUpRequest.kt
│   │       ├── LoginRequest.kt
│   │       ├── TokenResponse.kt
│   │       └── UserResponse.kt
│   └── gathering/
│       ├── GatheringApplicationService.kt
│       ├── port/out/GatheringPort.kt
│       └── dto/
│           ├── CreateGatheringRequest.kt
│           └── GatheringResponse.kt
│
├── adapter/                         # 어댑터 계층
│   ├── in/web/                     # REST API Controllers
│   │   ├── AuthController.kt       # 인증 API
│   │   ├── GatheringController.kt  # 모임 API
│   │   └── common/ApiResponse.kt
│   └── out/persistence/            # JPA Repositories
│       ├── entity/
│       │   ├── UserEntity.kt
│       │   └── GatheringEntity.kt
│       ├── UserJpaRepository.kt
│       ├── GatheringJpaRepository.kt
│       ├── UserAdapter.kt
│       └── GatheringAdapter.kt
│
└── common/                          # 공통 인프라
    ├── config/
    │   ├── WebSocketConfig.kt      # WebSocket 설정
    │   ├── RedisConfig.kt          # Redis 캐시 설정
    │   └── SwaggerConfig.kt        # API 문서 설정
    ├── security/
    │   ├── JwtTokenProvider.kt     # JWT 토큰 생성/검증
    │   ├── JwtAuthenticationFilter.kt
    │   └── SecurityConfig.kt       # Spring Security 설정
    └── exception/
        ├── ErrorCode.kt            # 에러 코드 정의
        ├── JikgwanException.kt     # 커스텀 예외
        └── GlobalExceptionHandler.kt
```

## 🛠️ 기술 스택

- **Language**: Kotlin 1.9.20
- **Framework**: Spring Boot 3.2.0
- **Database**: MySQL, H2 (테스트)
- **Cache**: Redis
- **Security**: Spring Security, JWT
- **Real-time**: WebSocket (STOMP)
- **Documentation**: Swagger/OpenAPI
- **Build Tool**: Gradle (Kotlin DSL)
- **Query**: QueryDSL

## 🎯 구현된 핵심 기능

### 1. 도메인 모델 (DDD)
- ✅ **User 도메인**: 회원가입, 로그인, 프로필 관리
  - Value Objects: Email, Password, Nickname, Profile
  - Inline Value Class로 UserId 타입 안전성 확보
- ✅ **Gathering 도메인**: 모임 생성, 조회, 수정, 삭제
  - Value Objects: GameInfo, MeetingPlace
  - 과거 경기 검증, 팀 매칭 로직
- ✅ **ChatRoom 도메인**: 채팅방 구조 (구현 대기)
  - 1:1 채팅방 관리
  - 참여자 권한 검증 로직

### 2. 아키텍처 패턴
- ✅ **Hexagonal Architecture**: 포트 & 어댑터 패턴
  - 도메인 로직과 외부 의존성 분리
  - UserPort, GatheringPort 인터페이스
- ✅ **DDD**: Entity, Value Object, Domain Service 분리
- ✅ **Clean Architecture**: 의존성 역전 원칙

### 3. Kotlin 특화
- ✅ **Inline Value Class**: UserId, GatheringId 등 (타입 안전성 + 성능)
- ✅ **Data Class**: 불변 도메인 모델
- ✅ **Null Safety**: ?, ?:, let 활용
- ✅ **Extension Functions**: 도메인 로직 확장
- ✅ **Scope Functions**: let, apply, run 활용

### 4. 보안
- ✅ **JWT 인증**: Access Token (1시간), Refresh Token (7일)
- ✅ **BCrypt**: 비밀번호 암호화 (강도 12)
- ✅ **Spring Security**: 인증/인가 필터
- ✅ **Stateless 세션**: JWT 기반 무상태 인증

### 5. API 문서화
- ✅ **Swagger/OpenAPI**: 자동 API 문서 생성
- ✅ **JWT Bearer 인증**: Swagger에서 테스트 가능

## 🚀 시작하기

### 요구사항

- JDK 17 이상
- MySQL 8.0 이상
- Redis 6.0 이상

### 실행 방법

#### 1. 데이터베이스 설정
```sql
CREATE DATABASE jikgwan CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER 'jikgwan_user'@'localhost' IDENTIFIED BY 'jikgwan_pass';
GRANT ALL PRIVILEGES ON jikgwan.* TO 'jikgwan_user'@'localhost';
```

#### 2. 환경 변수 설정
```bash
export DB_USERNAME=jikgwan_user
export DB_PASSWORD=jikgwan_pass
export JWT_SECRET=your-secret-key-here-must-be-long-enough
export REDIS_HOST=localhost
export REDIS_PORT=6379
```

#### 3. 빌드 및 실행
```bash
./gradlew build
./gradlew bootRun
```

#### 4. API 문서 확인
- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI Spec: http://localhost:8080/v3/api-docs

## 📝 주요 API 엔드포인트

### 인증 (Auth)
- `POST /api/auth/signup` - 회원가입
- `POST /api/auth/login` - 로그인 (JWT 토큰 발급)

### 모임 (Gathering)
- `POST /api/gatherings` - 모임 생성 (인증 필요)
- `GET /api/gatherings` - 모임 목록 조회
- `GET /api/gatherings/{id}` - 모임 상세 조회
- `PUT /api/gatherings/{id}` - 모임 수정 (인증 필요)
- `DELETE /api/gatherings/{id}` - 모임 삭제 (인증 필요)

## 🏗️ 아키텍처 특징

### Hexagonal Architecture (포트 & 어댑터)
- **Domain Layer**: 순수한 비즈니스 로직, 외부 의존성 없음
- **Application Layer**: 유스케이스 구현, 트랜잭션 관리
- **Adapter Layer**: 외부 세계와의 연결 (REST API, JPA 등)

### DDD 패턴
- **Entity**: User, Gathering, ChatRoom, Message
- **Value Object**: Email, Password, Nickname, GameInfo, Profile 등
  - 불변성, 자체 검증 로직 포함
- **Inline Value Class**: UserId, GatheringId (타입 안전성 + 런타임 오버헤드 없음)

### Kotlin 특화
```kotlin
// Inline Value Class - 타입 안전성 + 성능
@JvmInline
value class UserId(val value: Long)

// Data Class - 불변 도메인 모델
data class User(
    val id: UserId,
    val email: Email,
    val password: Password
) {
    // 비즈니스 로직은 도메인에
    fun canCreateGathering(): Boolean =
        profile?.isComplete() ?: false

    // 불변성 유지 (copy 활용)
    fun updateProfile(newProfile: Profile): User =
        copy(profile = newProfile, updatedAt = LocalDateTime.now())
}

// Extension Function
fun LocalDateTime.isPast(): Boolean =
    this.isBefore(LocalDateTime.now())

// Scope Function
user.profile?.let { profile ->
    updateProfileImage(profile.profileImage)
}
```

## 🔐 보안

- JWT 기반 인증/인가
- BCrypt 비밀번호 암호화
- Stateless 세션 관리
- CORS 설정
- 인증 필터 체인

## 🎨 코드 품질

### Kotlin 관용구 적용
- Data Class로 불변 모델
- Inline Value Class로 타입 안전성
- Extension Functions로 기능 확장
- Scope Functions (let, apply, run) 활용
- Null Safety 철저히 적용

### 도메인 주도 설계
- 비즈니스 로직을 도메인 계층에 집중
- Value Object로 유효성 검증
- 불변성 유지 (copy 활용)
- 의존성 역전 (Port & Adapter)

## 📋 다음 단계

### Phase 2 (예정)
- [ ] 채팅 기능 구현
  - ChatApplicationService
  - ChatController (WebSocket)
  - 실시간 메시지 전송
  - 메시지 저장/조회
- [ ] 프로필 관리
  - 프로필 업데이트 API
  - 이미지 업로드 (S3 또는 로컬 스토리지)

### Phase 3 (예정)
- [ ] 모임 필터링 (팀별, 날짜별)
- [ ] 페이징 최적화
- [ ] Redis 캐싱 적용
- [ ] N+1 문제 해결 (QueryDSL)

### Phase 4 (예정)
- [ ] 단위 테스트 (Kotest)
- [ ] 통합 테스트
- [ ] API 테스트 (MockMvc)
- [ ] Docker 컨테이너화
- [ ] CI/CD 파이프라인

## 📄 License

이 프로젝트는 MIT 라이선스를 따릅니다.

---

**직관에서 야구 팬들을 연결하세요! ⚾️🎯**
