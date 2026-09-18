# JWT Utils

A small web application that provides utilities for JSON Web Tokens (JWT) and password encoding.

## Features

- **Password Encoder:** Encodes a raw password using BCrypt.
- **HS512 Secret Key Generator:** Generates a 64-byte symmetric key for JWT signing with the HS512 algorithm.
- **ECDSA P-256 Key Pair Generator:** Generates an asymmetric key pair (secp256r1) for JWT signing with the ES256 algorithm.

## Tech Stack

- **Backend:**
  - Java 21
  - Spring Boot 3
  - Maven
- **Frontend:**
  - HTML
  - CSS
  - JavaScript

## Architecture

Layered (Controller → Service), no persistence.

```
config/     SecurityConfig - security filter chain and response headers
            PasswordEncoderConfig - the BCrypt PasswordEncoder bean
controller/ JwtUtilsController - REST entry point under /api
service/    PasswordEncoderService, JwtSecretKeyGenerator
dto/        request and response payloads, the API contract
model/      EncodedKeyPair - service return type, decoupled from the API contract
exception/  KeyGenerationException and the GlobalExceptionHandler advice
```

ADR: the frontend is served as static content from the backend itself, so every request is
same-origin and no CORS configuration is needed. `script.js` calls the API through relative
paths (`/api/...`) to keep that true in any environment. Serving the frontend from a different
origin would require adding an explicit CORS mapping.

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

2.  Run the application:
    ```sh
    ./mvnw spring-boot:run
    ```

3.  Open your browser and navigate to `http://localhost:8080`.

### Running the tests

```sh
./mvnw test
```

## Usage

The application provides a simple web interface with a dark theme and a Bento Grid layout.

- **Password Encoder:** Enter a password and click "Encode" to see the BCrypt hash.
- **HS512 Secret Key Generator:** Click "Generate" to get a new symmetric secret key.
- **ECDSA P-256 Secret Key Generator:** Click "Generate" to get a new public/private key pair.

## API Endpoints

### Encode Password

- **URL:** `/api/encode`
- **Method:** `POST`
- **Request Body:**
  ```json
  {
    "password": "your-password"
  }
  ```
- **Response `200`:**
  ```json
  {
    "encodedPassword": "..."
  }
  ```
- **Response `400`:** returned when `password` is missing or blank.
  ```json
  {
    "message": "password must not be blank"
  }
  ```

### Generate HS512 Secret Key

- **URL:** `/api/generateHS512`
- **Method:** `GET`
- **Response `200`:** a Base64Url encoded 512-bit key.
  ```json
  {
    "secretKey": "..."
  }
  ```

### Generate ECDSA P-256 Key Pair

- **URL:** `/api/generateECDSAP256`
- **Method:** `GET`
- **Response `200`:** Base64 encoded keys, X.509 for the public one and PKCS#8 for the private one.
  ```json
  {
    "publicKey": "...",
    "privateKey": "..."
  }
  ```
- **Response `500`:** returned when the runtime cannot provide the secp256r1 curve.

## Security Considerations

- **Same-origin only:** no CORS mapping is declared, so the API is only reachable from the
  origin that serves the UI. See the ADR above before changing the deployment topology.
- **Response headers:** `SecurityConfig` sets a restrictive Content Security Policy, HSTS with
  a two-year max age, and denies framing.
- **Password Encoding:** passwords are hashed with Spring Security's `BCryptPasswordEncoder`.
- **No Sensitive Logging:** neither raw passwords nor generated keys are ever logged.
- **Generated keys are secrets:** they are returned once and never stored. Keep them out of
  version control and put them in your own secret manager.
