package com.fylora.auth.data.model

import org.jetbrains.exposed.sql.Table

object UserDataTable: Table() {
    val fullName = varchar("full_name", length = 24)
    val amountOfMoney = long("amount_of_money")

    val id = reference("id", UserTable.id)
}