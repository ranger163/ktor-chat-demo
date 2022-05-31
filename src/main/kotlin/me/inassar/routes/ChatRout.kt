package me.inassar.routes

import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import io.ktor.network.sockets.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.sessions.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.consumeEach
import me.inassar.room.MemberExistsException
import me.inassar.room.RoomController
import me.inassar.session.ChatSession
import java.util.*

/**
 * Chat socket
 * Connecting to websocket and listening to incoming messages
 * @param roomController
 */
fun Route.connectToSocket(roomController: RoomController) {
    val connections = Collections.synchronizedSet<Connection?>(LinkedHashSet())

    webSocket("/connect") {
        println("Adding User!")
        val session = call.sessions.get<ChatSession>()
        if (session == null) {
            close(CloseReason(CloseReason.Codes.VIOLATED_POLICY, "No session found."))
            return@webSocket
        }

        try {
            roomController.connect(
                username = session.username,
                sessionId = session.sessionId,
                socket = this
            )

            incoming.consumeEach { frame ->
                if (frame is Frame.Text) {
                    roomController.sendMessage(
                        senderUsername = session.username,
                        message = frame.readText()
                    )
                }
            }
        } catch (e: MemberExistsException) {
            call.respond(HttpStatusCode.Conflict, e.message.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            roomController.disconnect(session.username)
        }
    }
}

/**
 * Get messages
 * Retrieving all messages from database
 * @param roomController
 */
fun Route.getMessages(roomController: RoomController) {
    get("/messages") {
        call.respond(
            HttpStatusCode.OK,
            roomController.getMessages()
        )
    }
}