package io.nais.depviz

import io.nais.depviz.data.ApplicationDependency

interface DepLoader {
    fun getApplicationDependenciesFromBigquery(): List<ApplicationDependency>
}
