#!/bin/sh
VERSION="1.0.0-SNAPSHOT"
AUTHOR="Zak Hassan <zak.hassan@redhat.com>"


echo "OPENSHIFT TEMPLATE DEPLOYER"
echo "    __  __________________    ____  ___  _________       ____  _______________________________ __  __"
echo "   /  |/  / ____/_  __/   |  / __ \/   |/_  __/   |     / __ \/ ____/ ____/  _/ ___/_  __/ __ \\ \/ /"
echo "  / /|_/ / __/   / / / /| | / / / / /| | / / / /| |    / /_/ / __/ / / __ / / \__ \ / / / /_/ / \  / "
echo " / /  / / /___  / / / ___ |/ /_/ / ___ |/ / / ___ |   / _, _/ /___/ /_/ // / ___/ // / / _, _/  / /  "
echo "/_/  /_/_____/ /_/ /_/  |_/_____/_/  |_/_/ /_/  |_|  /_/ |_/_____/\____/___//____//_/ /_/ |_|  /_/   "
echo " "

TEMPLATE=metadata-registry-api


# Clean up old template or pods that could be running from env.
echo "Removing and refreshing template"
oc deploy $TEMPLATE --cancel
oc delete all -l template=metadata-registry-api,openshift.io/deployment-config.name=metaregistry
oc delete dc $TEMPLATE
oc delete template $TEMPLATE
oc create -f metadata-template.json


# Pull Docker Images required by template:
# SET MINISHIFT ENVIRONMENT VARIABLES:
# eval $(minishift docker-env)
# export DOCKER_API_VERSION=1.23
# docker pull docker.io/metadatapoc/metadata-registry-web-ui
# docker pull docker.io/bzcareer/docker-kafka
# docker pull docker.io/bzcareer/docker-zookeeper
# docker pull docker.io/metadatapoc/metadata-registry-rest
# docker pull docker.io/openshift/mongodb-24-centos7 metadata-registry-api



# Run processor and deploy the template:
echo "Processing template metadata registry INSTALLATION:"
oc process -f metadata-template.json   DATABASE_SERVICE_NAME=metaregistry |    oc create -f -

#Skipping the build for now. Start the build so you get the latest image built from source:
#oc start-build metadataregistryrestbc -n oshinko --follow

#Run the deployment config so you can get your pods deployed using the image from the buildstep
oc deploy  metaregistry --latest


#Watch logs as things progress and debug for errors.
oc logs -f dc/metaregistry
