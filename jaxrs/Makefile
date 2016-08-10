.PHONY : build image

build:
	mvn clean install
image:
		docker   build  --rm -t  redhat/docker-metadata-registry-ws  .
