Store Management System
A RESTful store management application designed to simplify inventory tracking, enhance operational visibility, and provide a robust audit trail. Built with modern tools and technologies, the application ensures scalability, maintainability, and reliability.

Features
Inventory Management: Add, update, delete, and sell items in stock.
Real-time Event Tracking: Kafka-powered messaging for item transactions.
Comprehensive Reporting: Generate stock reports and audit logs.
RESTful API: Intuitive API for seamless integration.
Audit Trail: Detailed history of all inventory operations.
Technology Stack
Language: Java 17+
Framework: Spring Boot 3.x
Database: PostgreSQL
Messaging: Apache Kafka
Containerization: Docker & Docker Compose
Testing: JUnit, Mockito
Build Tool: Gradle
Getting Started
Prerequisites
Ensure you have the following installed:

Docker and Docker Compose
Java 17+
Gradle or Maven
Setup Instructions
Clone the Repository
git clone https://github.com/your-repo/store-management-system.git
cd store-management-system

Start Infrastructure
Spin up PostgreSQL and Kafka using Docker Compose:
docker-compose up -d

Run the Application
Use Gradle to start the application:
./gradlew bootRun

Access the API
The API will be available at:
http://localhost:8080

API Documentation
Explore detailed API documentation via Swagger:
http://localhost:8080/v3/api-docs

Endpoints Overview
Create Item
POST /api/v1/items
Request Body:
{ "name": "Item Name", "price": 99.99, "quantity": 100 }
Update Item
PUT /api/v1/items/{id}
Request Body:
{ "name": "Updated Name", "price": 149.99, "quantity": 150 }
Delete Item
DELETE /api/v1/items/{id}
Sell Item
POST /api/v1/items/sell/{id}?quantity=5
Stock Report
GET /api/v1/items/report
Response:
[ { "id": 1, "name": "Item Name", "price": 99.99, "quantity": 95, "soldQuantity": 5 } ]
Audit Trail
GET /api/v1/items/audit
Response:
[ { "id": 1, "itemId": 1, "itemName": "Item Name", "action": "SOLD", "price": 99.99, "quantity": 5, "stockLevel": 95, "description": "Item sold", "timestamp": "2025-01-10T02:33:00.783Z" } ]
Architecture
This project adheres to clean architecture principles:

Controllers: Handle HTTP requests and responses.
Services: Contain business logic.
Repositories: Manage data access.
Kafka Integration: Handle messaging for item transactions.
Auditing: Tracks all operations and maintains historical records.
Testing
Run all tests to ensure functionality and code quality:
./gradlew test

Test Coverage
Unit Tests: Covers core business logic.
Integration Tests: Ensures API and data layer integrity.
Code Quality
The project follows best practices:

SOLID Principles: Ensure maintainable and scalable code.
KISS and DRY Principles: Keep the code simple and avoid redundancy.
Javadoc Documentation: Every method and class is thoroughly documented.
Logging: Detailed logs for debugging and monitoring.
Error Handling: Graceful handling of exceptions.

Store Management System
A RESTful store management application designed to simplify inventory tracking, enhance operational visibility, and provide a robust audit trail. Built with modern tools and technologies, the application ensures scalability, maintainability, and reliability.

Features
Inventory Management: Add, update, delete, and sell items in stock.
Real-time Event Tracking: Kafka-powered messaging for item transactions.
Comprehensive Reporting: Generate stock reports and audit logs.
RESTful API: Intuitive API for seamless integration.
Audit Trail: Detailed history of all inventory operations.
Technology Stack
Language: Java 17+
Framework: Spring Boot 3.x
Database: PostgreSQL
Messaging: Apache Kafka
Containerization: Docker & Docker Compose
Testing: JUnit, Mockito
Build Tool: Gradle
Getting Started
Prerequisites
Ensure you have the following installed:

Docker and Docker Compose
Java 17+
Gradle or Maven
Setup Instructions
Clone the Repository
git clone https://github.com/your-repo/store-management-system.git
cd store-management-system

