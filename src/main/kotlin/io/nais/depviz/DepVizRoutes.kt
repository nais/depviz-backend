package io.nais.depviz


import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.api() {
    get("/dependecies") {


        call.respondText("UP")
    }

}