# Library-API Project 📚

This project is a simple Spring Boot application that provides a RESTful API for managing books. It allows users to perform CRUD (Create, Read, Update, Delete) operations on books using the API. It adding new books, updating existing ones, searching for books, and deleting books.
<br>
<br>



<!--## Features ✨

- **Add a Book:** Add a new book to the database.

- **Get All Books:** Retrieve a list of all books available in the database.
- **Add a Book:** Add a new book to the database.
- **Update a Book:** Update the details of an existing book.
- **Delete a Book:** Remove a book from the database by its ID.-->

## Project Structure 📂

The project consists of several components, each serving a specific purpose:

1. **Controller:** Defines the RESTful endpoints for handling book-related requests. It interacts with the Service to perform various operations.

2. **Service:** Contains the business logic for book-related operations. It interacts with the DAO to access the database.

3. **DAO:** Provides access to the database using Spring Data JPA. It extends the JpaRepository interface to enable CRUD operations.

4. **Entities:** Represents the entity (model) for a book. It is annotated with `@Entity` to indicate it's a JPA entity and corresponds to a database table.
<br>





## API Endpoints 🔗
[API Collection Documentation on Postman](https://documenter.getpostman.com/view/28691426/2s9XxwxEsr) for testing API
<br>

### Add Book 📗

Add a new book to the database.

- **URL:** POST `/addBook`
- **Request Body:**
  ```json
  {
    "bookName": "Book Title",
    "authorName": "Author Name",
    "bookPrice": 29.99,
    "stockQuantity": 50
  }
  ```
- **Response:**
  - 201 Created: Book added successfully
  - 409 CONFLICT: if there is a conflict (e.g., book not able to store ).
  - 500 INTERNAL_SERVER_ERROR: if there is an internal server error.

<br>

### Update Book 📘

Update an existing book's details by providing the book ID.

- **URL:** PUT `/updatebook/{bookid}`
- **Request Body:**
  ```json
  {
    "bookName": "Updated Book Name",
    "authorName": "Updated Author Name",
    "bookPrice": "Updated Book Price",
    "stockQuantity": "Updated Stock Quantity"
  }
  ```
- **Response:**
  - 202 Accepted: Book updated successfully
  - 404 Not Found: Book with the given ID not found



<br>
<br>

### GET Book 📔

| Endpoint                        | Method     | URL                                | Request Parameter         | Response                   |
|---------------------------------|------------|------------------------------------|---------------------------|----------------------------|
| Get All Books                   | **GET**    | [/getAllBooks]()                   | None                      | List of BookEntity objects |
| Get Book by ID                  | **GET**    | [/getBookById/](){id}              | id (Path parameter)       | BookEntity object          |
| Get Books by Name Starting With | **GET**    | [/bookNameStartWith/](){bookName}  | bookName (Path parameter) | List of BookEntity objects |
| Get Books by Name Ending With   | **GET**    | [/bookNameEndWith/](){bookName}    | bookName (Path parameter) | List of BookEntity objects |
| Get Books by Name Containing    | **GET**    | [/bookNameContain/](){bookName}    | bookName (Path parameter) | List of BookEntity objects |

<br>
<br>

### DELETE Book 📕

| Endpoint                | Method     | URL                                  | Request Parameter            | Response                                                                                                                    |
|-------------------------|------------|--------------------------------------|------------------------------|-----------------------------------------------------------------------------------------------------------------------------|
| Delete Book by ID       | **DELETE** | [/deleteBookById/](){id}             | id (Path parameter)          | [202 Accepted]() : Book deleted successfully    <p align="center" >OR</p>   [404 Not Found]() : Book with the given ID not found           |
| Delete Book by Name     | **DELETE** | [/deleteBookByName/](){bookName}     | bookName (Path parameter)    | [202 Accepted]() : Books deleted successfully   <p align="center" >OR</p>   [404 Not Found]() : No books found with the given name         |
| Delete Books by Author  | **DELETE** | [/deleteAllByAuthor/](){authorName}  | authorName (Path parameter)  | [202 Accepted]() : Books deleted successfully   <p align="center" >OR</p>   [404 Not Found]() : No books found with the given author name  |

<br>

## How to Run the Project 💨

1. Ensure you have Java and MySQL installed on your system.
2. Clone or download the project from the repository.
3. Import the project into your preferred IDE (e.g., Eclipse, IntelliJ).
4. Set up the MySQL database and update the database configurations in the `application.properties` file (not provided in the code).
5. Build and run the project using the IDE or by running `mvn spring-boot:run` command from the project root directory.
<br>



## Technologies Used 🌐

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- RESTful API
- JSON
- MySQL
- PostMan ([API Collection Documentation on Postman](https://documenter.getpostman.com/view/28691426/2s9XxwxEsr) for testing API)
- Mavin
<br>



<!---


### Delete Book by ID

Delete a book from the database using its ID.

- **URL:** DELETE `/deleteBookById/{id}`
- **Response:**
  - 202 Accepted: Book deleted successfully
  - 404 Not Found: Book with the given ID not found

### Delete Book by Name

Delete all books with the given specified book name.

- **URL:** DELETE `/deleteBookByName/{bookName}`
- **Response:**
  - 202 Accepted: Books deleted successfully
  - 404 Not Found: No books found with the given name

### Delete Books by Author

Delete all books with the given specified author name.

- **URL:** DELETE `/deleteAllByAuthor/{authorName}`
- **Response:**
  - 202 Accepted: Books deleted successfully
  - 404 Not Found: No books found with the given author name

### Get All Books

Get a list of all books present in the database.

- **URL:** GET `/getAllBooks`
- **Response:** List of BookEntity objects

### Get Book by ID

Get a book by its ID.

- **URL:** GET `/getBookById/{id}`
- **Response:** BookEntity object

### Get Books by Name Starting With

Get a list of books whose names start with the given specified prefix.

- **URL:** GET `/bookNameStartWith/{bookName}`
- **Response:** List of BookEntity objects

### Get Books by Name Ending With

Get a list of books whose names end with the given specified suffix.

- **URL:** GET `/bookNameEndWith/{bookName}`
- **Response:** List of BookEntity objects

### Get Books by Name Containing

Get a list of books whose names contain the given specified book name or character.

- **URL:** GET `/bookNameContain/{bookName}`
- **Response:** List of BookEntity objects

-->



<br>

## ‼️ Important Note ‼️

- You need to set up the database and make sure the application properties are correctly configured to run the project successfully.
<br>

## Upcoming Update
Adding more features, error handling, authentication, and security measures.
<br>
<br>

## Contributing 🤗

Feel free to explore and use these project. If you encounter any issues or have suggestions for improvements, please feel free to contribute or reach out for assistance.

Contributions are always welcome! ✨

See [`contributing.md`](https://github.com/ajaynegi45/Library-API/blob/main/contributing.md) for ways to get started.

Please adhere to this project's [`code_of_conduct.md`](https://github.com/ajaynegi45/Library-API/blob/main/code_of_conduct.md).
<br><br>

## Contact Information 📧

If you have any questions or would like to connect, please don't hesitate to reach out. I'd be more than happy to chat and learn from your experiences too.
<br>
<br>
**Email:** [contact me](mailto:contact@ajaynegi.co)
<br>
<br>

## Thankyou ❤️
Thank you for taking the time to explore my project. I hope you find them informative and useful in your journey to learn Java and enhance your programming skills. Your support and contributions are highly appreciated. 
Happy coding! ✨
<br><br>

## Authors

- 🙍🏻‍♂️ [@ajaynegi45](https://github.com/ajaynegi45)
