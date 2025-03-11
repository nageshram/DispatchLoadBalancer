# Dispatch Load Balancer - Spring Boot Application

This project is a Spring Boot application designed to optimize the allocation of delivery orders to a fleet of vehicles based on their locations. The application minimizes the total travel distance while considering vehicle capacities and order priorities.

---

## **Technologies Used**
- **Spring Boot**: 3.x
- **Java**: 17
- **MySQL**: 8.x (for production)
- **H2 Database**: 2.x (for testing)
- **Maven**: 3.x (for dependency management)
- **Mockito**: 5.x (for unit testing)
- **Postman**: For API testing

---

## **Features**
1. **Order Management**:
   - Accept delivery orders with details like `orderId`, `latitude`, `longitude`, `address`, `packageWeight`, and `priority`.
2. **Vehicle Management**:
   - Accept vehicle details with `vehicleId`, `capacity`, `currentLatitude`, `currentLongitude`, and `currentAddress`.
3. **Dispatch Optimization**:
   - Assign orders to vehicles based on priority and capacity.
   - Minimize total travel distance using the Haversine formula.
4. **Error Handling**:
   - Handle invalid input, overcapacity, and unassignable orders gracefully.

---

## **Implementation Details**
The application uses the **Haversine formula** to calculate the distance between two geographic coordinates (latitude and longitude). Orders are assigned to vehicles based on the following rules:
1. **Priority**: High-priority orders are assigned first.
2. **Capacity**: Orders are assigned to vehicles without exceeding their capacity.
3. **Distance**: Orders are assigned to the closest available vehicle.

---

## **How to Run the Project**

### **Prerequisites**
1. **Java 17**: Ensure Java 17 is installed.
2. **MySQL**: Install and run MySQL Server.
3. **Maven**: Install Maven for dependency management.

### **Steps to Run**
1. **Clone the Repository**:
   ```bash
   git clone https://github.com/nageshram/DispatchLoadBalancer.git
   cd DispatchLoadBalancer
   ```

2. **Configure MySQL**:
   - Create a database named `dispatch_db` in MySQL.
   - Update the `application.properties` file with your MySQL credentials:
     ```properties
     spring.datasource.url=jdbc:mysql://localhost:3306/dispatch_db?useSSL=false&serverTimezone=UTC
     spring.datasource.username=root
     spring.datasource.password=yourpassword
     ```

3. **Build the Project**:
   ```bash
   mvn clean install
   ```

4. **Run the Application**:
   ```bash
   mvn spring-boot:run
   ```

5. **Access the Application**:
   - The application will start at `http://localhost:8080`.

---

## **API Endpoints**

### **1. Input Delivery Orders**
- **Endpoint**: `POST /api/dispatch/orders`
- **Request Body**:
  ```json
  {
    "orders": [
      {
        "orderId": "ORD001",
        "latitude": 12.9716,
        "longitude": 77.5946,
        "address": "MG Road, Bangalore, Karnataka, India",
        "packageWeight": 10,
        "priority": "HIGH"
      },
      {
        "orderId": "ORD002",
        "latitude": 13.0827,
        "longitude": 80.2707,
        "address": "Anna Salai, Chennai, Tamil Nadu, India",
        "packageWeight": 20,
        "priority": "MEDIUM"
      }
    ]
  }
  ```
- **Response**:
  ```json
  {
    "message": "Delivery orders accepted.",
    "status": "success"
  }
  ```

### **2. Input Fleet Details**
- **Endpoint**: `POST /api/dispatch/vehicles`
- **Request Body**:
  ```json
  {
    "vehicles": [
      {
        "vehicleId": "VEH001",
        "capacity": 100,
        "currentLatitude": 12.9716,
        "currentLongitude": 77.6413,
        "currentAddress": "Indiranagar, Bangalore, Karnataka, India"
      },
      {
        "vehicleId": "VEH002",
        "capacity": 150,
        "currentLatitude": 13.0674,
        "currentLongitude": 80.2376,
        "currentAddress": "T Nagar, Chennai, Tamil Nadu, India"
      }
    ]
  }
  ```
- **Response**:
  ```json
  {
    "message": "Vehicle details accepted.",
    "status": "success"
  }
  ```

