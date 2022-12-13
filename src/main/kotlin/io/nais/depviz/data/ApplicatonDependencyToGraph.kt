package io.nais.depviz.data

import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger("GenerateGraph")


fun generateGraph(applicationDependencies: List<ApplicationDependency>): Graph {

    val tags = setOf(GraphTags(Tag.APP), GraphTags(Tag.TOPIC))
    val nodes = createNodes(applicationDependencies)
    val edges = createEdges(applicationDependencies, nodes)
    val clusters = nodes.values.map { GraphCluster.clusterOf(it.cluster) }.toSet()
    return Graph(nodes.values.toSet(), edges.toSet(), clusters, tags)
}

private fun createEdges(
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

private fun createNodes(applicationDependencies: List<ApplicationDependency>) =
    applicationDependencies.flatMap { app ->
        setOf(
            listOf(GraphNode.appOf(app)),
            app.readTopics.map { GraphNode.topicOf(it) },
            app.writeTopics.map { GraphNode.topicOf(it) },
        ).flatten()
    }.associateBy { it.key }
