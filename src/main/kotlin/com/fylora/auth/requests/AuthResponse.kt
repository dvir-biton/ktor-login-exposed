package com.fylora.auth.requests

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse (
    val token: String
)