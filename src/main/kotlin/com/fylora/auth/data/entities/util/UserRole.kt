package com.fylora.auth.data.entities.util

sealed class UserRole(val type: String) {
    data object Admin: UserRole("admin")
    data object User: UserRole("user")

    companion object {
        fun fromType(type: String): UserRole =
            when (type) {
                Admin.type -> Admin
                User.type -> User
                else -> User
            }
    }
}