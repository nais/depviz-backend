package io.nais.depviz.bigquery

import com.google.cloud.bigquery.FieldValueList
import io.ktor.http.*
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
    val repo: String,
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
                writeTopics = row["write_topics"].repeatedValue.map { it.stringValue }.toList(),
                repo = row["action_url"]?.getStringValue()?.toRepo() ?: ""
            )
        }

        fun String.getIngresses() = Json.decodeFromString<List<String>>(this)

        fun String.toRepo():String{

            val url = try {
                Url(this)
            } catch (_: URLParserException) {
                return ""
            }
            return if (url.host == "github.com" && url.pathSegments.size > 2) {
                "${url.pathSegments[1]}/${url.pathSegments[2]}"
            } else {
               ""
            }
        }
    }
}