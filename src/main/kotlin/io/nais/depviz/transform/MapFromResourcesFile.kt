package io.nais.depviz.transform

import org.slf4j.LoggerFactory


class MapFromResourcesFile(
    val delimiter: String = " ",
    val location: String = "/loc/"
) {
    private val LOGGER = LoggerFactory.getLogger("MapFromResourcesFile")

    fun readAndParseWithDelimiter(): Map<String, Int> =
        (readLinesFrom("nais.txt") + readLinesFrom("navikt.txt")).associate { pair(it) }

    private fun readLinesFrom(filename: String) =
        object {}.javaClass.getResourceAsStream("$location/$filename")!!.bufferedReader().readLines()

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