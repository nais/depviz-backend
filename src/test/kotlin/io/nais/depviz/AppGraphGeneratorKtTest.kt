package io.nais.depviz

import io.nais.depviz.data.ApplicationDependency
import io.nais.depviz.data.generateAppGraph
import io.nais.depviz.data.generateTeamGraph
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

internal class AppGraphGeneratorKtTest {


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
        ),
        ApplicationDependency(
            cluster = "cluster",
            name = "name2",
            team = "namespace",
            namespace = "namespace",
            image = "image",
            ingresses = mutableListOf("ingress1", "ingress2"),
            inboundApps = mutableListOf(),
            outboundApps = mutableListOf(),
            outboundHosts = mutableListOf("www.vg.no"),
            readTopics = mutableListOf("nav-prod.aura.kafka-canary-prod-gcp_v4"),
            writeTopics = mutableListOf()
        ),
        ApplicationDependency(
            cluster = "cluster",
            name = "name3",
            team = "namespace",
            namespace = "namespace",
            image = "image",
            ingresses = mutableListOf("ingress1", "ingress2"),
            inboundApps = mutableListOf(),
            outboundApps = mutableListOf(),
            outboundHosts = mutableListOf("www.vg.no"),
            readTopics = mutableListOf(),
            writeTopics = mutableListOf(
                "nav-prod.aura.kafka-canary-prod-gcp_v4"
            )
        ),
    )

    @Test
    fun `canary releases are filtered out`(){
       val graph = generateTeamGraph(list)
        assertThat(graph.nodes).hasSize(5)
        assertThat(graph.edges).hasSize(4)
        assertThat(graph.nodes.map { it.key }).doesNotContain("nav-prod.aura.kafka-canary-prod-gcp.v4")
    }


}