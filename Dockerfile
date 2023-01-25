FROM openjdk:17
MAINTAINER christosderetzis
COPY build/libs/hotel_booking_backend.jar hotel_booking_backend.jar
ENTRYPOINT ["java", "-jar", "/hotel_booking_backend.jar"]