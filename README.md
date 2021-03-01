### Wedemy App
This project is for Automated Testing (API and Web) purpose.

## Prerequisites
1. Apache Maven
2. NodeJS and NPM
3. Docker

## Build Backend
1. cd wedemy_backend
2. mvn clean package -DskipTests

## Build Frontend
1. cd wedemy_frontend
2. npm install
3. ng build --prod

## Build Docker
1. docker-compose up --no-start
2. docker start wedemy_pg wedemy_web (wait about 5 seconds then execute no.3)
3. docker start wedemy_api

## Running wedemy web
Open browser and navigate to http://localhost