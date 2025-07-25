# Watch Store 
Watch Store is a web application developed in Java using Spring Boot, which allows browsing and purchasing watches online. Additionally, the application offers an administrator panel for managing the store. This project was created for educational purposes as part of self-directed learning in software development. At the same time, it was designed with potential future business use in mind, making it a solid foundation for possible commercial expansion. 

![Gif](https://github.com/MichalTrzaska9/Watch-Store/blob/d90aaf995f035dc6c57c0711607d72ea308e26b3/Watch_Store.gif)

## Technologies Used
- **Java 17** - used as the main programming language for the backend layer. As a long-term support (LTS) release, it provides ongoing updates and bug fixes, ensuring long-term stability. Java is a mature and stable version, well suited for production-grade applications. 
- **Spring Boot** - chosen for its simplicity in setting up production-ready Java applications. It reduces boilerplate configuration and improves code readability.
- **Spring Security** - used to handle user authentication and authorization in a secure and standardized way.
- **PostgreSQL** - selected as the production database for its performance and stability.
- **H2** - an in-memory database, used in testing to ensure fast and isolated test environments. 
- **Spring Data JPA (Hibernate)** - used for managing data persistence and providing an abstraction layer over the database. Under the hood, it uses Hibernate as the default JPA provider to handle object-relational mapping (ORM).
- **Thymeleaf, HTML, CSS** - these technologies were used together to build the presentation layer of the application. Thymeleaf enables dynamic server-side rendering of HTML views, while HTML and CSS provide the structure and styling of the user interface.
- **Gradle** - used as the build automation tool to efficiently manage project dependencies and automate the build process. It simplifies configuration by integrating plugins for Java, Spring Boot and application packaging. In my opinion, it's more readable and understandable than Maven.
- **JUnit 5, Mockito, Spring Boot Test** - these testing tools were used to write and run unit and integration tests. JUnit 5 offers a modern testing platform, while Mockito helps with mocking dependencies, and Spring Boot Test simplifies application context testing.
- **Lombok** - used to reduce boilerplate code in Java classes, such as getters, setters and constructors. 

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


