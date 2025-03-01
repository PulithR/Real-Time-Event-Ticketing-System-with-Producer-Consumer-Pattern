# Real-Time Event Ticketing System with Producer-Consumer Pattern

## Overview
The Real-Time Event Ticketing System is a simulation designed to manage ticket sales and purchases using the Producer-Consumer pattern. The system ensures concurrency management, allowing multiple vendors to release tickets and customers to retrieve them simultaneously in a real-time environment. 

The project is built using Java for core functionalities, with a web application interface developed using React/Angular, Spring Boot, and MySQL.

## Features
- **Real-Time Ticket Transactions**: Simulates real-time ticket release and purchase.
- **Concurrency Control**: Implements the Producer-Consumer pattern for efficient handling of ticket operations.
- **Dynamic Configuration**: Allows users to configure ticketing parameters through a frontend interface.
- **Persistence**: Saves and retrieves configurations and logs using a MySQL database.
- **User-Friendly Web Interface**: Provides an intuitive interface for running simulations and viewing results.

## Tech Stack
- **Frontend**: React
- **Backend**: Spring Boot
- **Database**: MySQL
- **Core Language**: Java

## APIs
The backend exposes several APIs for managing the simulation and configurations:

### 1. **Start Simulation**
   - **Endpoint**: `/api/simulation/start`
   - **Method**: POST
   - **Request Body**:
     ```json
     {
       "ticketCapacity": 10,
       "totalTickets": 8,
       "vendors": 2,
       "customers": 3,
       "releaseRate": 1000,
       "retrievalRate": 500
     }
     ```
   - **Response**:
     ```json
     {
       "status": "Simulation started",
       "simulationId": "12345"
     }
     ```

### 2. **Stop Simulation**
   - **Endpoint**: `/api/simulation/stop`
   - **Method**: POST
   - **Request Body**:
     ```json
     {
       "simulationId": "12345"
     }
     ```
   - **Response**:
     ```json
     {
       "status": "Simulation stopped",
       "summary": {
         "vendors": [...],
         "customers": [...]
       }
     }
     ```

### 3. **Save Configuration**
   - **Endpoint**: `/api/configurations/save`
   - **Method**: POST
   - **Request Body**:
     ```json
     {
       "name": "Config1",
       "ticketCapacity": 10,
       "totalTickets": 8,
       "vendors": 2,
       "customers": 3,
       "releaseRate": 1000,
       "retrievalRate": 500
     }
     ```
   - **Response**:
     ```json
     {
       "status": "Configuration saved",
       "configId": "67890"
     }
     ```

### 4. **Load Configuration**
   - **Endpoint**: `/api/configurations/load/{configId}`
   - **Method**: GET
   - **Response**:
     ```json
     {
       "configId": "67890",
       "name": "Config1",
       "ticketCapacity": 10,
       "totalTickets": 8,
       "vendors": 2,
       "customers": 3,
       "releaseRate": 1000,
       "retrievalRate": 500
     }
     ```

### 5. **Get All Configurations**
   - **Endpoint**: `/api/configurations`
   - **Method**: GET
   - **Response**:
     ```json
     [
       {
         "configId": "67890",
         "name": "Config1",
         "ticketCapacity": 10,
         "totalTickets": 8,
         "vendors": 2,
         "customers": 3,
         "releaseRate": 1000,
         "retrievalRate": 500
       },
       ...
     ]
     ```

### 6. **Delete Configuration**
   - **Endpoint**: `/api/configurations/delete/{configId}`
   - **Method**: DELETE
   - **Response**:
     ```json
     {
       "status": "Configuration deleted"
     }
     ```

## How to Run the Project

### Prerequisites
1. Install [Java 17+](https://adoptium.net/)
2. Install [Node.js](https://nodejs.org/)
3. Install [MySQL](https://www.mysql.com/)
4. Install [Spring Boot](https://spring.io/projects/spring-boot)

### Backend Setup
1. Clone the repository:
   ```bash
   git clone https://github.com/PulithR/Real-Time-Event-Ticketing-System-with-Producer-Consumer-Pattern
   ```
2. Navigate to the backend folder and build the project:
   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```
3. Configure the `application.properties` file with your MySQL credentials.

### Frontend Setup
1. Navigate to the frontend folder:
   ```bash
   cd frontend
   ```
2. Install dependencies:
   ```bash
   npm install
   ```
3. Start the development server:
   ```bash
   npm start
   ```

### Access the Application
- Open your browser and navigate to `http://localhost:3000`

## Project Structure
```
web_app
│
├── server
│   ├── src
│   ├── pom.xml
│   └── ...
│
├── frontend
│   ├── src
│   ├── package.json
│   └── ...
│
└── README.md
```


## Contact
- **Pulith Rajanayake**
  - GitHub: [PulithR](https://github.com/PulithR/Real-Time-Event-Ticketing-System-with-Producer-Consumer-Pattern)
  - Email: pulithr@gmail.com

---

Feel free to explore the project and provide feedback!
