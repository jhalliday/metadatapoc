.PHONY : build image push

build:
	mvn clean install
image:
	./build.container.sh
push:
	./push.docker-registry.sh
