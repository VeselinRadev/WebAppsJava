# Spring Boot + PostgreSQL Dockerized Application

---

## 📦 Tech Stack

- Java 24
- Spring Boot 3.5.0
- PostgreSQL
- Docker + Docker Compose

---

## 🔧 Prerequisites

Make sure you have the following installed:

- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)

---

## 🗂️ Project Structure

```
.
├── src/                  # Source code
├── Dockerfile            # Builds the app into an image
├── docker-compose.yml    # Orchestrates app + DB
├── .env                  # Environment variables
├── application.yml       # Spring configuration (or application.properties)
└── README.md
```

---

## 🧪 .env Configuration

Create a `.env` file in the project root:

```env
DB_HOST=insurance
DB_PORT=5432
DB_NAME=insurance
DB_USER=user
DB_PASS=password
```

---

## 🐳 Running the App with Docker

### 1. Build & Run

```bash
docker-compose up --build
```

This will:

- Build the Spring Boot app using the `Dockerfile`
- Start the PostgreSQL database
- Set up environment variables from `.env`
- Map ports:
  - Spring Boot: [http://localhost:8080](http://localhost:8080)
  - PostgreSQL: `localhost:5432`

---

### 2. Stopping the App

```bash
docker-compose down
```

Use `-v` to also remove the volume:

```bash
docker-compose down -v
```

---

## 🧼 Cleaning Up

To remove all containers and images:

```bash
docker system prune -a
```

## DB structure

### Clients
- client_id - string uuid - pk
- first_name - string
- last-name - string
- email - string
- phonenumber
- address

### Cars
- car-plate - string - pk
- client_id - string - fk
- make - string
- model - string
- year - int
- vin - string

### Insurer
- insurer_id - string uuid - pk
- name - string
- email - string
- phone_number - string
- address

### Insurances
- insurance_id - string uuid - pk
- client_id - string - fk
- car-plate - string - fk
- insurer_id - string uuid - fk
- policy-number - long
- start-date - date
- end-date - date
- amount - int
- details - string

### Payments
- payment_id - string uuid - pk
- insurance-id - string - fk
- payment_date - date
- amount - int
- payment-method - enum { credit, debit, transfer }


## Table Relationships
-	Client → Cars (One-to-Many)
-	Client → Insurances (One-to-Many)
-	Cars → Insurances (One-to-Many)
-	Insurer → Insurances (One-to-Many)
-	Insurances → Payments (One-to-Many)