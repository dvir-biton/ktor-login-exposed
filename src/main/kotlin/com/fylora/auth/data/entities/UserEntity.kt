package com.fylora.auth.data.entities

import kotlinx.serialization.Serializable
import java.util.UUID

typealias ID = String

@Serializable
data class UserEntity(
    val username: String,
    val password: String,
    val salt: String,
    val role: String,

    val id: ID = UUID.randomUUID().toString()
)
