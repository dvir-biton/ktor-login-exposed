package com.fylora.core.logging.dao

import com.fylora.core.logging.entity.LogEntry

interface LogDao {
    suspend fun addLogEntry(logEntry: LogEntry): Boolean
    suspend fun getLastLogEntries(count: Int): List<LogEntry>
}
