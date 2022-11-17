package io.nais.depviz

interface DepLoader {
    fun getApplicationDepenciesFromBigquery(): List<ApplicationDependency>
}
