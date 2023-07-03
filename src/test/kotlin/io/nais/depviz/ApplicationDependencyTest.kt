package io.nais.depviz

import io.ktor.http.*
import io.nais.depviz.bigquery.ApplicationDependency.Companion.getIngresses
import io.nais.depviz.bigquery.ApplicationDependency.Companion.toRepo
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

    @Test
    fun testParseUrl(){
        val actionurl = "https://github.com/navikt/aap-kalkulator-frontend/actions/runs/5331278608"
        assertEquals("navikt/aap-kalkulator-frontend", actionurl.toRepo())
    }

    @Test
    fun testEmptyStringAsUrl(){
        val actionurl = ""
        assertEquals("", actionurl.toRepo())
    }


    @Test
    fun testMalformedUrl(){
        val actionurl = "http://dette er jo bare fjas/1/2"
        assertEquals("", actionurl.toRepo())
    }
}
