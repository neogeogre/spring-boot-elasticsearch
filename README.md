## Spring Data Elasticsearch Example

Demo to show a spring boot application wrtitten in kotlin with spring-data-elasticsearch

### Java setup

Install `sdkman`
To autoload the `.sdkmanrc`:
in `$HOME/.sdkman/etc/config` set `sdkman_auto_env=true`

### How to run

Start the Elasticsearch `docker-compose` with:

```bash
$ docker-compose up -d
``` 

Then you can run the application with:
```bash
./gradlew bootRun
```

Once everything is up and running open the browser and go to `localhost:8080`. You should see Swagger to interact with.

### How to test

The integration tests rely on [testcontainers](https://www.testcontainers.org/), run with:
```bash
./gradlew clean build
```

