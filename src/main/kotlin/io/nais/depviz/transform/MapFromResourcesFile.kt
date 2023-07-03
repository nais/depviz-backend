package io.nais.depviz.transform

import org.slf4j.LoggerFactory
import java.io.File


class MapFromResourcesFile(
    val delimiter: String = " ",
    val location: String = "src/main/resources/loc"
) {
    private val LOGGER = LoggerFactory.getLogger("MapFromResourcesFile")

    fun readAndParseWithDelimiter(): Map<String, Int> {
        return File(location)
            .walkTopDown()
            .filterNot { it.isDirectory }
            .map { parseToIntValues(it.absoluteFile) }
            .flatMap { it.asSequence() }
            .associateBy({ it.key }, { it.value })
    }

    fun parseToIntValues(file: File): Map<String, Int> {
        val lines = file.readLines()
        return lines.associate {
            pair(it)
        }
    }

    private fun pair(line: String): Pair<String, Int> {
        val (key, value) = line.split(delimiter)
        return try {
            key to value.toInt()
        } catch (e: RuntimeException) {
            LOGGER.error("Error parsing" + key + " to intvalue for " + value)
            key to 0
        }
    }
}