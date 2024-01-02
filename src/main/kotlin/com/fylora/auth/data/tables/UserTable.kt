package com.fylora.auth.data.tables

import org.jetbrains.exposed.sql.Table

object UserTable: Table() {
    val username = varchar("username", length = 24)
    val password = varchar("password", length = 128)
    val salt = varchar("salt", length = 64)
    val role = varchar("role", length = 16)

    val id = varchar("id", length = 36)
    override val primaryKey = PrimaryKey(id)
}
