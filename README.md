# 📚 Digital-Library-Management-System

A microservices-based **Library Management System** built with **Spring Boot**. The application consists of two independent services:  
- **Book Service**: Manages book information and availability.  
- **User Service**: Handles user operations and book borrowing/return activities.

---

## 🏗️ Architecture

The system follows a microservice architecture with two RESTful services:

| Service        | Port  | Description                                |
|----------------|-------|--------------------------------------------|
| `Book Service` | 8081  | Manages books, search, and availability     |
| `User Service` | 8082  | Manages users and borrowing transactions    |

Each service runs independently and uses an in-memory H2 database.

---

## ✨ Features

### 📘 Book Service
- CRUD operations for books
- Search by **title**, **author**, or **ISBN**
- Track book availability
- Borrow and return books
- Calculate penalty for overdue returns

### 👤 User Service
- CRUD operations for users
- Search by **name**, **email**, or **membership ID**
- Borrow and return books
- View borrowing history and active borrowings

---

## 🛠️ Tech Stack

- Java 17  
- Spring Boot  
- Spring Web / REST  
- H2 Database  
- Maven

---

## 🚀 Getting Started

### ✅ Prerequisites
- Java 17+
- Maven 3.6+

### 🔧 Running the Services

#### Book Service (Port 8081)
```bash
cd book-service
mvn spring-boot:run
```

#### User Service (Port 8082)
```bash
cd user-service
mvn spring-boot:run
```

## 🔌 API Endpoints

### 📘 Book Service – http://localhost:8081

#### 📗 Book Management
```
GET    /api/books                             // Get all books  
GET    /api/books/{id}                        // Get book by ID  
POST   /api/books                             // Add a new book  
PUT    /api/books/{id}                        // Update a book  
DELETE /api/books/{id}                        // Delete a book  
```

#### 🔍 Search
```
GET /api/books/search/title?title={title}     // Search books by title  
GET /api/books/search/author?author={author}  // Search books by author  
```

#### 📦 Availability & Borrowing
```
PATCH /api/books/{id}/availability?available={true|false}   // Update book availability  
POST  /api/books/{id}/borrow?userId={userId}                // Borrow a book  
POST  /api/books/{id}/return                                // Return a book  
GET   /api/books/{id}/penalty                               // Calculate penalty for overdue  
```

---

### 👤 User Service – http://localhost:8082

#### 🧑 User Management
```
GET    /api/users                                   // Get all users  
GET    /api/users/{id}                              // Get user by ID  
POST   /api/users                                   // Create a new user  
PUT    /api/users/{id}                              // Update a user  
DELETE /api/users/{id}                              // Delete a user  
```

#### 🔍 Search
```
GET /api/users/search/name?name={name}                       // Search users by name  
GET /api/users/search/email?email={email}                   // Search users by email  
GET /api/users/search/membership?membershipId={id}          // Search users by membership ID  
```

#### 📚 Borrowing
```
GET  /api/borrowings                                        // Get all borrowings  
GET  /api/borrowings/{id}                                   // Get borrowing by ID  
GET  /api/borrowings/user/{userId}                          // Get borrowings for a user  
GET  /api/borrowings/user/{userId}/active                   // Get active borrowings for a user  
POST /api/borrowings/borrow?userId={userId}&bookId={bookId} // Borrow a book  
POST /api/borrowings/{id}/return                            // Return a book  
```
