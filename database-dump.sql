-- MySQL dump 10.13  Distrib 5.7.32, for Linux (x86_64)
--
-- Host: localhost    Database: gatlingdemostore
-- ------------------------------------------------------
-- Server version	5.7.32-0ubuntu0.18.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `admin`
--

DROP TABLE IF EXISTS `admin`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `admin` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `admin`
--

LOCK TABLES `admin` WRITE;
/*!40000 ALTER TABLE `admin` DISABLE KEYS */;
INSERT INTO `admin` VALUES (1,'JWillett','$2a$10$WFVx94AaqAGV2gt3g9ey1OSh2d4Q3B8b5.whnw4JR7b1/dNWECgji');
/*!40000 ALTER TABLE `admin` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `categories`
--

DROP TABLE IF EXISTS `categories`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `categories` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `slug` varchar(45) NOT NULL,
  `sorting` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `categories`
--

LOCK TABLES `categories` WRITE;
/*!40000 ALTER TABLE `categories` DISABLE KEYS */;
INSERT INTO `categories` VALUES (5,'For Him','for-him',100),(6,'For Her','for-her',100),(7,'Unisex','unisex',100);
/*!40000 ALTER TABLE `categories` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `pages`
--

DROP TABLE IF EXISTS `pages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `pages` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `title` varchar(45) NOT NULL,
  `slug` varchar(45) NOT NULL,
  `content` text NOT NULL,
  `sorting` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `pages`
--

LOCK TABLES `pages` WRITE;
/*!40000 ALTER TABLE `pages` DISABLE KEYS */;
INSERT INTO `pages` VALUES (1,'Home','home','<h2>Welcome to the Gatling DemoStore!</h2><p>&nbsp;</p><p>This is a fictional / dummy eCommerce store that sells eyeglass cases.</p><p>Since this is a sandbox solution, no actual sales transactions are completed on this site.</p><p>&nbsp;</p><h3><strong>&lt; Select a category from the left-hand menu</strong></h3><p>&nbsp;</p><p>The purpose of this store is to teach the creation of test scripts with the <a href=\"https://gatling.io\">Gatling</a> load testing tool.</p><p>To find out more, check out the <a href=\"https://gatling.io/academy/\">Gatling Academy</a></p>',0),(10,'About us','about-us','<h2>About Us</h2><p>&nbsp;</p><p>This is a fictional / dummy eCommerce store that sells eyeglass cases.</p><p>The purpose of this site is to teach the creation of test scripts with the <a href=\"https://gatling.io\">Gatling</a> load testing tool</p><p>&nbsp;</p>',100),(11,'Contact','contact','<h2>Contact Us</h2><p>&nbsp;</p><p>To learn how this site can help teach you the creation of load testing scripts, check out the <a href=\"https://gatling.io/academy/\">Gatling Academy</a></p><p>If you need to contact the Gatling team directly, check the <a href=\"https://gatling.io/company/contact/\">Contact Us</a> page on the Gatling website&nbsp;</p>',100);
/*!40000 ALTER TABLE `pages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `products` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(45) NOT NULL,
  `slug` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `image` varchar(45) NOT NULL,
  `price` decimal(8,2) NOT NULL,
  `category_id` int(11) NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `category_id` (`category_id`),
  CONSTRAINT `category_id_fk` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (17,'Casual Black-Blue','casual-black-blue','<p>Some casual black &amp; blue glasses</p>','casual-blackblue-open.jpg',24.99,5,'2020-11-10 10:05:14','2020-11-10 10:05:14'),(19,'Black and Red Glasses','black-and-red-glasses','<p>A Black &amp; Red glasses case</p>','casual-blackred-open.jpg',18.99,5,'2020-11-15 16:52:20','2020-11-15 16:52:21'),(20,'Bright Yellow Glasses','bright-yellow-glasses','<p>A glasses case that is bright yellow</p>','casual-blue-open.jpg',17.99,5,'2020-11-15 16:54:22','2020-11-15 16:54:22'),(21,'Casual Brown Glasses','casual-brown-glasses','<p>A vintage glasses case in casual brown</p>','casual-brown-open.jpg',13.99,5,'2020-11-15 16:55:03','2020-11-15 16:55:03'),(22,'Deepest Blue','deepest-blue','<p>Glasses case with a deep blue design</p>','casual-darkblue-open.jpg',29.99,5,'2020-11-15 16:56:01','2020-11-15 16:56:01'),(23,'Light Blue Glasses','light-blue-glasses','<p>A glasses case in light blue</p>','casual-lightblue-open.jpg',17.99,5,'2020-11-15 16:56:48','2020-11-15 16:56:48'),(24,'Sky Blue Case','sky-blue-case','<p>A perfect sky blue glasses case</p>','casual-skyblue-open.jpg',8.99,5,'2020-11-15 16:57:55','2020-11-15 16:57:55'),(25,'White Casual Case','white-casual-case','<p>A simple white casual design glasses case</p>','casual-white-open.jpg',16.99,5,'2020-11-15 16:58:27','2020-11-15 16:58:27'),(26,'Perfect Pink','perfect-pink','<p>Perfectly pink glasses case</p>','casual-pink-open.jpg',19.99,6,'2020-11-15 16:58:53','2020-11-15 16:58:53'),(27,'Curved Black','curved-black','<p>A lovely design with a curved black case</p>','curve-black-open.jpg',12.99,6,'2020-11-15 16:59:32','2020-11-15 16:59:32'),(28,'Black Grey Curved','black-grey-curved','<p>Glasses case with a black grey curved design</p>','curve-blackgrey-open.jpg',14.99,6,'2020-11-15 17:00:31','2020-11-15 17:00:31'),(29,'Black Light Blue','black-light-blue','<p>Case with an ocean blue design on black</p>','curve-blacklightblue-open.jpg',23.99,6,'2020-11-15 17:01:05','2020-11-15 17:01:05'),(30,'Curved Pink','curved-pink','<p>Curved pink glasses case</p>','curve-blackpink-open.jpg',16.99,6,'2020-11-15 17:01:30','2020-11-15 17:01:30'),(31,'Velvet Red','velvet-red','<p>Beautiful glasses case with a curved red design</p>','curve-blackred-open.jpg',18.99,6,'2020-11-15 17:02:05','2020-11-15 17:02:05'),(32,'Deep Blue Ocean','deep-blue-ocean','<p>Deep blue ocean glasses case</p>','curve-blue-open.jpg',27.99,6,'2020-11-15 17:02:36','2020-11-15 17:02:36'),(33,'Curved Brown','curved-brown','<p>Curved brown glasses case perfect for him or her</p>','curve-brown-open.jpg',19.99,7,'2020-11-25 07:16:45','2020-11-25 07:16:46'),(34,'Leopard Skin','leopard-skin','<p>Leopard skin glasses case</p>','curve-brownpattern-open.jpg',22.99,7,'2020-11-15 17:03:42','2020-11-15 17:03:42'),(35,'Gold Design','gold-design','<p>Gold design glasses case</p>','curve-gold-open.jpg',39.99,7,'2020-11-15 17:04:13','2020-11-15 17:04:13'),(36,'Pink Panther','pink-panther','<p>Pink panther style glasses case</p>','curve-pinkpattern-open.jpg',27.99,7,'2020-11-15 17:04:51','2020-11-15 17:04:51'),(37,'Curve Ocean Sky','curve-ocean-sky','<p>Light blue design of the ocean</p>','curve-skyblue-open.jpg',18.99,7,'2020-11-15 17:05:25','2020-11-15 17:05:25'),(38,'Plain White','plain-white','<p>Simple plain white glasses case, with blue interior</p>','curve-white-open.jpg',9.99,7,'2020-11-15 17:05:56','2020-11-15 17:05:56'),(39,'White Leopard Pattern','white-leopard-pattern','<p>White leopard pattern design glasses case</p>','curve-whitepattern-open.jpg',13.99,7,'2020-11-15 17:06:32','2020-11-15 17:06:32');
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(45) NOT NULL,
  `password` varchar(255) NOT NULL,
  `email` varchar(45) NOT NULL,
  `phone_number` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'john','$2a$10$98T7JTswTQ0agIBTs44Tb.NKDOnhXaXMl2GQsovNAx5N0SUDTGBM2','john@gmail.com','11111111'),(2,'user1','$2a$10$bBOalN4L9Ha/Qtwe4p5jG.Y7BAJKE0atDLVWY62pplrFhY9ADm90G','user1@email.com','11111111'),(3,'user2','$2a$10$APQuj4jTQ0dnFXaTPb.61ejynOla7xlbNZC53o.2yM3w0Z9D.nRsm','user2@email.com','11111111'),(4,'user3','$2a$10$s9M5Ch3YBTbwNHGnkeGYquWdb4Psnwzs4hmbzNOozkres0nNXg.rO','user3@email.com','11111111');
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

-- Dump completed on 2021-01-09  8:44:34
