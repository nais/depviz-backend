package io.nais.depviz

import io.nais.depviz.data.ApplicationDependency
import io.nais.depviz.data.generateGraph
import org.junit.jupiter.api.Test

internal class ApplicatonDependencyToGraphKtTest {


    val list = listOf(
        ApplicationDependency(
            cluster = "cluster",
            name = "name",
            team = "namespace",
            namespace = "namespace",
            image = "image",
            ingresses = mutableListOf("ingress1", "ingress2"),
            inboundApps = mutableListOf("app1", "app2"),
            outboundApps = mutableListOf(),
            outboundHosts = mutableListOf("www.vg.no"),
            readTopics = mutableListOf("nav-prod.aura.kafka-canary-prod-gcp", "nav-prod.aura.kafka-canary-prod-gcp.v1"),
            writeTopics = mutableListOf(
                "nav-prod.aura.kafka-canary-prod-gcp.v2",
                "nav-prod.aura.kafka-canary-prod-gcp.v3"
            )
        )
    )

    @Test
    fun test() {
        println(generateGraph(list))
    }
}