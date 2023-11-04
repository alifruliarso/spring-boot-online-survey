FROM maven:3.9.5-eclipse-temurin-21-alpine AS java-build

WORKDIR /app/

COPY .mvn/ .mvn
COPY pom.xml ./
RUN mvn dependency:go-offline

COPY src src
RUN mvn package
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:21-jre-alpine

COPY --from=java-build /etc/passwd /etc/shadow /etc/
ARG DEPENDENCY=/app/target/dependency
COPY --from=java-build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=java-build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=java-build ${DEPENDENCY}/BOOT-INF/classes /app

ENV _JAVA_OPTIONS "-XX:MaxRAMPercentage=90 -Djava.security.egd=file:/dev/./urandom -Djava.awt.headless=true -Dfile.encoding=UTF-8"
ENTRYPOINT ["java","-cp","app:app/lib/*","com.galapea.techblog.springboot.onlinesurvey.SpringbootOnlineSurveyApplication"]