package io.nais.depviz

class DependencyService() {

    val dependecyList: MutableList<ApplicationDependency> = mutableListOf()

    fun dependecies(): List<ApplicationDependency> {
        return dependecyList
    }

    fun add(app: ApplicationDependency) {
        dependecyList.add(app)
    }

}