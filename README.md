## Description

REST API to handle Favorite artist entities (search, create) and provide ability to find top5 albums of favorite using itunes api.

Postman Collection for playing with API can be found here https://www.postman.com/donatasdaubar/zedge-homework

## Design Explanation
Project is split into three packages artist, itunes and config.

Itunes package handles calls to itunes api. Calls are
cached when needed. To increase data consistency we allow recalling itunes api till certain threshold. When threshold is 
reached cache is used if results for artist is available. To minimise itunes api calls we could check cached record lifespan
i.e. only refresh cache entries that were present for n+ minutes.

Artist package handles rest api for artists and provide services and repositories to provide needed functionality.

## Application Startup Instructions

### Running Application
```bash
# Install dependencies
$ ./mvnw install
# Run springboot
$ ./mvnw spring-boot:run
```

### Test
```bash
# Run tests
$ ./mvnw test
```