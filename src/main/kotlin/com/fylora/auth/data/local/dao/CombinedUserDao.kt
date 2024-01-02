package com.fylora.auth.data.local.dao

import com.fylora.auth.data.entities.ID
import com.fylora.auth.data.entities.UserEntity
import com.fylora.auth.data.entities.UserDataEntity

interface CombinedUserDao {
    suspend fun insertUser(userEntity: UserEntity, userDataEntity: UserDataEntity): Boolean
    suspend fun updateUser(userEntity: UserEntity, userDataEntity: UserDataEntity): Boolean
    suspend fun getUserWithDetailsById(id: ID): Pair<UserEntity, UserDataEntity>?
    suspend fun getUserWithDetailsByUsername(username: String): Pair<UserEntity, UserDataEntity>?
    suspend fun runCustomQuery(query: String): String
}