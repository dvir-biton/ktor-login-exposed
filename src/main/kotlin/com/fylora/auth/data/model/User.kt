package com.fylora.auth.data.model

import kotlinx.serialization.Serializable
import java.util.UUID

typealias ID = String

@Serializable
data class User(
    val username: String,
    val password: String,
    val salt: String,

    val id: ID = UUID.randomUUID().toString()
)
