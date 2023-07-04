package io.nais.depviz.model.internal

import io.nais.depviz.bigquery.ApplicationDependency
import io.nais.depviz.model.external.*
import org.slf4j.LoggerFactory

private val LOGGER = LoggerFactory.getLogger("InternalGraph")

class InternalGraph(private val applicationDependencies: List<ApplicationDependency>) {
    private val keysAndNodesMap = keysAndNodesMapFrom(applicationDependencies)
    val edges = createAppEdges(applicationDependencies, keysAndNodesMap)
    val nodes = keysAndNodesMap.values.toSet()
    val clusters = nodes.map { GraphCluster.clusterOf(it.cluster) }.toSet()
    val tags = setOf(GraphTag(Tag.APP), GraphTag(Tag.TOPIC))

    /**
     * Map(node key, size)
     */
    fun toSizedGraph(sizes: Map<String, Int>): Graph {
        LOGGER.info("SizeMap has ${sizes.keys.size} keys, with size characteristics " +
                "avg: ${sizes.values.average()}, " +
                "min: ${sizes.values.min()} " +
                "max: ${sizes.values.max()} " +
                "values larger than 1: ${sizes.values.filter { it<2 }.size}")
        return Graph(
            nodes = nodes.map { it.asGraphNode(sizes.getOrDefault(it.key, 0)) }.toSet(),
            edges = edges,
            clusters = clusters,
            tags = tags
        )
    }

    private fun keysAndNodesMapFrom(applicationDependencies: List<ApplicationDependency>) =
        applicationDependencies.flatMap { app ->
            setOf(
                listOf(InternalGraphNode.appOf(app)),
                app.readTopics.map { InternalGraphNode.topicOf(it) },
                app.writeTopics.map { InternalGraphNode.topicOf(it) },
            ).flatten()
        }.associateBy { it.key }

    private fun createAppEdges(
        applicationDependencies: List<ApplicationDependency>,
        nodes: Map<String, InternalGraphNode>
    ): Set<GraphEdge> =
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
        }.toSet()
}


data class InternalGraphNode(
    val key: String,
    val label: String,
    val tag: Tag,
    val cluster: String,
) {
    fun asGraphNode(size: Int) = GraphNode(key, label, tag, cluster, size)

    companion object {
        fun appOf(ad: ApplicationDependency) =
            InternalGraphNode(
                key = ad.key,
                label = ad.name,
                tag = Tag.APP,
                cluster = ad.team
            )

        fun topicOf(topic: String): InternalGraphNode {
            val components = topic.split(".")
            val name = components.subList(2, components.size).joinToString(".")
            return InternalGraphNode(
                key = topic,
                label = name,
                tag = Tag.TOPIC,
                cluster = components[1]
            )
        }
    }
}
