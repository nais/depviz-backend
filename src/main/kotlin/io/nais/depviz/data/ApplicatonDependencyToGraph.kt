package io.nais.depviz.data

import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger("GenerateGraph")


fun generateAppGraph(applicationDependencies: List<ApplicationDependency>): Graph {

    val tags = setOf(GraphTags(Tag.APP), GraphTags(Tag.TOPIC))
    val nodes = createAppNodes(applicationDependencies)
    val edges = createAppEdges(applicationDependencies, nodes)
    val clusters = nodes.values.map { GraphCluster.clusterOf(it.cluster) }.toSet()
    return Graph(nodes.values.toSet(), edges.toSet(), clusters, tags)
}

fun generateTeamGraph(applicationDependencies: List<ApplicationDependency>): Graph {
    val tags = setOf(GraphTags(Tag.TEAM), GraphTags(Tag.TOPIC))
    val appNodes = createAppNodes(applicationDependencies)
    val teamnodes = createTeamNodes(applicationDependencies)
    val edges = createTeamEdges(applicationDependencies, appNodes, teamnodes)
    val clusters = teamnodes.values.map { GraphCluster.clusterOf(it.cluster) }.toSet()
    return Graph(teamnodes.values.toSet(), edges.toSet(), clusters, tags)
}

fun createTeamEdges(
    applicationDependencies: List<ApplicationDependency>,
    appNodes: Map<String, GraphNode>,
    teamnodes: Map<String, GraphNode>
): List<GraphEdge> =
    applicationDependencies.flatMap { app ->
        setOf(
            app.outboundApps.mapNotNull { toApp ->
                val toAppNode = appNodes[toApp]
                if (toAppNode == null) {
                    LOGGER.info("No appnode for $toApp")
                    null
                } else if (toAppNode.cluster == app.team) {
                    LOGGER.info("Discarded edge from ${app.key} to $toApp, as they were both in team: ${app.team}.")
                    null
                } else {
                    GraphEdge.syncOf(teamnodes[app.team]!!, teamnodes[toAppNode.cluster]!!)
                }
            }
        ).flatten()
    }

fun createTeamNodes(applicationDependencies: List<ApplicationDependency>) =
    applicationDependencies.flatMap { app ->
        setOf(GraphNode.teamOf(app))
    }.associateBy { it.key }


private fun createAppEdges(
    applicationDependencies: List<ApplicationDependency>,
    nodes: Map<String, GraphNode>
): List<GraphEdge> =
    applicationDependencies.flatMap { app ->
        setOf(
            app.outboundApps.mapNotNull { outbound ->
                val to = nodes[outbound]
                if (to != null) {
                    GraphEdge.syncOf(nodes[app.key]!!, to)
                } else {
                    LOGGER.info("Tried to create edge from ${app.key} to $outbound, but outbound node is not defined.")
                    null
                }
            },
            app.writeTopics.map { topic -> GraphEdge.asyncOf(nodes[app.key]!!, nodes[topic]!!) },
            app.readTopics.map { topic -> GraphEdge.asyncOf(nodes[topic]!!, nodes[app.key]!!) }
        ).flatten()
    }

private fun createAppNodes(applicationDependencies: List<ApplicationDependency>) =
    applicationDependencies.flatMap { app ->
        setOf(
            listOf(GraphNode.appOf(app)),
            app.readTopics.map { GraphNode.topicOf(it) },
            app.writeTopics.map { GraphNode.topicOf(it) },
        ).flatten()
    }.associateBy { it.key }
