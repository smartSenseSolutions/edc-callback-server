FROM gradle:7.6.4-jdk17-alpine AS build
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle build --no-daemon -i -x test -x javadoc

FROM eclipse-temurin:17-jre-alpine
RUN mkdir /home/app
COPY --from=build /home/gradle/src/build/libs/wrapper-0.0.1-SNAPSHOT.jar /home/app
WORKDIR /home/app
RUN apk add --update \
    curl \
    && rm -rf /var/cache/apk/*
CMD java -Djava.security.egd=file:/dev/./urandom $MEMORY_LIMIT -jar wrapper-0.0.1-SNAPSHOT.jar