package io.nais.depviz.github

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GithubTest {

    @Test
    fun `can read organisations`() {
        val github = Github()
        assertEquals(3, github.repoData())

    }
}