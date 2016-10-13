.PHONY : build-mvn build-ui build-rest-api

build-mvn:
		mvn clean install
build-ui:
		./metadata-registry-web-ui/build.container.sh
build-rest-api:
		./jaxrs/build.container.sh
build-ui:
		./metadata-registry-web-ui/build.container.sh
push-ui:
		./metadata-registry-web-ui/docker.io.push.sh
push-rest-api:
		./jaxrs/docker.io.push.sh
