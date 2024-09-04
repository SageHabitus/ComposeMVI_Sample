package com.example.composemvi.data.source.remote.exception

object ApiErrorMessage {

    const val BAD_REQUEST_MESSAGE = "Bad request (400)"
    const val UNAUTHORIZED_MESSAGE = "Unauthorized access (401)"
    const val FORBIDDEN_MESSAGE = "Forbidden access (403)"
    const val NOT_FOUND_MESSAGE = "Resource not found (404)"
    const val TOO_MANY_REQUESTS_MESSAGE = "Too many requests (%d)"
    const val INTERNAL_SERVER_ERROR_MESSAGE = "Internal server error (500)"
    const val CLIENT_ERROR_MESSAGE = "Client error with code %d"
    const val SERVER_ERROR_MESSAGE = "Server error with code %d"
    const val UNKNOWN_HTTP_ERROR_MESSAGE = "Unknown HTTP error with code %d"
    const val NETWORK_ERROR_MESSAGE = "Network error"
    const val TIMEOUT_ERROR_MESSAGE = "Request timed out"
    const val JSON_PARSE_ERROR_MESSAGE = "JSON parse error"
    const val UNKNOWN_API_ERROR_MESSAGE = "Unknown API error"

    fun getDefaultErrorMessageForCode(code: Int): String = when (code) {
        400 -> BAD_REQUEST_MESSAGE
        401 -> UNAUTHORIZED_MESSAGE
        403 -> FORBIDDEN_MESSAGE
        404 -> NOT_FOUND_MESSAGE
        429 -> TOO_MANY_REQUESTS_MESSAGE.format(code)
        in 400..499 -> CLIENT_ERROR_MESSAGE.format(code)
        500 -> INTERNAL_SERVER_ERROR_MESSAGE
        in 500..599 -> SERVER_ERROR_MESSAGE.format(code)
        else -> UNKNOWN_HTTP_ERROR_MESSAGE.format(code)
    }
}
