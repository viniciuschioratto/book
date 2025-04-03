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

The application is running wth Docker, where we can use just a command and the application will be up and running with all the necessary resources.

## Setup

To run the application we can use the docker compose file provided in the root of the project. The docker compose file will create the container about our application and the necessary resources.
We can use the following command in the root of the project to start the application.

```sh
docker compose -f docker-compose.yml up --build 
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

After finish the test process we can check the code coverage report in the link from Jacoco in the terminal. Or we can find it inside the `build` folder in the project.

## Flows
### Book
The book API provide the following options:
- Create a new book
  - When we are creating a new book we need to provide the type and we have the following options:
    - NEW_RELEASE
    - REGULAR
    - OLD_EDITION
- Update a book
- Delete a book
  - It is a logical delete
  - As the purchase will be associated with a book, we can't delete a book that has a purchase associated
- Get a book by Id
- Get all books active
  - List all books that are active in the database

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
  - We need to provide a List of books and the user email.
  - The user email is necessary to calculate the loyalty points and apply discounts.
- Create a new purchase
  - We need to provide the user email and a list of books ids.
  - This API will create a new purchase and update the loyalty points from the user.
  - It will create the record about a purchase associated with a book and user.
- Get a purchase by Id

To use the Bookstore API we can list all the books and select the books we want to buy.
Before we calculate the price and purchase a book we need to create a user, so we can use the user API.

Once, we have a user and books we can calculate the price and purchase the books through the purchase API.

## Decisions
I took some decisions to implement the application, some of them are:
1. Create a user flow
   1. As we have the loyalty points I decided to create a user flow where we can have more than one user in the application and control de loyalty points.
2. Use the Hexagonal architecture
   1. I decided to use the hexagonal architecture to separate the application and adapters layers. This way we can have a better separation of concerns and make the application more flexible.
3. Docker compose
   1. I decided to use the docker compose to run the application, because it is easy to include new resources and run locally. Also, with this strategy we need only Docker to start the application.
4. Strategy Pattern
   1. I decide to use the Strategy Pattern to calculate the book price, because later if we need to add new types of books we can do it easily.
5. Integration tests
   1. I decided to use the integration tests to test the application as a whole, this way we can validate the interaction between all the layers in the application.
