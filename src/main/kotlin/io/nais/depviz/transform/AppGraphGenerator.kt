package io.nais.depviz.transform

import io.nais.depviz.bigquery.ApplicationDependency
import io.nais.depviz.model.external.Graph
import io.nais.depviz.model.internal.InternalGraph
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger("GenerateGraph")

class AppGraphGenerator(val applicationDependencies: List<ApplicationDependency>) {

    private val internalGraph = InternalGraph(applicationDependencies)

    fun byEdgeCount() =
        internalGraph.toSizedGraph(internalGraph.edges.groupingBy { edge -> edge.toKey }.eachCount())

    fun byLOC(locMap: Map<String, Int>): Graph {
        LOGGER.info("locmap " + locMap.entries.take(5).map { "${it.key} to ${it.value}" }.joinToString("::"))
        val sizes = applicationDependencies.associate {
            it.key to locMap.getOrDefault(it.repo, 1)
        }
        LOGGER.info("sizes " + sizes.entries.take(5).map { "${it.key} to ${it.value}" }.joinToString("::"))

        return internalGraph.toSizedGraph(sizes)
    }

     fun readFile() = MapFromResourcesFile().readAndParseWithDelimiter()
}