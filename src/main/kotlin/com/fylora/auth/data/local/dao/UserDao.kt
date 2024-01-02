package com.fylora.auth.data.local.dao

import com.fylora.auth.data.entities.ID
import com.fylora.auth.data.entities.UserEntity

interface UserDao {
    suspend fun getUserById(id: ID): UserEntity?
    suspend fun getUserByUsername(username: String): UserEntity?
}