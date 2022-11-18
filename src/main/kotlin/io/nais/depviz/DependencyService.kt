package io.nais.depviz

import io.nais.depviz.bigquery.BigQuery
import okhttp3.internal.immutableListOf
import org.slf4j.LoggerFactory
import kotlin.math.log


private val LOGGER = LoggerFactory.getLogger("DependencyService")

class DependencyService(private val depLoader: DepLoader) {

    var dependecyList: List<ApplicationDependency> = immutableListOf()

    fun dependecies(): List<ApplicationDependency> {
        return dependecyList
    }

    fun init() {
        dependecyList = depLoader.getApplicationDepenciesFromBigquery()
        LOGGER.info("read ${dependecyList.size} elements from bigquery")
    }
}