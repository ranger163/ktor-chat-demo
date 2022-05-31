package me.inassar.plugins

import io.ktor.application.*
import me.inassar.di.mainModule
import org.koin.ktor.ext.Koin

fun Application.configureKoin() {
    install(Koin) {
        modules(mainModule)
    }
}