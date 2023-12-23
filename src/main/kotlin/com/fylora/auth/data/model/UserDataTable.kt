package com.fylora.auth.data.model

import org.jetbrains.exposed.sql.Table

object UserDataTable: Table() {
    val fullName = varchar("full_name", length = 16)
    val amountOfMoney = long("amount_of_money")

    val id = UserTable.varchar("id", length = 36)
    override val primaryKey = PrimaryKey(id)
}