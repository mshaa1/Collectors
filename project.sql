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
-- Table structure for table `anagrafica`
--

DROP TABLE IF EXISTS `anagrafica`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `anagrafica` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nome` varchar(25) NOT NULL,
  `cognome` varchar(25) NOT NULL,
  `data_nascita` date NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `anagrafica`
--

LOCK TABLES `anagrafica` WRITE;
/*!40000 ALTER TABLE `anagrafica` DISABLE KEYS */;
/*!40000 ALTER TABLE `anagrafica` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `autore`
--

DROP TABLE IF EXISTS `autore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `autore` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `nome_autore` varchar(25) NOT NULL,
  `info` varchar(255) DEFAULT NULL,
  `ruolo` enum('esecutore','compositore') DEFAULT NULL,
  `ID_Anagrafica` int DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `ID_Anagrafica` (`ID_Anagrafica`),
  CONSTRAINT `autore_ibfk_1` FOREIGN KEY (`ID_Anagrafica`) REFERENCES `anagrafica` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `autore`
--

LOCK TABLES `autore` WRITE;
/*!40000 ALTER TABLE `autore` DISABLE KEYS */;
/*!40000 ALTER TABLE `autore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `colleziona_dischi`
--

DROP TABLE IF EXISTS `colleziona_dischi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `colleziona_dischi` (
  `ID_collezionista` int DEFAULT NULL,
  `ID_disco` int DEFAULT NULL,
  `numero_duplicati` int NOT NULL,
  KEY `ID_collezionista` (`ID_collezionista`),
  KEY `ID_disco` (`ID_disco`),
  CONSTRAINT `colleziona_dischi_ibfk_1` FOREIGN KEY (`ID_collezionista`) REFERENCES `collezionista` (`ID`),
  CONSTRAINT `colleziona_dischi_ibfk_2` FOREIGN KEY (`ID_disco`) REFERENCES `disco` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `colleziona_dischi`
--

LOCK TABLES `colleziona_dischi` WRITE;
/*!40000 ALTER TABLE `colleziona_dischi` DISABLE KEYS */;
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
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collezione`
--

LOCK TABLES `collezione` WRITE;
/*!40000 ALTER TABLE `collezione` DISABLE KEYS */;
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
  UNIQUE KEY `nickname` (`nickname`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `collezionista`
--

LOCK TABLES `collezionista` WRITE;
/*!40000 ALTER TABLE `collezionista` DISABLE KEYS */;
/*!40000 ALTER TABLE `collezionista` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `comprende_dischi`
--

DROP TABLE IF EXISTS `comprende_dischi`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `comprende_dischi` (
  `ID_collezione` int DEFAULT NULL,
  `ID_disco` int DEFAULT NULL,
  KEY `ID_collezione` (`ID_collezione`),
  KEY `ID_disco` (`ID_disco`),
  CONSTRAINT `comprende_dischi_ibfk_1` FOREIGN KEY (`ID_collezione`) REFERENCES `collezione` (`ID`),
  CONSTRAINT `comprende_dischi_ibfk_2` FOREIGN KEY (`ID_disco`) REFERENCES `disco` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `comprende_dischi`
--

LOCK TABLES `comprende_dischi` WRITE;
/*!40000 ALTER TABLE `comprende_dischi` DISABLE KEYS */;
/*!40000 ALTER TABLE `comprende_dischi` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `condivide`
--

DROP TABLE IF EXISTS `condivide`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `condivide` (
  `ID_collezionista` int DEFAULT NULL,
  `ID_collezione` int DEFAULT NULL,
  KEY `ID_collezionista` (`ID_collezionista`),
  KEY `ID_collezione` (`ID_collezione`),
  CONSTRAINT `condivide_ibfk_1` FOREIGN KEY (`ID_collezionista`) REFERENCES `collezionista` (`ID`),
  CONSTRAINT `condivide_ibfk_2` FOREIGN KEY (`ID_collezione`) REFERENCES `collezione` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `condivide`
--

LOCK TABLES `condivide` WRITE;
/*!40000 ALTER TABLE `condivide` DISABLE KEYS */;
/*!40000 ALTER TABLE `condivide` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `contiene_tracce`
--

DROP TABLE IF EXISTS `contiene_tracce`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `contiene_tracce` (
  `ID_disco` int DEFAULT NULL,
  `ID_traccia` int NOT NULL,
  KEY `ID_disco` (`ID_disco`),
  KEY `ID_traccia` (`ID_traccia`),
  CONSTRAINT `contiene_tracce_ibfk_1` FOREIGN KEY (`ID_disco`) REFERENCES `disco` (`ID`),
  CONSTRAINT `contiene_tracce_ibfk_2` FOREIGN KEY (`ID_traccia`) REFERENCES `traccia` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `contiene_tracce`
--

LOCK TABLES `contiene_tracce` WRITE;
/*!40000 ALTER TABLE `contiene_tracce` DISABLE KEYS */;
/*!40000 ALTER TABLE `contiene_tracce` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `dettagli_autore`
--

DROP TABLE IF EXISTS `dettagli_autore`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `dettagli_autore` (
  `ID_autore` int DEFAULT NULL,
  `ID_anagrafica` int DEFAULT NULL,
  KEY `ID_autore` (`ID_autore`),
  KEY `ID_anagrafica` (`ID_anagrafica`),
  CONSTRAINT `dettagli_autore_ibfk_1` FOREIGN KEY (`ID_autore`) REFERENCES `autore` (`ID`),
  CONSTRAINT `dettagli_autore_ibfk_2` FOREIGN KEY (`ID_anagrafica`) REFERENCES `anagrafica` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `dettagli_autore`
--

LOCK TABLES `dettagli_autore` WRITE;
/*!40000 ALTER TABLE `dettagli_autore` DISABLE KEYS */;
/*!40000 ALTER TABLE `dettagli_autore` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `disco`
--

DROP TABLE IF EXISTS `disco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `disco` (
  `ID` int NOT NULL AUTO_INCREMENT,
  `titolo` varchar(25) NOT NULL,
  `anno_uscita` date NOT NULL,
  `barcode` varchar(128) NOT NULL,
  `fomato` enum('vinile','cd','digitale') DEFAULT NULL,
  `stato_conservazione` enum('nuovo','come nuovo','ottimo','buono','accettabile') DEFAULT NULL,
  `descrizione_conservazione` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `disco`
--

LOCK TABLES `disco` WRITE;
/*!40000 ALTER TABLE `disco` DISABLE KEYS */;
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
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `etichetta`
--

LOCK TABLES `etichetta` WRITE;
/*!40000 ALTER TABLE `etichetta` DISABLE KEYS */;
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
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genere`
--

LOCK TABLES `genere` WRITE;
/*!40000 ALTER TABLE `genere` DISABLE KEYS */;
/*!40000 ALTER TABLE `genere` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `genere_associato`
--

DROP TABLE IF EXISTS `genere_associato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `genere_associato` (
  `ID_genere` int DEFAULT NULL,
  `ID_disco` int DEFAULT NULL,
  UNIQUE KEY `ID_disco` (`ID_disco`),
  KEY `ID_genere` (`ID_genere`),
  CONSTRAINT `genere_associato_ibfk_1` FOREIGN KEY (`ID_genere`) REFERENCES `genere` (`ID`),
  CONSTRAINT `genere_associato_ibfk_2` FOREIGN KEY (`ID_disco`) REFERENCES `disco` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `genere_associato`
--

LOCK TABLES `genere_associato` WRITE;
/*!40000 ALTER TABLE `genere_associato` DISABLE KEYS */;
/*!40000 ALTER TABLE `genere_associato` ENABLE KEYS */;
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
  PRIMARY KEY (`ID`)
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
-- Table structure for table `inciso`
--

DROP TABLE IF EXISTS `inciso`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `inciso` (
  `ID_disco` int DEFAULT NULL,
  `ID_etichetta` int DEFAULT NULL,
  KEY `ID_disco` (`ID_disco`),
  KEY `ID_etichetta` (`ID_etichetta`),
  CONSTRAINT `inciso_ibfk_1` FOREIGN KEY (`ID_disco`) REFERENCES `disco` (`ID`),
  CONSTRAINT `inciso_ibfk_2` FOREIGN KEY (`ID_etichetta`) REFERENCES `etichetta` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inciso`
--

LOCK TABLES `inciso` WRITE;
/*!40000 ALTER TABLE `inciso` DISABLE KEYS */;
/*!40000 ALTER TABLE `inciso` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `possiede_collezioni`
--

DROP TABLE IF EXISTS `possiede_collezioni`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `possiede_collezioni` (
  `ID_collezionista` int DEFAULT NULL,
  `ID_collezione` int DEFAULT NULL,
  KEY `ID_collezionista` (`ID_collezionista`),
  KEY `ID_collezione` (`ID_collezione`),
  CONSTRAINT `possiede_collezioni_ibfk_1` FOREIGN KEY (`ID_collezionista`) REFERENCES `collezionista` (`ID`),
  CONSTRAINT `possiede_collezioni_ibfk_2` FOREIGN KEY (`ID_collezione`) REFERENCES `collezione` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `possiede_collezioni`
--

LOCK TABLES `possiede_collezioni` WRITE;
/*!40000 ALTER TABLE `possiede_collezioni` DISABLE KEYS */;
/*!40000 ALTER TABLE `possiede_collezioni` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `produce_disco`
--

DROP TABLE IF EXISTS `produce_disco`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `produce_disco` (
  `ID_disco` int DEFAULT NULL,
  `ID_autore` int DEFAULT NULL,
  KEY `ID_disco` (`ID_disco`),
  KEY `ID_autore` (`ID_autore`),
  CONSTRAINT `produce_disco_ibfk_1` FOREIGN KEY (`ID_disco`) REFERENCES `disco` (`ID`),
  CONSTRAINT `produce_disco_ibfk_2` FOREIGN KEY (`ID_autore`) REFERENCES `autore` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `produce_disco`
--

LOCK TABLES `produce_disco` WRITE;
/*!40000 ALTER TABLE `produce_disco` DISABLE KEYS */;
/*!40000 ALTER TABLE `produce_disco` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `raffigurato`
--

DROP TABLE IF EXISTS `raffigurato`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `raffigurato` (
  `ID_disco` int DEFAULT NULL,
  `ID_immagine` int DEFAULT NULL,
  KEY `ID_disco` (`ID_disco`),
  KEY `ID_immagine` (`ID_immagine`),
  CONSTRAINT `raffigurato_ibfk_1` FOREIGN KEY (`ID_disco`) REFERENCES `disco` (`ID`),
  CONSTRAINT `raffigurato_ibfk_2` FOREIGN KEY (`ID_immagine`) REFERENCES `immagine` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `raffigurato`
--

LOCK TABLES `raffigurato` WRITE;
/*!40000 ALTER TABLE `raffigurato` DISABLE KEYS */;
/*!40000 ALTER TABLE `raffigurato` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `realizza_traccia`
--

DROP TABLE IF EXISTS `realizza_traccia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `realizza_traccia` (
  `ID_traccia` int DEFAULT NULL,
  `ID_autore` int DEFAULT NULL,
  KEY `ID_traccia` (`ID_traccia`),
  KEY `ID_autore` (`ID_autore`),
  CONSTRAINT `realizza_traccia_ibfk_1` FOREIGN KEY (`ID_traccia`) REFERENCES `traccia` (`ID`),
  CONSTRAINT `realizza_traccia_ibfk_2` FOREIGN KEY (`ID_autore`) REFERENCES `autore` (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `realizza_traccia`
--

LOCK TABLES `realizza_traccia` WRITE;
/*!40000 ALTER TABLE `realizza_traccia` DISABLE KEYS */;
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
  `titolo` varchar(25) NOT NULL,
  `durata` time NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `traccia`
--

LOCK TABLES `traccia` WRITE;
/*!40000 ALTER TABLE `traccia` DISABLE KEYS */;
/*!40000 ALTER TABLE `traccia` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-05-06 19:41:59
