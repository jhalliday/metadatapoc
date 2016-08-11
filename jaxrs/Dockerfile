FROM docker.io/anapsix/docker-oracle-java8

MAINTAINER Zak Hassan zak.hassan1010@gmail.com

WORKDIR /opt
RUN apt-get update
RUN apt-get install -y curl wget unzip

RUN curl -O -J -L  http://download.jboss.org/wildfly/10.0.0.Final/wildfly-10.0.0.Final.zip && unzip wildfly-10.0.0.Final.zip -d /opt && mkdir -p /opt/hibernate_ogm_wildfly10-5.0.1
RUN curl -O -J -L   https://sourceforge.net/projects/hibernate/files/hibernate-ogm/5.0.1.Final/hibernate-ogm-modules-wildfly10-5.0.1.Final.zip/download && unzip hibernate-ogm-modules-wildfly10-5.0.1.Final.zip -d /opt/hibernate_ogm_wildfly10-5.0.1

ENV JBOSS_HOME  /opt/wildfly-10.0.0.Final
ENV JBOSS_MODULEPATH  $JBOSS_HOME/modules:/opt/hibernate_ogm_wildfly10-5.0.1
ENV MONGODB_HOST_IP 192.168.99.100

COPY  target/jaxrs-1.0-SNAPSHOT.war  /opt/jaxrs-1.0-SNAPSHOT.war
COPY  src/main/docker/batchcommand.txt  /opt/batchcommand.txt
COPY  src/main/docker/entrypoint.sh    /etc/entrypoint.sh

EXPOSE 18080 9990 9999 4447 5455 7500 45700 45688 55200 54200 23364

ENTRYPOINT ["/etc/entrypoint.sh"]
