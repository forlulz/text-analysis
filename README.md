# Text-analysis

## Requirements
- JDK 17 or greater
- [TextRazor](https://www.textrazor.com/console) API key
- Docker (optional)


## Running unit tests

```console
export JAVA_HOME=/path/to/jdk
./mvnw clean test
```


## Running unit and integration tests


```console
export JAVA_HOME="/path/to/jdk"
export TEXTRAZOR_API_KEY="your TextRazor API key"
./mvnw clean verify
```


## Building Docker image

- using Docker

```console
docker build -t your/docker-image-name .
```

- using Maven

```console
./mvnw spring-boot:build-image -Dspring-boot.build-image.imageName=your/docker-image-name
```

## Running app

- using docker
```console
docker run \
  -p 8080:8080 \
  --env TEXTRAZOR_API_KEY="your TextRazor API key" \
  your/docker-image-name
```

- using Maven

```console
export JAVA_HOME="/path/to/jdk"
export TEXTRAZOR_API_KEY="your TextRazor API key"
./mvnw spring-boot:run
```

- running jar file

```console
export TEXTRAZOR_API_KEY="your TextRazor API key"
java -jar /path/to/jar-file.jar
```


## Usage

- POST contents from a text file to `/api/analysis/persons` route
- [jq](https://stedolan.github.io/jq/download/)

```console
curl -sF "text=$(cat /path/to/text-file.txt)" localhost:8080/api/analysis/persons | jq
```
