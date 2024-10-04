# Library Management System API Documentation

### Base URL
```
http://localhost:8080/api
```
# Borrowings API Endpoints

### Overview

The Borrowings API provides a set of endpoints to manage the borrowing and returning of books by library members. This includes functionalities such as borrowing books, returning borrowed books, handling fines, and retrieving borrowing records. This API supports pagination and sorting.

### 1. **Borrow a Book**

**Endpoint:** `/borrowings`  
**Method:** `POST`  
**Description:** Borrows a book from the library.

**Request Body:**
```json
{
    "book": {
        "bookId": 1
    },
    "member": {
        "memberId": 1
    }
}
```

**Success Response:**
- **Code:** `200 OK`
- **Content:**
  ```json
  [
      {
          "borrowingId": 1,
          "book": {
              "bookId": 1,
              "title": "Updated Book Title",
              "author": "Updated Author Name",
              "isbn": "123-4567891234",
              "publisher": "Updated Publisher Name",
              "publishedYear": 2022,
              "genre": "Updated Genre Name",
              "copiesAvailable": 10
          },
          "fine": null,
          "member": {
              "memberId": 1,
              "name": "Member Name",
              "email": "Member Email Id",
              "password": "Member password",
              "role": "Member role",
              "membershipDate": "2025-08-29T10:00:00Z"
          },
          "borrowDate": "2024-08-29T10:00:00Z",
          "dueDate": "2024-09-13T10:00:00Z",
          "returnDate": null
      }
  ]
  ```

**Error Responses:**
- **Code:** `404 NOT FOUND`
- **Message:** `Book not found` or `Not enough copies available`
- **Content:**
	 ```json
	  {
	      "timestamp": "2024-08-29T10:00:00Z",
	      "message": "Book not found",
	      "details": "/api/borrowings"
	  }
  ```

<br/>

---

<br/>

### 2. **Return a Book**

**Endpoint:** `/borrowings/{id}/return`  
**Method:** `PUT`  
**Description:** Returns a borrowed book to the library.

**Path Parameters:**
- `id` (Integer) : The unique identifier of the borrowing.

**Success Response:**
- **Code:** `200 OK`
- **Content:**
  ```json
      {
          "borrowingId": 1,
          "book": {
              "bookId": 1,
              "title": "Updated Book Title",
              "author": "Updated Author Name",
              "isbn": "123-4567891234",
              "publisher": "Updated Publisher Name",
              "publishedYear": 2022,
              "genre": "Updated Genre Name",
              "copiesAvailable": 10
          },
          "fine": {
              "fineId": 1,
              "amount": 10.00,
              "paid": true
          },
          "member": {
              "memberId": 1,
              "name": "Member Name",
              "email": "Member Email Id",
              "password": "Member password",
              "role": "Member role",
              "membershipDate": "2024-08-29T10:00:00Z"
          },
          "borrowDate": "2024-08-29T10:00:00Z",
          "dueDate": "2024-09-13T10:00:00Z",
          "returnDate": "2024-08-29T10:00:00Z"
      }
  ```

**Error Responses:**
- **Code:** `404 NOT FOUND`
 - **Message:** `Borrowing not found`, `Book has already been returned`, `Due date passed. Fine imposed, pay fine first to return the book`, or `Outstanding fine, please pay before returning the book`
- **Content:**
  ```json
  {
      "timestamp": "2024-08-29T10:00:00Z",
      "message": "Borrowing not found",
      "details": "/api/borrowings/{id}/return"
  }
  ```

<br/>

---

<br/>

### 3. **Pay Fine**

**Endpoint:** `/borrowings/{id}/pay-fine`  
**Method:** `PUT`  
**Description:** Pays the fine associated with a borrowing.

**Path Parameters:**
- `id` (Integer) : The unique identifier of the borrowing.

**Success Response:**
- **Code:** `200 OK`
- **Content:** `"PAID"`

**Error Responses:**
- **Code:** `404 NOT FOUND`
 - **Message:** `Borrowing not found`, `No outstanding fine found or fine already paid`
- **Content:**
  ```json
  {
      "timestamp": "2024-08-29T10:00:00Z",
      "message": "Borrowing not found",
      "details": "/api/borrowings/{id}/pay-fine"
  }
  ```

<br/>

---

<br/>

### 4. **Get All Borrowings**

**Endpoint:** `/borrowings`  
**Method:** `GET`  
**Description:** Retrieves a paginated, sorted list of all borrowings.

**Query Parameters:**

- `page` (Integer) : The page number of the result set (starting from 0). Default is `0`.
- `size` (Integer) : The number of books per page. Default is `5`.
- `sortBy` (String) : The field by which to sort the results (e.g., `borrowDate`, `book_title`). Default is `borrowDate`.
- `sortDir` (String) : The direction of sorting, either `asc` (ascending) or `desc` (descending). Default is `asc`.

