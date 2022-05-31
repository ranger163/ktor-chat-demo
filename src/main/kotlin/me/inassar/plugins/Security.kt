package me.inassar.plugins

import io.ktor.application.*
import io.ktor.sessions.*
import io.ktor.util.*
import me.inassar.session.ChatSession

fun Application.configureSecurity() {
    install(Sessions) {
        cookie<ChatSession>("SESSION")
    }

    intercept(ApplicationCallPipeline.Features) {
        if (call.sessions.get<ChatSession>() == null) {
            val username = call.parameters["username"] ?: "Guest"
            call.sessions.set(ChatSession(username = username, sessionId = generateNonce()))
        }
    }
}
