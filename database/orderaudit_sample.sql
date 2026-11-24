-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: orderaudit
-- ------------------------------------------------------
-- Server version	8.0.46

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `bom_items`
--

DROP TABLE IF EXISTS `bom_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `bom_items` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity_required` decimal(12,3) NOT NULL,
  `bom_id` bigint NOT NULL,
  `raw_material_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK61p0elxqsv3ek2hyxoqknoj4q` (`bom_id`),
  KEY `FKcrhuiwder9uwv027c0fxl9uto` (`raw_material_id`),
  CONSTRAINT `FK61p0elxqsv3ek2hyxoqknoj4q` FOREIGN KEY (`bom_id`) REFERENCES `boms` (`id`),
  CONSTRAINT `FKcrhuiwder9uwv027c0fxl9uto` FOREIGN KEY (`raw_material_id`) REFERENCES `raw_materials` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `bom_items`
--

LOCK TABLES `bom_items` WRITE;
/*!40000 ALTER TABLE `bom_items` DISABLE KEYS */;
INSERT INTO `bom_items` VALUES (1,40.000,1,1),(4,10.000,1,2),(5,1.200,1,3),(6,200.000,1,4),(7,10.000,1,5);
/*!40000 ALTER TABLE `bom_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `boms`
--

DROP TABLE IF EXISTS `boms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `boms` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `batch_output_packets` int DEFAULT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKmhxh3n0l9b3wtu7mdi0iwt5sa` (`product_id`),
  CONSTRAINT `FKmhxh3n0l9b3wtu7mdi0iwt5sa` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `boms`
--

LOCK TABLES `boms` WRITE;
/*!40000 ALTER TABLE `boms` DISABLE KEYS */;
INSERT INTO `boms` VALUES (1,200,1);
/*!40000 ALTER TABLE `boms` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `customer_type` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gstin` varchar(255) DEFAULT NULL,
  `name` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
INSERT INTO `customers` VALUES (1,'Madurai','Retailer','abc@example.com','','ABC Stores','0000000000','Tamil Nadu'),(2,'Madurai, Tamil Nadu','Retailer','','','Retailer A','','Tamil Nadu'),(3,'','Distributor','','','Distributor B','','Karnataka');
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `finished_goods_stock_transactions`
--

DROP TABLE IF EXISTS `finished_goods_stock_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `finished_goods_stock_transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` int NOT NULL,
  `reference` varchar(255) DEFAULT NULL,
  `transaction_date` datetime(6) DEFAULT NULL,
  `transaction_type` varchar(255) NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6hmi191ywhu0fhutld77myfi` (`product_id`),
  CONSTRAINT `FK6hmi191ywhu0fhutld77myfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `finished_goods_stock_transactions`
--

LOCK TABLES `finished_goods_stock_transactions` WRITE;
/*!40000 ALTER TABLE `finished_goods_stock_transactions` DISABLE KEYS */;
INSERT INTO `finished_goods_stock_transactions` VALUES (1,50,'OPENING-MURUKKU-1','2026-11-20 21:32:01.000000','OPENING_STOCK',1),(2,188,'PRODUCTION-3','2026-11-20 21:32:01.000000','PRODUCTION',1),(3,150,'SALES-ORDER-1','2026-11-22 08:17:33.142898','DISPATCH',1);
/*!40000 ALTER TABLE `finished_goods_stock_transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `invoices`
--

DROP TABLE IF EXISTS `invoices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `invoices` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `cgst_amount` decimal(12,2) DEFAULT NULL,
  `igst_amount` decimal(12,2) DEFAULT NULL,
  `invoice_date` datetime(6) DEFAULT NULL,
  `invoice_number` varchar(255) NOT NULL,
  `sgst_amount` decimal(12,2) DEFAULT NULL,
  `taxable_amount` decimal(12,2) DEFAULT NULL,
  `total_amount` decimal(12,2) DEFAULT NULL,
  `sales_order_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKl1x55mfsay7co0r3m9ynvipd5` (`invoice_number`),
  UNIQUE KEY `UKfydhaa15sa4l1v4qivm7dybrf` (`sales_order_id`),
  CONSTRAINT `FKl2cegqcg6si3mg11w4b6xj5nq` FOREIGN KEY (`sales_order_id`) REFERENCES `sales_orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `invoices`
--

LOCK TABLES `invoices` WRITE;
/*!40000 ALTER TABLE `invoices` DISABLE KEYS */;
INSERT INTO `invoices` VALUES (1,187.50,0.00,'2026-11-22 08:26:25.228555','INV-1',187.50,7500.00,7875.00,1);
/*!40000 ALTER TABLE `invoices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `payments` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `amount` decimal(12,2) NOT NULL,
  `payment_date` datetime(6) DEFAULT NULL,
  `payment_mode` varchar(255) DEFAULT NULL,
  `reference_number` varchar(255) DEFAULT NULL,
  `invoice_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKrbqec6be74wab8iifh8g3i50i` (`invoice_id`),
  CONSTRAINT `FKrbqec6be74wab8iifh8g3i50i` FOREIGN KEY (`invoice_id`) REFERENCES `invoices` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `payments`
--

LOCK TABLES `payments` WRITE;
/*!40000 ALTER TABLE `payments` DISABLE KEYS */;
INSERT INTO `payments` VALUES (1,4000.00,'2026-11-22 08:34:06.089471','UPI','TEST-PAY-001',1);
/*!40000 ALTER TABLE `payments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `production_material_usage`
--

DROP TABLE IF EXISTS `production_material_usage`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `production_material_usage` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `actual_quantity` decimal(12,3) NOT NULL,
  `production_run_id` bigint NOT NULL,
  `raw_material_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK24yryw1yyltnotj1p5fa899t4` (`production_run_id`),
  KEY `FK5lnn29mqfetbt4iqxxfk4nvcx` (`raw_material_id`),
  CONSTRAINT `FK24yryw1yyltnotj1p5fa899t4` FOREIGN KEY (`production_run_id`) REFERENCES `production_runs` (`id`),
  CONSTRAINT `FK5lnn29mqfetbt4iqxxfk4nvcx` FOREIGN KEY (`raw_material_id`) REFERENCES `raw_materials` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `production_material_usage`
--

LOCK TABLES `production_material_usage` WRITE;
/*!40000 ALTER TABLE `production_material_usage` DISABLE KEYS */;
INSERT INTO `production_material_usage` VALUES (11,40.000,3,1),(12,10.500,3,2),(13,1.200,3,3),(14,194.000,3,4),(15,9.000,3,5);
/*!40000 ALTER TABLE `production_material_usage` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `production_runs`
--

DROP TABLE IF EXISTS `production_runs`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `production_runs` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `actual_output_packets` int NOT NULL,
  `damaged_packets` int NOT NULL,
  `labour_cost` decimal(12,2) DEFAULT NULL,
  `overhead_cost` decimal(12,2) DEFAULT NULL,
  `production_date` datetime(6) DEFAULT NULL,
  `bom_id` bigint NOT NULL,
  `batch_reference` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK29pjw0ydiud7407swcrktat4t` (`batch_reference`),
  KEY `FK33b9wb41r1y4vipax7w4rig2f` (`bom_id`),
  CONSTRAINT `FK33b9wb41r1y4vipax7w4rig2f` FOREIGN KEY (`bom_id`) REFERENCES `boms` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `production_runs`
--

LOCK TABLES `production_runs` WRITE;
/*!40000 ALTER TABLE `production_runs` DISABLE KEYS */;
INSERT INTO `production_runs` VALUES (3,188,6,1200.00,600.00,'2026-11-20 21:08:07.845706',1,'MURUKKU-BATCH-001');
/*!40000 ALTER TABLE `production_runs` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `gst_rate` decimal(5,2) DEFAULT NULL,
  `pack_size` varchar(255) DEFAULT NULL,
  `product_name` varchar(255) NOT NULL,
  `selling_price` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,5.00,'200g','Murukku',50.00);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `raw_material_stock_transactions`
--

DROP TABLE IF EXISTS `raw_material_stock_transactions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `raw_material_stock_transactions` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `quantity` decimal(12,3) NOT NULL,
  `reference` varchar(255) DEFAULT NULL,
  `transaction_date` datetime(6) DEFAULT NULL,
  `transaction_type` varchar(255) NOT NULL,
  `unit_cost` decimal(12,2) DEFAULT NULL,
  `raw_material_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfs3owkxcxyafxx5d2w2h6qtt1` (`raw_material_id`),
  CONSTRAINT `FKfs3owkxcxyafxx5d2w2h6qtt1` FOREIGN KEY (`raw_material_id`) REFERENCES `raw_materials` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `raw_material_stock_transactions`
--

LOCK TABLES `raw_material_stock_transactions` WRITE;
/*!40000 ALTER TABLE `raw_material_stock_transactions` DISABLE KEYS */;
INSERT INTO `raw_material_stock_transactions` VALUES (1,100.000,'Opening Stock','2026-11-20 08:54:02.587877','ADJUSTMENT_IN',42.00,1),(2,30.000,'Opening Stock','2026-11-20 08:55:36.883345','ADJUSTMENT_IN',140.00,2),(3,5.000,'Opening Stock','2026-11-20 08:55:55.329629','ADJUSTMENT_IN',200.00,3),(4,500.000,'Opening Stock','2026-11-20 08:56:16.797803','ADJUSTMENT_IN',2.50,4),(5,30.000,'Opening Stock','2026-11-20 08:56:45.081956','ADJUSTMENT_IN',15.00,5),(6,60.000,'PURCHASE-001','2026-11-20 09:00:41.206227','PURCHASE',45.00,1),(17,40.000,'PRODUCTION-3','2026-11-20 21:08:08.017741','ISSUE',NULL,1),(18,10.500,'PRODUCTION-3','2026-11-20 21:08:08.047774','ISSUE',NULL,2),(19,1.200,'PRODUCTION-3','2026-11-20 21:08:08.073311','ISSUE',NULL,3),(20,194.000,'PRODUCTION-3','2026-11-20 21:08:08.099147','ISSUE',NULL,4),(21,9.000,'PRODUCTION-3','2026-11-20 21:08:08.122801','ISSUE',NULL,5);
/*!40000 ALTER TABLE `raw_material_stock_transactions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `raw_materials`
--

DROP TABLE IF EXISTS `raw_materials`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `raw_materials` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `material_name` varchar(255) NOT NULL,
  `unit` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `raw_materials`
--

LOCK TABLES `raw_materials` WRITE;
/*!40000 ALTER TABLE `raw_materials` DISABLE KEYS */;
INSERT INTO `raw_materials` VALUES (1,'Flour','kg'),(2,'Oil','litre'),(3,'Salt and Spice','kg'),(4,'Packaging Pouch','pieces'),(5,'Carton','pieces');
/*!40000 ALTER TABLE `raw_materials` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sales_orders`
--

DROP TABLE IF EXISTS `sales_orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sales_orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_date` datetime(6) DEFAULT NULL,
  `quantity` int NOT NULL,
  `status` varchar(255) NOT NULL,
  `customer_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKfs1owechmxg3lvej5vq1s8t8i` (`customer_id`),
  KEY `FKhsraflqdp5d8vfsarc5hpe5i7` (`product_id`),
  CONSTRAINT `FKfs1owechmxg3lvej5vq1s8t8i` FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`),
  CONSTRAINT `FKhsraflqdp5d8vfsarc5hpe5i7` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sales_orders`
--

LOCK TABLES `sales_orders` WRITE;
/*!40000 ALTER TABLE `sales_orders` DISABLE KEYS */;
INSERT INTO `sales_orders` VALUES (1,'2026-11-20 22:14:22.865644',150,'DISPATCHED',2,1),(2,'2026-11-22 08:46:55.748740',120,'PENDING',3,1);
/*!40000 ALTER TABLE `sales_orders` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-11-24 10:20:02
