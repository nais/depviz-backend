package io.nais.depviz.github

import org.kohsuke.github.GitHubBuilder

class Github {
    data class Repository(
        val language: String,
        val commitsLastYear: Int,
        val name: String,
        val orgName: String
    )

    private val github = GitHubBuilder().withOAuthToken("my_personal_token", "user_id_OR_org_name").build()

    fun repoData(): Map<String, Repository> {
        return github.myOrganizations.map { org ->
            org.value.repositories.entries.associate { repo ->
                repo.value.fullName to Repository(
                    language = repo.value.language,
                    commitsLastYear = repo.value.statistics.commitActivity.toList().sumOf { it.total },
                    name = repo.value.name,
                    orgName = org.value.name
                )
            }
        }.flatMap { it.values }.associateBy{ it.name }
    }
}