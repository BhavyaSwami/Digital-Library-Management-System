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
