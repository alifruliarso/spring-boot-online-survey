# Web-based Survey using Spring Boot and GridDB

> **Connect** with me through [Upwork](https://www.upwork.com/freelancers/~018d8a1d9dcab5ac61), [LinkedIn](https://linkedin.com/in/alifruliarso), [Email](mailto:alif.ruliarso@gmail.com), [Twitter](https://twitter.com/alifruliarso)

## Technology Stack
Spring Boot, Docker, Thymeleaf, Maven, chart.js\
Database: GridDB 5.1.0


## Run Application with Docker Compose

Build the docker image: 
```shell
docker compose build
```

Run the docker image: 

```shell
docker compose up
```

The website available at http://localhost:8080

- Re-build only the app image
  ```shell
  docker-compose build --no-cache survey-app
  ```

### For development supporting auto-reload

**Prerequisites**:

- [Eclipse Temurin](https://adoptium.net/temurin/releases/)
- [Docker](https://docs.docker.com/engine/install/)

**Build for local environment**
  ```shell
  docker compose -f .\docker-compose-dev.yml up
  ```
**Format code**
  ```shell
  mvn spotless:apply
   ```

### Example of API Payload see [TestAPI.http](TestAPI.http)

### GridDB Operations
- Exec into docker container
  ```shell
  $ gs_sh
  gs> setcluster clusterD dockerGridDB 239.0.0.1 31999 $node0
  gs> connect $clusterD
  ```

### Build smaller docker image using jlink

  ```shell
  docker-compose -f docker-compose-jlink.yml build survey-appjlink
  ```

- Check docker image
  
  ```shell
  docker run --entrypoint "ls"  8cd6d9a09cee -al /app
  docker run --entrypoint "du"  8cd6d9a09cee -sh /app
  docker run --entrypoint "ls"  8cd6d9a09cee -al /user/java/jdk21
  docker run --entrypoint "du"  8cd6d9a09cee -sh /user/java/jdk21
  docker run --entrypoint "java"  8cd6d9a09cee -version
  ```