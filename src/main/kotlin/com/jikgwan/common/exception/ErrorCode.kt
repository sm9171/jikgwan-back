package com.jikgwan.common.exception

enum class ErrorCode(
    val code: String,
    val message: String
) {
    // User
    USER_NOT_FOUND("U001", "사용자를 찾을 수 없습니다"),
    DUPLICATE_EMAIL("U002", "이미 사용 중인 이메일입니다"),
    PROFILE_NOT_COMPLETE("U003", "프로필을 완성해주세요"),
    INVALID_PASSWORD("U004", "비밀번호가 일치하지 않습니다"),

    // Gathering
    GATHERING_NOT_FOUND("G001", "모임을 찾을 수 없습니다"),
    PAST_GAME_EXCEPTION("G002", "과거 경기에 대한 모임은 생성할 수 없습니다"),
    CANNOT_APPLY_OWN_GATHERING("G003", "본인이 만든 모임에는 참여할 수 없습니다"),

    // Chat
    CHAT_ROOM_NOT_FOUND("C001", "채팅방을 찾을 수 없습니다"),
    UNAUTHORIZED_CHAT_ACCESS("C002", "채팅방 접근 권한이 없습니다"),

    // Auth
    UNAUTHORIZED("A001", "인증이 필요합니다"),
    INVALID_TOKEN("A002", "유효하지 않은 토큰입니다"),
    EXPIRED_TOKEN("A003", "만료된 토큰입니다")
}
