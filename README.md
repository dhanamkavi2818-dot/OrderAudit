
# OrderAudit – Order-to-Audit Management System

A web-based Order Management, Production Tracking, Inventory, and GST Invoicing System developed for the NIT Trichy Winter Internship 2026 application.

## Project Overview

OrderAudit is designed for a small food manufacturing business to manage its operations from customer orders to production, inventory tracking, invoicing, and reporting.

The application demonstrates a complete order-to-audit workflow using Java Spring Boot and MySQL.

## Technology Stack

- Backend: Java 21, Spring Boot
- Frontend: HTML, CSS, Thymeleaf
- Database: MySQL
- Security: Spring Security
- PDF Generation: OpenPDF
- Build Tool: Maven
- Version Control: Git and GitHub

## Key Features

- Role-based login for Admin, Salesman, and Production users
- Customer and product management
- Sales order creation, approval, and dispatch
- Bill of Materials (BOM) management
- Production planning and actual material consumption tracking
- Raw material and finished goods stock management
- Production variance analysis with alerts
- GST invoice generation and PDF download
- Customer payment tracking and outstanding balances
- Sales and stock CSV reports
- Dashboard displaying key business statistics

## User Roles

| Role | Responsibilities |
|------|------------------|
| Admin | Reports, master data, and overall management |
| Salesman | Customers, sales orders, invoices, and payments |
| Production | Production runs, BOM, and inventory tracking |

## Demo Login Credentials

These are demonstration-only accounts configured in Spring Security.

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | Admin@123 |
| Salesman | salesman | Sales@123 |
| Production | production | Production@123 |

**Note:** These fixed credentials are for local demonstration only and must not be used in a production deployment.

## Installation Requirements

- Java JDK 21
- MySQL Server
- Git
- Maven or the included Maven Wrapper

## Database Setup


## Database Setup

1. Install MySQL Server 8.0 or later.
2. Open MySQL Workbench.
3. Create a new, empty database:

   ```sql
   CREATE DATABASE orderaudit;
   USE orderaudit;
   ```

4. Open the following SQL file in MySQL Workbench:

   `database/orderaudit_sample.sql`

5. Select the orderaudit database and execute the entire SQL script.

   Import only into an empty database, as the script contains
   DROP TABLE statements.

6. Copy `src/main/resources/application.properties.example`
   and rename the copy to `application.properties`.

7. Replace `YOUR_MYSQL_PASSWORD` with your local MySQL password.

### Sample Database

The sample database contains 12 tables with preloaded
demonstration records, including:

- Customers and products
- Raw materials and BOM
- Production batches and material consumption
- Inventory transactions
- Sales orders
- GST invoices and payments

The sample data represents the completed benchmark scenario
provided in the internship problem statement.

The database import has been successfully tested using
a separate MySQL database.


## Run the Application

Open a terminal in the project root.

On Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

Open:

http://localhost:8080/

Log in using one of the demonstration accounts.

## Demonstration Scenario

The application has been tested locally with a Murukku 200g manufacturing scenario.

- Planned production: 200 packets
- Actual good production: 188 packets
- Damaged pouches: 6
- Production yield variance: -6%
- Oil consumption variance: +5%
- Carton consumption variance: -10%
- Retailer A order: 150 packets
- Retailer A invoice total: INR 7,875
- Payment received: INR 4,000
- Outstanding amount: INR 3,875
- Distributor B order: 120 packets
- Finished goods shortage: 32 packets

The demonstration records are stored in the local MySQL database and are not automatically included when cloning the repository.

## Reports

The system supports:

- Sales register CSV export
- Stock summary CSV export
- Production variance report
- GST invoice PDF download

## Project Status

Core application features have been implemented and manually tested using the internship demonstration scenario.

Further improvements may include automated tests, sample-data initialization, and deployment configuration.

## Author

Dhanalakshmi

Developed as part of the NIT Trichy Winter Internship 2026 application.
