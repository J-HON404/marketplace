USE marketplace;


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

