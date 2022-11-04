package io.nais.depviz

class DependencyService() {

    val dependecyList: MutableList<ApplicationDependency> = mutableListOf()

    private val deps = dependecyList
    fun dependecies(): ApplicationList {
        return ApplicationList(deps)
    }

    fun add(app: ApplicationDependency) {
        deps.add(app)
    }

}