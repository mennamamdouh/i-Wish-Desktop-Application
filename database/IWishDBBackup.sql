-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: IWishDB
-- ------------------------------------------------------
-- Server version	8.0.31

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
-- Table structure for table `contributions`
--

DROP TABLE IF EXISTS `contributions`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contributions` (
  `ContributionID` int NOT NULL AUTO_INCREMENT,
  `UserID` int DEFAULT NULL,
  `FriendID` int DEFAULT NULL,
  `ItemID` int DEFAULT NULL,
  `AMOUNT` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ContributionID`),
  KEY `fk_Contributionsuser` (`UserID`),
  KEY `fk_Contributionsfriend` (`FriendID`),
  KEY `fk_Contributionsitem` (`ItemID`),
  CONSTRAINT `fk_Contributionsfriend` FOREIGN KEY (`FriendID`) REFERENCES `users` (`UserID`),
  CONSTRAINT `fk_Contributionsitem` FOREIGN KEY (`ItemID`) REFERENCES `items` (`ItemID`),
  CONSTRAINT `fk_Contributionsuser` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contributions`
--

LOCK TABLES `contributions` WRITE;
/*!40000 ALTER TABLE `contributions` DISABLE KEYS */;
INSERT INTO `contributions` VALUES (1,2,1,4,250.00),(2,2,4,4,100.00),(3,2,5,4,150.00),(8,1,2,4,200.00),(9,1,3,4,300.00);
/*!40000 ALTER TABLE `contributions` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `contributionsUpdates` AFTER INSERT ON `contributions` FOR EACH ROW BEGIN
    
    DECLARE notificationText VARCHAR(255);
    DECLARE addedAmount DECIMAL(10, 2);
    DECLARE totalContributionsAmount DECIMAL(10, 2);
    DECLARE itemPrice DECIMAL(10, 2);
    DECLARE contributingFriendsIds VARCHAR(255);
    DECLARE userName VARCHAR(50);
    DECLARE friendId INT;
    DECLARE friendIdIndex INT;

    
    SET addedAmount = NEW.amount;
    SELECT SUM(C.amount) INTO totalContributionsAmount FROM Contributions AS C WHERE C.ItemID = NEW.ItemID AND C.UserID = NEW.UserID;
    SELECT I.Price INTO itemPrice FROM Items AS I WHERE I.ItemID = NEW.ItemID;

    
    IF totalContributionsAmount <= itemPrice THEN
        SELECT CONCAT(F.FullName, " has contributed on ", I.ItemName, " that is on your wishlist with amount = ", addedAmount, " LE.") INTO notificationText
            FROM Users AS F
            INNER JOIN Contributions AS C
                ON F.UserID = C.FriendID
            INNER JOIN Items AS I
                ON I.ItemID = C.ItemID
            WHERE F.UserID = NEW.FriendID
            LIMIT 1;
        
        
        INSERT INTO Notifications(UserID, FriendID, ItemID, NotificationType, Notification)
            VALUES(NEW.UserID, NEW.FriendID, NEW.ItemID, "SOMEONE_CONTRIBUTED", notificationText);
        
        
        IF totalContributionsAmount = itemPrice THEN
            SELECT CONCAT("Congratulations! Your friend(s) ", GROUP_CONCAT(DISTINCT F.FullName SEPARATOR ', '), " got you ", I.ItemName, ".") INTO notificationText
                FROM Contributions AS C
                INNER JOIN Items AS I
                    ON I.ItemID = NEW.ItemID
                INNER JOIN Users AS F
                    ON F.UserID = C.FriendID
                GROUP BY C.UserID, I.ItemID
                LIMIT 1;

            
            INSERT INTO Notifications(UserID, FriendID, ItemID, NotificationType, Notification)
                VALUES(NEW.UserID, NULL, NEW.ItemID, "CONTRIBUTION_COMPLETED", notificationText);

            
            
            SELECT GROUP_CONCAT(DISTINCT C.FriendID) INTO contributingFriendsIds
                FROM Contributions AS C
                WHERE C.UserID = NEW.UserID AND C.ItemID = NEW.ItemID;
            
            
            SET friendIdIndex = 1;
            WHILE friendIdIndex > 0 DO
                SET friendId = CAST(SUBSTRING_INDEX(contributingFriendsIds, ',', 1) AS UNSIGNED INT);

                
                SELECT FullName INTO userName FROM Users WHERE UserID = NEW.UserID;
                SELECT CONCAT("Your contribution helped your friend ", userName, " get ", ItemName, "!") INTO notificationText
                    FROM Items WHERE ItemID = NEW.ItemID;

                
                INSERT INTO Notifications(UserID, FriendID, ItemID, NotificationType, Notification)
                    VALUES(friendId, NEW.UserID, NEW.ItemID, "FRIEND_GOT_ITEM", notificationText);

                
                SET friendIdIndex = INSTR(contributingFriendsIds, ',');
                SET contributingFriendsIds = SUBSTRING(contributingFriendsIds, friendIdIndex + 1);

            END WHILE;
        END IF;
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `updateWishlistProgress` AFTER INSERT ON `contributions` FOR EACH ROW BEGIN
    DECLARE totalContributions DECIMAL(10, 2);
    DECLARE itemPrice DECIMAL(10, 2);
    DECLARE newProgress DECIMAL(10, 2);
    DECLARE newRemaining DECIMAL(10, 2);

    SET itemPrice = (SELECT I.Price FROM Items AS I WHERE I.ItemID = NEW.ItemID);
    SET totalContributions = (SELECT SUM(C.Amount) FROM Contributions AS C WHERE C.UserID = NEW.UserID AND C.ItemID = NEW.ItemID GROUP BY C.UserID, C.ItemID);
    
    SET newProgress = totalContributions / itemPrice * 100;
    SET newRemaining = itemPrice - totalContributions;

    UPDATE Wishlist AS W SET W.Progress = newProgress, W.Remaining = newRemaining WHERE W.UserID = NEW.UserID AND W.ItemID = NEW.ItemID;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `friendship`
--

DROP TABLE IF EXISTS `friendship`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `friendship` (
  `UserID` int NOT NULL,
  `FriendID` int NOT NULL,
  `FriendshipStatus` enum('Pending','Accepted') NOT NULL,
  PRIMARY KEY (`UserID`,`FriendID`),
  KEY `fk_friendshipfriend` (`FriendID`),
  CONSTRAINT `fk_friendshipfriend` FOREIGN KEY (`FriendID`) REFERENCES `users` (`UserID`),
  CONSTRAINT `fk_friendshipuser` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `friendship`
--

LOCK TABLES `friendship` WRITE;
/*!40000 ALTER TABLE `friendship` DISABLE KEYS */;
INSERT INTO `friendship` VALUES (2,1,'Accepted'),(2,3,'Accepted'),(3,1,'Accepted'),(4,1,'Accepted'),(4,2,'Pending'),(4,3,'Accepted');
/*!40000 ALTER TABLE `friendship` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `friendRequest` AFTER INSERT ON `friendship` FOR EACH ROW BEGIN
    DECLARE notificationText VARCHAR(255);
    
    IF (NEW.FriendshipStatus = 'Pending') THEN
        SELECT CONCAT(FullName, " has sent a friend request for you.") INTO notificationText FROM Users WHERE UserID = NEW.FriendID;
        INSERT INTO Notifications(UserID, FriendID, ItemID, NotificationType, Notification)
            VALUES(NEW.UserID, NEW.FriendID, NULL, "REQUEST_SENT", notificationText);
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `requestAccepted` AFTER UPDATE ON `friendship` FOR EACH ROW BEGIN
    DECLARE notificationText VARCHAR(255);
    
    IF (NEW.FriendshipStatus = 'Accepted') THEN
        SELECT CONCAT(FullName, " has accepted your friend request.") INTO notificationText FROM Users WHERE UserID = NEW.FriendID;
        INSERT INTO Notifications(UserID, FriendID, ItemID, NotificationType, Notification)
            VALUES(NEW.UserID, NEW.FriendID, NULL, "REQUEST_ACCEPTED", notificationText);
    END IF;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `items`
--

DROP TABLE IF EXISTS `items`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `items` (
  `ItemID` int NOT NULL AUTO_INCREMENT,
  `ItemName` varchar(50) NOT NULL,
  `ItemPhoto` varchar(255) DEFAULT NULL,
  `Price` decimal(10,2) NOT NULL,
  PRIMARY KEY (`ItemID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `items`
