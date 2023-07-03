package io.nais.depviz.transform

import java.io.File

class MapFromResourcesFile(fileName: String, val delimiter: String = " ") {
    private val lines = File("src/main/resources", fileName).readLines()

    fun parseToIntValues(): Map<String, Int> = lines.associate {
        val (key, value) = it.split(delimiter)
        key to value.toInt()
    }
}