FROM maven:3.9.5-eclipse-temurin-21-alpine AS java-build

WORKDIR /app/

COPY .mvn/ .mvn
COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src src
RUN mvn package
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

RUN jlink -v \
    --add-modules java.base,java.logging,java.xml,jdk.unsupported,java.sql,java.naming,java.desktop,java.management,java.security.jgss,java.instrument \
    --strip-debug \
    --compress 2 \
    --no-header-files \
    --no-man-pages \
    --vendor-version="i made this" \
    --output /customjre

FROM alpine:3.18.4
ENV JAVA_HOME /user/java/jdk21
ENV PATH ${JAVA_HOME}/bin:$PATH
COPY --from=java-build /customjre ${JAVA_HOME}

ARG DEPENDENCY=/app/target/dependency
COPY --from=java-build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=java-build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=java-build ${DEPENDENCY}/BOOT-INF/classes /app

ENV _JAVA_OPTIONS "-XX:MaxRAMPercentage=90 -Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true -Dfile.encoding=UTF-8"
ENTRYPOINT ["java","-cp","app:app/lib/*","com.galapea.techblog.springboot.onlinesurvey.SpringbootOnlineSurveyApplication"]