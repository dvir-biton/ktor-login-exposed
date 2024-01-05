package com.fylora.core.logging.entity

import kotlinx.serialization.Serializable

@Serializable
data class LogEntry(
    val timestamp: Long = System.currentTimeMillis(),
    val level: String,
    val message: String,

    val userId: String?,
    val id: Int? = null
)
