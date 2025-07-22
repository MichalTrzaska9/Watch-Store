# Watch Store 
Watch Store is a web application developed in Java using Spring Boot, which allows browsing and purchasing watches online. Additionally, the application offers an administrator panel for managing the store.

![Gif](https://github.com/MichalTrzaska9/Watch-Store/blob/d90aaf995f035dc6c57c0711607d72ea308e26b3/Watch_Store.gif)

## Technologies Used
- Java 17
- Spring Boot
- Spring Security
- PostgreSQL (production), H2 (tests)
- Hibernate, JPA
- Thymeleaf, HTML, CSS
- Gradle
- JUnit 5, Mockito, Spring Boot Test
- Lombok

## System Features
The online store project defines three types of users:
- Administrator - has access to the admin panel, from which they manage user accounts, the watch catalog, available brands and orders.
- Customer - manages their own account, can change their password and personal information, add products to the cart and places orders.
- Unregistered User - can browse the catalog and has the ability to log in and register in the system. 

## Setup and Run
- Clone the repository: `git clone https://github.com/MichalTrzaska9/Watch-Store.git`
- Go to the project directory: `cd Watch-Store`
- The project is configured to use a PostgreSQL database by default. Update your `application.properties` with the correct database credentials.
- Run the project: `gradlew bootRun`
- Navigate to the store homepage: http://localhost:8080/watches

## Sample Data
The application generates sample data for watches, brands and users.
- Administrator (login: `kowalski@wp.pl`, password: `a`)
- Customer (login: `nowak@wp.pl`, password: `a`)

## Tests
To verify the application's correctness, both unit tests and integration tests are implemented.
- To run the tests, execute the command: `gradlew test`
- The test report is available in the file at the following path: `build/reports/tests/test/index.html`

## Author
Michał Trzaska
`michaltrzaska18@gmail.com`


