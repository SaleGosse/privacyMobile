-- MySQL dump 10.15  Distrib 10.0.34-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: privacyDB
-- ------------------------------------------------------
-- Server version	10.0.34-MariaDB-0ubuntu0.16.04.1

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
-- Table structure for table `Conversation`
--

DROP TABLE IF EXISTS `Conversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Conversation` (
  `idConversation` int(11) NOT NULL AUTO_INCREMENT,
  `convName` varchar(255) DEFAULT NULL,
  `creationDate` varchar(255) DEFAULT NULL,
  `lastDate` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idConversation`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Conversation`
--

LOCK TABLES `Conversation` WRITE;
/*!40000 ALTER TABLE `Conversation` DISABLE KEYS */;
INSERT INTO `Conversation` VALUES (1,'First Conv','25/05/2018','25/05/2018 10:05'),(2,'Second Conv','25/05/2018','26/05/2018 10:15'),(3,'La 3eme Conv','03/15/2013','03/06/2018 22:22');
/*!40000 ALTER TABLE `Conversation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Message`
--

DROP TABLE IF EXISTS `Message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Message` (
  `idMessage` int(11) NOT NULL AUTO_INCREMENT,
  `idConversation` int(11) DEFAULT NULL,
  `idUser` int(11) DEFAULT NULL,
  `content` text,
  `date` datetime DEFAULT NULL,
  PRIMARY KEY (`idMessage`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Message`
--

LOCK TABLES `Message` WRITE;
/*!40000 ALTER TABLE `Message` DISABLE KEYS */;
INSERT INTO `Message` VALUES (1,1,1,'Salut !','2018-05-25 10:00:34'),(2,1,2,'Salut ! Comment tu vas ?!','2018-05-25 10:45:21'),(3,1,1,'Tranquille, j\'ai des bonbons dans ma camionette...','2018-05-25 10:55:34'),(4,1,1,'Th','2018-05-25 11:59:56'),(5,1,1,'Test','2018-06-05 14:12:55'),(7,1,2,'Trpp','2018-06-05 14:57:20'),(8,3,2,'Salut ! ','2018-06-05 14:57:49'),(9,3,1,'','2018-06-05 14:59:20'),(10,3,1,'Salut l\'ami ','2018-06-05 14:59:33'),(11,3,2,'Coucou','2018-06-05 15:02:37'),(12,3,1,'','2018-06-05 15:03:09'),(13,3,1,'Salut salut ','2018-06-05 15:03:24'),(14,1,2,'Jtai','2018-06-05 15:03:57'),(15,1,1,'Pont','2018-06-05 15:04:12'),(16,3,2,'Je is ','2018-06-05 15:06:37'),(17,3,2,'I\'ve been e for a ','2018-06-05 15:06:39'),(18,3,2,'Ggzzu s','2018-06-05 15:06:40'),(24,1,1,'Trop','2018-06-06 13:18:54'),(25,1,1,'Test','2018-06-06 14:37:34'),(26,1,1,'Rhgfg','2018-06-06 14:37:49');
/*!40000 ALTER TABLE `Message` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `MessageStatus`
--

DROP TABLE IF EXISTS `MessageStatus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MessageStatus` (
  `idMessageStatus` int(11) NOT NULL AUTO_INCREMENT,
  `idMessage` int(11) DEFAULT NULL,
  `idUser` int(11) DEFAULT NULL,
  `notified` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`idMessageStatus`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `MessageStatus`
--

LOCK TABLES `MessageStatus` WRITE;
/*!40000 ALTER TABLE `MessageStatus` DISABLE KEYS */;
INSERT INTO `MessageStatus` VALUES (1,7,1,1),(2,11,1,NULL);
/*!40000 ALTER TABLE `MessageStatus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Profile`
--

DROP TABLE IF EXISTS `Profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Profile` (
  `idProfile` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` int(11) DEFAULT NULL,
  `lastName` varchar(255) DEFAULT NULL,
  `firstName` varchar(255) DEFAULT NULL,
  `phoneNumber` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idProfile`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Profile`
--

LOCK TABLES `Profile` WRITE;
/*!40000 ALTER TABLE `Profile` DISABLE KEYS */;
INSERT INTO `Profile` VALUES (1,1,'Loiseau','Titi','06.98.00.23.11'),(2,2,'Lechat','Grosminet','06.98.01.23.11');
/*!40000 ALTER TABLE `Profile` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `cookie` varchar(255) DEFAULT NULL,
  `pub_key` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`idUser`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `User`
--

LOCK TABLES `User` WRITE;
/*!40000 ALTER TABLE `User` DISABLE KEYS */;
INSERT INTO `User` VALUES (1,'titi','463dfe4008d391a10dd6d1faaaa9fc83b9528392bc5930a28b271485df1418be','dd89f3771953ed0d373bcd07cd27fea61e6e5a9659a49cb33e57ba3d570dbe3b',NULL),(2,'grosminet','e64b0c8af7c39513dc33ec7a448b929a2504276058b3178563bae6fb2f1c581f','52bcf67cbdd50e00a253b1430a50f30480b10f80ce6111b4b0893ff28609c5e5',NULL);
/*!40000 ALTER TABLE `User` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `linkConversation`
--

DROP TABLE IF EXISTS `linkConversation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `linkConversation` (
  `idLinkConversation` int(11) NOT NULL AUTO_INCREMENT,
  `idUser` int(11) DEFAULT NULL,
  `idConversation` int(11) DEFAULT NULL,
  PRIMARY KEY (`idLinkConversation`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `linkConversation`
--

LOCK TABLES `linkConversation` WRITE;
/*!40000 ALTER TABLE `linkConversation` DISABLE KEYS */;
INSERT INTO `linkConversation` VALUES (1,1,1),(2,2,1),(3,1,3),(4,2,3);
/*!40000 ALTER TABLE `linkConversation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2018-06-10 16:47:46
