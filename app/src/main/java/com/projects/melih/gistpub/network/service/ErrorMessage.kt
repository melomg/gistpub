package com.projects.melih.gistpub.network.service

/**
 * Created by melih on 11.11.2017.
 */
data class ErrorMessage(var errorCode: ErrorCode = ErrorCode.GENERAL_ERROR)

enum class ErrorCode {
    GENERAL_ERROR,
    NO_NETWORK
}
