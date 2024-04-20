# Restaurant API

## How To Run

### With Docker
1. Clone this repository `git clone https://github.com/JoshuaPangaribuan/restaurant-api.git`
2. Go to the project directory `cd restaurant-api`
3. Install Makefile on [Windows](https://stackoverflow.com/questions/2532234/how-to-run-a-makefile-in-windows) or [Linux](https://stackoverflow.com/questions/11934997/how-to-install-make-in-windows-7)
4. Run `make run-docker` to run the docker container
5. The application will be running on `http://localhost:8081`

### With Docker Compose
1. Clone this repository `git clone https://github.com/JoshuaPangaribuan/restaurant-api.git`
2. Go to the project directory `cd restaurant-api`
3. Install Docker Compose on [Windows](https://docs.docker.com/compose/install/) or [Linux](https://docs.docker.com/compose/install/)
4. Run `make run-compose` to run the project
5. The application will be running on `http://localhost:8081`
6. Validate the application by running `docker ps -a` and check if the container is running

## Dependency

| Nama                         | Deskripsi                                                                                                                 | Versi       | Official Documentation                                                                           |
|------------------------------|---------------------------------------------------------------------------------------------------------------------------|-------------|--------------------------------------------------------------------------------------------------|
| spring-boot-starter-data-jpa | Starter for using Spring Data JPA with Hibernate                                                                          | 3.2.4       | [docs](https://spring.io/projects/spring-boot)                                                   |
| spring-boot-starter-web      | Starter for building web, including RESTful, applications using Spring MVC. Uses Tomcat as the default embedded container | 3.2.4       | [docs](https://spring.io/projects/spring-boot)                                                   |
| flyway-core                  | Database migration tool                                                                                                   | -           | [docs](https://flywaydb.org/documentation/)                                                      |
| spring-boot-devtools         | Automatic restart and live reload during development                                                                      | 3.2.4       | [docs](https://docs.spring.io/spring-boot/docs/current/reference/html/using.html#using.devtools) |
| lombok                       | Lombok is a java library that automatically plugs into your editor and build tools, spicing up your java                  | -           | [docs](https://projectlombok.org/features/all)                                                   |
| spring-boot-starter-test     | Starter for testing Spring Boot applications with libraries including JUnit, Hamcrest and Mockito                         | 3.2.4       | [docs](https://spring.io/projects/spring-boot)                                                   |
| jakarta.validation-api       | Jakarta Bean Validation defines a metadata model and API for JavaBean validation                                          | 3.0.0       | [docs](https://beanvalidation.org/3.0/)                                                          |
| hibernate-validator          | Hibernate Validator, JSR 380 Reference Implementation                                                                     | 7.0.1.Final | [docs](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/)           |
| h2                           | H2 Database Engine                                                                                                        | -           | [docs](https://www.h2database.com/html/main.html)                                                |
| mockito-core                 | Most popular Mocking framework for unit tests written in Java                                                             | 3.12.4      | [docs](https://site.mockito.org/)                                                                |
| zxing-core                   | ZXing ("Zebra Crossing") barcode scanning library for Java, Android                                                       | 3.4.1       | [docs](https://zxing.github.io/zxing/)                                                           |
| zxing-javase                 | ZXing ("Zebra Crossing") barcode scanning library for Java, Android                                                       | 3.4.1       | [docs](https://zxing.github.io/zxing/)                                                           |
| minio                        | MinIO is a High Performance Object Storage, built for cloud applications and DevOps                                       | 8.3.0       | [docs](https://docs.min.io/)                                                                     |