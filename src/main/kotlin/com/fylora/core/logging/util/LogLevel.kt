package com.fylora.core.logging.util

sealed class LogLevel(val type: String) {
    data object Info: LogLevel("info")
    data object Error: LogLevel("error")
}