**Example Requests:**

1. **Basic Request:**
   ```
   GET /borrowings
   ```
   Retrieves the first 5 borrowings sorted by `borrowDate` in ascending order.

2. **Pagination:**

	Pagination allows the API to split the data into multiple pages. Use `page` to specify the page number (starting from 0), and `size` to define how many items are returned per page.
	
	**Example:**

   ```
   GET /borrowings?page=0&size=7
   ```
   Retrieves the first 7 borrowings (on the first page).   
   ```
   GET /borrowings?page=1&size=7
   ```

   Retrieves the next 7 borrowings (on the second page).

3. **Sorting:**

   ```
   GET /borrowings?sortBy=book_title&sortDir=desc
   ```

   Retrieves borrowings sorted by the `book_title` field in descending order.
   
   ```
   GET /borrowings?sortBy=member_name&sortDir=asc
   ```

   Retrieves borrowings sorted by the `member_name` field in ascending order.

	**Supported Values for sortBy Query Parameter:**

	| **Value**                  | **Description**                                    |
	|----------------------------|---------------------------------------------------|
	| `borrowingId`             | Unique identifier for the borrowing record.       |
	| `book_title`              | Title of the borrowed book.                       |
	| `book_author`             | Author of the borrowed book.                      |
	| `book_isbn`               | ISBN number of the borrowed book.                 |
	| `book_publisher`          | Publisher of the borrowed book.                   |
	| `book_publishedYear`      | Year the borrowed book was published.             |
	| `book_genre`              | Genre of the borrowed book.                       |
	| `book_copiesAvailable`     | Number of copies available of the borrowed book.  |
	| `member_name`             | Name of the member who borrowed the book.        |
	| `member_email`            | Email address of the member who borrowed the book.|
	| `member_role`             | Role of the member (e.g., ADMIN).      |
	| `borrowDate`              | Date when the book was borrowed.                  |
	| `dueDate`                 | Date when the borrowed book is due.               |
	| `returnDate`              | Date when the borrowed book was returned.         |
	| `fine_amount`             | Amount of fine associated with the borrowing.     |
	| `fine_paid`               | Indicates whether the fine has been paid.         |

4. **Pagination + Sorting:**
   ```
   GET /borrowings?page=1&size=5&sortBy=member_name&sortDir=asc
   ```

   Retrieves the second page with 5 borrowings, sorted by `member_name` in ascending order.

**Success Response:**
- **Code:** `200 OK`
- **Content:**
  ```json
  {
	  "content": [
	      {
	          "borrowingId": 1,
	          "book": {
	              "bookId": 5,
	              "title": "The Silent Patient",
	              "author": "Alex Michaelides",
	              "isbn": "978-1250233655",
	              "publisher": "Celadon Books",
	              "publishedYear": 2019,
	              "genre": "Psychological Thriller",
               "copiesAvailable": 3
	          },
	          "fine": null,
	          "member": {
	              "memberId": 2,
	              "name": "Alice",
	              "email": "alice@example.com",
	              "password": "alice#123",
	              "role": "USER",
	              "membershipDate":"2025-09-15T10:00:00.000+00:00"
	            },
	            "borrowDate": "2024-10-03T14:30:00.000+00:00",
	            "dueDate": "2024-10-17T14:30:00.000+00:00",
	            "returnDate": null
	      }
	  ],
	    "pageable": {
	      "pageNumber": 0,
	      "pageSize": 1,
	      "sort": {
	          "empty": false,
	          "sorted": true,
	          "unsorted": false
	      },
	      "offset": 0,
          "paged": true,
	      "unpaged": false
	  },
	  "last": true,
	  "totalElements": 1,
	  "totalPages": 1,
	  "size": 1,
	  "number": 0,
	  "sort": {
	      "empty": false,
	      "sorted": true,
	      "unsorted": false
	  },
	  "first": true,
      "numberOfElements": 1,
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
     "details": "/api/borrowings"
  }
  ```

<br/>

---

<br/>

### 5. **Get All Borrowings of a Member**

**Endpoint:** `/borrowings/member/{memberId}`  
**Method:** `GET`  
**Description:** Retrieves paginated, sorted list of all borrowings of a specific member.

**Path Parameters:**
- `memberId` (Integer) : The unique identifier of the member.

**Query Parameters:**

- `page` (Integer) : The page number of the result set (starting from 0). Default is `0`.
- `size` (Integer) : The number of books per page. Default is `5`.
- `sortBy` (String) : The field by which to sort the results (e.g., `borrowDate`, `book_title`). Default is `borrowDate`.
- `sortDir` (String) : The direction of sorting, either `asc` (ascending) or `desc` (descending). Default is `asc`.

**Example Requests:**

