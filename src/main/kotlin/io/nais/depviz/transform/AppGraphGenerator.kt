package io.nais.depviz.transform

import io.nais.depviz.bigquery.ApplicationDependency
import io.nais.depviz.model.external.Graph
import io.nais.depviz.model.external.GraphEdge
import io.nais.depviz.model.internal.InternalGraph
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger("GenerateGraph")


fun generateAppGraph(applicationDependencies: List<ApplicationDependency>): Graph {
    val internalGraph = InternalGraph(applicationDependencies)
    return internalGraph.toSizedGraph(sizingByCount(internalGraph.edges))

}

fun sizingByCount(edges: Set<GraphEdge>): Map<String, Int> = edges.groupingBy { edge -> edge.toKey }.eachCount()