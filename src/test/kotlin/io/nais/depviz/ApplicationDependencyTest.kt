package io.nais.depviz

import io.nais.depviz.bigquery.ApplicationDependency.Companion.getIngresses
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

internal class ApplicationDependencyTest {

    @Test
    fun testParseIngresses() {
        val listOfingresses =
            "[\"https://www-q1.nav.no/dagpenger/forskudd\",\"https://coronapenger-dialog-frontend.nais.oera-q.local\"]"
        val expected =
            listOf("https://www-q1.nav.no/dagpenger/forskudd", "https://coronapenger-dialog-frontend.nais.oera-q.local")


        assertEquals(expected, listOfingresses.getIngresses())
    }
}