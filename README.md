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
<img width="1572" alt="image" src="https://user-images.githubusercontent.com/13315419/162679038-34e0768c-051b-445d-8cb6-ce07d9633a3d.png">

Basic workflow

<img width="929" alt="image" src="https://user-images.githubusercontent.com/13315419/162678988-b05f0435-7004-4e88-a38a-4baec8714e25.png">



Still needs some work to be done
