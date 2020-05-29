FROM openjdk:8-jdk-alpine

ARG DEPENDENCY=build/dependency
COPY ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY ${DEPENDENCY}/META-INF /app/META-INF
COPY ${DEPENDENCY}/BOOT-INF/classes /app

ENTRYPOINT exec java -XX:+PrintFlagsFinal -XX:+UseSerialGC $JAVA_OPTIONS -cp app:app/lib/* ru.bmstu.cp.rsoi.analyzer.Application
