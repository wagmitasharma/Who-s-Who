FROM azul/zulu-openjdk:11
VOLUME /tmp
EXPOSE 8080
COPY build/libs/who-s-who-0.0.1-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dcom.sun.management.jmxremote", "-Dcom.sun.management.jmxremote.port=9999", "-Dcom.sun.management.jmxremote.rmi.port=9999", "-Dcom.sun.management.jmxremote.authenticate=false", "-Dcom.sun.management.jmxremote.ssl=false", "-Djava.rmi.server.hostname=127.0.0.1","-jar","app.jar"]
RUN chmod a+rwx /app.jar