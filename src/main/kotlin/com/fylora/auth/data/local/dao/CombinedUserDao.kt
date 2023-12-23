package com.fylora.auth.data.local.dao

import com.fylora.auth.data.model.ID
import com.fylora.auth.data.model.User
import com.fylora.auth.data.model.UserData

interface CombinedUserDao {
    fun insertUser(user: User, userData: UserData)
    fun getUserWithDetailsById(id: ID): Pair<User, UserData>?
    fun getUserWithDetailsByUsername(username: String): Pair<User, UserData>?
}