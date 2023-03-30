package io.nais.depviz.model.external

import io.nais.depviz.model.internal.InternalGraphNode
import io.nais.depviz.transform.teamToPO
import kotlinx.serialization.Serializable


@Serializable
data class Graph(
    var nodes: Set<GraphNode>,
    val edges: Set<GraphEdge>,
    val clusters: Set<GraphCluster>,
    val tags: Set<GraphTag>
) {
    companion object {
        fun empty() = Graph(emptySet(), emptySet(), emptySet(), emptySet())
    }
}

enum class GraphEdgeType { ASYNC, SYNC }
enum class Tag { APP, TOPIC, TEAM}

@Serializable
data class GraphEdge(
    val fromKey: String,
    val fromTag: Tag,
    val toKey: String,
    val toTag: Tag,
    val type: GraphEdgeType
) {

    companion object {
        fun syncOf(from: InternalGraphNode, to: InternalGraphNode) = GraphEdge(from.key, from.tag, to.key, to.tag, GraphEdgeType.SYNC)
        fun asyncOf(from: InternalGraphNode, to: InternalGraphNode) = GraphEdge(from.key, from.tag, to.key, to.tag, GraphEdgeType.ASYNC)
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
data class GraphTag(
    val key: Tag
)

@Serializable
data class GraphNode(
    val key: String,
    val label: String,
    val tag: Tag,
    val cluster: String,
    val size: Int
)

