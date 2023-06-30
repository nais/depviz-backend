package io.nais.depviz.github

import org.kohsuke.github.GitHubBuilder

class Github {
    data class Repository(
        val language: String,
        val commitsLastYear: Int,
        val name: String,
        val orgName: String
    )

    private val orgs = listOf<String>( "nais")

    private val github = GitHubBuilder().withOAuthToken(
        "PAT", "audunstrand"
    ).build()

    fun repoData(): Map<String, Repository> {
        return github.myOrganizations.filter { orgs.contains(it.key) }.map { org ->
            println(org.value.name)
            org.value.repositories.entries.associate { repo ->
                println(repo.value.fullName)
                repo.value.fullName to Repository(
                    language = repo.value.language.orEmpty(),
                    commitsLastYear = repo.value.statistics.commitActivity.toList().sumOf { it.total },
                    name = repo.value.name,
                    orgName = org.value.name
                )
            }
        }.flatMap { it.values }.associateBy { it.name }
    }
}