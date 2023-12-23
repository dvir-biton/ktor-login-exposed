package com.fylora.auth.data.local.dao

import com.fylora.auth.data.model.ID
import com.fylora.auth.data.model.User
import com.fylora.auth.data.model.UserData

interface CombinedUserDao {
    suspend fun insertUser(user: User, userData: UserData): Boolean
    suspend fun updateUser(id: ID, user: User, userData: UserData): Boolean
    suspend fun getUserWithDetailsById(id: ID): Pair<User, UserData>?
    suspend fun getUserWithDetailsByUsername(username: String): Pair<User, UserData>?
}