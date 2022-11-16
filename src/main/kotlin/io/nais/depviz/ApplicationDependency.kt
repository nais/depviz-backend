package io.nais.depviz

import kotlinx.serialization.Serializable

@Serializable
data class ApplicationDependency(
    val cluster: String,
    val name: String,
    val team: String,
    val namespace: String,
    val image: String,
    val ingresses: List<String> = mutableListOf(),
    val inboundApps: List<String> = mutableListOf(),
    val outboundApps: List<String> = mutableListOf(),
    val outboundHosts: List<String> = mutableListOf(),
    val readTopics: List<String> = mutableListOf(),
    val writeTopics: List<String> = mutableListOf()
)