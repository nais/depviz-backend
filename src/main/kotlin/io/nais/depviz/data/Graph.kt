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
enum class Tag { APP, TOPIC, TEAM;

    fun toTeamTag(): Tag {
        return when (APP) {
            this -> TEAM
            else -> this
        }
    }
}

@Serializable
data class GraphEdge(
    val fromKey: String,
    val fromTag: Tag,
    val toKey: String,
    val toTag: Tag,
    val type: GraphEdgeType
) {

    fun asTeamEdge(nodes: Map<String, GraphNode>) =
        GraphEdge(nodes[fromKey]!!.key, fromTag.toTeamTag(), nodes[toKey]!!.key, toTag.toTeamTag(), type)


    companion object {
        fun syncOf(from: InternalGraphNode, to: InternalGraphNode) = GraphEdge(from.key, from.tag, to.key, to.tag, GraphEdgeType.SYNC)
        fun asyncOf(from:InternalGraphNode, to: InternalGraphNode) = GraphEdge(from.key, from.tag, to.key, to.tag, GraphEdgeType.ASYNC)
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
    val size: Int
){
    fun asTeamNode() =
        if (tag == Tag.APP) {
            GraphNode(
                key = cluster,
                label = cluster,
                tag = Tag.TEAM,
                cluster = teamToPO.getOrDefault(cluster, ""),
                size = size
            )
        } else {
            GraphNode(
                key = key,
                label = label,
                tag = Tag.TOPIC,
                cluster = teamToPO.getOrDefault(cluster, ""),
                size = size
            )
        }
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
            InternalGraphNode(key = ad.key,
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

