#!/bin/sh
VERSION="1.0.0-SNAPSHOT"
AUTHOR="Zak Hassan <zak.hassan@redhat.com>"


echo   " Building ..."
docker   build  --rm -t  redhat/docker-metadata-registry-ws  .
echo   " Done .."
