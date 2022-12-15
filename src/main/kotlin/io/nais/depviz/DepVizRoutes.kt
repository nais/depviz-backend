package io.nais.depviz


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.api(srv: DependencyService) {

    get("/api/apps") {
        val graph = srv.appGraph()
        if (graph.nodes.isEmpty()) call.respond(HttpStatusCode.NotFound)
        call.respond(graph)
    }
    get("/api/teams") {
        val graph = srv.teamGraph()
        if (graph.nodes.isEmpty()) call.respond(HttpStatusCode.NotFound)
        call.respond(graph)
    }

}