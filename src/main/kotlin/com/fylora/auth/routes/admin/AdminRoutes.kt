package com.fylora.auth.routes.admin

import com.fylora.auth.data.entities.UserDataEntity
import com.fylora.auth.data.entities.UserEntity
import com.fylora.auth.data.entities.util.UserRole
import com.fylora.auth.data.local.dao.CombinedUserDao
import com.fylora.auth.data.local.dao.UserDao
import com.fylora.auth.logging.dao.LogDao
import com.fylora.auth.requests.admin.AdminRequest
import com.fylora.auth.requests.admin.AdminSignUpRequest
import com.fylora.auth.requests.admin.util.AdminAction
import com.fylora.auth.routes.MAX_USERNAME_LENGTH
import com.fylora.auth.security.hashing.HashingService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val ADMIN_SIGNUP_ROUTE: String = System.getenv("ADMIN_SIGNUP_ROUTE")
val ADMIN_PANEL_ROUTE: String = System.getenv("ADMIN_PANEL_ROUTE")
val ADMIN_TOKEN: String = System.getenv("ADMIN_TOKEN")

fun Route.adminSignup(
    hashingService: HashingService,
    userDao: UserDao,
    combinedUserDao: CombinedUserDao
) {
    post(ADMIN_SIGNUP_ROUTE) {
        val request = call.receiveNullable<AdminSignUpRequest>() ?: kotlin.run {
            call.respond(HttpStatusCode.BadRequest)
            return@post
        }
        if(request.adminToken != ADMIN_TOKEN) {
            call.respond(
                HttpStatusCode.Conflict,
                message = "Wrong admin token, aren't you admin? ;)"
            )
            return@post
        }
        if(request.username.length > MAX_USERNAME_LENGTH) {
            call.respond(
                HttpStatusCode.Conflict,
                message = "The username cannot be more than $MAX_USERNAME_LENGTH characters (Max database length)"
            )
            return@post
        }
        if(userDao.getUserByUsername(request.username) != null) {
            call.respond(
                HttpStatusCode.Conflict,
                message = "The username is already taken, to avoid conflicts it's recommended to choose a different username"
            )
            return@post
        }

        val saltedHash = hashingService.generateSaltedHash(request.password)
        val userEntity = UserEntity(
            username = request.username,
            password = saltedHash.hash,
            salt = saltedHash.salt,
            role = UserRole.Admin.type
        )
        val userDataEntity = UserDataEntity(
            fullName = "Admin" + request.username,
            amountOfMoney = 0
        )

        val wasAcknowledged = combinedUserDao.insertUser(userEntity, userDataEntity)
        if(!wasAcknowledged) {
            call.respond(
                HttpStatusCode.Conflict,
                message = "Unknown error occurred, Couldn't insert user"
            )
            return@post
        }

        call.respond(HttpStatusCode.OK)
    }
}

fun Route.adminPanel(
    userDao: UserDao,
    combinedUserDao: CombinedUserDao,
    logDao: LogDao
) {
    authenticate {
        post(ADMIN_PANEL_ROUTE) {
            val principal = call.principal<JWTPrincipal>()
            val userId = principal?.getClaim("userId", String::class) ?: kotlin.run {
                call.respond(
                    HttpStatusCode.Conflict,
                    message = "No userId found"
                )
                return@post
            }
            val user = userDao.getUserById(userId) ?: kotlin.run {
                call.respond(
                    HttpStatusCode.Conflict,
                    message = "No user found"
                )
                return@post
            }

            if(UserRole.fromType(user.role) != UserRole.Admin) {
                call.respond(
                    HttpStatusCode.Conflict,
                    message = "You are not an admin"
                )
                return@post
            }

            val request = call.receiveNullable<AdminRequest>() ?: kotlin.run {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }

            when(val action = request.action) {
                is AdminAction.CreateUser -> {
                    val result = combinedUserDao.insertUser(
                        userEntity = action.user,
                        userDataEntity = action.userDataEntity,
                    )

                    if(!result) {
                        call.respond(
                            HttpStatusCode.Conflict,
                            message = "Unknown error occurred, Couldn't create user"
                        )
                        return@post
                    }
                    call.respond(HttpStatusCode.OK)
                }
                is AdminAction.RunSqlQuery -> {
                    if("DROP" in action.query || "DELETE" in action.query) {
                        call.respond(
                            HttpStatusCode.Conflict,
                            message = "No permission to make such action, please use the local database connection to make this action"
                        )
                        return@post
                    }

                    val result = combinedUserDao.runCustomQuery(action.query)
                    call.respond(HttpStatusCode.OK, Json.encodeToString(result))
                }
                is AdminAction.UpdateUser -> {
                    val result = combinedUserDao.updateUser(action.user, action.userDataEntity)

                    if(!result) {
                        call.respond(
                            HttpStatusCode.Conflict,
                            message = "Unknown error occurred, Couldn't create user"
                        )
                        return@post
                    }
                    call.respond(HttpStatusCode.OK)
                }
                is AdminAction.ViewLogs -> {
                    val result = logDao.getLastLogEntries(action.count)

                    call.respond(HttpStatusCode.OK, Json.encodeToString(result))
                }
                is AdminAction.GetUserById -> {
                    val result = combinedUserDao.getUserWithDetailsById(action.id)

                    call.respond(HttpStatusCode.OK, Json.encodeToString(result))
                }
                is AdminAction.GetUserByUsername -> {
                    val result = combinedUserDao.getUserWithDetailsByUsername(action.username)

                    call.respond(HttpStatusCode.OK, Json.encodeToString(result))
                }
            }
        }
    }
}