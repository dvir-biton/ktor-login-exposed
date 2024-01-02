package com.fylora.auth.requests.admin.util

import com.fylora.auth.data.entities.ID
import com.fylora.auth.data.entities.UserDataEntity
import com.fylora.auth.data.entities.UserEntity
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface AdminAction {
    @Serializable
    @SerialName("create_user")
    data class CreateUser(val user: UserEntity, val userDataEntity: UserDataEntity): AdminAction
    @Serializable
    @SerialName("update_user")
    data class UpdateUser(val user: UserEntity, val userDataEntity: UserDataEntity): AdminAction
    @Serializable
    @SerialName("view_logs")
    data class ViewLogs(val count: Int): AdminAction
    @Serializable
    @SerialName("get_user_with_id")
    data class GetUserById(val id: ID): AdminAction
    @Serializable
    @SerialName("get_user_with_username")
    data class GetUserByUsername(val username: String): AdminAction
    @Serializable
    @SerialName("run_query")
    data class RunSqlQuery(val query: String): AdminAction
}