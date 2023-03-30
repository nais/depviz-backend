package io.nais.depviz.routes


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.nais.depviz.DependencyService

fun Route.api(srv: DependencyService) {

    get("/api") {
        val graph = srv.appGraph("counts")
        if (graph.nodes.isEmpty()) call.respond(HttpStatusCode.NotFound)
        call.respond(graph)
    }

    get("/api/apps") {
        val graph = srv.appGraph("counts")
        if (graph.nodes.isEmpty()) call.respond(HttpStatusCode.NotFound)
        call.respond(graph)
    }
    get("/api/teams") {
        val graph = srv.teamGraph()
        if (graph.nodes.isEmpty()) call.respond(HttpStatusCode.NotFound)
        call.respond(graph)
    }

}