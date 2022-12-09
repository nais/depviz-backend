package io.nais.depviz.data

import com.google.cloud.bigquery.FieldValueList
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

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

) {

    val key: String =  "$cluster.$namespace.$name"

    companion object {
        fun fromBq(row: FieldValueList): ApplicationDependency {
            val cluster = row["cluster"].stringValue
            val name = row["name"].stringValue
            val team = row["team"].stringValue
            val namespace = row["namespace"].stringValue
            val image = row["image"].stringValue

            val ingresses = getIngresses( row["ingresses"].stringValue)
            val inboundApps = row["inbound_apps"].repeatedValue.map { it.stringValue }.toList()
            val outboundApps = row["outbound_apps"].repeatedValue.map { it.stringValue }.toList()
            val outboundHosts = row["outbound_hosts"].repeatedValue.map { it.stringValue }.toList()
            val readTopics = row["read_topics"].repeatedValue.map { it.stringValue }.toList()
            val writeTopics = row["write_topics"].repeatedValue.map { it.stringValue }.toList()

            return ApplicationDependency(
                cluster,
                name,
                team,
                namespace,
                image,
                ingresses,
                inboundApps,
                outboundApps,
                outboundHosts,
                readTopics,
                writeTopics
            )
        }

        fun getIngresses(listOfingresses: String): List<String> {
            return Json.decodeFromString<List<String>>(listOfingresses)
        }
    }
}