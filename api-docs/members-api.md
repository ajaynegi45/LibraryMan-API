# Library Management System API Documentation


### Base URL

```
http://localhost:8080/api
```

# Members API Endpoints

### Overview

The Members API provides endpoints to manage library members. This includes functionalities to retrieve, add, update, and delete members. The API supports pagination, sorting, and detailed member information.


### 1. **Get All Members**

**Endpoint:** `/members`  
**Method:** `GET`  
**Description:** Retrieves a paginated, sorted list of all members in the library.

**Query Parameters:**

- `page` (Integer) : The page number of the result set (starting from 0). Default is `0`.
- `size` (Integer) : The number of members per page. Default is `5`.
- `sortBy` (String) : The field by which to sort the results (e.g., `name`, `memberId`, `email`). Default is `name`.
- `sortDir` (String) : The direction of sorting, either `asc` (ascending) or `desc` (descending). Default is `asc`.

**Example Requests:**

1. **Basic Request:**
   ```
   GET /members
   ```
   Retrieves the first 5 members sorted by `name` in ascending order.

2. **Pagination:**

	Pagination allows the API to split the data into multiple pages. Use  `page`  to 		
specify the page number (starting from 0), and  `size`  to define how many items are returned per page.

	**Example:**
	 ```
	 GET /members?page=0&size=7
	```
   Retrieves the first 7 members (on the first page).  	   
   ```
   GET /members?page=1&size=7
   ```
   Retrieves the next 7 members (on the second page).

3. **Sorting:**

   ```
   GET /members?sortBy=memberId&sortDir=desc
   ```
   Retrieves members sorted by `memberId` in descending order.
   
   ```
   GET /members?sortBy=email&sortDir=asc
   ```

   Retrieves members sorted by the `email` field in ascending order.
   
	**Supported Values for sortBy Query Parameter:**
	
	| **Value**                    | **Description**         												  |
	|--------------------------|----------------------------------------------------------- |
	| `memberId`       		   | Unique identifier for a member   							  |
	| `name`           			   | Member's name                              							  |
	| `email`      				   | Member's email                           	 							  |	
	| `membershipDate`     | The date the member joined									  |
	| `role`                         | Role of the member (ADMIN, USER, LIBRARIAN)  |

4. **Pagination + Sorting:**

   ```
   GET /members?page=0&size=5&sortBy=membershipDate&sortDir=asc
   ```
   Retrieves the first page with 5 members, sorted by `membershipDate` in ascending order.

**Success Response:**
- **Code:** `200 OK`
- **Content:**
  ```json
  {
    "content": [
        {
            "memberId": 1,
            "name": "John Doe",
            "email": "johndoe@example.com",
            "password": "jack@11",
            "role": "USER",
            "membershipDate": "2025-10-04T00:00:00.000+00:00"
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
    "totalPages": 1,
    "totalElements": 1,
    "size": 1,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "numberOfElements": 1,
    "first": true,
    "empty": false
  }
  ```

**Error Responses:**
- **Code:** `400 BAD REQUEST`
- **Message:** `The specified 'sortBy' value is invalid.`
- **Content:**
  ```json
  {
      "timestamp": "2024-10-04T10:00:00Z",
      "message": "The specified 'sortBy' value is invalid.",
      "details": "/api/members"
  }
  ```

<br/>

---

<br/>

### 2. **Get Member by ID**

**Endpoint:** `/members/{id}`  
**Method:** `GET`  
**Description:** Retrieves the details of a member by their ID.

**Path Parameters:**
- `id` (Integer) : The unique identifier of the member.

**Success Response:**
- **Code:** `200 OK`
- **Content:**
  ```json
  {
	  "memberId": 1,
	  "name": "John Doe",
      "email": "johndoe@example.com",
      "password": "jack@11",
      "role": "USER",
      "membershipDate": "2025-10-04T00:00:00.000+00:00"
  }
  ```

**Error Responses:**
- **Code:** `404 NOT FOUND`
- **Message:** `Member not found`
- **Content:**
  ```json
  {
      "timestamp": "2024-10-04T12:51:41.374+00:00",
      "message": "Member not found",
      "details": "/api/members/{id}"
  }
  ```

<br/>

---

<br/>

### 3. **Add a New Member**

**Endpoint:** `/members`  
**Method:** `POST`  
**Description:** Adds a new member to the library.

**Request Body:**
```json
{
    "name": "John Doe",
    "email": "johndoe@example.com",
    "password": "jack@11",
    "role": "USER",
    "membershipDate": "2025-10-04"
}
```

**Success Response:**
- **Code:** `201 CREATED`
- **Content:**
  ```json
  {
      "memberId": 1,
      "name": "John Doe",
      "email": "johndoe@example.com",
      "password": "jack@11",
      "role": "USER",
      "membershipDate": "2025-10-04T00:00:00.000+00:00"
  }
  ```

**Error Responses:**
- None

<br/>

---

<br/>

### 4. **Update Member Details**

**Endpoint:** `/members/{id}`  
**Method:** `PUT`  
**Description:** Updates the details of an existing member by their ID.

**Path Parameters:**
- `id` (Integer) : The unique identifier of the member.

**Request Body:**
```json
{
    "name": "Updated Name",
    "email": "updatedemail@example.com",
    "password": "Updated Password",
    "role": "Updated Role",
    "membershipDate": "2025-10-04"
}
```

**Success Response:**
- **Code:** `200 OK`
- **Content:**
  ```json
  {
      "memberId": 1,
      "name": "Updated Name",
      "email": "updatedemail@example.com",
	  "password": "Updated Password",
      "role": "Updated Role",
      "membershipDate": "2025-10-04T00:00:00.000+00:00"
  }
  ```

**Error Responses:**
- **Code:** `404 NOT FOUND`
- **Message:** `Member not found`
- **Content:**
  ```json
  {
      "timestamp": "2024-10-04T10:00:00Z",
      "message": "Member not found",
      "details": "/api/members/{id}"
  }
  ```
    
<br/>

---

<br/>

### 5. **Delete Member**

**Endpoint:** `/members/{id}`  
**Method:** `DELETE`  
**Description:** Deletes a member from the library by their ID.

**Path Parameters:**
- `id` (Integer) : The unique identifier of the member.

**Success Response:**
- **Code:** `204 NO CONTENT`

**Error Responses:**
- **Code:** `404 NOT FOUND`
- **Message:** `Member not found`
- **Content:**
  ```json
  {
      "timestamp": "2024-10-04T10:00:00Z",
      "message": "Member not found",
      "details": "/api/members/{id}"
  }
  ```


<br/>
<br/>
<br/>
<br/>
<br/>
