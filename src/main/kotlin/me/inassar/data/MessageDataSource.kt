package me.inassar.data

import me.inassar.data.model.Message

interface MessageDataSource {

    suspend fun getMessages(): List<Message>

    suspend fun insertMessage(message: Message)
}