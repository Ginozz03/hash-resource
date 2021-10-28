FROM adoptopenjdk/openjdk11:latest

RUN mkdir -p /software

ADD target/crittografia-0.0.1-SNAPSHOT.jar /software/crittografia-0.0.1-SNAPSHOT.jar

CMD java -Dserver.port=$PORT $JAVA_OPTS -jar /software/crittografia-0.0.1-SNAPSHOT.jar