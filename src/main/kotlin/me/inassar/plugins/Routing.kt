package me.inassar.plugins

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import me.inassar.room.RoomController
import me.inassar.routes.connectToSocket
import me.inassar.routes.getMessages
import org.koin.ktor.ext.get
import org.koin.ktor.ext.inject

fun Application.configureRouting() {
    val roomController by inject<RoomController>()
    install(Routing) {
        connectToSocket(roomController)
        getMessages(roomController)
        website()
    }
}

fun Route.website() {
    get("/") {
        call.respondText("Hi Ahmed")
    }
}