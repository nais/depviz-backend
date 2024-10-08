FROM eclipse-temurin:17-alpine

COPY build/libs/*.jar /app/

ENV LOG_FORMAT="logstash"
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 \
               -XX:+HeapDumpOnOutOfMemoryError \
               -XX:HeapDumpPath=/oom-dump.hprof"

WORKDIR /app

CMD ["java", "-jar", "app.jar"]