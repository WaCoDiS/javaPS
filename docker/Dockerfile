FROM alpine/git as clone
WORKDIR /app
RUN echo "invalidate the cache with another echo string"
RUN git clone https://github.com/WaCoDiS/javaPS.git javaps \
    && git clone https://github.com/52North/javaps-iohandler.git javaps/javaps-iohandler \
	&& git clone https://github.com/WaCoDiS/javaps-wacodis-backend.git wacodis-backend \
	&& git -C ./javaps/javaps-iohandler checkout develop \
	&& git -C ./javaps checkout develop \
	&& git -C ./wacodis-backend checkout master
	
FROM maven:3.5-jdk-8-alpine as build 
WORKDIR /app
COPY --from=clone /app/javaps /app/javaps
COPY --from=clone /app/wacodis-backend /app/wacodis-backend
RUN mvn -f ./javaps/pom.xml clean install -DskipTests -pl !webapp
RUN mvn -f ./javaps/javaps-iohandler/pom.xml clean install
RUN mvn -f ./wacodis-backend/pom.xml clean install -DskipTests
RUN mvn -f ./javaps/pom.xml install -DskipTests

FROM tomcat:9-jre8-slim
ARG WAR_FILE=wacodis-wps.war
ARG TARGET_DIR=wacodis-wps/

WORKDIR /app
COPY --from=build /app/javaps/webapp/target/${WAR_FILE} $CATALINA_HOME/webapps/
# extract WAR file
RUN cd ${CATALINA_HOME}/webapps/ && \
    unzip ${WAR_FILE} -d ${TARGET_DIR} && \
    rm 	${WAR_FILE}
# overwrite log config
COPY ./log4j2_docker.xml ${CATALINA_HOME}/webapps/${TARGET_DIR}WEB-INF/classes/log4j2.xml

ENV JAVA_OPTS="-Xmx4g -Xms2g"
