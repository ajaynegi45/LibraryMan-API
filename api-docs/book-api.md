# Library Management System API Documentation


### Base URL

```
http://localhost:8080/api
```

# Book API Endpoints

### Overview

The Book API provides a set of endpoints to manage the library's book collection. This includes functionalities to retrieve, add, update, and delete books. The API supports pagination, sorting, and detailed book information.

### 1. **Get All Books**

**Endpoint:** `/books`  
**Method:** `GET`  
**Description:** Retrieves a paginated, sorted list of all books available in the library.

**Query Parameters:**

- `page` (Integer) : The page number of the result set (starting from 0). Default is `0`.
- `size` (Integer) : The number of books per page. Default is `5`.
- `sortBy` (String) : The field by which to sort the results (e.g., `title`, `author`, `publishedYear`). Default is `title`.
- `sortDir` (String) : The direction of sorting, either `asc` (ascending) or `desc` (descending). Default is `asc`.

**Example Requests:**

1. **Basic Request:**
   ```
   GET /books
   ```
   Retrieves the first 5 books sorted by `title` in ascending order.

2. **Pagination:**

	Pagination allows the API to split the data into multiple pages. Use `page` 		
	to specify the page number (starting from 0), and `size` to define how 
	many items are returned per page.
	
	**Example:**

   ```
   GET /books?page=0&size=7
   ```
   Retrieves the first 7 books (on the first page).   
   ```
   GET /books?page=1&size=7
   ```

   Retrieves the next 7 books (on the second page).

3. **Sorting:**

   ```
   GET /books?sortBy=publishedYear&sortDir=desc
   ```

   Retrieves books sorted by the `publishedYear` field in descending order.
   

   ```
   GET /books?sortBy=author&sortDir=asc
   ```

   Retrieves books sorted by the `author` field in ascending order.

	**Supported Values for sortBy Query Parameter:**

	| **Value**                  | **Description**                                   |
	|----------------------------|---------------------------------------------------|
	| `bookId`                   | Unique identifier for the book.      		 |
	| `title`                    | Title of the book.               	         |
	| `author`                   | Author of the book.              	         |
	| `isbn`                     | ISBN number of the book.           	         |
	| `publisher`                | Publisher of the book.         	          	 |
	| `publishedYear`            | Year the book was published.             	 |
	| `genre`                    | Genre of the book.                       	 |
	| `copiesAvailable`          | Number of copies available of the book.  	 |

4. **Pagination + Sorting:**
   ```
   GET /books?page=1&size=5&sortBy=author&sortDir=asc
   ```

   Retrieves the second page with 5 books, sorted by `author` in ascending order.

**Success Response:**
- **Code:** `200 OK`
- **Content:**
  ```json
  {
	"content": [
	        {
	            "bookId": 11,
	            "title": "Circe",
	            "author": "Madeline Miller",
	            "isbn": "978-0316388681",
	            "publisher": "Little, Brown and Company",
	            "publishedYear": 2018,
	            "genre": "Fantasy",
	            "copiesAvailable": 7
	        },
	        {
	            "bookId": 10,
	            "title": "Educated",
	            "author": "Tara Westover",
	            "isbn": "978-0399590504",
	            "publisher": "Random House",
	            "publishedYear": 2018,
	            "genre": "Memoir",
	            "copiesAvailable": 5
	        },
	        {
	            "bookId": 12,
	            "title": "The Night Circus",
	            "author": "Erin Morgenstern",
	            "isbn": "978-0385534635",
	            "publisher": "Doubleday",
	            "publishedYear": 2011,
	            "genre": "Fantasy",
	            "copiesAvailable": 2
	        }
	    ],
	   "pageable": {
	      "pageNumber": 0,
	      "pageSize": 5,
	      "sort": {
           "sorted": true,
	          "unsorted": false,
	          "empty": false
           },
	      "offset": 0,
	      "paged": true,
          "unpaged": false
	  },
	  "last": false,
	  "totalPages": 1,
	  "totalElements": 3,
	  "first": true,
	  "numberOfElements": 3,
	  "size": 5,
	  "number": 0,
	  "sort": {
	      "sorted": true,
	      "unsorted": false,
	      "empty": false
	  },
      "empty": false
	}
  ```

