CREATE DATABASE  IF NOT EXISTS `forum` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `forum`;
-- MySQL dump 10.13  Distrib 8.0.13, for Win64 (x86_64)
--
-- Host: localhost    Database: forum
-- ------------------------------------------------------
-- Server version	8.0.13

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `block`
--

DROP TABLE IF EXISTS `block`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `block` (
  `blocker` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `blocked` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`blocker`,`blocked`),
  KEY `blockedCon_idx` (`blocked`),
  CONSTRAINT `blockedCon` FOREIGN KEY (`blocked`) REFERENCES `username` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `blockerCon` FOREIGN KEY (`blocker`) REFERENCES `username` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `block`
--

LOCK TABLES `block` WRITE;
/*!40000 ALTER TABLE `block` DISABLE KEYS */;
/*!40000 ALTER TABLE `block` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deletedmessages`
--

DROP TABLE IF EXISTS `deletedmessages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `deletedmessages` (
  `messageID` int(10) unsigned NOT NULL,
  `deletedFromUser` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  PRIMARY KEY (`messageID`,`deletedFromUser`),
  KEY `usernameCon_idx` (`deletedFromUser`),
  CONSTRAINT `messageCon` FOREIGN KEY (`messageID`) REFERENCES `messages` (`messageid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usernameCon` FOREIGN KEY (`deletedFromUser`) REFERENCES `username` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deletedmessages`
--

LOCK TABLES `deletedmessages` WRITE;
/*!40000 ALTER TABLE `deletedmessages` DISABLE KEYS */;
/*!40000 ALTER TABLE `deletedmessages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `forumthreads`
--

DROP TABLE IF EXISTS `forumthreads`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `forumthreads` (
  `threadID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `locked` int(1) NOT NULL DEFAULT '0',
  `posts` int(10) unsigned DEFAULT '0',
  PRIMARY KEY (`threadID`)
) ENGINE=InnoDB AUTO_INCREMENT=60 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `forumthreads`
--

LOCK TABLES `forumthreads` WRITE;
/*!40000 ALTER TABLE `forumthreads` DISABLE KEYS */;
/*!40000 ALTER TABLE `forumthreads` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `messages`
--

DROP TABLE IF EXISTS `messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `messages` (
  `messageID` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `fromUser` varchar(45) DEFAULT NULL,
  `toUser` varchar(45) DEFAULT NULL,
  `message` varchar(8000) DEFAULT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `edited` int(1) NOT NULL DEFAULT '0',
  `dateEdited` timestamp NULL DEFAULT NULL,
  `lastEditor` varchar(45) DEFAULT NULL,
  `seen` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`messageID`),
  KEY `UsersInteraction_idx` (`fromUser`),
  KEY `UsersInteraction2_idx` (`toUser`),
  CONSTRAINT `UsersInteraction` FOREIGN KEY (`fromUser`) REFERENCES `username` (`username`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `UsersInteraction2` FOREIGN KEY (`toUser`) REFERENCES `username` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=227 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `messages`
--

LOCK TABLES `messages` WRITE;
/*!40000 ALTER TABLE `messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `messages` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thread_messages`
--

DROP TABLE IF EXISTS `thread_messages`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `thread_messages` (
  `thred_message_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `username` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `message` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `threadID` int(10) unsigned NOT NULL,
  `edited` int(1) NOT NULL DEFAULT '0',
  `dateEdited` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`thred_message_id`),
  KEY `usernamencoonn_idx` (`username`),
  KEY `threadIdCONN_idx` (`threadID`),
  CONSTRAINT `threadIdCONN` FOREIGN KEY (`threadID`) REFERENCES `forumthreads` (`threadid`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `usernamencoonn` FOREIGN KEY (`username`) REFERENCES `username` (`username`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thread_messages`
--

LOCK TABLES `thread_messages` WRITE;
/*!40000 ALTER TABLE `thread_messages` DISABLE KEYS */;
/*!40000 ALTER TABLE `thread_messages` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `thread_messages_AFTER_INSERT` AFTER INSERT ON `thread_messages` FOR EACH ROW BEGIN
	declare IDofThread INT(10);
    declare numberOfposts int(10);
    SET IDofThread= (select ThreadID from thread_messages order by thred_message_id desc limit 1);
    SET numberOfposts = (select count(threadID) from thread_messages where threadID=IDofThread);
    update forumthreads set posts=numberOfposts where threadID=IDofThread;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `thread_messages_AFTER_DELETE` AFTER DELETE ON `thread_messages` FOR EACH ROW BEGIN
	declare IDofThread INT(10);
    declare numberOfposts int(10);
    set IDofThread = old.threadID;
	SET numberOfposts = (select count(threadID) from thread_messages where threadID=IDofThread);
    update forumthreads set posts=numberOfposts where threadID=IDofThread;
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `username`
--

DROP TABLE IF EXISTS `username`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `username` (
  `username` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `password` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `name` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `surname` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `level` int(11) NOT NULL DEFAULT '0',
  `banned` int(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `username`
--

LOCK TABLES `username` WRITE;
/*!40000 ALTER TABLE `username` DISABLE KEYS */;
INSERT INTO `username` VALUES ('admin','admin','Theodoros','Bouras','damcbour@hotmail.com',4,0),('user','1234','kostas','papadopoulos','kostas@hotmail.com',0,0),('user1','1234','John','papadopoulos','john@hotmail.com',1,0),('user2','1234','maria','georgiou','maria@hotmail.com',2,0),('user3','1234','Jimmy','laou','jimmy@hotmail.com',3,0);
/*!40000 ALTER TABLE `username` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-01-18  5:27:28