1. **Basic Request:**
   ```
   GET /borrowings/member/1
   ```
   Retrieves the first 5 borrowings of a member (with memberId 1) sorted by `borrowDate` in ascending order.

2. **Pagination:**

	Pagination allows the API to split the data into multiple pages. Use `page` to specify the page number (starting from 0), and `size` to define how many items are returned per page.
	
	**Example:**

   ```
   GET /borrowings/member/1?page=0&size=7
   ```
   Retrieves the first 7 borrowings of a member with memberId 1 (on the first page).   
   ```
   GET /borrowings/member/1?page=1&size=7
   ```
   Retrieves the next 7 borrowings of a member with memberId 1 (on the second page).

3. **Sorting:**

   ```
   GET /borrowings/member/1?sortBy=book_title&sortDir=desc
   ```

   Retrieves borrowings sorted by the `book_title` field in descending order.
   
   ```
   GET /borrowings/member/1?sortBy=member_name&sortDir=asc
   ```

   Retrieves borrowings sorted by the `member_name` field in ascending order.


	**Supported Values for sortBy Query Parameter:**

	| **Value**                  | **Description**                                    |
	|----------------------------|---------------------------------------------------|
	| `borrowingId`             | Unique identifier for the borrowing record.       |
	| `book_title`              | Title of the borrowed book.                       |
	| `book_author`             | Author of the borrowed book.                      |
	| `book_isbn`               | ISBN number of the borrowed book.                 |
	| `book_publisher`          | Publisher of the borrowed book.                   |
	| `book_publishedYear`      | Year the borrowed book was published.             |
	| `book_genre`              | Genre of the borrowed book.                       |
	| `book_copiesAvailable`     | Number of copies available of the borrowed book.  |
	| `member_name`             | Name of the member who borrowed the book.        |
	| `member_email`            | Email address of the member who borrowed the book.|
	| `member_role`             | Role of the member (e.g., ADMIN).      |
	| `borrowDate`              | Date when the book was borrowed.                  |
	| `dueDate`                 | Date when the borrowed book is due.               |
	| `returnDate`              | Date when the borrowed book was returned.         |
	| `fine_amount`             | Amount of fine associated with the borrowing.     |
	| `fine_paid`               | Indicates whether the fine has been paid.         |

4. **Pagination + Sorting:**
   ```
   GET /borrowings/member/1?page=1&sortBy=dueDate&sortDir=asc
   ```

   Retrieves the second page with 5 borrowings, sorted by `dueDate` in ascending order.


**Success Response:**
- **Code:** `200 OK`
- **Content:**
  ```json
  {
	 "content": [
	     {
	         "borrowingId": 3,
	         "book": {
	             "bookId": 8,
	             "title": "Where the Crawdads Sing",
	             "author": "Delia Owens",
	             "isbn": "978-0735219090",
	             "publisher": "G.P. Putnam's Sons",
	             "publishedYear": 2018,
	             "genre": "Fiction",
	             "copiesAvailable": 5
          },
	         "fine": null,
	         "member": {
	             "memberId": 4,
	             "name": "Joe",
	             "email": "joeh@gmail.com",
	             "password": "joe#@33",
	             "role": "LIBRARIAN",
	             "membershipDate": "2025-08-29T10:00:00.000+00:00"
	         },
	         "borrowDate": "2024-10-02T10:59:52.547+00:00",
	         "dueDate": "2024-10-17T10:59:52.547+00:00",
	         "returnDate": null
	     }
	 ],
	 "pageable": {
	     "pageNumber": 0,
	     "pageSize": 1,
	     "sort": {
	         "empty": false,
	         "sorted": true,
	         "unsorted": false
	     },
	     "offset": 0,
	     "paged": true,
	     "unpaged": false
	 },
	 "last": true,
	 "totalElements": 1,
	 "totalPages": 1,
	 "size": 1,
	 "number": 0,
	 "sort": {
	     "empty": false,
	     "sorted": true,
	     "unsorted": false
	 },
	 "first": true,
	 "numberOfElements": 1,
	 "empty": false
  }
  ```

**Error Responses:**
- **Code:** `404 NOT FOUND`
	- **Message:** `Member didn't borrow any book`
	- **Content:**
	  ```json
	  {
	     "timestamp": "2024-08-29T10:00:00Z",
	     "message": "Member didn't borrow any book",
	     "details": "/api/borrowings/member/{memberId}"
	  }
	  ```

- **Code:** `400 BAD REQUEST`
	- **Message:** `The specified 'sortBy' value is invalid.`
	- **Content:**
	  ```json
		{
		   "timestamp": "2024-10-03T07:37:08.364+00:00",
		   "message": "The specified 'sortBy' value is invalid.",
		   "details": "/api/borrowings/member/{memberId}"
		}
	  ```

<br/>
<br/>
<br/>
<br/>
<br/>
