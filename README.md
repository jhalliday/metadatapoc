# Metadata Registry

This repository contains submodules so you will need to execute:

```
git clone --recursive https://github.com/zmhassan/metadatapoc
```

or

```
git clone https://github.com/zmhassan/metadatapoc
git sub module init
```

## Environment Variables:

### `META_LOG_LOCATION`
* This is required by the metadata-consumer app as it will be looking in this location for data
* Usage example:
```bash

META_LOG_LOCATION=logs/analytics.log

```


### `META_SCHEMA_LOCATION`
* This is required by the metadata-producer app as it will be looking in this location for data
* Usage example:
```bash

META_SCHEMA_LOCATION=/tmp/schema.json

```


### `META_KAFKA_BROKERLIST`
* This is required by the metadata-producer app as it will be looking in this location for data
* Usage example:
```bash

META_KAFKA_BROKERLIST="localhost:9092"

```

### `DOCKER_HOST`
* Used for running producer and consumer in docker containers
* This is required for development only
* Usage example:
```bash

DOCKER_HOST=tcp://<host>:2375

```



## Building from source

```
mvn clean install -Prelease
cd assembly/target
tar -zxvf <NAME>.tar.gz # or unzip ./assembly/target/metadata-distro-assembly-1.0.0-SNAPSHOT-bin.zip

```

## Usage example:

### Metadata consumer or producer

```bash

    cd assembly/target/
    java -jar metadata-consumer-1.0.0-SNAPSHOT.jar
    java -jar metadata-producer-1.0.0-SNAPSHOT.jar


```


## Docker

```

```

## PAAS: Kubernetes/Openshift v3

```

```
