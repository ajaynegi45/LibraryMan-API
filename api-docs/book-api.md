# Library Management System API Documentation

---

#### Base URL

```
/api
```

---

# Book API Endpoints

### 1. **Get All Books**

**Endpoint:** `/books`  
**Method:** `GET`  
**Description:** Retrieves a list of all books available in the library.

**Success Response:**
- **Code:** `200 OK`
- **Content:**
  ```json
  [
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
     
  ]
  ```

**Error Responses:**  
None

<br/>

---

<br/>

### 2. **Get Book by ID**

**Endpoint:** `/books/{id}`  
**Method:** `GET`  
**Description:** Retrieves the details of a book by its ID.

**Path Parameters:**
- `id` (int) : The unique identifier of the book.

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
- `id` (int) : The unique identifier of the book.

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
- `id` (int) : The unique identifier of the book.

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




