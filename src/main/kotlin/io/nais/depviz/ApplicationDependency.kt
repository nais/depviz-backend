package io.nais.depviz

import com.google.cloud.bigquery.FieldValueList
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.parse

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

    companion object {
        fun fromBq(row: FieldValueList): ApplicationDependency {
            val cluster = row["cluster"].stringValue
            val name = row["name"].stringValue
            val team = row["team"].stringValue
            val namespace = row["namespace"].stringValue
            val image = row["image"].stringValue

            val ingresses = getIngresses( row["ingresses"].stringValue)
            val inboundApps = row["inboundApps"].repeatedValue.map { it.stringValue }.toList()
            val outboundApps = row["outboundApps"].repeatedValue.map { it.stringValue }.toList()
            val outboundHosts = row["outboundHosts"].repeatedValue.map { it.stringValue }.toList()
            val readTopics = row["readTopics"].repeatedValue.map { it.stringValue }.toList()
            val writeTopics = row["writeTopics"].repeatedValue.map { it.stringValue }.toList()

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