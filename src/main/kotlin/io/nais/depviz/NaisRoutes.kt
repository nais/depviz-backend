package io.nais.depviz


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.prometheus.client.CollectorRegistry
import io.prometheus.client.exporter.common.TextFormat

fun Route.nais() {
    get("/internal/isalive") {
        call.respondText("UP")
    }
    get("/internal/isready") {
        call.respondText("UP")
    }
    get("/internal/prometheus") {
        val names = call.request.queryParameters.getAll("name")?.toSet() ?: emptySet()
        call.respondTextWriter(ContentType.parse(TextFormat.CONTENT_TYPE_004), HttpStatusCode.OK) {
            TextFormat.write004(this, CollectorRegistry.defaultRegistry.filteredMetricFamilySamples(names))
        }
    }
}