
# OrderAudit – Technical Design Note

**Project:** Order-to-Audit System for a Small Food Manufacturer  
**Technology:** Java 21, Spring Boot, MySQL, Thymeleaf  
**Author:** Dhanalakshmi

## 1. System Overview

OrderAudit is a web application designed to manage the business operations of a small food manufacturing company. It connects sales orders, production, raw material consumption, finished goods inventory, GST invoicing, payments, and reporting.

The application follows a layered architecture:

- **Presentation Layer:** HTML, CSS, and Thymeleaf templates display forms, dashboards, and reports.
- **Controller Layer:** Spring MVC controllers handle user requests and route them to the appropriate services.
- **Service Layer:** Java services implement business rules, stock calculations, and order processing.
- **Data Layer:** Spring Data JPA repositories manage database operations.
- **Database:** MySQL stores customers, products, orders, production records, invoices, payments, and stock transactions.

**Architecture Flow:**

Browser → Spring Security → Controllers → Services → JPA Repositories → MySQL

## 2. Database Design

The application uses relational entities to represent business information.

| Entity | Purpose |
|---|---|
| Customer | Stores retailer and distributor details |
| Product | Stores finished product information |
| RawMaterial | Stores ingredients and packaging materials |
| BOM / BOMItem | Defines required materials for production |
| SalesOrder | Records customer orders and status |
| ProductionRun | Stores planned and actual production |
| ProductionMaterialUsage | Records actual material consumption |
| RawMaterialStockTransaction | Tracks raw material stock movements |
| FinishedGoodsStockTransaction | Tracks finished goods movements |
| Invoice | Stores invoice amounts and GST |
| Payment | Records payments received against invoices |

JPA repositories provide database access, while service classes handle business operations.

## 3. Order-to-Invoice Workflow

1. A salesman creates an order for a customer.
2. The order is reviewed and approved.
3. The application checks finished goods availability.
4. If stock is sufficient, the order can be dispatched.
5. Dispatch updates finished goods inventory.
6. A GST invoice is generated for the sale.
7. Payments are recorded against the invoice.
8. Outstanding balances are calculated.

For the demonstration scenario, Retailer A orders 150 Murukku packets at INR 50 each.

Taxable amount = 150 × 50 = INR 7,500

GST at 5% = INR 375

Invoice total = INR 7,875

Payment received = INR 4,000

Outstanding balance = INR 3,875

Distributor B orders 120 packets when only 88 are available, resulting in a shortage of 32 packets. The order remains pending.

## 4. Production and Inventory Design

The Bill of Materials (BOM) defines the planned raw material quantities for a production batch.

For 200 Murukku packets, the planned quantities are:

- Flour: 40 kg
- Oil: 10 L
- Spices: 1.2 kg
- Pouches: 200
- Cartons: 10

A production run records planned output, actual good output, damaged materials, labour costs, overhead costs, and actual raw material usage.

The demonstration production run records 188 good packets and 6 damaged pouches.

Inventory transactions track raw material receipts, production consumption, finished goods production, and dispatches.

Finished goods stock:

Opening stock (50) + Production (188) − Dispatch (150) = **88 packets**

## 5. Production Variance Analysis

OrderAudit compares actual production performance against planned BOM values.

**Material Variance (%) = ((Actual Quantity − Planned Quantity) / Planned Quantity) × 100**

**Yield Variance (%) = ((Actual Output − Planned Output) / Planned Output) × 100**

A tolerance of ±3% is used to identify significant deviations.

| Metric | Variance | Result |
|---|---:|---|
| Production yield | -6% | Alert |
| Flour | 0% | Normal |
| Oil | +5% | Alert |
| Spices | 0% | Normal |
| Pouches | -3% | Normal |
| Cartons | -10% | Alert |

This helps identify production inefficiencies and unusual material consumption.

## 6. Security and Reporting

Spring Security provides authentication and role-based authorization.

- **Admin:** Management, master data, and reporting access.
- **Salesman:** Customer, order, invoice, and payment operations.
- **Production:** BOM, production, and inventory operations.

Passwords are encoded using BCrypt. The demonstration accounts are configured in memory and are intended only for local evaluation.

Reporting features include sales CSV export, stock CSV export, production variance reporting, and downloadable GST invoice PDFs using OpenPDF.

## 7. Design Decisions and Limitations

**Design decisions:**
- Spring Boot provides a structured Java backend.
- MySQL supports relational business data.
- Thymeleaf enables server-rendered web pages without a separate frontend framework.
- Stock transactions provide a history of inventory movements.
- Role-based access separates sales, production, and administrative responsibilities.

**Current limitations:**
- Demonstration user accounts are stored in memory.
- Sample business data must currently be entered into the local database.
- Automated test coverage and production deployment configuration require further work.

The application demonstrates the main order-to-audit workflow and provides a foundation for future improvements.
