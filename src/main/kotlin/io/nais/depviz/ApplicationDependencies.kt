package io.nais.depviz

data class ApplicationDependencies(
    val cluster:String,
    val name:String,
    val team:String,
    val namespace:String,
    val image:String,
    val ingresses: List<String>,
    val inboundApps: List<String>,
    val outboundApps: List<String>,
    val outboundHosts: List<String>,
    val readTopics: List<String>,
    val writeTopics: List<String>
)
