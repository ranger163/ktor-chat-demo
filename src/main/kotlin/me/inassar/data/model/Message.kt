package me.inassar.data.model

import org.bson.codecs.pojo.annotations.BsonId
import org.bson.types.ObjectId
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    @BsonId
    val id: String = ObjectId().toString(),
    val textMessage: String,
    val username: String,
    val timestamp: Long
)
