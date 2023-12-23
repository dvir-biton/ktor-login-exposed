package com.fylora

import com.fylora.auth.authModule
import com.fylora.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

@Suppress("Unused")
fun Application.module() {
    configureSerialization()
    configureMonitoring()
    authModule()
}
