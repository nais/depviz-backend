package io.nais.depviz

import io.nais.depviz.bigquery.BigQuery
import okhttp3.internal.immutableListOf

class DependencyService(private val depLoader: DepLoader) {

    var dependecyList: List<ApplicationDependency> = immutableListOf()

    fun dependecies(): List<ApplicationDependency> {
        return dependecyList
    }

    fun init() {
        dependecyList = depLoader.getApplicationDepenciesFromBigquery()
    }
}