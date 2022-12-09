package io.nais.depviz

import io.nais.depviz.data.ApplicationDependency
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*

internal class ApplicationDependencyTest {

    @Test
    fun testParseIngresses() {
        val listOfingresses =
            "[\"https://www-q1.nav.no/dagpenger/forskudd\",\"https://coronapenger-dialog-frontend.nais.oera-q.local\"]"
        val expected =
            listOf("https://www-q1.nav.no/dagpenger/forskudd", "https://coronapenger-dialog-frontend.nais.oera-q.local")


        assertEquals(expected, ApplicationDependency.getIngresses(listOfingresses))
    }
}