package io.nais.depviz


import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.api(srv: DependencyService) {

    val service = srv

    get("/dependencies") {
        val graph = service.graph()
        if (graph.nodes.isEmpty()) call.respond(HttpStatusCode.NotFound)
        call.respond(graph)
    }

}