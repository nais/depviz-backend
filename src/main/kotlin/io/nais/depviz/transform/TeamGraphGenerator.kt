package io.nais.depviz.transform

import io.nais.depviz.bigquery.ApplicationDependency
import io.nais.depviz.model.external.*
import io.nais.depviz.model.external.Tag.*


fun generateTeamGraph(applicationDependencies: List<ApplicationDependency>): Graph {
    val appGraphGenerator = AppGraphGenerator(applicationDependencies)
    val appGraph = appGraphGenerator.byEdgeCount()
    val rawTeamNodes = appGraph.nodes.associate { appNode -> appNode.key to asTeamNode(appNode) }
    val rawTeamEdges = appGraph.edges.map { appEdge -> asTeamEdge(appEdge, rawTeamNodes) }.toSet()
    val topicsToRemove = topicsToRemove(rawTeamEdges)

    val teamNodes = rawTeamNodes.values.distinctBy { it.key }.filterNot { it.key in topicsToRemove }
    val syncTeamEdges = rawTeamEdges.filter { it.type == GraphEdgeType.SYNC }.filterNot { it.fromKey == it.toKey }

    val asyncTeamEdges = rawTeamEdges.filter { it.type == GraphEdgeType.ASYNC }
        .filterNot { it.fromTag == TOPIC && it.fromKey in topicsToRemove }
        .filterNot { it.toTag == TOPIC && it.toKey in topicsToRemove }

    return Graph(
        teamNodes.toSet(),
        (syncTeamEdges + asyncTeamEdges).toSet(),
        teamToPO.values.map { GraphCluster.clusterOf(it) }.toSet(),
        setOf(GraphTag(TEAM), GraphTag(TOPIC))
    )
}


private fun asTeamEdge(edge: GraphEdge, nodes: Map<String, GraphNode>) = GraphEdge(
    fromKey = nodes[edge.fromKey]!!.key,
    fromTag = toTeamTag(edge.fromTag),
    toKey = nodes[edge.toKey]!!.key,
    toTag = toTeamTag(edge.toTag),
    type = edge.type
)

private fun toTeamTag(tag: Tag) = if (APP == tag) TEAM else tag

private fun asTeamNode(appNode: GraphNode) = if (appNode.tag == Tag.APP) {
    GraphNode(
        key = appNode.cluster,
        label = appNode.cluster,
        tag = Tag.TEAM,
        cluster = teamToPO.getOrDefault(appNode.cluster, ""),
        size = appNode.size
    )
} else {
    GraphNode(
        key = appNode.key,
        label = appNode.label,
        tag = Tag.TOPIC,
        cluster = teamToPO.getOrDefault(appNode.cluster, ""),
        size = appNode.size
    )
}


private fun topicsToRemove(edges: Set<GraphEdge>): Set<String> {
    val singleAsyncFromEdges = singleAsyncEdge(edges, GraphEdge::fromTag, GraphEdge::fromKey)
    val singleAsyncToEdges = singleAsyncEdge(edges, GraphEdge::toTag, GraphEdge::toKey)
    return singleAsyncFromEdges intersect singleAsyncToEdges
}

private fun singleAsyncEdge(edges: Set<GraphEdge>, tag: (GraphEdge) -> Tag, key: (GraphEdge) -> String) =
    edges
        .filter { it.type == GraphEdgeType.ASYNC }
        .filter { tag(it) == TOPIC }
        .groupBy { key(it) }
        .filter { it.value.size == 1 }.keys