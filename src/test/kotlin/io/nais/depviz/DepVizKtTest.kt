package io.nais.depviz

import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.nais.depviz.bigquery.ApplicationDependency
import io.nais.depviz.bigquery.DepLoader
import io.nais.depviz.data.Graph
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.util.*

class DepVizKtTest {

    @Disabled
    @Test
    internal fun `api gives app nodes`() {
        val fileTestLoader = FileTestLoader()
        withTestApplication(
            moduleFunction = { depvizApi(depLoader = fileTestLoader) }
        ) {
            val testCall: TestApplicationCall = handleRequest(method = HttpMethod.Get, uri = "/api")
            testCall.response.status() == HttpStatusCode.OK
        }
    }


    @Test
    internal fun `app endpoint gives app nodes`() {
        val fileTestLoader = FileTestLoader()
        withTestApplication(
            moduleFunction = { depvizApi(depLoader = fileTestLoader) }
        ) {
            val testCall: TestApplicationCall = handleRequest(method = HttpMethod.Get, uri = "/api/apps")
            Json.decodeFromString<Graph>(testCall.response.content!!)
            testCall.response.status() == HttpStatusCode.OK
        }
    }

    @Disabled
    @Test
    internal fun `team view`() {
        val fileTestLoader = FileTestLoader()
        withTestApplication(
            moduleFunction = { depvizApi(depLoader = fileTestLoader) }
        ) {
            val testCall: TestApplicationCall = handleRequest(method = HttpMethod.Get, uri = "/api/teams")
            testCall.response.status() == HttpStatusCode.OK
        }
    }

    @Test
    @Disabled
    internal fun `isready is ready`() = testApplication {
        application {
            depvizApi(depLoader = TestLoader())
        }
        val response = client.get("/internal/isready")
        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
    }


    @Test
    internal fun `next extension function returns the current date adjusted with timeofday when timeofday has not yet passed`() {
        val oslo = ZoneId.of("Europe/Oslo")
        val morningMarch31 = ZonedDateTime.of(LocalDateTime.of(2020, 1, 31, 8, 0), oslo)
        val midday = LocalTime.of(12, 0, 0)

        val noonMarch31 = ZonedDateTime.of(LocalDateTime.of(2020, 1, 31, 12, 0), oslo)
        assertThat(morningMarch31.next(midday)).isEqualTo(Date.from(noonMarch31.toInstant()))
    }


    @Test
    internal fun `next extension function returns the current date with the current timeofday when timeofday happens now`() {
        val oslo = ZoneId.of("Europe/Oslo")
        val morningMarch31 = ZonedDateTime.of(LocalDateTime.of(2020, 1, 31, 12, 0), oslo)
        val midday = LocalTime.of(12, 0, 0)

        val noonMarch31 = ZonedDateTime.of(LocalDateTime.of(2020, 1, 31, 12, 0), oslo)
        assertThat(morningMarch31.next(midday)).isEqualTo(Date.from(noonMarch31.toInstant()))
    }

    @Test
    internal fun `next extension function returns the next day adjusted with timeofday when timeofday already happened on this day`() {
        val oslo = ZoneId.of("Europe/Oslo")
        val eveningMarch31 = ZonedDateTime.of(LocalDateTime.of(2020, 1, 31, 18, 0), oslo)
        val midday = LocalTime.of(12, 0, 0)

        val noonFebruray1 = ZonedDateTime.of(LocalDateTime.of(2020, 2, 1, 12, 0), oslo)
        assertThat(eveningMarch31.next(midday)).isEqualTo(Date.from(noonFebruray1.toInstant()))
    }


}

class TestLoader : DepLoader {
    override fun getApplicationDependenciesFromBigquery(): List<ApplicationDependency> {
        return listOf(
            ApplicationDependency(
                cluster = "cluster",
                name = "name",
                team = "team",
                namespace = "namespace",
                image = "iamge",
                ingresses = mutableListOf("ingress1", "ingress2"),
                inboundApps = mutableListOf("app1", "app2"),
                outboundApps = mutableListOf(),
                outboundHosts = mutableListOf("www.vg.no"),
                readTopics = mutableListOf("topic1.x.y"),
                writeTopics = mutableListOf()
            )
        )
    }
}

class FileTestLoader : DepLoader {
    override fun getApplicationDependenciesFromBigquery(): List<ApplicationDependency> {
        val jsonString = javaClass.getResourceAsStream("/dataset.json")?.bufferedReader().use {
            it?.readText() ?: ""
        }
        return Json.decodeFromString(jsonString)
    }
}

