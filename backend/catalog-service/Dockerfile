FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /build

COPY pom.xml ./
COPY .mvn/ .mvn
COPY mvnw ./

COPY api-gateway/pom.xml ./api-gateway/
COPY catalog-service/pom.xml ./catalog-service/
COPY order-service/pom.xml ./order-service/
COPY order-service-worker/pom.xml ./order-service-worker/
COPY user-service/pom.xml ./user-service/
COPY warehouse-service/pom.xml ./warehouse-service/
RUN --mount=type=cache,target=/root/.m2 ./mvnw dependency:go-offline -B -pl catalog-service -am

COPY catalog-service/src ./catalog-service/src

RUN --mount=type=cache,target=/root/.m2 ./mvnw clean package -DskipTests -pl catalog-service -am
RUN java -Djarmode=tools -jar catalog-service/target/*-exec.jar extract --layers --launcher --destination extracted_layers

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

COPY --from=builder /build/extracted_layers/dependencies/ ./
COPY --from=builder /build/extracted_layers/spring-boot-loader/ ./
COPY --from=builder /build/extracted_layers/snapshot-dependencies/ ./
COPY --from=builder /build/extracted_layers/application/ ./

ENV JAVA_OPTS="-XX:+UseG1GC -XX:MaxRAMPercentage=75.0"

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher" ]