Start Infrastructure
Spin up PostgreSQL and Kafka using Docker Compose:
docker-compose up -d

Run the Application
Use Gradle to start the application:
./gradlew bootRun

Access the API
The API will be available at:
http://localhost:8080

API Documentation
Explore detailed API documentation via Swagger:
http://localhost:8080/v3/api-docs

Endpoints Overview
Create Item
POST /api/v1/items
Request Body:
json
Copy code
{
  "name": "Item Name",
  "price": 99.99,
  "quantity": 100
}
Update Item
PUT /api/v1/items/{id}
Request Body:
json
Copy code
{
  "name": "Updated Name",
  "price": 149.99,
  "quantity": 150
}
Delete Item
DELETE /api/v1/items/{id}
Sell Item
POST /api/v1/items/sell/{id}?quantity=5
Stock Report
GET /api/v1/items/report
Response:
json
Copy code
[
  {
    "id": 1,
    "name": "Item Name",
    "price": 99.99,
    "quantity": 95,
    "soldQuantity": 5
  }
]
Audit Trail
GET /api/v1/items/audit
Response:
json
Copy code
[
  {
    "id": 1,
    "itemId": 1,
    "itemName": "Item Name",
    "action": "SOLD",
    "price": 99.99,
    "quantity": 5,
    "stockLevel": 95,
    "description": "Item sold",
    "timestamp": "2025-01-10T02:33:00.783Z"
  }
]
Architecture
This project adheres to clean architecture principles:

Controllers: Handle HTTP requests and responses.
Services: Contain business logic.
Repositories: Manage data access.
Kafka Integration: Handle messaging for item transactions.
Auditing: Tracks all operations and maintains historical records.
Testing
Run all tests to ensure functionality and code quality:
./gradlew test

Test Coverage
Unit Tests: Covers core business logic.
Integration Tests: Ensures API and data layer integrity.
Code Quality
The project follows best practices:

SOLID Principles: Ensure maintainable and scalable code.
KISS and DRY Principles: Keep the code simple and avoid redundancy.
Javadoc Documentation: Every method and class is thoroughly documented.
Logging: Detailed logs for debugging and monitoring.
Error Handling: Graceful handling of exceptions.


readme

# Store Management System

A RESTful store management application built with Spring Boot, PostgreSQL, and Kafka.

## Features

- Full CRUD operations for inventory items
- Real-time event tracking with Kafka
- Audit trail for all operations
- RESTful API
- Comprehensive test coverage

## Technology Stack

- Java 17
- Spring Boot 3.x
- PostgreSQL
- Apache Kafka
- Docker & Docker Compose

## Getting Started

### Prerequisites
- Docker and Docker Compose
- Java 17+
- Maven/Gradle

### Setup

1. Clone the repository
2. Start infrastructure:
  bash
docker-compose up -d
 
3. Run the application:  bash
./gradlew bootRun
  ## API Documentation

### Items API

#### Create Item
POST /api/v1/items  json
{
"name": "Item Name",
"price": 99.99,
"quantity": 100
}
 #### Update Item
PUT /api/v1/items/{id} json
{
"name": "Updated Name",
"price": 149.99,
"quantity": 150
}
 
#### Delete Item
DELETE /api/v1/items/{id}

#### Sell Item
POST /api/v1/items/sell/{id}?quantity=5

#### Get Stock Report
GET /api/v1/items/report

#### Get Audit Trail
GET /api/v1/items/audit

## Architecture

The application follows clean architecture principles:
- Controllers handle HTTP requests
- Services contain business logic
- Repositories manage data access
- Kafka handles event messaging
- Audit trail tracks all operations

## Testing

Run tests:  bash
./gradlew test
 
## Code Quality

The project maintains high code quality through:
- Unit tests
- Integration tests
- Clean code practices
- SOLID principles
- Proper error handling
- Detailed logging 
