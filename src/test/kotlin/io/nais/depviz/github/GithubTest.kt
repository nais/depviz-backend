package io.nais.depviz.github

import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class GithubTest {

    @Disabled
    @Test
    fun `can read organisations`() {
        val github = Github()
        assertEquals(3, github.repoData())

    }
}