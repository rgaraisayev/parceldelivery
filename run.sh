cd eureka-server && ./gradlew bootJar && cd ../notification-service && ./gradlew bootJar && cd ../auth-service && ./gradlew bootJar && cd ../apigw-service && ./gradlew bootJar && cd ../delivery-service && ./gradlew bootJar && docker compose build && docker compose stop && docker compose up -d



