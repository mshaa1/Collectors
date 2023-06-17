-- MySQL dump 10.13  Distrib 8.0.33, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: collectors
-- ------------------------------------------------------
-- Server version	8.0.33

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
-- Table structure for table `autore`
--

DROP TABLE IF EXISTS `autore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `autore` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(20) DEFAULT NULL,
  `cognome` varchar(20) DEFAULT NULL,
  `data_nascita` date NOT NULL,
  `nome_autore` varchar(25) NOT NULL,
  `info` varchar(255) DEFAULT NULL,
  `ruolo` enum('esecutore','compositore','poliedrico') DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autore`
--

LOCK TABLES `autore` WRITE;
/*!40000 ALTER TABLE `autore` DISABLE KEYS */;
INSERT INTO `autore` VALUES (1,'Hitori','Gotou','2004-02-21','GuitarHero',NULL,'poliedrico'),(2,'Nijika','Ijichi','2004-05-29','Nijika',NULL,'poliedrico'),(3,'Ryo','Yamada','2002-09-18','Ryo',NULL,'poliedrico'),(4,'Kita','Ikuyo','2003-04-21','Kita','la chitarra scappata','poliedrico'),(5,NULL,NULL,'1988-12-23','Tobu',NULL,'poliedrico'),(6,NULL,NULL,'1997-10-05','Janji',NULL,'poliedrico'),(7,NULL,NULL,'2007-01-06','BURNOUT SYNDROMES',NULL,'poliedrico');
/*!40000 ALTER TABLE `autore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `colleziona_dischi`
--

DROP TABLE IF EXISTS `colleziona_dischi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `colleziona_dischi` (
  `numero_duplicati` int NOT NULL,
  `ID_collezionista` int NOT NULL,
  `ID_disco` int NOT NULL,
  KEY `ID_collezionista` (`ID_collezionista`),
  KEY `ID_disco` (`ID_disco`),
  CONSTRAINT `colleziona_dischi_ibfk_1` FOREIGN KEY (`ID_collezionista`) REFERENCES `collezionista` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `colleziona_dischi_ibfk_2` FOREIGN KEY (`ID_disco`) REFERENCES `disco` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colleziona_dischi`
--

LOCK TABLES `colleziona_dischi` WRITE;
/*!40000 ALTER TABLE `colleziona_dischi` DISABLE KEYS */;
INSERT INTO `colleziona_dischi` VALUES (1,1,2),(1,1,3),(1,1,4),(1,2,5),(1,2,6),(1,1,2),(1,1,3),(1,1,4),(1,2,5),(1,2,6);
/*!40000 ALTER TABLE `colleziona_dischi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collezione`
--

DROP TABLE IF EXISTS `collezione`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collezione` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(25) NOT NULL,
  `flag` tinyint(1) DEFAULT NULL,
  `ID_collezionista` int NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `ID_collezionista` (`ID_collezionista`),
  CONSTRAINT `collezione_ibfk_1` FOREIGN KEY (`ID_collezionista`) REFERENCES `collezionista` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collezione`
--

LOCK TABLES `collezione` WRITE;
/*!40000 ALTER TABLE `collezione` DISABLE KEYS */;
INSERT INTO `collezione` VALUES (1,'Dr.Stone OST',1,1),(2,'Canzoni',1,2),(3,'Anime',0,3),(4,'kessoku',0,4),(5,'Main playlist',0,1),(6,'House',0,2);
/*!40000 ALTER TABLE `collezione` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `collezionista`
--

DROP TABLE IF EXISTS `collezionista`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `collezionista` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `email` varchar(320) NOT NULL,
  `nickname` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `nickname` (`nickname`),
  CONSTRAINT `collezionista_chk_1` CHECK (regexp_like(`email`,_utf8mb4'[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]+$'))
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collezionista`
--

LOCK TABLES `collezionista` WRITE;
/*!40000 ALTER TABLE `collezionista` DISABLE KEYS */;
INSERT INTO `collezionista` VALUES (1,'whywhywhy@gmail.com','Mr.Why'),(2,'napoliteam@outlook.com','TeamNapoli'),(3,'loacker@temp232.it','SupaHacka'),(4,'AloneGotou@gmail.jp','GuitarHero');
/*!40000 ALTER TABLE `collezionista` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comprende_dischi`
--

DROP TABLE IF EXISTS `comprende_dischi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comprende_dischi` (
  `ID_collezione` int NOT NULL,
  `ID_disco` int NOT NULL,
  KEY `ID_collezione` (`ID_collezione`),
  KEY `ID_disco` (`ID_disco`),
  CONSTRAINT `comprende_dischi_ibfk_1` FOREIGN KEY (`ID_collezione`) REFERENCES `collezione` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `comprende_dischi_ibfk_2` FOREIGN KEY (`ID_disco`) REFERENCES `disco` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comprende_dischi`
--

LOCK TABLES `comprende_dischi` WRITE;
/*!40000 ALTER TABLE `comprende_dischi` DISABLE KEYS */;
INSERT INTO `comprende_dischi` VALUES (1,2),(1,3),(1,4),(2,5),(2,6),(2,1),(2,2),(2,3),(3,1),(3,2),(3,3),(3,4),(4,1),(5,2),(5,3),(5,4),(5,5),(5,6),(6,5),(6,6);
/*!40000 ALTER TABLE `comprende_dischi` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `Update_Duplicati_On_Insert_Comprende_Dischi` AFTER INSERT ON `comprende_dischi` FOR EACH ROW BEGIN
    CALL gestione_disco(NEW.ID_collezione, NEW.ID_Disco, 'INSERT');
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
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `Update_Duplicati_On_Update_Comprende_Dischi` AFTER UPDATE ON `comprende_dischi` FOR EACH ROW BEGIN
    IF OLD.ID_collezione = NEW.ID_collezione AND OLD.ID_disco <> NEW.ID_disco THEN
        CALL gestione_disco(OLD.ID_collezione, OLD.ID_Disco, 'DELETE');
        CALL gestione_disco(NEW.ID_collezione, NEW.ID_Disco, 'INSERT');
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
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER `Update_Duplicati_On_Delete_Comprende_Dischi` AFTER DELETE ON `comprende_dischi` FOR EACH ROW BEGIN
    CALL gestione_disco(OLD.ID_collezione, OLD.ID_Disco, 'DELETE');
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `condivide`
--

DROP TABLE IF EXISTS `condivide`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `condivide` (
  `ID_collezione` int NOT NULL,
  `ID_collezionista` int NOT NULL,
  KEY `ID_collezione` (`ID_collezione`),
  KEY `ID_collezionista` (`ID_collezionista`),
  CONSTRAINT `condivide_ibfk_1` FOREIGN KEY (`ID_collezione`) REFERENCES `collezione` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `condivide_ibfk_2` FOREIGN KEY (`ID_collezionista`) REFERENCES `collezionista` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `condivide`
--

LOCK TABLES `condivide` WRITE;
/*!40000 ALTER TABLE `condivide` DISABLE KEYS */;
INSERT INTO `condivide` VALUES (2,4),(3,1);
/*!40000 ALTER TABLE `condivide` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disco`
--

DROP TABLE IF EXISTS `disco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `disco` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `titolo` varchar(35) NOT NULL,
  `anno_uscita` smallint NOT NULL,
  `barcode` varchar(128) DEFAULT NULL,
  `formato` enum('vinile','cd','digitale') DEFAULT NULL,
  `stato_conservazione` enum('nuovo','come nuovo','ottimo','buono','accettabile') DEFAULT NULL,
  `descrizione_conservazione` varchar(255) DEFAULT NULL,
  `ID_etichetta` int DEFAULT NULL,
  `ID_genere` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `ID_etichetta` (`ID_etichetta`),
  KEY `ID_genere` (`ID_genere`),
  CONSTRAINT `disco_ibfk_1` FOREIGN KEY (`ID_etichetta`) REFERENCES `etichetta` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE,
  CONSTRAINT `disco_ibfk_2` FOREIGN KEY (`ID_genere`) REFERENCES `genere` (`ID`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disco`
--

LOCK TABLES `disco` WRITE;
/*!40000 ALTER TABLE `disco` DISABLE KEYS */;
INSERT INTO `disco` VALUES (1,'kessoku bando album',2022,'92999282','digitale',NULL,NULL,1,2),(2,'Dr. Stone Original Soundtrack',2019,'2767236','cd','come nuovo',NULL,3,1),(3,'Dr. Stone Original Soundtrack 2',2021,'274829','vinile','ottimo','ripescato dal mare',3,1),(4,'Dr. Stone Original Soundtrack 3',2023,'72483','digitale','accettabile','non completo',3,1),(5,'Tobu collection',2023,'243432','digitale',NULL,NULL,2,3),(6,'Janji',2022,'378465783','digitale',NULL,NULL,2,3);
/*!40000 ALTER TABLE `disco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `etichetta`
--

DROP TABLE IF EXISTS `etichetta`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `etichetta` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(25) NOT NULL,
  `sede_legale` varchar(255) NOT NULL,
  `email` varchar(320) NOT NULL,
  PRIMARY KEY (`ID`),
  CONSTRAINT `etichetta_chk_1` CHECK (regexp_like(`email`,_utf8mb4'[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+.[a-zA-Z]+$'))
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etichetta`
--

LOCK TABLES `etichetta` WRITE;
/*!40000 ALTER TABLE `etichetta` DISABLE KEYS */;
INSERT INTO `etichetta` VALUES (1,'Aniplex','Tokyo','Aniplex.business@aho.jp'),(2,'NCS','Oslo','nocopyrightsongs@support.com'),(3,'TMS','Tokyo','TMSCompany@jld.com');
/*!40000 ALTER TABLE `etichetta` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genere`
--

DROP TABLE IF EXISTS `genere`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genere` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(25) NOT NULL,
  PRIMARY KEY (`ID`),
  UNIQUE KEY `nome` (`nome`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genere`
--

LOCK TABLES `genere` WRITE;
/*!40000 ALTER TABLE `genere` DISABLE KEYS */;
INSERT INTO `genere` VALUES (1,'OST'),(3,'Progressive Electronic'),(2,'Rock');
/*!40000 ALTER TABLE `genere` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `immagine`
--

DROP TABLE IF EXISTS `immagine`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `immagine` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `file` varchar(255) NOT NULL,
  `didascalia` varchar(1000) DEFAULT NULL,
  `ID_disco` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `ID_disco` (`ID_disco`),
  CONSTRAINT `immagine_ibfk_1` FOREIGN KEY (`ID_disco`) REFERENCES `disco` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `immagine`
--

LOCK TABLES `immagine` WRITE;
/*!40000 ALTER TABLE `immagine` DISABLE KEYS */;
/*!40000 ALTER TABLE `immagine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Temporary view structure for view `lista_dischi`
--

DROP TABLE IF EXISTS `lista_dischi`;
/*!50001 DROP VIEW IF EXISTS `lista_dischi`*/;
SET @saved_cs_client     = @@character_set_client;
/*!50503 SET character_set_client = utf8mb4 */;
/*!50001 CREATE VIEW `lista_dischi` AS SELECT 
 1 AS `ID`,
 1 AS `titolo`,
 1 AS `anno di uscita`,
 1 AS `genere`,
 1 AS `formato`,
 1 AS `stato_conservazione`,
 1 AS `descrizione_conservazione`,
 1 AS `barcode`,
 1 AS `azienda`,
 1 AS `sede_legale`,
 1 AS `email`*/;
