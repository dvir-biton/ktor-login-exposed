package com.fylora.auth.requests.admin

import com.fylora.auth.requests.admin.util.AdminAction
import kotlinx.serialization.Serializable

@Serializable
data class AdminRequest(
    val action: AdminAction
)
