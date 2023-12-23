package com.fylora.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class UserData(
    val fullName: String,
    val amountOfMoney: Long,
    val id: ID? = null,
)
