RUNNING_CONTAINER_NAME = "restaurant-api"
DOCKER_IMAGE = "joshuapangaribuan/restaurant-api:latest"
PORT = 8081

build-docker:
	mvn package
	docker build -t $(DOCKER_IMAGE) .

build-docker-multi:
	docker build -t $(DOCKER_IMAGE)  -f Dockerfile-Multi .

run-compose:
	mvn package
	docker-compose up --detach

run-docker:
	make build-docker
	docker network create --driver bridge $(RUNNING_CONTAINER_NAME)
	docker run --name minio \
		--detach \
		--publish 9000:9000 \
		--publish 9001:9001 \
		--network $(RUNNING_CONTAINER_NAME) \
		-e "MINIO_ROOT_PASSWORD=minio" \
		-e "MINIO_ROOT_USER=minio123" \
		bitnami/minio:latest
	docker run --name $(RUNNING_CONTAINER_NAME) \
		-e app.port=$(PORT) \
		-e minio.url=http://minio:9000 \
		-e minio.access-key=minio\
		-e minio.secret-key=minio123\
		-e minio.bucket-name=qr-codes\
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