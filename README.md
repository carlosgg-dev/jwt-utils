# JWT Utils

A simple web application that provides utilities for JSON Web Tokens (JWT) and password encoding.

## Features

- **Password Encoder:** Encodes a raw password using BCrypt.
- **JWT Secret Key Generator:** Generates a secure 64-byte secret key for JWT signing with the HS512 algorithm.

## Tech Stack

- **Backend:**
  - Java 21
  - Spring Boot 3
  - Maven
- **Frontend:**
  - HTML
  - CSS
  - JavaScript

## Getting Started

### Prerequisites

- Java 21 or later
- Maven 3.6 or later

### Installation and Running

1.  Clone the repository:
    ```sh
    git clone https://github.com/your-username/jwt-utils.git
    cd jwt-utils
    ```

2.  **Important:** Before running the application, open `src/main/java/jwt/jwt_utils/security/WebConfig.java` and replace `"https://your-frontend-domain.com"` with the actual origin of your frontend application. This is a crucial security measure to prevent unauthorized domains from accessing your API.

3.  Run the application using Maven:
    ```sh
    ./mvnw spring-boot:run
    ```

4.  Open your browser and navigate to `http://localhost:8080`.

## Usage

The application provides a simple and interactive web interface with a modern dark theme and a Bento Grid layout.

- **Password Encoder:** Enter a password in the input field and click "Encode" to see the BCrypt-encoded hash.
- **JWT Secret Key Generator:** Click the "Generate" button to get a new secure secret key.

## API Endpoints

The application exposes the following REST API endpoints:

### Encode Password

- **URL:** `/api/encode`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
    "password": "your-password"
  }
  ```
- **Response:**
  ```json
  {
    "encodedPassword": "..."
  }
  ```

### Generate JWT Secret Key

- **URL:** `/api/generate`
- **Method:** `GET`
- **Response:**
  ```json
  {
    "secretKey": "..."
  }
  ```

## Security Considerations

- **CORS Configuration:** The application uses a restrictive CORS policy. You must configure the allowed origins in `src/main/java/jwt/jwt_utils/security/WebConfig.java` to match your frontend application's domain.
- **Password Encoding:** The application uses Spring Security's `BCryptPasswordEncoder` to encode passwords, which is a strong and widely-used hashing algorithm.
- **No Sensitive Logging:** The application is configured to avoid logging sensitive information such as passwords or generated secret keys.
