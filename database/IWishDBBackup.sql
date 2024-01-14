-- MySQL dump 10.13  Distrib 8.0.35, for Linux (x86_64)
--
-- Host: localhost    Database: IWishDB
-- ------------------------------------------------------
-- Server version	8.0.35

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
-- Table structure for table `Contributions`
--

DROP TABLE IF EXISTS `Contributions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Contributions` (
  `ContributionID` int NOT NULL AUTO_INCREMENT,
  `UserID` int DEFAULT NULL,
  `FriendID` int DEFAULT NULL,
  `ItemID` int DEFAULT NULL,
  `AMOUNT` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ContributionID`),
  KEY `fk_Contributionsuser` (`UserID`),
  KEY `fk_Contributionsfriend` (`FriendID`),
  KEY `fk_Contributionsitem` (`ItemID`),
  CONSTRAINT `fk_Contributionsfriend` FOREIGN KEY (`FriendID`) REFERENCES `Users` (`UserID`),
  CONSTRAINT `fk_Contributionsitem` FOREIGN KEY (`ItemID`) REFERENCES `Items` (`ItemID`),
  CONSTRAINT `fk_Contributionsuser` FOREIGN KEY (`UserID`) REFERENCES `Users` (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Contributions`
--

LOCK TABLES `Contributions` WRITE;
/*!40000 ALTER TABLE `Contributions` DISABLE KEYS */;
INSERT INTO `Contributions` VALUES (1,1,3,1,649.50),(2,1,2,1,0.50);
/*!40000 ALTER TABLE `Contributions` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Friendship`
--

DROP TABLE IF EXISTS `Friendship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Friendship` (
  `UserID` int NOT NULL,
  `FriendID` int NOT NULL,
  `FriendshipStatus` enum('Pending','Accepted') NOT NULL,
  PRIMARY KEY (`UserID`,`FriendID`),
  KEY `fk_friendshipfriend` (`FriendID`),
  CONSTRAINT `fk_friendshipfriend` FOREIGN KEY (`FriendID`) REFERENCES `Users` (`UserID`),
  CONSTRAINT `fk_friendshipuser` FOREIGN KEY (`UserID`) REFERENCES `Users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Friendship`
--

LOCK TABLES `Friendship` WRITE;
/*!40000 ALTER TABLE `Friendship` DISABLE KEYS */;
INSERT INTO `Friendship` VALUES (2,1,'Accepted'),(3,1,'Accepted');
/*!40000 ALTER TABLE `Friendship` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Items`
--

DROP TABLE IF EXISTS `Items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Items` (
  `ItemID` int NOT NULL AUTO_INCREMENT,
  `ItemName` varchar(50) NOT NULL,
  `ItemPhoto` blob,
  `Price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ItemID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Items`
--

LOCK TABLES `Items` WRITE;
/*!40000 ALTER TABLE `Items` DISABLE KEYS */;
INSERT INTO `Items` VALUES (1,'Ticket to Japan',NULL,650.00),(2,'Fiat Tipo ',NULL,5000.00),(3,'cadillac fleetwood 1990 ',NULL,5000.00),(4,'TV',NULL,500.00),(5,'Home',NULL,2.00);
/*!40000 ALTER TABLE `Items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Users`
--

DROP TABLE IF EXISTS `Users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Users` (
  `UserID` int NOT NULL AUTO_INCREMENT,
  `Email` varchar(50) NOT NULL,
  `FullName` varchar(50) NOT NULL,
  `UserPhoto` blob,
  `Password` varchar(50) NOT NULL,
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Users`
--

LOCK TABLES `Users` WRITE;
/*!40000 ALTER TABLE `Users` DISABLE KEYS */;
INSERT INTO `Users` VALUES (1,'abdallah.mohamed0079@gmail.com','Abdullah Attia',NULL,'root'),(2,'diaaahmed38@gmail.com','Diaa Ahmed',NULL,'root'),(3,'mahmoudhatem96@gmail.com','Mahmoud Hatem',NULL,'root'),(4,'mennamamdouh@gmail.com','Menna Mamdouh',NULL,'root'),(5,'omarramadan1425@gmail.com','Omar Ramadan',NULL,'root');
/*!40000 ALTER TABLE `Users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Wishlist`
--

DROP TABLE IF EXISTS `Wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Wishlist` (
  `UserID` int NOT NULL,
  `ItemID` int NOT NULL,
  `Status` enum('In Progress','Complete') NOT NULL,
  PRIMARY KEY (`UserID`,`ItemID`),
  KEY `fk_wishlistitem` (`ItemID`),
  CONSTRAINT `fk_wishlistitem` FOREIGN KEY (`ItemID`) REFERENCES `Items` (`ItemID`),
  CONSTRAINT `fk_wishlistuser` FOREIGN KEY (`UserID`) REFERENCES `Users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Wishlist`
--

LOCK TABLES `Wishlist` WRITE;
/*!40000 ALTER TABLE `Wishlist` DISABLE KEYS */;
INSERT INTO `Wishlist` VALUES (1,1,'Complete'),(3,3,'In Progress');
/*!40000 ALTER TABLE `Wishlist` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-12-31 20:09:57
