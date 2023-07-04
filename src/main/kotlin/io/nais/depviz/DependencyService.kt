package io.nais.depviz

import io.nais.depviz.bigquery.ApplicationDependency
import io.nais.depviz.bigquery.DepLoader
import io.nais.depviz.model.external.Graph
import io.nais.depviz.transform.*
import org.slf4j.LoggerFactory


private val LOGGER = LoggerFactory.getLogger("DependencyService")


class DependencyService(private val depLoader: DepLoader) {

    private var appGraphByEdgeCount = Graph.empty()
    private var appGraphByLOC = Graph.empty()
    private var teamGraph = Graph.empty()

    fun appGraphEdgeCount() = appGraphByEdgeCount

    fun appGraphLOC() = appGraphByLOC

    fun teamGraph() = teamGraph

    fun buildGraph() {
        val dependencyList = depLoader.getApplicationDependenciesFromBigquery()
        LOGGER.info("read ${dependencyList.size} elements from bigquery")
        val filteredDependencyList = filterApplicationDependency(dependencyList, "canary")
        LOGGER.info("removed ${dependencyList.size - filteredDependencyList.size} elements containing the word 'canary' from list, and also references to those elements.")

        val appGraphGenerator = AppGraphGenerator(filteredDependencyList)
        appGraphByEdgeCount = appGraphGenerator.byEdgeCount()
        LOGGER.info("generated graph sized by edge count with ${appGraphByEdgeCount.nodes.size} nodes,${appGraphByEdgeCount.edges.size} edges, ${appGraphByEdgeCount.clusters.size} clusters and ${appGraphByEdgeCount.tags.size} tags")
        appGraphByLOC = appGraphGenerator.byLOC(appGraphGenerator.readFile())
        LOGGER.info("generated graph with ${appGraphLOC().nodes.size} nodes,${appGraphByLOC.edges.size} edges, ${appGraphLOC().clusters.size} clusters and ${appGraphByLOC.tags.size} tags")
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