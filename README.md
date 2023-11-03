# Blood Pressure Diary

I just want to try to add data and plot it without a complicated setup. Keeping my blood pressure under control.

## Development Quick Start

1. Copy `applicaton.properties` to a `application-personal.properties` file.
2. Change some things, I recommend something like this:
   ```
   # dev-mode disables OAuth2, enables Form login (developer/developer) and inserts data for this user.
   # HINT: use this with an H2 database configured in spring.datasource properties
   app.dev-mode=true
   
   spring.h2.console.enabled=true
   
   # H2
   spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
   spring.datasource.username=sa
   
   # Postgres
   #spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
   #spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   #spring.datasource.username=postgres
   #spring.datasource.password=password
   #spring.datasource.driver-class-name=org.postgresql.Driver
   
   spring.jpa.hibernate.ddl-auto=update
   
   spring.datasource.hikari.maximum-pool-size=6
   
   spring.security.oauth2.client.registration.google.client-id=123
   spring.security.oauth2.client.registration.google.client-secret=123
   ```
3. Provide a postgres database that matches the configuration on the previous step. I recommend using Docker.

4. Run with: 
   ```
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=personal`
   ```

> _NOTE_: You can also configure your IDE to set the `personal` profile.   

4. Go to `http://localhost:8080` and login with user `developer` and password `developer`.

## Docker Compose deployment

To easy deployment I added a `Dockerfile` and a `docker-compose.yml` files. Checkout the repo, create a `myenv.env` file and then run:

```env
server.port=9095
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres
spring.datasource.password=<password>
spring.security.oauth2.client.registration.google.client-id=<client-id-from-google>
spring.security.oauth2.client.registration.google.client-secret=<client-secret-from-google>
```

```shell
docker-compose up -d
```

