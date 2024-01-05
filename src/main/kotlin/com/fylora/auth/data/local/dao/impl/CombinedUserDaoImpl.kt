package com.fylora.auth.data.local.dao.impl

import com.fylora.auth.data.local.dao.CombinedUserDao
import com.fylora.auth.data.local.dao.UserDao
import com.fylora.auth.data.local.dao.UserDataDao
import com.fylora.auth.data.local.database.DatabaseFactory
import com.fylora.auth.data.entities.*
import com.fylora.auth.data.tables.UserDataTable
import com.fylora.auth.data.tables.UserTable
import com.fylora.core.logging.dao.LogDao
import com.fylora.core.logging.entity.LogEntry
import com.fylora.core.logging.util.LogLevel
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update

class CombinedUserDaoImpl(
    private val userDao: UserDao,
    private val userDataDao: UserDataDao,
    private val logDao: LogDao
): CombinedUserDao {
    override suspend fun insertUser(userEntity: UserEntity, userDataEntity: UserDataEntity): Boolean = DatabaseFactory.dbQuery {
        try {
            transaction {
                UserTable.insert {
                    it[username] = userEntity.username
                    it[password] = userEntity.password
                    it[salt] = userEntity.salt
                    it[role] = userEntity.role
                    it[id] = userEntity.id
                }

                UserDataTable.insert {
                    it[fullName] = userDataEntity.fullName
                    it[amountOfMoney] = userDataEntity.amountOfMoney
                    it[id] = userEntity.id
                }
            }
            logDao.addLogEntry(
                LogEntry(
                    level = LogLevel.Info.type,
                    message = "Inserted a new user to the database, data:" +
                            "\nUsername: ${userEntity.username}" +
                            "\nName: ${userDataEntity.fullName}",
                    userId = userEntity.id,
                )
            )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            logDao.addLogEntry(
                LogEntry(
                    level = LogLevel.Error.type,
                    message = "Error inserting a new user to the database, data:" +
                            "\nUsername: ${userEntity.username}" +
                            "\nName: ${userDataEntity.fullName}" +
                            "\nError message: ${e.message}",
                    userId = userEntity.id,
                )
            )
            false
        }
    }

    override suspend fun updateUser(userEntity: UserEntity, userDataEntity: UserDataEntity): Boolean = DatabaseFactory.dbQuery {
        try {
            transaction {
                val updatedUserRows = UserTable.update({ UserTable.id eq userEntity.id }) {
                    it[username] = userEntity.username
                    it[password] = userEntity.password
                    it[salt] = userEntity.salt
                }

                val updatedUserDataRows = UserDataTable.update({ UserDataTable.id eq userEntity.id }) {
                    it[fullName] = userDataEntity.fullName
                    it[amountOfMoney] = userDataEntity.amountOfMoney
                }

                LogEntry(
                    level = LogLevel.Info.type,
                    message = "Updated a user in the database, data:" +
                            "\nUsername: ${userEntity.username}" +
                            "\nName: ${userDataEntity.fullName}",
                    userId = userEntity.id,
                )
                updatedUserRows > 0 && updatedUserDataRows > 0
            }
        } catch (e: Exception) {
            e.printStackTrace()
            LogEntry(
                level = LogLevel.Error.type,
                message = "Error updating a user in the database, data:" +
                        "\nUsername: ${userEntity.username}" +
                        "\nName: ${userDataEntity.fullName}" +
                        "\nError message: ${e.message}",
                userId = userEntity.id,
            )
            false
        }
    }

    override suspend fun getUserWithDetailsById(id: ID): Pair<UserEntity, UserDataEntity>? = DatabaseFactory.dbQuery {
        val user = userDao.getUserById(id) ?: return@dbQuery null
        val userData = userDataDao.getUserDataById(id) ?: return@dbQuery null

        Pair(user, userData)
    }

    override suspend fun getUserWithDetailsByUsername(username: String): Pair<UserEntity, UserDataEntity>? = DatabaseFactory.dbQuery {
        val user = userDao.getUserByUsername(username) ?: return@dbQuery null
        val userData = userDataDao.getUserDataById(user.id) ?: return@dbQuery null

        Pair(user, userData)
    }

    override suspend fun runCustomQuery(query: String): String = DatabaseFactory.dbQuery {
        transaction {
            val resultList = mutableListOf<JsonElement>()
            exec(query) { rs ->
                val metaData = rs.metaData
                val columnCount = metaData.columnCount
                while (rs.next()) {
                    val json = buildJsonObject {
                        for (i in 1..columnCount) {
                            put(metaData.getColumnName(i), rs.getString(i))
                        }
                    }
                    resultList.add(json)
                }
            }
            Json.encodeToString(resultList)
        }
    }
}