#!/usr/bin/env bash

function getRepos() {
  gh api \
    -H "Accept: application/vnd.github+json" \
    -H "X-GitHub-Api-Version: 2022-11-28" \
    /orgs/navikt/repos --paginate | jq '.[].full_name' | tr -d '"' >repos.txt
  no_of_repos=$(cat repos.txt | wc -l)
  echo "$no_of_repos repos in org"
}

function cleanRepoFile() {
  cat loc.txt | awk '{print $1;}' >loc2.txt
  comm -23 <(sort repos.txt) <(sort loc2.txt) >repos_clean.txt
}

function clone() {
  Lines=$(cat repos_clean.txt)
  for Repo in $Lines; do
    gh repo clone $Repo tmprepo  -- --depth 1
    cd tmprepo
    loc=$(scc -i bash,go,groovy,java,js,jsp,ipynb,kt,pl,py,rb,sh,sql,tf,ts,vue,rs.jsx -f json --no-size --no-cocomo -d | jq '.[].Lines' |  awk '{ sum += $1 } END { print sum }')
    echo  "$Repo $loc"
    (echo  "$Repo $loc")>>../loc.txt
    cd ..
    rm -rf tmprepo
    no_of_repos=$(cat repos.txt | wc -l)
    repos_done=$(cat loc.txt | wc -l)
    echo "('$repos_done / $no_of_repos - $Repo is deleted automatically)\n\n\n"
  done
}

getRepos
cleanRepoFile
clone
