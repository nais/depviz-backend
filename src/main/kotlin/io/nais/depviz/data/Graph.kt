package io.nais.depviz.data

import kotlinx.serialization.Serializable


@Serializable
data class Graph(
    var nodes: Set<GraphNode>,
    val edges: Set<GraphEdge>,
    val clusters: Set<GraphCluster>,
    val tags: Set<GraphTags>
) {
    companion object {
        fun empty() = Graph(emptySet(), emptySet(), emptySet(), emptySet())
    }
}

enum class GraphEdgeType { ASYNC, SYNC }
enum class Tag { APP, TOPIC, TEAM }

@Serializable
data class GraphEdge(
    val fromKey: String,
    val toKey: String,
    val type: GraphEdgeType
) {

    companion object {
        fun syncOf(from: GraphNode, to: GraphNode) = GraphEdge(from.key, to.key, GraphEdgeType.SYNC)
        fun asyncOf(from: GraphNode, to: GraphNode) = GraphEdge(from.key, to.key, GraphEdgeType.ASYNC)
    }
}


@Serializable
data class GraphCluster(
    val key: String

) {
    companion object {
        fun clusterOf(cluster: String) = GraphCluster(cluster)
    }
}

@Serializable
data class GraphTags(
    val key: Tag
)

@Serializable
data class GraphNode(
    val key: String,
    val label: String,
    val tag: Tag,
    val cluster: String,
    val ingresses: List<String>
) {
    companion object {
        fun appOf(ad: ApplicationDependency) =
            GraphNode(ad.key, ad.name, Tag.APP, ad.team, ad.ingresses)

        fun teamOf(ad: ApplicationDependency) =
            GraphNode(ad.team, ad.team, Tag.TEAM, "PO", emptyList())


        fun topicOf(topic: String): GraphNode {
            val components = topic.split(".")
            val name = components.subList(2, components.size).joinToString(".")
            return GraphNode(topic, name, Tag.TOPIC, components[1], emptyList())
        }
    }
}

