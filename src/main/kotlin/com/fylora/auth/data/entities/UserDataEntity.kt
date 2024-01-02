package com.fylora.auth.data.entities

import kotlinx.serialization.Serializable

@Serializable
data class UserDataEntity(
    val fullName: String,
    val amountOfMoney: Long,

    val id: ID? = null,
)
