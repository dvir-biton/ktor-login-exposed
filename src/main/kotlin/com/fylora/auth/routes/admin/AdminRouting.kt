package com.fylora.auth.routes.admin

import com.fylora.auth.data.local.dao.CombinedUserDao
import com.fylora.auth.data.local.dao.UserDao
import com.fylora.auth.logging.dao.LogDao
import com.fylora.auth.security.hashing.HashingService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureAdminRouting(
    hashingService: HashingService,
    userDao: UserDao,
    combinedUserDao: CombinedUserDao,
    logDao: LogDao
) {
    routing {
        adminSignup(hashingService, userDao, combinedUserDao)
        adminPanel(userDao, combinedUserDao, logDao)
    }
}