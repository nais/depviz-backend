#!/usr/bin/env bash

function getRepos() {
  gh api \
    -H "Accept: application/vnd.github+json" \
    -H "X-GitHub-Api-Version: 2022-11-28" \
    /orgs/navikt/repos --paginate | jq '.[].full_name' | tr -d '"' >repos.txt
}

function cleanRepoFile() {
  cat loc.txt | awk '{print $1;}' >loc2.txt
  comm -23 <(sort repos.txt) <(sort loc2.txt) >repos_clean.txt
}

function clone() {
  Lines=$(cat repos_clean.txt)
  for Repo in $Lines; do
    gh repo clone $Repo tmprepo -- --depth 1
    cd tmprepo
    (echo -n "$Repo " &&
      git ls-files | xargs wc -l | tail -1 | awk '{print $1;}') && echo >>../loc.txt
    cd ..
    rm -rf tmprepo
    printf "('$Repo is deleted automatically)\n\n\n"
  done
}

getRepos
cleanRepoFile
clone
