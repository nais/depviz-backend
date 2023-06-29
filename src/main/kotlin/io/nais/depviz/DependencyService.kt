package io.nais.depviz

import io.nais.depviz.bigquery.ApplicationDependency
import io.nais.depviz.bigquery.DepLoader
import io.nais.depviz.model.external.Graph
import io.nais.depviz.transform.generateAppGraph
import io.nais.depviz.transform.generateTeamGraph
import io.nais.depviz.transform.sizingByCount
import io.nais.depviz.transform.sizingByLOC
import org.slf4j.LoggerFactory


private val LOGGER = LoggerFactory.getLogger("DependencyService")


class DependencyService(private val depLoader: DepLoader) {

    private var appGraphByEdgeCount = Graph.empty()
    private var appGraphByLOC = Graph.empty()
    private var teamGraph = Graph.empty()
    private var nodeSizes: Map<String, Map<String, Int>> = emptyMap()

    fun appGraph(size: String) = appGraphByEdgeCount

    fun teamGraph() = teamGraph

    fun buildGraph() {
        val dependencyList = depLoader.getApplicationDependenciesFromBigquery()
        LOGGER.info("read ${dependencyList.size} elements from bigquery")
        val filteredDependencyList = filterApplicationDependency(dependencyList, "canary")
        LOGGER.info("removed ${dependencyList.size - filteredDependencyList.size} elements containing the word 'canary' from list, and also references to those elements.")
        appGraphByEdgeCount = generateAppGraph(filteredDependencyList) { graph -> sizingByCount(graph.edges) }
        appGraphByLOC = generateAppGraph(filteredDependencyList) { graph -> sizingByLOC(graph.edges) }
        LOGGER.info("generated graph with ${appGraphByEdgeCount.nodes.size} nodes,${appGraphByEdgeCount.edges.size} edges, ${appGraphByEdgeCount.clusters.size} clusters and ${appGraphByEdgeCount.tags.size} tags")
        teamGraph = generateTeamGraph(filteredDependencyList)
        LOGGER.info("generated graph with ${teamGraph.nodes.size} nodes,${teamGraph.edges.size} edges, ${teamGraph.clusters.size} clusters and ${teamGraph.tags.size} tags")
    }

    private fun filterApplicationDependency(applicationDependencies: List<ApplicationDependency>, keyword: String) =
        applicationDependencies
            .filterNot { it.key.contains(keyword) }
            .map { dependency ->
                dependency.copy(
                    readTopics = dependency.readTopics.filterNot { it.contains(keyword) },
                    writeTopics = dependency.writeTopics.filterNot { it.contains(keyword) }
                )
            }

}