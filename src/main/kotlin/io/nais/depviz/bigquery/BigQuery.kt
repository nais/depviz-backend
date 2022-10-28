package io.nais.depviz.bigquery

import com.google.cloud.bigquery.*
import org.slf4j.LoggerFactory

class BigQuery {
    val table = "table"
    val dataset = "dataset"
    val project = "nais-analyse-prod-2dcc"


    private companion object {
        private val log = LoggerFactory.getLogger(BigQuery::class.java)
    }

    private val bigquery =
        BigQueryOptions.newBuilder()
            .setLocation("europe-north1")
            .setProjectId(project)
            .build()

}

