FROM openjdk:17 AS build-env
ENV APP_HOME=/root/dev/hotel_booking_backend/
RUN mkdir -p $APP_HOME/src/main/java
WORKDIR $APP_HOME

# Copy all the files
COPY ./build.gradle ./gradlew ./gradlew.bat $APP_HOME
COPY gradle $APP_HOME/gradle
COPY ./src $APP_HOME/src/
RUN microdnf install findutils
# Build desirable JAR
RUN ./gradlew clean bootJar

FROM openjdk:17
WORKDIR /root/
COPY --from=build-env '/root/dev/hotel_booking_backend/build/libs/hotel_booking_backend.jar' '/hotel_booking_backend.jar'
CMD ["java","-jar","/hotel_booking_backend.jar"]