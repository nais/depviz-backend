#!/usr/bin/env bash

function getRepos() {
    gh api \
      -H "Accept: application/vnd.github+json" \
      -H "X-GitHub-Api-Version: 2022-11-28" \
      /orgs/nais/repos --paginate | jq '.[].full_name' | tr -d '"' > repos.txt
}

function clone() {
  Lines=$(cat repos.txt)
  for Repo in $Lines
  do
    gh repo clone $Repo tmprepo -- --depth 1
    cd tmprepo
    (echo -n "$Repo " &&
    git ls-files | xargs wc -l | tail -1 | awk '{print $1;}') >> ../loc.txt
    cd ..
    rm -rf tmprepo
    printf "('$Repo is deleted automatically)\n\n\n"
  done
}