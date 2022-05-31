package me.inassar.room

import io.ktor.http.cio.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.inassar.data.MessageDataSource
import me.inassar.data.model.Message
import java.util.concurrent.ConcurrentHashMap

class RoomController(private val messageDataSource: MessageDataSource) {

    private val members = ConcurrentHashMap<String, Member>()

    /**
     * Connect
     * Connecting user members to websocket and throwing an exception if this user is already subscribed.
     * @param username
     * @param sessionId
     * @param socket
     */
    fun connect(
        username: String,
        sessionId: String,
        socket: WebSocketSession
    ) {
        if (members.containsKey(username))
            throw MemberExistsException()

        println("$username: IS CONNECTED")

        members[username] = Member(
            username = username,
            sessionId = sessionId,
            webSocket = socket
        )
    }

    /**
     * Send message
     * Saving message into database then broadcasting it again to all socket subscribers.
     * @param senderUsername
     * @param message
     */
    suspend fun sendMessage(
        senderUsername: String,
        message: String
    ) {
        // Constructing message object.
        val messageEntity = Message(
            textMessage = message,
            username = senderUsername,
            timestamp = System.currentTimeMillis()
        )

        // Saving message into database.
        messageDataSource.insertMessage(messageEntity)

        println("$senderUsername: SAYS -> $message")

        members.values.forEach { member ->
            // Encoding message into json string.
            val broadcastMessage = Json.encodeToString(messageEntity)
            // Sending message to other socket subscribers.
            member.webSocket.send(Frame.Text(broadcastMessage))
        }
    }

    /**
     * Get messages
     * Getting all messages from database
     * @return
     */
    suspend fun getMessages(): List<Message> = messageDataSource.getMessages()

    /**
     * Disconnect
     * Disconnecting member from the websocket
     * @param username
     */
    suspend fun disconnect(username: String) {
        // Closing websocket for a subscribed socket user.
        members[username]?.webSocket?.close()

        // Removing this user from the socket.
        if (members.containsKey(username))
            members.remove(username)
    }
}