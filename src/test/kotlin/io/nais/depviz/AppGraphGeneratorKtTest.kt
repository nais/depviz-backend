package io.nais.depviz

import io.nais.depviz.bigquery.ApplicationDependency
import io.nais.depviz.transform.AppGraphGenerator
import io.nais.depviz.transform.generateTeamGraph
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
            ),
            repo = "org/repo1"

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
            writeTopics = mutableListOf(),
            repo = "org/repo2"
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
            ),
            repo = "org/repo3"
        ),
    )

    val locMap  = mapOf(
        "org/repo1" to 1000,
        "org/repo2" to 2000,
        "org/repo3" to 3000
    )

    @Test
    fun `canary releases are filtered out`(){
       val graph = generateTeamGraph(list)
        assertThat(graph.nodes).hasSize(5)
        assertThat(graph.edges).hasSize(4)
        assertThat(graph.nodes.map { it.key }).doesNotContain("nav-prod.aura.kafka-canary-prod-gcp.v4")
    }


    @Test
    fun `size by LOC`(){

        val appGraphGenerator = AppGraphGenerator(list)
        val x = appGraphGenerator.byLOC(locMap)
        assertThat(x).isNotNull

    }


    @Test
    fun `size by count`(){

        val appGraphGenerator = AppGraphGenerator(list)
        val x = appGraphGenerator.byEdgeCount()
        assertThat(x).isNotNull

    }



}