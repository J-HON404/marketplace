
-- MariaDB dump 10.19  Distrib 10.6.24-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: marketplace
-- ------------------------------------------------------
-- Server version	10.6.24-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `cart_items`
--

DROP TABLE IF EXISTS `cart_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `cart_items` (
  `quantity` int(11) NOT NULL,
  `cart_id` bigint(20) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKpcttvuq4mxppo8sxggjtn5i2c` (`cart_id`),
  KEY `FK1re40cjegsfvw58xrkdp6bac6` (`product_id`),
  CONSTRAINT `FK1re40cjegsfvw58xrkdp6bac6` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKpcttvuq4mxppo8sxggjtn5i2c` FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `cart_items`
--

LOCK TABLES `cart_items` WRITE;
/*!40000 ALTER TABLE `cart_items` DISABLE KEYS */;
/*!40000 ALTER TABLE `cart_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `carts`
--

DROP TABLE IF EXISTS `carts`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `carts` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `shop_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK64t7ox312pqal3p7fg9o503c2` (`user_id`),
  KEY `FKuw3798o19ixwd5do7dt385sw` (`shop_id`),
  CONSTRAINT `FK8oqddhpqn2jaybi60ck33okeq` FOREIGN KEY (`user_id`) REFERENCES `profiles` (`id`),
  CONSTRAINT `FKuw3798o19ixwd5do7dt385sw` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `carts`
--

