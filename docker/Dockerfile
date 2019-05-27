FROM alpine/git as clone
WORKDIR /app
RUN git clone https://github.com/WaCoDiS/javaPS.git javaps \
	&& git clone https://github.com/WaCoDiS/javaps-wacodis-backend.git wacodis-backend \
	&& git clone https://github.com/52North/javaps-iohandler.git javaps-iohandler \
	&& git -C ./javaps checkout wacodis/eo-hackathon \
	&& git -C ./wacodis-backend checkout feature/land-classification
	
FROM maven:3.5-jdk-8-alpine as build 
WORKDIR /app
COPY --from=clone /app/javaps /app/javaps
COPY --from=clone /app/wacodis-backend /app/wacodis-backend
COPY --from=clone /app/javaps-iohandler /app/javaps-iohandler
RUN mvn -f ./javaps/pom.xml clean install -DskipTests -pl !webapp\
	&& mvn -f ./javaps-iohandler/pom.xml clean install \
	&& mvn -f ./wacodis-backend/pom.xml clean install -DskipTests \
	&& mvn -f ./javaps/pom.xml clean install -DskipTests -pl webapp

FROM tomcat:9-jre8-slim
WORKDIR /app
COPY --from=build /app/javaps/webapp/target/wacodis-wps.war $CATALINA_HOME/webapps/
ENV JAVA_OPTS="-Xmx4g -Xms2g"