**Error Responses:**
- **Code:** `400 BAD REQUEST`
- **Message:** `The specified 'sortBy' value is invalid.`
- **Content:**
  ```json
  {
      "timestamp": "2024-10-03T07:37:08.364+00:00",
      "message": "The specified 'sortBy' value is invalid.",
      "details": "/api/books"
  }
  ```
  
<br/>

---

<br/>

### 2. **Get Book by ID**

**Endpoint:** `/books/{id}`  
**Method:** `GET`  
**Description:** Retrieves the details of a book by its ID.

**Path Parameters:**
- `id` (Integer) : The unique identifier of the book.

**Success Response:**
- **Code:** `200 OK`
- **Content:**
  ```json
  {
      "bookId": 1,
      "title": "Book Title",
      "author": "Author Name",
      "isbn": "123-4567891234",
      "publisher": "Publisher Name",
      "publishedYear": 2021,
      "genre": "Genre Name",
      "copiesAvailable": 5
  }
  ```

**Error Responses:**
- **Code:** `404 NOT FOUND`
- **Message:** `Book not found`
- **Content:**
  ```json
  {
      "timestamp": "2024-08-29T10:00:00Z",
      "message": "Book not found",
      "details": "/api/books/{id}"
  }
  ```

<br/>

---

<br/>

### 3. **Add a New Book**

**Endpoint:** `/books`  
**Method:** `POST`  
**Description:** Adds a new book to the library's collection.

**Request Body:**
```json
{
    "title": "Book Title",
    "author": "Author Name",
    "isbn": "123-4567891234",
    "publisher": "Publisher Name",
    "publishedYear": 2021,
    "genre": "Genre Name",
    "copiesAvailable": 5
}
```

**Success Response:**
- **Code:** `201 CREATED`
- **Content:**
  ```json
  {
      "bookId": 1,
      "title": "Book Title",
      "author": "Author Name",
      "isbn": "123-4567891234",
      "publisher": "Publisher Name",
      "publishedYear": 2021,
      "genre": "Genre Name",
      "copiesAvailable": 5
  }
  ```

**Error Responses:**  
None

<br/>

---

<br/>

### 4. **Update Book Details**

**Endpoint:** `/books/{id}`  
**Method:** `PUT`  
**Description:** Updates the details of an existing book by its ID.

**Path Parameters:**
- `id` (Integer) : The unique identifier of the book.

**Request Body:**
```json
{
    "title": "Updated Book Title",
    "author": "Updated Author Name",
    "isbn": "123-4567891234",
    "publisher": "Updated Publisher Name",
    "publishedYear": 2022,
    "genre": "Updated Genre Name",
    "copiesAvailable": 10
}
```

**Success Response:**
- **Code:** `200 OK`
- **Content:**
  ```json
  {
      "bookId": 1,
      "title": "Updated Book Title",
      "author": "Updated Author Name",
      "isbn": "123-4567891234",
      "publisher": "Updated Publisher Name",
      "publishedYear": 2022,
      "genre": "Updated Genre Name",
      "copiesAvailable": 10
  }
  ```

**Error Responses:**
- **Code:** `404 NOT FOUND`
- **Message:** `Book not found`
- **Content:**
  ```json
  {
      "timestamp": "2024-08-29T10:00:00Z",
      "message": "Book not found",
      "details": "/api/books/{id}"
  }
  ```

<br/>

---

<br/>

### 5. **Delete Book**

**Endpoint:** `/books/{id}`  
**Method:** `DELETE`  
**Description:** Deletes a book from the library by its ID.

**Path Parameters:**
- `id` (Integer) : The unique identifier of the book.

**Success Response:**
- **Code:** `204 NO CONTENT`

**Error Responses:**
- **Code:** `404 NOT FOUND`
- **Message:** `Book not found`
- **Content:**
  ```json
  {
      "timestamp": "2024-08-29T10:00:00Z",
      "message": "Book not found",
      "details": "/api/books/{id}"
  }
  ```


<br/>
<br/>
<br/>
<br/>
<br/>
