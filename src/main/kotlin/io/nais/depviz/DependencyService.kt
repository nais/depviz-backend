package io.nais.depviz

import io.nais.depviz.data.ApplicationDependency
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
        val filteredDependencyList = filterApplicationDependency(dependencyList, "canary")
        LOGGER.info("removed ${dependencyList.size - filteredDependencyList.size} elements containing the word 'canary' from list, and also references to those elements.")
        graph = generateGraph(filteredDependencyList)
        LOGGER.info("generated graph with ${graph.nodes.size} nodes,${graph.edges.size} edges, ${graph.clusters.size} clusters and ${graph.tags.size} tags")
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