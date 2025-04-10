# Employee Management System

## Setup Instructions

1. **Clone the repository**:
   https://github.com/chukwuebukanwaturuba/employee_management_service.git
   cd employee_management_service.git

2. Build the Docker image:
    Ensure that Docker is installed on your machine.
    To build the Docker image, run:
     docker build -t employee-management-system .

3. Run Docker Compose:
    Ensure Docker Compose is installed.
    To start all services (app, database, and RabbitMQ), run:
     docker-compose up -d

4. Access the Application:
    The app should now be running at http://localhost:8080.
     http://localhost:8080/swagger-ui/index.html


   API Endpoints and Sample Requests/Responses:

       POST /api/v1/admin/register
Description: Registers a new admin to the system.

Request:
        {
  "firstName": "Chukwuebuka",
  "lastName": "Nwaturuba",
  "email": "ebusclement@gmail.com",
  "phoneNumber": "07038559543",
  "password": "Ebus1234",
  "gender": "male"
}

Response:
        {
    "message": "success",
    "time": "2025-04-10 2:41:46 PM",
    "data": {
        "message": "CustomerProfile saved",
        "usersDto": {
            "firstName": "Chukwuebuka",
            "lastName": "Nwaturuba",
            "email": "ebusclement@gmail.com",
            "phoneNumber": "07038559543",
            "gender": "MALE"
        },
        "responseDto": {}
    },
    "hasError": false
}

POST /api/v1/admin/add-employee
Description: Registers a new employee to the system.

Request:
        {
  "firstName": "Chigozie",
  "lastName": "Agulonu",
  "email": "johnpiklis@gmail.com",
  "phoneNumber": "07031014708",
  "password": "John@123",
  "gender": "MALE",
  "role": "MANAGER",
  "department": "IT"
}

Response:
          {
    "message": "success",
    "time": "2025-04-10 3:47:06 PM",
    "data": {
        "message": "Employee added successfully",
        "usersDto": {
            "firstName": "Chigozie",
            "lastName": "Agulonu",
            "email": "johnpiklis@gmail.com",
            "phoneNumber": "07031014708",
            "gender": "MALE"
        },
        "responseDto": {}
    },
    "hasError": false
}

OTHER ENDPOINTS CAN BE EASILY ACCESSED VIA THE SWAGGER



Architecture Decisions

1. Dockerized Approach
Reasoning: I opted for Docker to create a consistent environment across development and production. Docker eliminates the "it works on my machine" issue and makes setting up the necessary dependencies, like PostgreSQL and RabbitMQ, more seamless. With Docker, the application is guaranteed to run the same way regardless of where it's deployed, simplifying the development and deployment process.

2. Spring Boot for Backend
Reasoning: Spring Boot was my choice for the backend due to its flexibility, speed of development, and deep integration with a variety of technologies. It offers a wide range of built-in features, like security, persistence, and REST APIs, making it an ideal framework for quickly building scalable microservices. Additionally, it integrates well with both PostgreSQL and RabbitMQ, two technologies I’m using in this project.

3. PostgreSQL as the Database
Reasoning: PostgreSQL is an open-source relational database that I selected for its reliability and robust feature set. It’s a strong choice when handling structured data and transactional applications, and I appreciate its ability to handle complex queries efficiently. It integrates easily with Spring Boot, which makes database management smooth. For this project, PostgreSQL meets the needs of maintaining scalable, consistent data.

4. RabbitMQ for Message Queue
Reasoning: I chose RabbitMQ as the message broker to manage asynchronous communication between different parts of the system. RabbitMQ helps decouple services, making the system more resilient and scalable. Using RabbitMQ allows for better message queuing and handling of requests, making sure each service can operate independently and efficiently, without unnecessary delays or dependencies.



Assumptions Made
Database Configuration: The database is assumed to be PostgreSQL, with a predefined schema (ebuka).

RabbitMQ Setup: RabbitMQ has been preconfigured for message queueing.

Spring Profiles: The system uses a development profile (dev), and settings like database URLs are configured via environment variables.

API Authentication: The system is assumed to use JWT-based authentication, although this wasn't fully implemented for the sake of simplicity in the prototype.