SET character_set_client = @saved_cs_client;

--
-- Table structure for table `produce_disco`
--

DROP TABLE IF EXISTS `produce_disco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produce_disco` (
  `ID_disco` int NOT NULL,
  `ID_autore` int NOT NULL,
  KEY `ID_disco` (`ID_disco`),
  KEY `ID_autore` (`ID_autore`),
  CONSTRAINT `produce_disco_ibfk_1` FOREIGN KEY (`ID_disco`) REFERENCES `disco` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `produce_disco_ibfk_2` FOREIGN KEY (`ID_autore`) REFERENCES `autore` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produce_disco`
--

LOCK TABLES `produce_disco` WRITE;
/*!40000 ALTER TABLE `produce_disco` DISABLE KEYS */;
INSERT INTO `produce_disco` VALUES (1,1),(1,2),(1,3),(1,4),(2,7),(3,7),(4,7),(5,5),(6,6);
/*!40000 ALTER TABLE `produce_disco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `realizza_traccia`
--

DROP TABLE IF EXISTS `realizza_traccia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `realizza_traccia` (
  `ID_traccia` int NOT NULL,
  `ID_autore` int NOT NULL,
  KEY `ID_traccia` (`ID_traccia`),
  KEY `ID_autore` (`ID_autore`),
  CONSTRAINT `realizza_traccia_ibfk_1` FOREIGN KEY (`ID_traccia`) REFERENCES `traccia` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `realizza_traccia_ibfk_2` FOREIGN KEY (`ID_autore`) REFERENCES `autore` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `realizza_traccia`
--

LOCK TABLES `realizza_traccia` WRITE;
/*!40000 ALTER TABLE `realizza_traccia` DISABLE KEYS */;
INSERT INTO `realizza_traccia` VALUES (1,1),(1,2),(1,3),(2,1),(2,3),(3,2),(3,3),(4,1),(4,3),(5,1),(5,4),(6,3),(7,3),(8,2),(9,4),(10,3),(10,4),(11,2),(11,1),(12,3),(12,4),(13,1),(13,3),(14,1),(15,7),(16,7),(17,7),(18,7),(19,7),(20,7),(21,7),(22,7),(23,7),(24,7),(25,7),(26,7),(27,5),(28,5),(29,5),(30,5),(31,6),(32,6),(33,6),(34,6);
/*!40000 ALTER TABLE `realizza_traccia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `traccia`
--

DROP TABLE IF EXISTS `traccia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `traccia` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `titolo` varchar(50) NOT NULL,
  `durata` time NOT NULL,
  `ID_disco` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `ID_disco` (`ID_disco`),
  CONSTRAINT `traccia_ibfk_1` FOREIGN KEY (`ID_disco`) REFERENCES `disco` (`ID`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=35 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `traccia`
--

LOCK TABLES `traccia` WRITE;
/*!40000 ALTER TABLE `traccia` DISABLE KEYS */;
INSERT INTO `traccia` VALUES (1,'Seishun Complex','00:03:23',1),(2,'Hitoribocchi Tokyo','00:03:52',1),(3,'Distortion!!','00:03:23',1),(4,'Secret Base','00:03:52',1),(5,'Guitar, Loneliness and Blue Planet','00:03:48',1),(6,'I Can\'t Sing a Love Song','00:03:08',1),(7,'That Band','00:03:33',1),(8,'Karakara','00:04:25',1),(9,'The Little Sea','00:03:43',1),(10,'What Is Wrong With','00:03:47',1),(11,'Never Forget','00:03:43',1),(12,'If I Could Be a Constellation','00:04:18',1),(13,'Flashbacker','00:04:35',1),(14,'Rockn\' Roll, Morning Light Falls on You','00:04:31',1),(15,'STONE WORLD','00:03:27',2),(16,'Turned To Stone','00:01:28',2),(17,'Beginning','00:02:39',2),(18,'From Zero','00:02:38',2),(19,'Trash is a Treasure','00:02:42',3),(20,'Won\'t Give Up','00:02:15',3),(21,'One Small Step','00:04:11',3),(22,'Wake Up, Senku!!','00:01:41',3),(23,'Senku\'s Story','00:04:35',4),(24,'Spinning Gears','00:02:45',4),(25,'Kohaku VS Homura','00:02:32',4),(26,'Connecting Dots','00:02:24',4),(27,'Caelum','00:03:23',5),(28,'Colors','00:03:52',5),(29,'Candyland','00:03:23',5),(30,'Cloud 9','00:03:52',5),(31,'Shadows','00:03:52',6),(32,'Dawn','00:03:29',6),(33,'Aurora','00:04:22',6),(34,'Horizon','00:03:17',6);
/*!40000 ALTER TABLE `traccia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Final view structure for view `lista_dischi`
--

/*!50001 DROP VIEW IF EXISTS `lista_dischi`*/;
/*!50001 SET @saved_cs_client          = @@character_set_client */;
/*!50001 SET @saved_cs_results         = @@character_set_results */;
/*!50001 SET @saved_col_connection     = @@collation_connection */;
/*!50001 SET character_set_client      = utf8mb4 */;
/*!50001 SET character_set_results     = utf8mb4 */;
/*!50001 SET collation_connection      = utf8mb4_0900_ai_ci */;
/*!50001 CREATE ALGORITHM=UNDEFINED */
/*!50013 DEFINER=`root`@`localhost` SQL SECURITY DEFINER */
/*!50001 VIEW `lista_dischi` AS select `disco`.`ID` AS `ID`,`disco`.`titolo` AS `titolo`,`disco`.`anno_uscita` AS `anno di uscita`,`genere`.`nome` AS `genere`,`disco`.`formato` AS `formato`,`disco`.`stato_conservazione` AS `stato_conservazione`,`disco`.`descrizione_conservazione` AS `descrizione_conservazione`,`disco`.`barcode` AS `barcode`,`etichetta`.`nome` AS `azienda`,`etichetta`.`sede_legale` AS `sede_legale`,`etichetta`.`email` AS `email` from ((`disco` join `etichetta` on((`disco`.`ID_etichetta` = `etichetta`.`ID`))) join `genere` on((`disco`.`ID_genere` = `genere`.`ID`))) */;
/*!50001 SET character_set_client      = @saved_cs_client */;
/*!50001 SET character_set_results     = @saved_cs_results */;
/*!50001 SET collation_connection      = @saved_col_connection */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-06-18  1:25:20
