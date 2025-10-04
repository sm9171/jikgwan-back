package com.jikgwan.adapter.`in`.web.common

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ErrorResponse? = null
) {
    companion object {
        fun <T> success(data: T): ApiResponse<T> = ApiResponse(
            success = true,
            data = data
        )

        fun <T> error(error: ErrorResponse): ApiResponse<T> = ApiResponse(
            success = false,
            error = error
        )
    }
}

data class ErrorResponse(
    val errorCode: String,
    val message: String,
    val fieldErrors: Map<String, String>? = null
)
