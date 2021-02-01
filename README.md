# auction-with-microservices

A demo project for implementing microservices. Using spring and 
kafka.

## Setup Steps

Assuming there is an existing docker engine.
### Kafka docker:
```shell
curl -sSL https://raw.githubusercontent.com/bitnami/bitnami-docker-kafka/master/docker-compose.yml > docker-compose.yml
sudo docker-compose up -d
```
### MariaDB docker:
```shell
sudo docker run --name microservice-mariadb -e MYSQL_ROOT_PASSWORD=root -p 3306:3306 -d mariadb:latest
sudo docker exec -it microservice-mariadb mysql -uroot -p
```
### To compile the services:
auctionservice, inventoryservice, paymentservice, userservice and zookeeper
projects must be compiled using `mvn install` in their respective
root folders. Note that they are separate projects, not modules
on a single root project.

## To run the system:
Execute:
```shell
sh start_services.sh
```
Service parameters can be configured from the above script

## To observe service statuses:
Visit:
http://localhost:9999/api/instances

## Usage:
Request examples can be found as a Postman collection at
`CENG555.postman_collection.json`

## To stop the system:
Execute:
```shell
sh stop_services.sh
```
