# java-microservice-mongo-jokes
Test microservice retrieving jokes from external source and saving them into MongoDB

## Building and running the application

### 1. Prepare the docker image for the microservice (skip if using the project default repository: chrisc4343)

a) Build image locally using spring-boot, from the top level of the project (containing pom.xml):

**mvn spring-boot:build-image -Ddocker.repository=...**

For example, **mvn spring-boot:build-image -Ddocker.repository=chrisc4343** will generate the image: **chrisc4343/jokes-crisc:v1**

b) Push the newly created image to the docker repository:

**docker image push docker.io/[docker.repository]/jokes-crisc:v1**

Please replace **[docker.repository]** with the docker repository to be used. Example: **docker image push docker.io/chrisc4343/jokes-crisc:v1** 


### 2. Launch docker application:

All the operations should be executed from the top level of the repository, where the "docker-compose.yml" and ".env" files are located.

a) Inside the ".env" file, update the DOCKER_REPOSITORY variable with the docker repository name used when generating the image.

Example: **DOCKER_REPOSITORY=chrisc4343**

b) start the microservice and the MongoDB container:

**docker compose up**

### 3. Testing the application

From the browser, please launch requests following the pattern: **http://localhost:8080/jokes?count=...**

As requested, the count query param is optional

### 4. Shutting down the application

To shut down the application, we should use:

**docker compose down**

### 5. Observations:

a) MongoDB specific configurations are specified inside the ".env" file.

b) There is a "docker-compose-local" file used to set up the infrastructure (MongoDB container) to facilitate testing. 

