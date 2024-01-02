package com.fylora.auth.data.local.dao

import com.fylora.auth.data.entities.ID
import com.fylora.auth.data.entities.UserDataEntity

interface UserDataDao {
    suspend fun getUserDataById(id: ID): UserDataEntity?
}