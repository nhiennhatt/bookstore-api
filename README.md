# Bookstore API

A Spring Boot REST API for managing an online bookstore. The application supports user authentication, book catalog
management, categories, book variants, collections, orders, addresses, payment intent creation, object storage
integration, and shipping-address lookup through GHN.

## Tech Stack

- Java
- Spring Boot
- Spring MVC
- Spring Data JPA
- Spring Security
- Jakarta Validation
- PostgreSQL
- Lombok
- Springdoc OpenAPI / Swagger UI
- MinIO
- Stripe
- GHN shipping/address APIs
- Maven

## Start develop

### Prerequisites

- **JDK 17** (aligned with `java.version` in `pom.xml`)
- **Maven**, or use the included wrapper (`./mvnw`)
- **PostgreSQL** running locally (default URL targets `localhost:5432`, database name `bookstore`)
- **MinIO** on `127.0.0.1:9000` if you exercise uploads / object storage
- Optional API credentials (**Stripe**, **GHN**) when testing checkout, webhooks, or shipping lookups

### Database

Create an empty PostgreSQL database named `bookstore`. Point `spring.datasource.url`, username, and password at your
instance (see Spring Boot externalized configuration below).

### Configuration

Do **not** commit real secrets. Prefer environment variables or a local properties file kept out of git (for example
`application-local.properties` with `spring.profiles.active=local`). 
- Define key-value pairs in `src/main/resources/application.properties` (see `sample.app.properties` for reference)

### Run the API

From the repository root:

```bash
./mvnw spring-boot:run
```

Run the test suite:

```bash
./mvnw test
```

With the app up, Swagger UI is available at `/api-docs` (see `springdoc.swagger-ui.path` in `application.properties`).

## Main Features

- User registration, login, and refresh-token authentication
- JWT-based protected endpoints
- Role-based access control for administration/content management operations
- Book listing, detail lookup, image upload, and category assignment
- Category CRUD and category image upload
- Book variant CRUD and variant image upload
- Book collections with sortable books
- Order creation, preview, lookup, and admin filtering
- Stripe payment intent creation
- Authenticated user profile, addresses, and order history
- GHN province, district, and ward lookup
- OpenAPI documentation support

## Project Structure

- `bookstoreapi`
    - `common`
        - `classes`
        - `enums`
        - `filters`
    - `configs`
    - `controllers`
    - `dto`
    - `exceptions`
    - `models`
    - `repository`
        - `customs`
        - `projection`
    - `services`
    - `utils`
    - `validations`

## Domain Entities

The application defines the following JPA entities:

| Entity              | Table                  | Purpose                                                |
|---------------------|------------------------|--------------------------------------------------------|
| `User`              | `users`                | Application users and authentication-related user data |
| `Book`              | `books`                | Book catalog records                                   |
| `BookVariant`       | `book_variants`        | Sellable variants of a book, such as format or edition |
| `Category`          | `categories`           | Book categories                                        |
| `Order`             | `orders`               | Customer orders                                        |
| `OrderDetail`       | `order_details`        | Individual order line items                            |
| `OrderCoupon`       | `order_coupons`        | Coupons applied to orders                              |
| `Coupon`            | `coupons`              | Coupon definitions                                     |
| `CouponWallet`      | `coupon_wallets`       | User-owned coupons                                     |
| `CouponRestriction` | `coupon_restrictions`  | Coupon usage restrictions                              |
| `UserAddress`       | `user_addresses`       | Saved user shipping addresses                          |
| `Review`            | `reviews`              | Book reviews                                           |
| `Notify`            | `notifies`             | User notifications                                     |
| `BookCollection`    | `collections`          | Curated book collections                               |
| `CollectionBook`    | collection-books table | Books assigned to collections                          |

