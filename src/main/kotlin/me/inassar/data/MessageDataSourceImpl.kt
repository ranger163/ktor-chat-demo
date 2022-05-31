package me.inassar.data

import me.inassar.data.model.Message
import org.litote.kmongo.coroutine.CoroutineDatabase

class MessageDataSourceImpl(
    private val database: CoroutineDatabase
) : MessageDataSource {

    private val messages = database.getCollection<Message>()

    override suspend fun getMessages(): List<Message> =
        messages.find()
            .descendingSort(Message::timestamp)
            .toList()

    override suspend fun insertMessage(message: Message) {
        messages.insertOne(message)
    }
}