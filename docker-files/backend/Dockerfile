FROM openjdk:11.0.7-jre-slim-buster

ENV HOST_POSTGRES=w_db
ENV PORT_POSTGRES=5432
ENV DB_POSTGRES=wedemy
ENV USERNAME_POSTGRES=postgres
ENV PASSWORD_POSTGRES=postgres

WORKDIR /app

COPY elearning-0.0.1-SNAPSHOT.jar .

CMD ["java", "-jar", "/app/elearning-0.0.1-SNAPSHOT.jar"]