### **3. Retrieve Dispatch Plan**
- Here i am not throwing an exception for unassigned orders instead of that i had included the unassigned orders along with dispatch plan so that , we can track unassigned orders and assign the vehicle in the next dispatch plan, observe the response at the end ( if 2 unassigned order present then response will follow like this..)
```json
{
"status":"partial_success",
 "message":"2 orders unassigned due to capacity constraints",
 "dispatchPlan": [
      {
        "vehicleId": "VEH001",
        "totalLoad": 30,
        "totalDistance": "15.25 km",
        "assignedOrders": [
          {
            "orderId": "ORD001",
            "latitude": 12.9716,
            "longitude": 77.5946,
            "address": "MG Road, Bangalore, Karnataka, India",
            "packageWeight": 10,
            "priority": "HIGH"
          }
        ]
      },
      {
        "vehicleId": "VEH002",
        "totalLoad": 20,
        "totalDistance": "10.50 km",
        "assignedOrders": [
          {
            "orderId": "ORD002",
            "latitude": 13.0827,
            "longitude": 80.2707,
            "address": "Anna Salai, Chennai, Tamil Nadu, India",
            "packageWeight": 20,
            "priority": "MEDIUM"
          }
    ],
"unassignedOrders":[
         {
            "orderId": "ORD003",
            "latitude": 33.0827,
            "longitude": 60.2707,
            "address": "Street example, Banglore, Karnataka, India",
            "packageWeight": 20,
            "priority": "MEDIUM"
          },
         {
            "orderId": "ORD004",
            "latitude": 15.0827,
            "longitude": 90.2707,
            "address": "Some Home, Chennai, Tamil Nadu, India",
            "packageWeight": 20,
            "priority": "MEDIUM"
          }

]
}
```
-- if no un assigned orders then response will look like this.
- **Endpoint**: `GET /api/dispatch/plan`
- **Response**:
  ```json
  {
    "status": "success",
    "message": "All orders assigned successfully",
    "dispatchPlan": [
      {
        "vehicleId": "VEH001",
        "totalLoad": 30,
        "totalDistance": "15.25 km",
        "assignedOrders": [
          {
            "orderId": "ORD001",
            "latitude": 12.9716,
            "longitude": 77.5946,
            "address": "MG Road, Bangalore, Karnataka, India",
            "packageWeight": 10,
            "priority": "HIGH"
          }
        ]
      },
      {
        "vehicleId": "VEH002",
        "totalLoad": 20,
        "totalDistance": "10.50 km",
        "assignedOrders": [
          {
            "orderId": "ORD002",
            "latitude": 13.0827,
            "longitude": 80.2707,
            "address": "Anna Salai, Chennai, Tamil Nadu, India",
            "packageWeight": 20,
            "priority": "MEDIUM"
          }
        ]
      }
    ],
    "unassignedOrders": []
  }
  ```

---
 **Test Cases**

 **Input 1**
- **Orders**:
  ```json
  {
    "orders": [
      {
        "orderId": "ORD001",
        "latitude": 12.9716,
        "longitude": 77.5946,
        "address": "MG Road, Bangalore, Karnataka, India",
        "packageWeight": 10,
        "priority": "HIGH"
      },
      {
        "orderId": "ORD002",
        "latitude": 13.0827,
        "longitude": 80.2707,
        "address": "Anna Salai, Chennai, Tamil Nadu, India",
        "packageWeight": 20,
        "priority": "MEDIUM"
      }
    ]
  }
  ```
- **Vehicles**:
  ```json
  {
    "vehicles": [
      {
        "vehicleId": "VEH001",
        "capacity": 100,
        "currentLatitude": 12.9716,
        "currentLongitude": 77.6413,
        "currentAddress": "Indiranagar, Bangalore, Karnataka, India"
      },
      {
        "vehicleId": "VEH002",
        "capacity": 150,
        "currentLatitude": 13.0674,
        "currentLongitude": 80.2376,
        "currentAddress": "T Nagar, Chennai, Tamil Nadu, India"
      }
    ]
  }
  ```

### **Output 1**
- **Dispatch Plan**:
  ```json
  {
    "status": "success",
    "message": "All orders assigned successfully",
    "dispatchPlan": [
      {
        "vehicleId": "VEH001",
        "totalLoad": 10,
        "totalDistance": "5.25 km",
        "assignedOrders": [
          {
            "orderId": "ORD001",
            "latitude": 12.9716,
            "longitude": 77.5946,
            "address": "MG Road, Bangalore, Karnataka, India",
            "packageWeight": 10,
            "priority": "HIGH"
          }
        ]
      },
      {
        "vehicleId": "VEH002",
        "totalLoad": 20,
        "totalDistance": "10.50 km",
        "assignedOrders": [
          {
            "orderId": "ORD002",
            "latitude": 13.0827,
            "longitude": 80.2707,
            "address": "Anna Salai, Chennai, Tamil Nadu, India",
            "packageWeight": 20,
            "priority": "MEDIUM"
          }
        ]
      }
    ],
    "unassignedOrders": []
  }
  ```

---

## **Contact**
For any questions or issues, please contact:
- **Your Name**: [Nagesha](mailto:nagesha2r@gmail.com)

---
