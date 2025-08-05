# online-shopping-website

---

### ✅ **Account Service Responsibilities**

The **Account Service** handles everything related to **user account management** and **authentication**. Its main functions include:

1.  **Create Account (Registration)**

    -   Accept user details and create a new account.

    -   Validate:

        -   Unique email.

        -   Strong password policy.

    -   Hash password before storing.

    -   Optionally send a welcome email (future extension).

2.  **Update Account**

    -   Allow users to update:

        -   **Name**

        -   **Shipping Address**

        -   **Billing Address**

        -   **Payment Method**

    -   Ensure the email (username) is immutable or requires special verification if changed.

    -   Validate data before update.

3.  **Account Lookup**

    -   Retrieve user details by:

        -   User ID

        -   Email (for authentication or admin lookup).

    -   Exclude sensitive fields like password hash in responses.

4.  **Authentication & Token Generation**

    -   Login with email and password.

    -   Validate credentials against hashed password.

    -   Generate **JWT token** after successful login.

    -   Token will be used to authenticate requests to other services (Order, Payment).

5.  **Authorization**

    -   Apply **role-based authorization** (`ROLE_USER`, `ROLE_ADMIN`) for restricted actions like deleting accounts or admin lookups.

6.  **Password Management**

    -   Allow user to reset password securely (future extension).

    -   Current scope: update password via `PUT /account/{id}` with old password validation.

7.  **Delete Account** (Optional but common)

    -   Soft delete user or mark account as inactive (to preserve order history).


---

### ✅ **Account Data Model**

User account should store the following **fields**:

| Field | Required | Notes |
| --- | --- | --- |
| **id** | Yes | UUID (Primary Key) |
| **email** | Yes | Unique, used for login |
| **username** | Yes | Display name |
| **password** | Yes | Hashed (BCrypt) |
| **shippingAddress** | Yes | Full address object (street, city, zip, etc.) |
| **billingAddress** | Yes | Can be same as shipping |
| **paymentMethod** | Yes | Store masked card info or payment token |
| **roles** | Yes | Default `ROLE_USER` |
| **createdAt** | Yes | Timestamp |
| **updatedAt** | Yes | Timestamp |

---

### ✅ **Endpoints**

| HTTP Method | Endpoint | Description |
| --- | --- | --- |
| `POST` | `/api/accounts/register` | Create account |
| `POST` | `/api/accounts/login` | Login & get JWT |
| `GET` | `/api/accounts/me` | Get current account info |
| `GET` | `/api/accounts/{id}` | Get account by ID (admin only) |
| `PUT` | `/api/accounts/{id}` | Update account info |
| `DELETE` | `/api/accounts/{id}` | Delete account (optional) |

---

### ✅ **Key Security Logic**

-   Password:

    -   Hash using **BCryptPasswordEncoder**.

    -   Never store plain text.

-   JWT:

    -   `Authorization: Bearer <token>` in headers.

    -   Validate in every secured endpoint.

-   Sensitive Data:

    -   Exclude password and payment details from API responses.


---

### ✅ **Missing Pieces to Consider**

-   **Validation layer** (e.g., `@Valid` + custom constraints for strong passwords, valid emails).

-   **Error handling** (e.g., email already exists, invalid password).

-   **DTO mapping** (avoid exposing entity directly).

-   **Audit fields** (createdAt, updatedAt).

-   **Role management** (basic user vs admin).

-   **Integration with Payment Service** (future step: validate default payment method token).


---

Would you like me to now **write the complete Account Service skeleton code** including:

-   **Entity**

-   **DTOs**

-   **Repository**

-   **Service Layer**

-   **Controller**

-   **Spring Security (JWT-based)** configuration