LOCK TABLES `carts` WRITE;
/*!40000 ALTER TABLE `carts` DISABLE KEYS */;
INSERT INTO `carts` VALUES (1,21,25),(2,20,29),(3,20,30);
/*!40000 ALTER TABLE `carts` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customers`
--

DROP TABLE IF EXISTS `customers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `customers` (
  `id` bigint(20) NOT NULL,
  `address` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FKpog72rpahj62h7nod9wwc28if` FOREIGN KEY (`id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKq64fie6qnn6prgg8kl65b5cwx` FOREIGN KEY (`id`) REFERENCES `profiles` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customers`
--

LOCK TABLES `customers` WRITE;
/*!40000 ALTER TABLE `customers` DISABLE KEYS */;
/*!40000 ALTER TABLE `customers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_items`
--

DROP TABLE IF EXISTS `order_items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_items` (
  `price` double NOT NULL,
  `quantity` int(11) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKbioxgbv59vetrxe0ejfubep1w` (`order_id`),
  KEY `FKocimc7dtr037rh4ls4l95nlfi` (`product_id`),
  CONSTRAINT `FKbioxgbv59vetrxe0ejfubep1w` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`),
  CONSTRAINT `FKocimc7dtr037rh4ls4l95nlfi` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_items`
--

LOCK TABLES `order_items` WRITE;
/*!40000 ALTER TABLE `order_items` DISABLE KEYS */;
INSERT INTO `order_items` VALUES (52.99,1,2,3,4),(78,1,14,12,5),(52.99,1,15,12,4),(78,1,16,13,5),(78,1,17,14,5),(52.99,10,18,14,4),(78,1,19,15,5),(52.99,1,20,16,4),(52.99,3,21,17,4),(52.99,7,22,18,4),(52.99,1,23,19,4),(1,1,24,20,14),(1,1,27,23,27),(100000,2,28,24,27),(1,1,29,25,38),(1,1,30,26,36),(23.65,5,31,27,43),(17.98,3,32,28,44),(11.1,1,33,29,45),(11.1,6,34,30,45),(11,1,35,31,45);
/*!40000 ALTER TABLE `order_items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `order_notices`
--

DROP TABLE IF EXISTS `order_notices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `order_notices` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `text` varchar(255) NOT NULL,
  `type_notice` enum('CONFIRMED_DELIVERED','READY_TO_ELABORATING','SHIPPING_DETAILS_SET') NOT NULL,
  `order_id` bigint(20) DEFAULT NULL,
  `recipient_id` bigint(20) DEFAULT NULL,
  `reference_id` bigint(20) DEFAULT NULL,
  `type` enum('CONFIRMED_DELIVERED','ORDER_DELETED','READY_TO_ELABORATING','REMIND_DELIVERY','SHIPPING_DETAILS_SET') DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK80g2vmesl21ofb37k1uxutctx` (`order_id`),
  CONSTRAINT `FK80g2vmesl21ofb37k1uxutctx` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `order_notices`
--

LOCK TABLES `order_notices` WRITE;
/*!40000 ALTER TABLE `order_notices` DISABLE KEYS */;
/*!40000 ALTER TABLE `order_notices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_date` datetime(6) NOT NULL,
  `total` double NOT NULL,
  `shop_id` bigint(20) NOT NULL,
  `estimated_delivery_date` date DEFAULT NULL,
  `status` enum('READY_TO_ELABORATING','SHIPPING_DETAILS_SET','REMIND_DELIVERY','CONFIRMED_DELIVERED') DEFAULT NULL,
  `tracking_id` varchar(255) DEFAULT NULL,
  `profile_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKeieprmmaadhys18lur996ikv4` (`profile_id`),
  KEY `FK21gttsw5evi5bbsvleui69d7r` (`shop_id`),
  CONSTRAINT `FK21gttsw5evi5bbsvleui69d7r` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`),
  CONSTRAINT `FKeieprmmaadhys18lur996ikv4` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
INSERT INTO `orders` VALUES (3,'2025-12-17 14:30:00.000000',52.99,20,'2025-12-20','CONFIRMED_DELIVERED','1',25),(12,'2025-12-20 20:06:36.005100',130.99,20,'2025-12-21','CONFIRMED_DELIVERED','1111111',25),(13,'2025-12-20 20:19:57.148609',78,20,'2025-12-20','CONFIRMED_DELIVERED','111',25),(14,'2025-12-20 20:23:16.959741',607.9,20,'2025-12-20','CONFIRMED_DELIVERED','1111',25),(15,'2025-12-20 21:06:09.714151',78,20,'2025-12-20','CONFIRMED_DELIVERED','111',25),(16,'2025-12-20 21:06:43.904145',52.99,20,'2025-12-21','CONFIRMED_DELIVERED','111',25),(17,'2025-12-21 17:42:03.617718',158.97,20,NULL,'READY_TO_ELABORATING',NULL,25),(18,'2025-12-21 17:42:33.551127',370.93,20,NULL,'READY_TO_ELABORATING',NULL,25),(19,'2025-12-21 18:15:53.901899',52.99,20,NULL,'READY_TO_ELABORATING',NULL,25),(20,'2025-12-22 18:23:59.815566',1,21,'2025-12-22','CONFIRMED_DELIVERED','111',25),(21,'2025-12-22 18:43:39.942308',1,20,'2025-12-22','CONFIRMED_DELIVERED','111',25),(22,'2025-12-22 18:58:21.308912',1,20,NULL,'READY_TO_ELABORATING',NULL,25),(23,'2025-12-22 19:18:30.182302',1,20,NULL,'READY_TO_ELABORATING',NULL,25),(24,'2025-12-22 21:25:44.582220',200000,20,'2025-12-22','CONFIRMED_DELIVERED','111111',25),(25,'2025-12-23 08:06:09.076290',1,20,'2025-12-26','CONFIRMED_DELIVERED','111',25),(26,'2025-12-23 14:23:00.679310',1,21,'2025-12-23','CONFIRMED_DELIVERED','44442233232',25),(27,'2025-12-23 14:44:05.684779',118.25,20,NULL,'READY_TO_ELABORATING',NULL,25),(28,'2025-12-23 15:08:04.656102',53.94,20,NULL,'READY_TO_ELABORATING',NULL,25),(29,'2025-12-23 20:43:32.265882',11.1,20,NULL,'READY_TO_ELABORATING',NULL,25),(30,'2026-01-13 11:35:25.401526',66.6,20,'2026-01-18','CONFIRMED_DELIVERED','11',25),(31,'2026-01-18 16:11:19.096167',11,20,'2026-01-19','SHIPPING_DETAILS_SET','111',25);
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_followers`
--

DROP TABLE IF EXISTS `product_followers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_followers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKtafqc66xsh5ooipyjm3t6c463` (`product_id`,`user_id`),
  KEY `FKfg0wrw29pyomvcxw1iobvseb8` (`user_id`),
  CONSTRAINT `FK5wud9nn4yt4g10dj26pad3yt6` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`),
  CONSTRAINT `FKfg0wrw29pyomvcxw1iobvseb8` FOREIGN KEY (`user_id`) REFERENCES `profiles` (`id`),
  CONSTRAINT `FKjdlour2rmublidq60gnrp3q7x` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_followers`
--

LOCK TABLES `product_followers` WRITE;
/*!40000 ALTER TABLE `product_followers` DISABLE KEYS */;
/*!40000 ALTER TABLE `product_followers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `product_notices`
--

DROP TABLE IF EXISTS `product_notices`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `product_notices` (
  `expire_date` date DEFAULT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) NOT NULL,
  `text` varchar(255) NOT NULL,
  `type_notice` enum('AVAILABILITY','INFO','PROMOTION','STOCK','WARNING') NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FKnb4oyw7x35ui07xpt27nh83hy` (`product_id`),
  CONSTRAINT `FKnb4oyw7x35ui07xpt27nh83hy` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `product_notices`
--

LOCK TABLES `product_notices` WRITE;
/*!40000 ALTER TABLE `product_notices` DISABLE KEYS */;
INSERT INTO `product_notices` VALUES ('2026-01-30',35,45,'prossima settimana sconto speciale 50%','PROMOTION'),('2026-01-23',36,45,'messaggio','PROMOTION'),('2026-01-25',37,52,'Spedizioni bloccate','WARNING');
/*!40000 ALTER TABLE `product_notices` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `availability_date` date NOT NULL,
  `description` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` double NOT NULL,
  `quantity` int(11) NOT NULL,
  `shop_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK1v921q4b49b3tc5mamxvhej0l` (`shop_id`,`name`),
  CONSTRAINT `FK7kp8sbhxboponhx3lxqtmkcoj` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (4,'2025-12-19','hydro-protein ','Nutraff Whey-Pepti',52.99,0,20),(5,'2025-12-20','integratore','Metadone-XK',78,0,20),(14,'2025-12-21','fff','fff',1,0,21),(27,'2025-12-22','vvvv','vvv',100000,0,20),(36,'2025-12-23','100% vegana','poltrona',1,0,21),(38,'2025-12-23','aaa','lampadina',1,0,20),(43,'2025-12-23','60 compresse','eaa',23.65,0,20),(44,'2025-12-23','100% plastica contaminata','Protein shaker',17.98,0,20),(45,'2026-01-22','bevanda a base di caffeina e taurina','Energy powder ',12,11,20),(52,'2026-01-25','55 pollici','televisione',789,12,21);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profiles`
--

DROP TABLE IF EXISTS `profiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `profiles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `role` enum('CUSTOMER','SELLER') NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKlnk8iosvsrn5614xw3lgnybgk` (`email`),
  UNIQUE KEY `UKgkg1kpmjy7f1hm6ugsoxmgisq` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profiles`
--

LOCK TABLES `profiles` WRITE;
/*!40000 ALTER TABLE `profiles` DISABLE KEYS */;
INSERT INTO `profiles` VALUES (21,'mario@mail.com','$2a$10$pGHExnvdVZ98CSB41M6ciOLoy9bVlSjmR0Yk46CVDyfAquG0AYZ4G','mario','via roma','SELLER'),(22,'gigi@mail.com','$2a$10$ppVV8jjF4A/u03zqNQCxhem6NwkYzi8Hv4cKzlLqkgEe2rHwkcgBW','gigi','via roma 1 ','SELLER'),(25,'mirko@mail.com','$2a$10$ptrh8YWRFcSert8bimKA8upItid5PrwTeWTojbKFxdPsJHXwa67UK','mirko','via roma 1','CUSTOMER'),(29,'giorgio@mail.com','$2a$10$BXsVPAx9vEBTrI.VdnJldOLLn.tO5UhmEdJ9AKVjN0W5ShkSnhf0y','giorgio','via roma 1','CUSTOMER'),(30,'andrea@mail.com','$2a$10$f0/aVZ6ZzKAqlgqk.GIoBu6rxnLQCNVTxapPprwIoQcfYJkj4jWKe','andrea','via roma 1','CUSTOMER'),(31,'luca@mail.com','$2a$10$bVf91fAyx3ruMUh1BJe2LuBSd8LQqr3U9wErGStDC20LBn1qWYD7O','luca','via roma 1 ','SELLER');
/*!40000 ALTER TABLE `profiles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sellers`
--

DROP TABLE IF EXISTS `sellers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `sellers` (
  `id` bigint(20) NOT NULL,
  `shop_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK9j1lvfxfu6owkwwwta16ells` (`shop_id`),
  CONSTRAINT `FKc8xkoyta8drrik5bwta6ssjr0` FOREIGN KEY (`id`) REFERENCES `profiles` (`id`),
  CONSTRAINT `FKk7qxx5fjlp4iccdfabqvluxk8` FOREIGN KEY (`id`) REFERENCES `users` (`id`),
  CONSTRAINT `FKlowk8uws8c3vigdj0vbq8h86d` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sellers`
--

LOCK TABLES `sellers` WRITE;
/*!40000 ALTER TABLE `sellers` DISABLE KEYS */;
/*!40000 ALTER TABLE `sellers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shops`
--

DROP TABLE IF EXISTS `shops`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `shops` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `profile_id` bigint(20) NOT NULL,
  `category` enum('CASA_ARREDAMENTO','MODA_ACCESSORI','TECNOLOGIA_ELETTRONICA','SALUTE_FITNESS') DEFAULT NULL,
  `shop_category` enum('CASA_ARREDAMENTO','MODA_ACCESSORI','SALUTE_FITNESS','TECNOLOGIA_ELETTRONICA') DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKar5yyuartm46e1brh920fpfiv` (`name`),
  UNIQUE KEY `UK1vvgh1m7otlfrwg0dmmm1ogrp` (`profile_id`),
  CONSTRAINT `FKg2174o88t3xtt7uw2oh4dht1r` FOREIGN KEY (`profile_id`) REFERENCES `profiles` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shops`
--

LOCK TABLES `shops` WRITE;
/*!40000 ALTER TABLE `shops` DISABLE KEYS */;
INSERT INTO `shops` VALUES (20,'marioShop',21,'SALUTE_FITNESS','CASA_ARREDAMENTO'),(21,'gigiShop',22,NULL,'TECNOLOGIA_ELETTRONICA'),(22,'lucaShop',31,NULL,'CASA_ARREDAMENTO');
/*!40000 ALTER TABLE `shops` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `email` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-02-05 13:19:31
