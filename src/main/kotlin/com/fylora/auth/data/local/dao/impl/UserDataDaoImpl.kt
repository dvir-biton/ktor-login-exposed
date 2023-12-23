package com.fylora.auth.data.local.dao.impl

import com.fylora.auth.data.local.dao.UserDataDao
import com.fylora.auth.data.local.database.DatabaseFactory
import com.fylora.auth.data.model.*
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

class UserDataDaoImpl: UserDataDao {
    override suspend fun getUserDataById(id: ID): UserData? = DatabaseFactory.dbQuery {
        UserDataTable
            .select { UserDataTable.id eq id }
            .map(::rowToUserData)
            .singleOrNull()
    }

    private fun rowToUserData(row: ResultRow) = UserData(
        fullName = row[UserDataTable.fullName],
        amountOfMoney = row[UserDataTable.amountOfMoney],
        id = row[UserDataTable.id]
    )
}