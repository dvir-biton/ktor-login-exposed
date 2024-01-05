package com.fylora.core.handlers

import kotlinx.serialization.Serializable

@Serializable
data class ErrorResponse(
    val error: String
)
