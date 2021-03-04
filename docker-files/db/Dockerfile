FROM postgres:13.2-alpine

ENV POSTGRES_PASSWORD=postgres
ENV POSTGRES_DB=wedemy

COPY wedemy.sql /docker-entrypoint-initdb.d
