package com.fylora.auth.data.local.dao

import com.fylora.auth.data.model.ID
import com.fylora.auth.data.model.UserData

interface UserDataDao {
    suspend fun getUserDataById(id: ID): UserData?
    suspend fun getUserDataByUsername(username: String): UserData?
}