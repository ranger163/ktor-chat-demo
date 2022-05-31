package me.inassar

import io.ktor.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import me.inassar.plugins.*

fun main() {
    chatServer {
        module()
    }
}

fun Application.module() {
    configureKoin()
    configureSockets()
    configureRouting()
    configureSecurity()
    configureMonitoring()
    configureSerialization()
}

/**
 * Chat server
 * This function will hold server configurations if needed to be changed later
 * @param module
 * @receiver
 */
fun chatServer(module: Application.() -> Unit) {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        module()
    }.start(wait = true)
}