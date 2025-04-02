# Bookstore - Backend

This application expose some endpoints about the Bookstore,
where we can work with book, user and purchase.

## Architecture

The application uses a hexagonal architecture, where we have the application and adapters layer.
The application layer is responsible for the business rules and the domains.
The adapters layer is separated in two concepts, the inbound and outbound.
The inbound is responsible for exposing resources (API, Queue listener, ...) so that the application can receive communication from external services.
The outbound is responsible for communicating with external services (Database, Queue, API, ...), the application can communicate with external services through the outbound.

The application is using the Liquibase as the database schema change management to control the database changes.
After start the application we can access the database in the PgAdmin interface.

Also, this application is using the Swagger to expose all the endpoints in the API.

## Setup

To run the application we can use the docker compose file provided in the root of the project. The docker compose file will create the container about our application and the necessary resources.
We can use the following command in the root of the project to start the application.

```sh
docker compose -f docker-compose.yml up  
```

After start the application we can access the following url:
- [bookstore API](http://localhost:8080/swagger-ui/index.html#/) - The swagger interface about the Bookstore API.
- [pgadmin](http://localhost:5050/) - The database interface.
    - We need to provide the following user and password to access the database interface:
        - Username: `admin@example.com`
        - Password: `admin`
    - Also, we need to use the password `admin` to access the database server.


## Tests
The application is using Mockito, JUnit and Testcontainers. The unit test are testing isolated layers and the integration tests are testing the application as a whole.
In the integration tests we are using the Testcontainers to create the containers from a docker compose file and run the tests. With this tests we can validate the interaction between all the layers in the application (API, Mappers, Services, Repositories and Database).

To run the tests we can use the following command.

```sh
./gradlew test
```

After finish the build process we can check the code coverage report in the link from Jacoco in the terminal. Or we can find it inside the `build` folder in the project.

## Flows
### Book
The book API provide the following options:
- Create a new book
- Update a book
- Delete a book
  - It is a logical delete
  - As the purchase will be associated with a book, we can't delete a book that has a purchase associated
- Get a book by Id
- Get all books active

### User
The user API provide the following options:
- Create a new user
- Update a user
- Get a user by email
  - With this API we can retrieve the loyalty points from a user.

### Purchase
The purchase API provide the following options:
- Calculate the Price
  - This API is responsible to calculate the price of a purchase based on the book and the user.
- Create a new purchase
  - This API will create a new purchase and update the loyalty points from the user.
  - It will create the record about a purchase associated with a book and user.
- Get a purchase by Id
