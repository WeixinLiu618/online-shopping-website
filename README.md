`online-shopping-website`

### **Core Microservices**

1. **AccountService**

   * Manages user accounts, roles, and profile information.

   * Stores account data in MySQL.

2. **AuthService**

   * Handles authentication – Login.

   * Uses JWT tokens.

   * Integrates with `AccountService` to validate users.

3. **ItemService**

   * Provides item metadata and inventory management.

   * Stores data in MongoDB.

   * GET endpoints are public, while POST/PATCH are restricted to `ROLE_ADMIN`.

4. **OrderService**

   * Handles order creation, updates, and status tracking.

   * Supports both synchronous REST APIs and asynchronous Kafka events (`order.created`, `order.paid`, `order.cancelled`).

   * Uses Cassandra to persist order states.

5. **PaymentService**

   * Simulates payment flow (submit, lookup).

   * Listens for `order.created` events and publishes `order.paid`.

   * Stores payment records (possibly Cassandra as well).

### **Infrastructure Components**

6. **API\_Gateway**

   * Central entry point for clients.

   * Routes requests to appropriate services.

   * Secures endpoints using Spring Cloud Gateway.

7. **EurekaServer**

   * Service discovery for all microservices.

   * Each service registers itself to Eureka.

未完待续：

* Thorough exception handling  
* Wallet Service （mock payment)  
* Acid of db  
1. How will the item quantity change after order, and what if multiple people order simultaneously(lack of item)—-- fallback  
   1. After order, send msg to item service with order timestamp( make sure item db 做完一次quantity update 才能进行下一次 乐观锁. If lack of item, cancel order  
   2. After payment successfully, send msg to item service,  make sure item db 做完一次quantity update 才能进行下一次 乐观锁. If lack of item, do the refund and cancel order

Kafka ：同一个event 不要放不同的topic
