# Prophius Limited Social Media API

This is a Social Media API project built with Spring Boot that allows users to create posts, follow/unfollow other users, like and comment on posts. The API also includes user authentication and authorization using JWT tokens.

## Technologies Used

- Java 17
- Spring Boot 3.1.2
- Spring Data JPA
- Spring Data REST
- Spring Security
- PostgreSQL
- JWT (Java JWT library)
- Lombok
- Springfox (Swagger)
- Hibernate Validator
- MapStruct
- Apache Commons Lang
- Guava

## Setting Up the Application

1. Clone the repository to your local machine.

2. Make sure you have PostgreSQL installed and running. Update the `application.properties` file in the `src/main/resources` directory with your database configuration:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/social-media-api
   spring.datasource.username=postgres
   spring.datasource.password=1234
   spring.jpa.hibernate.ddl-auto=create
   spring.jpa.show-sql=false
   spring.jpa.properties.hibernate.format_sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
   ```

   Make sure to change the `spring.datasource.username` and `spring.datasource.password` to your PostgreSQL credentials.

3. Build the application using Maven:

   ```bash
   mvn clean package
   ```

4. Run the application:

   ```bash
   java -jar target/Prophius-Limited-Task-0.0.1-SNAPSHOT.jar
   ```

   The application will be accessible at `http://localhost:8080`.

5. You can use Swagger UI to interact with the API and explore available endpoints. Access the Swagger UI at `http://localhost:8080/swagger-ui.html`.

## API Endpoints

The API provides the following endpoints:

- `/api/users`: CRUD operations for managing users.
- `/api/posts`: CRUD operations for managing posts.
- `/api/comments`: CRUD operations for managing comments.
- `/api/auth/register`: Endpoint to register a new user.
- `/api/auth/login`: Endpoint to login and obtain a JWT token.

## User Authentication and Authorization

User authentication is implemented using Spring Security and JWT tokens. To access the protected endpoints, you need to provide a valid JWT token in the `Authorization` header of your HTTP requests:

```http
Authorization: Bearer {JWT_TOKEN}
```
##SWAGGER LOGIN
To include the password when testing the API on Swagger, you can use Swagger's authentication feature to provide the necessary credentials. Here's how you can do it:

1. Open the Swagger UI in your web browser by accessing the following URL:

   ```
   http://localhost:8080/swagger-ui.html
   ```

2. In the Swagger UI, you should see a button labeled "Authorize" at the top right corner. Click on it.

3. A dialog box will pop up, prompting you to enter the credentials. Enter the username and password you want to use for authentication.

   - Username: user
   - Password: mySecretPassword123

4. Click the "Authorize" button to save the credentials.

5. Once you have authorized with the correct credentials, Swagger will include the Authorization header with the JWT token in all subsequent API requests, and you'll be able to access the protected endpoints.

Remember that the JWT token must be obtained by sending a valid login request to the `/api/auth/login` endpoint first. After successful authentication, you'll receive the JWT token in the response, and Swagger will automatically include it in the Authorization header for subsequent requests.

If you are using the correct JWT token and the credentials are correct, you should be able to access the protected endpoints and test the API with the given user credentials.

## Unit Tests

The project includes unit tests to ensure the correctness of API endpoints and service methods. To run the tests, execute the following command:

```bash
mvn test
```

## Bonus Features

The application includes the following bonus features:

- Pagination and Sorting: The listing of posts and comments supports pagination and sorting.
- Swagger Documentation: Swagger UI is integrated to provide comprehensive API documentation.
- Validation: Request data is validated using Hibernate Validator annotations.

## Dockerization

This application can be containerized using Docker. To build the Docker image, run the following command:

```bash
docker build -t ann/prophius .

To run the Docker container, use the following command:

bash
Copy code
docker run -p 8080:8080 ann/prophius
The application will be accessible at http://localhost:8080.

Docker Image: ann/prophius

Note: Make sure you have Docker installed and running on your system.

...

vbnet
Copy code

Remember to replace the previous Docker information with the updated content, reflecting the Docker image name as `ann/prophius`.

After making the changes, save the README file, commit the changes to your repository, and push the updates to make them available to others.





## Conclusion

Congratulations! You have set up the Social Media API successfully. Feel free to explore the API endpoints and interact with it using Swagger UI or your preferred REST client. If you encounter any issues or have any questions, please feel free to reach out to the developer at chikodiann@gmail.com. Happy coding!
