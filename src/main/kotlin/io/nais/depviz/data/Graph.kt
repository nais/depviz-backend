package io.nais.depviz.data

import kotlinx.serialization.Serializable


@Serializable
data class Graph(
    var nodes: Set<SizedGraphNode>,
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

    fun asTeamEdge(nodes: Map<String, SizedGraphNode>) =
        GraphEdge(nodes[fromKey]!!.key, fromTag.toTeamTag(), nodes[toKey]!!.key, toTag.toTeamTag(), type)


    companion object {
        fun syncOf(from: GraphNode, to: GraphNode) = GraphEdge(from.key, from.tag, to.key, to.tag, GraphEdgeType.SYNC)
        fun asyncOf(from:GraphNode, to: GraphNode) = GraphEdge(from.key, from.tag, to.key, to.tag, GraphEdgeType.ASYNC)
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
data class SizedGraphNode(
    val key: String,
    val label: String,
    val tag: Tag,
    val cluster: String,
    val size: Int
){
    fun asTeamNode() =
        if (tag == Tag.APP) {
            SizedGraphNode(
                key = cluster,
                label = cluster,
                tag = Tag.TEAM,
                cluster = teamToPO.getOrDefault(cluster, ""),
                size = size
            )
        } else {
            SizedGraphNode(
                key = key,
                label = label,
                tag = Tag.TOPIC,
                cluster = teamToPO.getOrDefault(cluster, ""),
                size = size
            )
        }
}


data class GraphNode(
    val key: String,
    val label: String,
    val tag: Tag,
    val cluster: String,
) {
    fun asSizedGraphNode(size: Int) = SizedGraphNode(key, label, tag, cluster, size)

    companion object {
        fun appOf(ad: ApplicationDependency) =
            GraphNode(key = ad.key,
                label = ad.name,
                tag = Tag.APP,
                cluster = ad.team
            )
        
        fun topicOf(topic: String): GraphNode {
            val components = topic.split(".")
            val name = components.subList(2, components.size).joinToString(".")
            return GraphNode(
                key = topic,
                label = name,
                tag = Tag.TOPIC,
                cluster = components[1]
            )
        }
    }
}

