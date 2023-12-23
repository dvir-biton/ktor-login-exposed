package com.fylora.auth

import com.fylora.auth.data.local.dao.impl.CombinedUserDaoImpl
import com.fylora.auth.data.local.dao.impl.UserDaoImpl
import com.fylora.auth.data.local.dao.impl.UserDataDaoImpl
import com.fylora.auth.data.local.database.DatabaseFactory
import com.fylora.auth.routes.configureAuthRouting
import com.fylora.auth.security.configureSecurity
import com.fylora.auth.security.hashing.SHA256HashingService
import com.fylora.auth.security.token.JwtTokenService
import com.fylora.auth.security.token.TokenConfig
import io.ktor.server.application.*

const val TOKEN_EXPIRATION_TIME = 2629746000L // 1 month

@Suppress("Unused")
fun Application.authModule() {
    val tokenService = JwtTokenService()
    val tokenConfig = TokenConfig(
        issuer = environment.config.property("jwt.issuer").getString(),
        audience = environment.config.property("jwt.audience").getString(),
        expiresIn = TOKEN_EXPIRATION_TIME, // 1 month
        secret = System.getenv("JWT_SECRET")
    )
    val hashingService = SHA256HashingService()
    val userDao = UserDaoImpl()
    val userDataDao = UserDataDaoImpl()
    val combinedUserDao = CombinedUserDaoImpl(userDao, userDataDao)

    DatabaseFactory.init()
    configureSecurity(tokenConfig)
    configureAuthRouting(hashingService, userDao, combinedUserDao, tokenService, tokenConfig)
}