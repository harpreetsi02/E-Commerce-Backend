# E-Commerce Backend API

A Spring Boot backend for a simplified e-commerce system, built as Project 2 in a self-guided Java + Spring Boot mentorship roadmap. Focus areas: many-to-many relationships via bridge entities, multi-table transactional workflows, query optimization, and idempotent API design.

## 🛠️ Tech Stack

- **Java 21**
- **Spring Boot 4.x**
- **Spring Data JPA / Hibernate**
- **MySQL / MariaDB**
- **Maven**
- No Lombok (boilerplate written manually by design, for deeper understanding of entity/DTO/mapper mechanics)

## 📦 Modules

| Module | Description |
|---|---|
| **Category** | Basic CRUD with unique-name constraint |
| **Product** | Linked to Category via `@ManyToOne`; supports search/filter/pagination |
| **User** | Simple user profile (no authentication yet — planned for Project 3) |
| **Cart & CartItem** | Cart-Product many-to-many relationship modeled as a bridge entity carrying `quantity` |
| **Order & OrderItem** | Transactional checkout flow: stock deduction, order creation, and cart clearing as a single atomic operation |

## 🎯 Core Concepts Implemented

- **Entity/DTO/Mapper separation** — manual mapping, no MapStruct, for full control and understanding
- **Bridge (join) entities** — `CartItem` / `OrderItem` model many-to-many relationships that carry extra data (`quantity`, `priceAtOrderTime`)
- **N+1 query problem** — identified via `show-sql` logging and resolved using `JOIN FETCH` queries across Product, Cart, and Order retrieval paths
- **`@Transactional` (real multi-table use case)** — order placement atomically updates product stock, creates the order + order items, and clears the cart; any failure (e.g. insufficient stock) rolls back all changes
- **Optimistic Locking (`@Version`)** — prevents lost-update race conditions on concurrent stock updates, surfaced as `409 Conflict`
- **Price snapshotting** — `OrderItem.priceAtOrderTime` preserves historical pricing independent of later product price changes
- **Order status state machine** — enum-encapsulated valid transitions (`PENDING → CONFIRMED → SHIPPED → DELIVERED`, `CANCELLED`), rejecting invalid jumps
- **Idempotency** — `Idempotency-Key` header prevents duplicate order creation on client retries
- **Specification API + Pagination** — dynamic, null-safe product filtering by name, category, and price range

## 📁 Package Structure

```
com.backend.ecommercebackend
├── entity
├── dto
│   ├── request
│   └── response
├── mapper
├── repository
├── specification
├── service
├── controller
└── exception
```

## 🔌 API Overview

| Method | Endpoint | Description |
|---|---|---|
| POST | `/category` | Create a category |
| GET | `/category` / `/category/{id}` | List / fetch categories |
| POST | `/products` | Create a product |
| GET | `/products/search` | Search/filter products (name, category, price range, pagination) |
| POST | `/users` | Create a user |
| GET / POST / DELETE | `/users/{userId}/cart/items` | View / add / remove cart items |
| POST | `/users/{userId}/orders` | Place an order (requires `Idempotency-Key` header) |
| GET | `/users/{userId}/orders` / `/{orderId}` | View order history / a specific order |
| PATCH | `/users/{userId}/orders/{orderId}/status` | Update order status |

## ⚠️ Known Limitations (by design, for this stage of the roadmap)

- No authentication/authorization — `userId` is passed directly in the URL path. This is a deliberate placeholder; real auth (Spring Security + JWT) is scoped for Project 3.
- No payment integration — order placement stops at stock deduction and order creation.

## 🗺️ Roadmap Context

This is Project 2 of a 10-project Java/Spring Boot mentorship series. Project 1 (Task Management API) covered pagination/filtering, exception handling, N+1 fundamentals, and `@Transactional` internals. Project 3 will introduce Spring Security, JWT, and role-based access control.