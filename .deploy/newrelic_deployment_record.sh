#!/bin/sh

NEW_RELIC_APP_ID=$1
NEW_RELIC_API_KEY=$2
VERSION=$3
CI_COMMIT_MESSAGE=$4
GITLAB_USER_EMAIL=$5

curl -s -X POST "https://api.newrelic.com/v2/applications/$NEW_RELIC_APP_ID/deployments.json" -H "X-Api-Key:$NEW_RELIC_API_KEY" -i -H "Content-Type: application/json" -d "{\"deployment\": {\"revision\": \"$VERSION\",\"changelog\": \"${CI_COMMIT_MESSAGE//[$'\r\n']}\",\"user\": \"$GITLAB_USER_EMAIL\"}}"