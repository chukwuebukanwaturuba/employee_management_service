# Build stage
FROM maven:3.8.7-openjdk-18 AS build
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM amazoncorretto:17
WORKDIR /app

# Copy the built jar
COPY --from=build /build/target/app.jar app.jar

EXPOSE 8080

# Environment variables
ENV DB_URL=jdbc:postgresql://db:5432/ebuka
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV ACTIVE_PROFILE=dev

# Run the application
CMD ["java", "-Dspring.profiles.active=${ACTIVE_PROFILE}", "-Dspring.datasource.url=${DB_URL}", "-Dspring.datasource.username=${SPRING_DATASOURCE_USERNAME}", "-Dspring.datasource.password=${SPRING_DATASOURCE_PASSWORD}", "-jar", "app.jar"]


## Build stage
#FROM maven:3.8.7-openjdk-18 AS build
#WORKDIR /build
#COPY pom.xml .
#RUN mvn dependency:go-offline
#COPY src ./src
#RUN mvn clean package -DskipTests
#
## Runtime stage
#FROM amazoncorretto:17
#ARG PROFILE=dev
#ARG APP_VERSION=1.0.1
#
#WORKDIR /app
#COPY --from=build /build/target/employee-management-system-*.jar /app/
#
## Extract the JAR version
#RUN APP_VERSION=$(ls /app | grep *.jar | awk 'NR==2{split($0,a,"-"); print a[3]}' | awk '{sub(/.jar$/,"")}1')\
#    && echo "Building container with BSN v-$version"
#EXPOSE 8080
#
#ENV DB_URL=jdbc:postgresql://localhost:5432/ebuka
#ENV MAILDEV_URL=localhost
#
#ENV ACTIVE_PROFILE=${PROFILE}
#ENV JAR_VERSION=${APP_VERSION}
#
#CMD java -jar -Dspring.profiles.active=${ACTIVE_PROFILE} -Dspring.datasource.url=${DB_URL}  employee-management-system-${JAR_VERSION}.jar