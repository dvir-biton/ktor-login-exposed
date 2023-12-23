package com.fylora.auth.data.local.dao

import com.fylora.auth.data.model.ID
import com.fylora.auth.data.model.User

interface UserDao {
    suspend fun getUserById(id: ID): User?
    suspend fun getUserByUsername(username: String): User?
}