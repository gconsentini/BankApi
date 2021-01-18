FROM gradle:6.4.1-jdk8 as builder

USER root

ENV APP_DIR /app
WORKDIR $APP_DIR

COPY build.gradle.kts $APP_DIR/
COPY settings.gradle.kts $APP_DIR/

## --- Force download and cache of gradle dependencies and save it to /gradle-cache folder
RUN gradle dependencies
RUN mkdir -p /app/src/main/kotlin/
RUN echo 'package build\nclass DummyApp\nfun main(){}\n' >> /app/src/main/kotlin/DummyApp.kt
RUN gradle -g /gradle-cache compileKotlin --no-daemon
RUN rm src/main/kotlin/DummyApp.kt
## ---

COPY . $APP_DIR

RUN gradle -g /gradle-cache build --no-daemon -x test


EXPOSE 8089

# -----------------------------------------------------------------------------

FROM openjdk:12-alpine3.9

ARG SERVER_PORT=8089

ENV JAVA_OPTS="-server -XX:InitialRAMPercentage=80.0 -XX:MaxRAMPercentage=80.0 -XX:+HeapDumpOnOutOfMemoryError"
RUN apk add --no-cache tini

WORKDIR /app

COPY --from=builder /app/entrypoint.sh /app
COPY --from=builder /app/build/libs/*.jar /app/

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["tini", "-s", "--", "sh", "entrypoint.sh"]
