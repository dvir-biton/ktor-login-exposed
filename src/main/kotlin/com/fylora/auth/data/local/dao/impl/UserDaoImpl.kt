package com.fylora.auth.data.local.dao.impl

import com.fylora.auth.data.local.dao.UserDao
import com.fylora.auth.data.local.database.DatabaseFactory
import com.fylora.auth.data.model.ID
import com.fylora.auth.data.model.User
import com.fylora.auth.data.model.UserTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

class UserDaoImpl: UserDao {
    override suspend fun getUserById(id: ID): User? =  DatabaseFactory.dbQuery {
        UserTable
            .select { UserTable.id eq id }
            .map(::rowToUser)
            .singleOrNull()
    }

    override suspend fun getUserByUsername(username: String): User? = DatabaseFactory.dbQuery {
        UserTable
            .select { UserTable.username eq username }
            .map(::rowToUser)
            .singleOrNull()
    }

    private fun rowToUser(row: ResultRow) = User(
        username = row[UserTable.username],
        password = row[UserTable.password],
        salt = row[UserTable.salt],
        id = row[UserTable.id]
    )
}