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
    val ingresses: List<String>,
    val inboundApps: List<String>,
    val outboundApps: List<String>,
    val outboundHosts: List<String>,
    val readTopics: List<String>,
    val writeTopics: List<String>,
    val key: String = "$cluster.$namespace.$name"

) {
    companion object {
        fun fromBq(row: FieldValueList): ApplicationDependency {

            return ApplicationDependency(
                cluster = row["cluster"].stringValue,
                name = row["name"].stringValue,
                team = row["team"].stringValue,
                namespace = row["namespace"].stringValue,
                image = row["image"].stringValue,
                ingresses = (row["ingresses"].stringValue).getIngresses(),
                inboundApps = row["inbound_apps"].repeatedValue.map { it.stringValue }.toList(),
                outboundApps = row["outbound_apps"].repeatedValue.map { it.stringValue }.toList(),
                outboundHosts = row["outbound_hosts"].repeatedValue.map { it.stringValue }.toList(),
                readTopics = row["read_topics"].repeatedValue.map { it.stringValue }.toList(),
                writeTopics = row["write_topics"].repeatedValue.map { it.stringValue }.toList()
            )
        }

        fun String.getIngresses(): List<String> {
            return Json.decodeFromString<List<String>>(this)
        }
    }
}