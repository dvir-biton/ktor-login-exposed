package com.fylora.auth.logging.dao

import com.fylora.auth.data.local.database.DatabaseFactory
import com.fylora.auth.logging.entity.LogEntry
import com.fylora.auth.logging.table.LogTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SortOrder
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class LogDaoImpl : LogDao {
    override suspend fun addLogEntry(logEntry: LogEntry) = DatabaseFactory.dbQuery {
        try {
            transaction {
                LogTable.insert {
                    it[timestamp] = System.currentTimeMillis()
                    it[level] = logEntry.level
                    it[message] = logEntry.message
                    it[userId] = logEntry.userId
                }
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getLastLogEntries(count: Int): List<LogEntry> = DatabaseFactory.dbQuery {
        transaction {
            LogTable.selectAll()
                .orderBy(LogTable.timestamp to SortOrder.DESC)
                .limit(count)
                .map(::toLogEntry)
        }
    }

    private fun toLogEntry(row: ResultRow): LogEntry {
        return LogEntry(
            id = row[LogTable.id],
            timestamp = row[LogTable.timestamp],
            level = row[LogTable.level],
            message = row[LogTable.message],
            userId = row[LogTable.userId]
        )
    }
}