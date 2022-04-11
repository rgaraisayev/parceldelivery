# Parcel Delivery Apis
Parcel delivery application backend apis. Developed using microservice archtecture

To start all services execute run.sh

Gateway address: http://localhost:8504/

Delivery service swagger : http://localhost:8505/swagger-ui.html

Auth service swagger : http://localhost:8503/swagger-ui.html


# How it works

#### Api gateway
Stands in front and redirect connections to appropriate services

#### Eureka server
Used for service registery

#### RabbitMQ
Used for handling notification events


User story
<img width="1568" alt="image" src="https://user-images.githubusercontent.com/13315419/162677582-33459822-9428-4be0-bc51-aad9de1709a8.png">

Basic workflow

<img width="950" alt="image" src="https://user-images.githubusercontent.com/13315419/162677198-69dde2af-4bbe-4d34-8c95-0a5db14b4fdd.png">



Still needs some work to be done
