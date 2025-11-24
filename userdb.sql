/*
SQLyog Ultimate v8.32 
MySQL - 8.0.27 : Database - userdb
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`userdb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `userdb`;

/*Table structure for table `users` */

DROP TABLE IF EXISTS `users`;

CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  UNIQUE KEY `phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `users` */

insert  into `users`(`id`,`username`,`password`,`phone`,`email`,`create_time`) values (1,'user_810b08f2684','123321','15635869009',NULL,'2025-11-05 12:05:52'),(2,'admin','123321',NULL,NULL,'2025-11-05 12:06:43'),(3,'15645768978','321321',NULL,NULL,'2025-11-05 12:08:43'),(5,'user_df34496c933','123321','18535677887',NULL,'2025-11-05 12:24:16'),(6,'user_d412bdf4219','123321','15635869001',NULL,'2025-11-05 12:47:17'),(7,'user_beee3063411','123321','15635869002',NULL,'2025-11-05 12:51:52'),(8,'user_f0229699279','15635869099','15635869099',NULL,'2025-11-05 13:41:42'),(9,'user_659374c2283','123321','15635869033',NULL,'2025-11-05 19:57:07');

/*Table structure for table `verification_codes` */

DROP TABLE IF EXISTS `verification_codes`;

CREATE TABLE `verification_codes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `phone` varchar(20) NOT NULL,
  `code` varchar(10) NOT NULL,
  `type` enum('REGISTER','LOGIN') NOT NULL,
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `expire_time` timestamp NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_phone_type` (`phone`,`type`),
  KEY `idx_expire_time` (`expire_time`)
) ENGINE=InnoDB AUTO_INCREMENT=58 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*Data for the table `verification_codes` */

insert  into `verification_codes`(`id`,`phone`,`code`,`type`,`create_time`,`expire_time`) values (11,'15635869000','602791','REGISTER','2025-11-05 12:43:32','2025-11-05 12:48:32'),(12,'15635869005','445525','REGISTER','2025-11-05 12:44:18','2025-11-05 12:49:18'),(16,'15635869003','385194','REGISTER','2025-11-05 12:52:36','2025-11-05 12:57:36'),(17,'15635869037','436005','REGISTER','2025-11-05 13:11:00','2025-11-05 13:12:00'),(18,'15635869088','764759','REGISTER','2025-11-05 13:19:51','2025-11-05 13:20:51'),(20,'15638690332','519115','REGISTER','2025-11-05 13:31:03','2025-11-05 13:32:03'),(23,'17785787789','461513','REGISTER','2025-11-05 13:35:55','2025-11-05 13:36:55'),(27,'15635869077','446165','REGISTER','2025-11-05 14:32:45','2025-11-05 14:33:45'),(33,'15635864009','141824','REGISTER','2025-11-05 15:05:14','2025-11-05 15:06:14'),(35,'15635833009','095122','REGISTER','2025-11-05 15:19:31','2025-11-05 15:20:31'),(43,'15635869099','964316','LOGIN','2025-11-05 19:24:17','2025-11-05 19:25:17'),(47,'15635869009','858473','LOGIN','2025-11-05 19:33:33','2025-11-05 19:34:33'),(50,'15645768978','120845','REGISTER','2025-11-05 19:44:08','2025-11-05 19:45:08');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
