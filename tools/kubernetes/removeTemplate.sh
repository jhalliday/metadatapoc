#!/bin/sh
echo "Removing and refreshing template"

oc delete all -l template=metadata-registry-api,openshift.io/deployment-config.name=metadataregistry
oc delete all -l openshift.io/deployment-config.name=metadataregistry
oc delete all -l template=metadata-registry-api
oc delete dc metadataregistry
oc delete template metadata-registry-api

oc create -f template.json
