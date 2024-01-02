package com.fylora.auth.data.local.database

import com.fylora.auth.data.tables.UserDataTable
import com.fylora.auth.data.tables.UserTable
import com.fylora.auth.logging.table.LogTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val driverClassName = System.getenv("DB_DRIVER")
        val jdbcURL = System.getenv("DB_URL")
        val user = System.getenv("DB_USER")
        val password = System.getenv("DB_PASSWORD")

        val database = Database.connect(jdbcURL, driverClassName, user, password)

        transaction(database) {
            SchemaUtils.create(UserTable, UserDataTable, LogTable)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}