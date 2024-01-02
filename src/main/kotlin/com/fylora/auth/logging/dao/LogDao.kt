package com.fylora.auth.logging.dao

import com.fylora.auth.logging.entity.LogEntry

interface LogDao {
    suspend fun addLogEntry(logEntry: LogEntry): Boolean
    suspend fun getLastLogEntries(count: Int): List<LogEntry>
}
