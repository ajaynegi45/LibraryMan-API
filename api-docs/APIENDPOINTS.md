

#### 6. **Borrow a Book**

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

---

#### 7. **Return a Book**

**Endpoint:** `/borrowings/{id}/return`  
**Method:** `PUT`  
**Description:** Returns a borrowed book to the library.

**Path Parameters:**
- `id` (int) : The unique identifier of the borrowing.

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

---

#### 8. **Pay Fine**

**Endpoint:** `/borrowings/{id}/pay-fine`  
**Method:** `PUT`  
**Description:** Pays the fine associated with a borrowing.

**Path Parameters:**
- `id` (int) : The unique identifier of the borrowing.

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

---

#### 9. **Get All Borrowings**

**Endpoint:** `/borrowings`  
**Method:** `GET`  
**Description:** Retrieves a list of all borrowings.

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
  ]
  ```

**Error Responses:**  
None

---

#### 10. **Get All Borrowings of a Member**

**Endpoint:** `/borrowings/member/{memberId}`  
**Method:** `GET`  
**Description:** Retrieves all borrowings of a specific member.

**Path Parameters:**
- `memberId` (int) : The unique identifier of the member.

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
  ]
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


---

### Global Error Handling

All endpoints will return a standardized error response in case of exceptions:

**Error Response Structure:**
```json
{
    "timestamp": "2024-08-29T10:00:00Z",
    "message": "Error Message",
    "details": "Request Path"
}
```

### Common Error Codes

- **404 NOT FOUND:** The requested resource was not found.
- **400 BAD REQUEST:** The request was invalid or cannot be otherwise served.

---

This documentation provides a clear and concise guide to the available API endpoints, including their respective methods, success responses, and potential error responses.