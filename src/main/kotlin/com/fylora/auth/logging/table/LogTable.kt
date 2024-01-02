package com.fylora.auth.logging.table

import org.jetbrains.exposed.sql.Table

object LogTable : Table() {
    val timestamp = long("timestamp")
    val level = varchar("level", 24)
    val message = text("message")
    val userId = (varchar("user_id", 36)).nullable()

    val id = integer("id").autoIncrement()
    override val primaryKey = PrimaryKey(id)
}