package io.nais.depviz

import io.nais.depviz.data.Graph
import io.nais.depviz.data.generateGraph
import org.slf4j.LoggerFactory


private val LOGGER = LoggerFactory.getLogger("DependencyService")

class DependencyService(private val depLoader: DepLoader) {

    var graph = Graph.empty()

    fun graph() = graph

    fun buildGraph() {
        val dependencyList = depLoader.getApplicationDependenciesFromBigquery()
        LOGGER.info("read ${dependencyList.size} elements from bigquery")
        graph = generateGraph(dependencyList)
        LOGGER.info("generated graph with ${graph.nodes.size} nodes,${graph.edges.size} edges, ${graph.clusters.size} clusters and ${graph.tags.size} tags")
    }
}