# Library Management System API Documentation

<br/>

#### Base URL

```
http://localhost:8080/api
```
---

# Introduction
This documentation provides a clear and concise guide to the available API endpoints, including their respective methods, success responses, and potential error responses.

<br/>

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



<br/>
<br/>
<br/>
<br/>
<br/>