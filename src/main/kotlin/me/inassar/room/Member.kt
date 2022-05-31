package me.inassar.room

import io.ktor.http.cio.websocket.*

data class Member(
    val username: String,
    val sessionId: String,
    val webSocket: WebSocketSession
)
