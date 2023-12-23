package com.fylora.auth.data.local.dao.impl

import com.fylora.auth.data.local.dao.CombinedUserDao
import com.fylora.auth.data.local.dao.UserDao
import com.fylora.auth.data.local.dao.UserDataDao
import com.fylora.auth.data.local.database.DatabaseFactory
import com.fylora.auth.data.model.*
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class CombinedUserDaoImpl(
    private val userDao: UserDao,
    private val userDataDao: UserDataDao
): CombinedUserDao {
    override suspend fun insertUser(user: User, userData: UserData): Boolean = DatabaseFactory.dbQuery {
        try {
            transaction {
                UserTable.insert {
                    it[username] = user.username
                    it[password] = user.password
                    it[salt] = user.salt
                    it[id] = user.id
                }

                UserDataTable.insert {
                    it[fullName] = userData.fullName
                    it[amountOfMoney] = userData.amountOfMoney
                    it[id] = user.id
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun updateUser(id: ID, user: User, userData: UserData): Boolean = DatabaseFactory.dbQuery {
        try {
            transaction {
                val updatedUserRows = UserTable.update({ UserTable.id eq id }) {
                    it[username] = user.username
                    it[password] = user.password
                    it[salt] = user.salt
                }

                val updatedUserDataRows = UserDataTable.update({ UserDataTable.id eq id }) {
                    it[fullName] = userData.fullName
                    it[amountOfMoney] = userData.amountOfMoney
                }

                updatedUserRows > 0 && updatedUserDataRows > 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getUserWithDetailsById(id: ID): Pair<User, UserData>? = DatabaseFactory.dbQuery {
        val user = userDao.getUserById(id) ?: return@dbQuery null
        val userData = userDataDao.getUserDataById(id) ?: return@dbQuery null

        Pair(user, userData)
    }

    override suspend fun getUserWithDetailsByUsername(username: String): Pair<User, UserData>? = DatabaseFactory.dbQuery {
        val user = userDao.getUserByUsername(username) ?: return@dbQuery null
        val userData = userDataDao.getUserDataById(user.id) ?: return@dbQuery null

        Pair(user, userData)
    }
}