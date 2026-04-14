# FurniCraft — Project Structure

```
furnicraft/
├── pom.xml
├── src/
│   └── main/
│       ├── java/com/furnicraft/
│       │   ├── FurnicraftApplication.java
│       │   ├── config/
│       │   │   ├── SecurityConfig.java
│       │   │   ├── WebMvcConfig.java
│       │   │   └── DataInitializer.java
│       │   ├── controller/
│       │   │   ├── HomeController.java
│       │   │   ├── AuthController.java
│       │   │   ├── ProductController.java
│       │   │   ├── OrderController.java
│       │   │   ├── AppointmentController.java
│       │   │   ├── UserDashboardController.java
│       │   │   └── admin/
│       │   │       ├── AdminDashboardController.java
│       │   │       ├── AdminCategoryController.java
│       │   │       ├── AdminProductController.java
│       │   │       ├── AdminWoodTypeController.java
│       │   │       ├── AdminOrderController.java
│       │   │       ├── AdminAppointmentController.java
│       │   │       └── AdminUserController.java
│       │   ├── dto/
│       │   │   ├── UserRegistrationDto.java
│       │   │   ├── ProductDto.java
│       │   │   ├── CategoryDto.java
│       │   │   ├── WoodTypeDto.java
│       │   │   ├── OrderDto.java
│       │   │   ├── OrderItemDto.java
│       │   │   └── AppointmentDto.java
│       │   │   └── OrderRequestDto.java
│       │   ├── entity/
│       │   │   ├── User.java
│       │   │   ├── Role.java
│       │   │   ├── Category.java
│       │   │   ├── Product.java
│       │   │   ├── ProductImage.java
│       │   │   ├── WoodType.java
│       │   │   ├── ProductWoodPrice.java
│       │   │   ├── Order.java
│       │   │   ├── OrderItem.java
│       │   │   └── AppointmentRequest.java
│       │   ├── enums/
│       │   │   ├── OrderStatus.java
│       │   │   ├── AppointmentStatus.java
│       │   │   └── StockStatus.java
│       │   ├── exception/
│       │   │   ├── GlobalExceptionHandler.java
│       │   │   ├── ResourceNotFoundException.java
│       │   │   └── FileStorageException.java
│       │   ├── repository/
│       │   │   ├── UserRepository.java
│       │   │   ├── RoleRepository.java
│       │   │   ├── CategoryRepository.java
│       │   │   ├── ProductRepository.java
│       │   │   ├── ProductImageRepository.java
│       │   │   ├── WoodTypeRepository.java
│       │   │   ├── ProductWoodPriceRepository.java
│       │   │   ├── OrderRepository.java
│       │   │   ├── OrderItemRepository.java
│       │   │   └── AppointmentRequestRepository.java
│       │   └── service/
│       │       ├── UserService.java
│       │       ├── ProductService.java
│       │       ├── CategoryService.java
│       │       ├── WoodTypeService.java
│       │       ├── OrderService.java
│       │       ├── AppointmentService.java
│       │       ├── FileStorageService.java
│       │       └── impl/
│       │           ├── UserServiceImpl.java
│       │           ├── ProductServiceImpl.java
│       │           ├── CategoryServiceImpl.java
│       │           ├── WoodTypeServiceImpl.java
│       │           ├── OrderServiceImpl.java
│       │           ├── AppointmentServiceImpl.java
│       │           └── FileStorageServiceImpl.java
│       └── resources/
│           ├── application.properties
│           ├── static/
│           │   ├── css/
│           │   │   └── style.css
│           │   └── js/
│           │       └── main.js
│           └── templates/
│               ├── layouts/
│               │   └── main.html
│               ├── fragments/
│               │   ├── header.html
│               │   ├── footer.html
│               │   ├── navbar.html
│               │   ├── admin-sidebar.html
│               │   └── messages.html
│               ├── home.html
│               ├── auth/
│               │   ├── login.html
│               │   └── register.html
│               ├── products/
│               │   ├── list.html
│               │   └── details.html
│               ├── user/
│               │   ├── dashboard.html
│               │   ├── orders.html
│               │   └── appointments.html
│               ├── admin/
│               │   ├── dashboard.html
│               │   ├── categories/
│               │   │   ├── list.html
│               │   │   └── form.html
│               │   ├── products/
│               │   │   ├── list.html
│               │   │   └── form.html
│               │   ├── wood-types/
│               │   │   ├── list.html
│               │   │   └── form.html
│               │   ├── orders/
│               │   │   ├── list.html
│               │   │   └── details.html
│               │   ├── appointments/
│               │   │   └── list.html
│               │   └── users/
│               │       └── list.html
│               └── error/
│                   ├── 403.html
│                   ├── 404.html
│                   └── 500.html
```