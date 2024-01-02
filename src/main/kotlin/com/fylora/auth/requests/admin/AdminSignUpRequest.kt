package com.fylora.auth.requests.admin

import kotlinx.serialization.Serializable

@Serializable
data class AdminSignUpRequest(
    val adminToken: String,
    val username: String,
    val password: String
)
