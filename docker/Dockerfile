FROM alpine/git as clone
WORKDIR /app
RUN echo hallo2
RUN git clone https://github.com/WaCoDiS/javaPS.git javaps \
    && git clone https://github.com/52North/javaps-iohandler.git javaps/javaps-iohandler \
	&& git clone https://github.com/WaCoDiS/javaps-wacodis-backend.git wacodis-backend \
	&& git -C ./javaps/javaps-iohandler checkout develop \
	&& git -C ./javaps checkout wacodis/eo-hackathon \
	&& git -C ./wacodis-backend checkout feature/land-classification
	
FROM maven:3.5-jdk-8-alpine as build 
WORKDIR /app
COPY --from=clone /app/javaps /app/javaps
COPY --from=clone /app/wacodis-backend /app/wacodis-backend
RUN mvn -f ./javaps/pom.xml clean install -DskipTests -pl !webapp
RUN mvn -f ./javaps/javaps-iohandler/pom.xml clean install
RUN mvn -f ./wacodis-backend/pom.xml clean install -DskipTests
RUN mvn -f ./javaps/pom.xml install -DskipTests

FROM tomcat:9-jre8-slim
WORKDIR /app
COPY --from=build /app/javaps/webapp/target/wacodis-wps.war $CATALINA_HOME/webapps/
ENV JAVA_OPTS="-Xmx4g -Xms2g"