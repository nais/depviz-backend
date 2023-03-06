package io.nais.depviz

import io.nais.depviz.data.*
import org.slf4j.LoggerFactory


private val LOGGER = LoggerFactory.getLogger("DependencyService")

class DependencyService(private val depLoader: DepLoader) {

    private var appGraph = Graph.empty()
    private var teamGraph = Graph.empty()
    private var nodeSizes: Map<String, Map<String, Int>> = emptyMap()

    fun appGraph(size: String): Graph {
        appGraph.nodes.forEach { it.size = nodeSizes[size].orEmpty().getOrDefault(it.key, -1) }
        return appGraph
    }

    fun teamGraph() = teamGraph

    fun buildGraph() {
        val dependencyList = depLoader.getApplicationDependenciesFromBigquery()
        LOGGER.info("read ${dependencyList.size} elements from bigquery")
        val filteredDependencyList = filterApplicationDependency(dependencyList, "canary")
        LOGGER.info("removed ${dependencyList.size - filteredDependencyList.size} elements containing the word 'canary' from list, and also references to those elements.")
        appGraph = generateAppGraph(filteredDependencyList)
        LOGGER.info("generated graph with ${appGraph.nodes.size} nodes,${appGraph.edges.size} edges, ${appGraph.clusters.size} clusters and ${appGraph.tags.size} tags")
        teamGraph = generateTeamGraph(filteredDependencyList)
        LOGGER.info("generated graph with ${teamGraph.nodes.size} nodes,${teamGraph.edges.size} edges, ${teamGraph.clusters.size} clusters and ${teamGraph.tags.size} tags")
        nodeSizes = createNodeSizes(appGraph.edges)
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

    fun createNodeSizes(edges: Set<GraphEdge>): Map<String, Map<String, Int>> =
        mapOf("counts" to edges.groupingBy { edge -> edge.toKey }.eachCount())

}