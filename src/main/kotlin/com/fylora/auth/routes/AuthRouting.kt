package com.fylora.auth.routes

import com.fylora.auth.data.local.dao.CombinedUserDao
import com.fylora.auth.data.local.dao.UserDao
import com.fylora.auth.security.hashing.HashingService
import com.fylora.auth.security.token.TokenConfig
import com.fylora.auth.security.token.TokenService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureAuthRouting(
    hashingService: HashingService,
    userDao: UserDao,
    combinedUserDao: CombinedUserDao,
    tokenService: TokenService,
    tokenConfig: TokenConfig
) {
    routing {
        login(hashingService, userDao, tokenService, tokenConfig)
        signUp(hashingService, userDao, combinedUserDao)
        authenticate()
        getUserInfo()
    }
}