--

LOCK TABLES `items` WRITE;
/*!40000 ALTER TABLE `items` DISABLE KEYS */;
INSERT INTO `items` VALUES (1,'Ticket to Japan',NULL,650.00),(2,'Fiat Tipo ',NULL,5000.00),(3,'cadillac fleetwood 1990 ',NULL,5000.00),(4,'TV',NULL,500.00),(5,'Home',NULL,2.00),(6,'Shampoo',NULL,300.00);
/*!40000 ALTER TABLE `items` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notifications`
--

DROP TABLE IF EXISTS `notifications`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notifications` (
  `NotificationID` int NOT NULL AUTO_INCREMENT,
  `UserID` int NOT NULL,
  `FriendID` int DEFAULT NULL,
  `ItemID` int DEFAULT NULL,
  `Seen` tinyint(1) DEFAULT NULL,
  `NotificationType` enum('REQUEST_SENT','REQUEST_ACCEPTED','SOMEONE_CONTRIBUTED','FRIEND_GOT_ITEM','CONTRIBUTION_COMPLETED') DEFAULT NULL,
  `Notification` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`NotificationID`),
  KEY `fk_user_notification` (`UserID`),
  KEY `fk_friend_notification` (`FriendID`),
  KEY `fk_item_notification` (`ItemID`),
  CONSTRAINT `fk_friend_notification` FOREIGN KEY (`FriendID`) REFERENCES `users` (`UserID`),
  CONSTRAINT `fk_item_notification` FOREIGN KEY (`ItemID`) REFERENCES `items` (`ItemID`),
  CONSTRAINT `fk_user_notification` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notifications`
--

LOCK TABLES `notifications` WRITE;
/*!40000 ALTER TABLE `notifications` DISABLE KEYS */;
INSERT INTO `notifications` VALUES (1,2,1,4,NULL,'SOMEONE_CONTRIBUTED','Abdullah Attia has contributed on TV that is on your wishlist with amount = 250.00 LE.'),(2,2,4,4,NULL,'SOMEONE_CONTRIBUTED','Menna Mamdouh has contributed on TV that is on your wishlist with amount = 100.00 LE.'),(3,2,5,4,NULL,'SOMEONE_CONTRIBUTED','Omar Ramadan has contributed on TV that is on your wishlist with amount = 150.00 LE.'),(4,2,NULL,4,NULL,'CONTRIBUTION_COMPLETED','Congratulations! Your friend(s) Abdullah Attia, Menna Mamdouh, Omar Ramadan got you TV.'),(5,1,2,4,NULL,'FRIEND_GOT_ITEM','Your contribution helped your friend Diaa Ahmed get TV!'),(6,4,2,4,NULL,'FRIEND_GOT_ITEM','Your contribution helped your friend Diaa Ahmed get TV!'),(7,5,2,4,NULL,'FRIEND_GOT_ITEM','Your contribution helped your friend Diaa Ahmed get TV!'),(8,1,2,4,NULL,'SOMEONE_CONTRIBUTED','Diaa Ahmed has contributed on TV that is on your wishlist with amount = 200.00 LE.'),(9,1,3,4,NULL,'SOMEONE_CONTRIBUTED','Mahmoud Hatem has contributed on TV that is on your wishlist with amount = 300.00 LE.'),(10,1,NULL,4,NULL,'CONTRIBUTION_COMPLETED','Congratulations! Your friend(s) Diaa Ahmed, Mahmoud Hatem got you TV.'),(11,2,1,4,NULL,'FRIEND_GOT_ITEM','Your contribution helped your friend Abdullah Attia get TV!'),(12,3,1,4,NULL,'FRIEND_GOT_ITEM','Your contribution helped your friend Abdullah Attia get TV!'),(13,1,2,4,NULL,'SOMEONE_CONTRIBUTED','Diaa Ahmed has contributed on TV that is on your wishlist with amount = 200.00 LE.'),(14,1,3,4,NULL,'SOMEONE_CONTRIBUTED','Mahmoud Hatem has contributed on TV that is on your wishlist with amount = 300.00 LE.'),(15,1,NULL,4,NULL,'CONTRIBUTION_COMPLETED','Congratulations! Your friend(s) Diaa Ahmed, Mahmoud Hatem got you TV.'),(16,2,1,4,NULL,'FRIEND_GOT_ITEM','Your contribution helped your friend Abdullah Attia get TV!'),(17,3,1,4,NULL,'FRIEND_GOT_ITEM','Your contribution helped your friend Abdullah Attia get TV!'),(18,1,2,4,NULL,'SOMEONE_CONTRIBUTED','Diaa Ahmed has contributed on TV that is on your wishlist with amount = 200.00 LE.'),(19,1,3,4,NULL,'SOMEONE_CONTRIBUTED','Mahmoud Hatem has contributed on TV that is on your wishlist with amount = 300.00 LE.'),(20,1,NULL,4,NULL,'CONTRIBUTION_COMPLETED','Congratulations! Your friend(s) Diaa Ahmed, Mahmoud Hatem got you TV.'),(21,2,1,4,NULL,'FRIEND_GOT_ITEM','Your contribution helped your friend Abdullah Attia get TV!'),(22,3,1,4,NULL,'FRIEND_GOT_ITEM','Your contribution helped your friend Abdullah Attia get TV!');
/*!40000 ALTER TABLE `notifications` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `UserID` int NOT NULL AUTO_INCREMENT,
  `Email` varchar(50) NOT NULL,
  `FullName` varchar(50) NOT NULL,
  `UserPhoto` varchar(255) DEFAULT NULL,
  `Password` varchar(50) NOT NULL,
  `DateOfBirth` date NOT NULL,
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'abdallah.mohamed0079@gmail.com','Abdullah Attia','resources/male-avatar.png','root','1999-11-24'),(2,'diaaahmed38@gmail.com','Diaa Ahmed','resources/male-avatar.png','root','1999-05-30'),(3,'mahmoudhatem96@gmail.com','Mahmoud Hatem','resources/male-avatar.png','root','1996-07-13'),(4,'mennamamdouh@gmail.com','Menna Mamdouh','resources/female-avatar.png','root','2000-01-07'),(5,'omarramadan1425@gmail.com','Omar Ramadan','resources/male-avatar.png','root','2001-06-30');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `wishlist`
--

DROP TABLE IF EXISTS `wishlist`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `wishlist` (
  `UserID` int NOT NULL,
  `ItemID` int NOT NULL,
  `Progress` decimal(10,2) DEFAULT NULL,
  `Remaining` decimal(10,2) DEFAULT NULL,
  PRIMARY KEY (`UserID`,`ItemID`),
  KEY `fk_item_wishlist` (`ItemID`),
  CONSTRAINT `fk_item_wishlist` FOREIGN KEY (`ItemID`) REFERENCES `items` (`ItemID`),
  CONSTRAINT `fk_user_wishlist` FOREIGN KEY (`UserID`) REFERENCES `users` (`UserID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `wishlist`
--

LOCK TABLES `wishlist` WRITE;
/*!40000 ALTER TABLE `wishlist` DISABLE KEYS */;
INSERT INTO `wishlist` VALUES (1,4,100.00,0.00),(2,4,0.00,500.00);
/*!40000 ALTER TABLE `wishlist` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = cp850 */ ;
/*!50003 SET character_set_results = cp850 */ ;
/*!50003 SET collation_connection  = cp850_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `setWishlistDefaults` BEFORE INSERT ON `wishlist` FOR EACH ROW BEGIN
    SET NEW.Progress = 0;
    SET NEW.Remaining = (SELECT I.Price FROM Items AS I WHERE I.ItemID = NEW.ItemID);
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-01-20 15:02:57
