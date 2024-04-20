RUNNING_CONTAINER_NAME = "restaurant-api"
DOCKER_IMAGE = "restaurant-api:latest"
PORT = 8081

build-docker:
	mvn package
	docker build -t $(DOCKER_IMAGE) .

run-compose:
	mvn package -DskipTests=true
	docker-compose up --detach

run-docker:
	make build-docker
	docker network create --driver bridge $(RUNNING_CONTAINER_NAME)
	docker run --name $(RUNNING_CONTAINER_NAME) \
		-e app.port=$(PORT) \
		--detach \
		--publish $(PORT):$(PORT) \
		--network $(RUNNING_CONTAINER_NAME) \
 		$(DOCKER_IMAGE)

cleanup-docker:
	docker stop $(RUNNING_CONTAINER_NAME)
	docker system prune -fa
	docker volume prune -f
	docker network prune -f

cleanup-compose:
	docker-compose down
	docker system prune -fa
	docker volume prune -f
	docker network prune -f

PHONY: build-docker run-compose run-docker cleanup-docker cleanup-compose