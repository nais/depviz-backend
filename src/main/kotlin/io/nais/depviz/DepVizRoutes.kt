package io.nais.depviz


import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlinx.serialization.json.JsonArray

fun Route.api(srv: DependencyService) {

    val service = srv

    get("/dependecies") {
        val dependecies = service.dependecies()
        if (dependecies.list.isEmpty()) call.respond(HttpStatusCode.NotFound)
        call.respond(dependecies)
    }

}