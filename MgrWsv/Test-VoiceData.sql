-- --------------------------------------------------------
-- 호스트:                          127.0.0.1
-- 서버 버전:                        11.0.2-MariaDB - mariadb.org binary distribution
-- 서버 OS:                        Win64
-- HeidiSQL 버전:                  12.3.0.6589
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- sample 데이터베이스 구조 내보내기
CREATE DATABASE IF NOT EXISTS `sample` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
USE `sample`;

-- 테이블 sample.voicedata 구조 내보내기
CREATE TABLE IF NOT EXISTS `voicedata` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `admindata` varchar(255) DEFAULT NULL,
  `audio_file` varchar(40) NOT NULL,
  `content` longtext DEFAULT NULL,
  `created_date` date DEFAULT NULL,
  `declaration` varchar(255) DEFAULT NULL,
  `disdata` varchar(255) DEFAULT NULL,
  `mfcc` varchar(255) DEFAULT NULL,
  `modified_date` date DEFAULT NULL,
  `persent` varchar(255) DEFAULT NULL,
  `reroll` varchar(255) DEFAULT NULL,
  `user_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKdyyu1lvq3e3tgyainv4lwcjtn` (`user_id`),
  CONSTRAINT `FKdyyu1lvq3e3tgyainv4lwcjtn` FOREIGN KEY (`user_id`) REFERENCES `user` (`idx`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- 테이블 데이터 sample.voicedata:~1 rows (대략적) 내보내기
DELETE FROM `voicedata`;
INSERT INTO `voicedata` (`id`, `admindata`, `audio_file`, `content`, `created_date`, `declaration`, `disdata`, `mfcc`, `modified_date`, `persent`, `reroll`, `user_id`) VALUES
	(1, NULL, 'qqq', 'qqqqqqqkqqq', '2023-07-17', '1234444', '0', '1', NULL, '0.88', NULL, 1);

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
