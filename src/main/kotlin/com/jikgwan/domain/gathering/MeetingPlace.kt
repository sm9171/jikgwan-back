package com.jikgwan.domain.gathering

@JvmInline
value class MeetingPlace(val value: String) {
    init {
        require(value.isNotBlank()) {
            "만날 장소를 입력해주세요"
        }
    }
}
