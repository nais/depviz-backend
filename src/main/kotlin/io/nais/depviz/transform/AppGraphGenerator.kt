package io.nais.depviz.transform

import io.nais.depviz.bigquery.ApplicationDependency
import io.nais.depviz.model.internal.InternalGraph
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger("GenerateGraph")

class AppGraphGenerator(val applicationDependencies: List<ApplicationDependency>) {

    private val internalGraph = InternalGraph(applicationDependencies)

    fun byEdgeCount() =
        internalGraph.toSizedGraph(internalGraph.edges.groupingBy { edge -> edge.toKey }.eachCount())

    fun byLOC(locMap: Map<String, Int>) = internalGraph.toSizedGraph(applicationDependencies.associate {
        it.key to locMap.getOrDefault(it.repo, 1)
    })

     fun readFile() = MapFromResourcesFile("loc/loc_nais.txt").parseToIntValues()
}