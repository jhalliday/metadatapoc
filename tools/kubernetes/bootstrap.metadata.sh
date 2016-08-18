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


# Clean up old template or pods that could be running from env.
echo "Removing and refreshing template"
oc delete all -l template=metadata-registry-api
oc delete template metadata-registry-api
oc create -f template.json

# Run processor and deploy the template:
echo "Processing template metadata registry INSTALLATION:"
oc process -f template.json    DATABASE_SERVICE_NAME=metaregistrydeployer |    oc create -f -

#Start the build so you get the latest image built from source:
oc start-build metadataregistryrestbc -n oshinko --follow

#Run the deployment config so you can get your pods deployed using the image from the buildstep
oc deploy metadata-application-web --latest
oc deploy metadataregistry --latest

#Watch logs as things progress and debug for errors.
oc logs -f dc/metadataregistry
