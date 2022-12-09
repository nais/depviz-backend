package io.nais.depviz.bigquery

import com.google.cloud.bigquery.BigQueryOptions
import com.google.cloud.bigquery.JobId
import com.google.cloud.bigquery.JobInfo
import com.google.cloud.bigquery.QueryJobConfiguration
import io.nais.depviz.data.ApplicationDependency
import io.nais.depviz.data.ApplicationDependency.Companion.fromBq
import io.nais.depviz.DepLoader
import org.slf4j.LoggerFactory
import java.util.*


class BigQuery : DepLoader {
    val table = "dataproduct_apps_unique_v3"
    val dataset = "dataproduct_apps"
    val project = "nais-analyse-prod-2dcc"


    private companion object {
        private val log = LoggerFactory.getLogger(BigQuery::class.java)
    }

    private val bigquery =
        BigQueryOptions.newBuilder()
            .setLocation("europe-north1")
            .setProjectId(project)
            .build().service

    override fun getApplicationDependenciesFromBigquery(): List<ApplicationDependency> {
        val queryConfig = QueryJobConfiguration.newBuilder(
            """
                SELECT * FROM `aura-prod-d7e3.dataproduct_apps.dataproduct_apps_unique_v3` 
                WHERE dato = (SELECT MAX(dato) FROM `aura-prod-d7e3.dataproduct_apps.dataproduct_apps_unique_v3`)
                AND cluster in ("prod-gcp" ,"prod-fss" )
                ORDER BY dato DESC

            """.trimIndent()
        ).setUseLegacySql(false)
            .build()

        val jobId = JobId.of(UUID.randomUUID().toString())
        var queryJob = bigquery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build()).waitFor()

        if (queryJob == null) {
            throw RuntimeException("Job no longer exists");
        } else if (queryJob.getStatus().getError() != null) {
            throw RuntimeException(queryJob.getStatus().getError().toString());
        }

        return queryJob.getQueryResults().iterateAll().map { fromBq(it) }.toList()


    }

}

