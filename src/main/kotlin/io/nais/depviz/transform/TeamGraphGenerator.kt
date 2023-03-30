package io.nais.depviz.transform

import io.nais.depviz.bigquery.ApplicationDependency
import io.nais.depviz.data.*


fun generateTeamGraph(applicationDependencies: List<ApplicationDependency>): Graph {
    val appGraph = generateAppGraph(applicationDependencies)
    val rawTeamNodes = appGraph.nodes.associate { appNode -> appNode.key to appNode.asTeamNode() }
    val rawTeamEdges = appGraph.edges.map { appEdge -> appEdge.asTeamEdge(rawTeamNodes) }.toSet()
    val topicsToRemove = topicsToRemove(rawTeamEdges)

    val teamNodes = rawTeamNodes.values
        .distinctBy { it.key }
        .filterNot { it.key in topicsToRemove }

    val syncTeamEdges = rawTeamEdges
        .filter { it.type == GraphEdgeType.SYNC }
        .filterNot { it.fromKey == it.toKey }

    val asyncTeamEdges = rawTeamEdges.filter { it.type == GraphEdgeType.ASYNC }
        .filterNot { it.fromTag == Tag.TOPIC && it.fromKey in topicsToRemove }
        .filterNot { it.toTag == Tag.TOPIC && it.toKey in topicsToRemove }

    return Graph(
        teamNodes.toSet(),
        (syncTeamEdges + asyncTeamEdges).toSet(),
        teamToPO.values.map { GraphCluster.clusterOf(it) }.toSet(),
        setOf(GraphTags(Tag.TEAM), GraphTags(Tag.TOPIC))
    )
}

fun topicsToRemove(edges: Set<GraphEdge>) =
    singleAsyncEdge(edges, GraphEdge::fromTag, GraphEdge::fromKey) intersect singleAsyncEdge(edges, GraphEdge::toTag, GraphEdge::toKey)

private fun singleAsyncEdge(edges: Set<GraphEdge>, tag: (GraphEdge) -> Tag, key: (GraphEdge) -> String) =
    edges.filter { it.type == GraphEdgeType.ASYNC }
        .filter { tag(it) == Tag.TOPIC }
        .groupBy { key(it) }.filter { it.value.size == 1 }
        .keys