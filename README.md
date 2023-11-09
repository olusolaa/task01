# Clustered Data Warehouse

Clustered Data Warehouse is a Java Spring application designed for managing foreign exchange (FX) deals. This service allows users to create, retrieve, and manage FX deal information efficiently with robust error handling and data validation.


# Project Structure

The project has the following directory layout:

````
├── .idea
├── .mvn
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com.bloomberg.clustereddatawarehouse
│   │   │       ├── controllers
│   │   │       │   └── FXDealController
│   │   │       ├── dtos
│   │   │       │   └── FXDealDto
│   │   │       ├── exceptionhandler
│   │   │       │   └── GlobalExceptionHandler
│   │   │       ├── exceptions
│   │   │       │   ├── DuplicateFXDealException
│   │   │       │   ├── InvalidRequestException
│   │   │       │   └── NotFoundException
│   │   │       ├── models
│   │   │       │   └── FXDeal
│   │   │       ├── repositories
│   │   │       ├── services
│   │   │       │   └── serviceimpl
│   │   │       │       └── FXDealService
│   │   │       └── util
│   │   │           ├── ApiResponse
│   │   │           └── ResponseBuilder
│   │   └── resources
│   └── test
│       ├── java
│       │   └── com.bloomberg.clustereddatawarehouse
│       │       ├── controllers
│       │       │   └── FXDealControllerTest
│       │       └── serviceimpl
│       │           └── FXDealServiceImplTest
│       └── resources
├── target
├── .gitignore
├── doc.json
├── docker-compose.yml
├── Dockerfile
├── HELP.md
├── Makefile
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
````

Please navigate to the respective directories to find the related code and resources.


## Run Locally via Make
To run this application, you will need to clone the repository and aso have Docker installed.


Clone the project

```bash
git clone git@github.com:olusolaa/task01.git (ssh)
git clone https://github.com/olusolaa/task01.git (https)
```

Go to the project directory

```bash
cd clustered-data-warehouse
```

Build the project

```bash
make build
```

Run the project

```bash
make run
```

Stop the project

```bash
make stop
```

Test the project

```bash
make test
```

Swagger URL for API documentation when the server is running:

```bash
http://localhost:8081/swagger-ui/index.html
```


## API Reference
All URLs are relative to:
- for `docker` http://localhost:8081
- for running it via `terminal` http://localhost:8080

### Save FX Deal

```http
POST /api/v1/fx-deals
```

| Method | HTTP Request         | Description                  |
|--------|----------------------|------------------------------|
| `POST` | `/api/v1/fx-deals`   | Endpoint for saving FX deals |

#### Sample Request
```json
{
  "unique_id": "PQ-ytd99",
  "amount": 10000.00,
  "from_currency": "USD",
  "to_currency": "GHS",
  "timestamp": "2023-11-09T12:00:00"
}
```

#### Sample Response
```json
{
    "message": "FX Deal saved successfully",
    "status": "CREATED",
    "data": {
        "unique_id": "DEAL-hjuk123",
        "from_currency": "USD",
        "to_currency": "GHS",
        "timestamp": "2023-11-09T12:00:00",
        "amount": 15000.00
    }
}
```

### Get FX Deal by ID

```http
GET /api/v1/fx-deals/{dealId}
```

| Method | HTTP Request                   | Description                               |
|--------|--------------------------------|-------------------------------------------|
| `GET`  | `/api/v1/fx-deals/{dealId}`    | Endpoint to get a particular FX deal by ID |

#### Sample Request
``` 
localhost:8080/api/v1/fx-deals/PQ-ytd99
```

#### Sample Response
```json
{
    "message": "FX Deal retrieved successfully",
    "status": "OK",
      "data": {
        "unique_id": "DEAL-hjuk123",
        "from_currency": "USD",
        "to_currency": "GHS",
        "timestamp": "2023-11-09T12:00:00",
        "amount": 15000.00
      }
}
```

### Get All FX Deals with Pagination

```http
GET /api/v1/fx-deals?page={page}&size={size}
```

| Method | HTTP Request                           | Description                                          |
|--------|----------------------------------------|------------------------------------------------------|
| `GET`  | `/api/v1/fx-deals?page={page}&size={size}` | Endpoint for retrieving all FX deals with pagination |

#### Sample Request
``` 
localhost:8080/api/v1/fx-deals?page=0&size=10
```

#### Sample Response
```json
{
    "message": "FX deals retrieved successfully",
    "status": "OK",
    "data": {
        "content": [
               "data": {
                    "unique_id": "DEAL-hjuk123",
                    "from_currency": "USD",
                    "to_currency": "GHS",
                    "timestamp": "2023-11-09T12:00:00",
                    "amount": 15000.00
                }
            // ... other FX deals
        ],
        "page_number": 0,
        "page_size": 10,
        "total_elements": 100,
        "total_pages": 10,
        "last": false
    }
}
