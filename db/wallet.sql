/*
SQLyog Community v12.2.6 (64 bit)
MySQL - 5.6.33 : Database - wallet
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`wallet` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `wallet`;

/*Table structure for table `l_client_logger` */

DROP TABLE IF EXISTS `l_client_logger`;

CREATE TABLE `l_client_logger` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(15) unsigned DEFAULT NULL,
  `location` varchar(256) DEFAULT NULL,
  `method` varchar(1024) DEFAULT NULL,
  `message` text,
  `date` datetime DEFAULT NULL,
  `log_level` enum('info','fatal','error','warn','info','debug','trace','all') DEFAULT 'all',
  PRIMARY KEY (`id`),
  KEY `FK_java_script_logger` (`user_id`),
  CONSTRAINT `FK_java_script_logger` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5874 DEFAULT CHARSET=latin1;

/*Table structure for table `l_wallet_logger` */

DROP TABLE IF EXISTS `l_wallet_logger`;

CREATE TABLE `l_wallet_logger` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(15) unsigned DEFAULT NULL,
  `wallet_id` int(11) DEFAULT NULL,
  `partition_id` int(11) DEFAULT NULL,
  `level` enum('info','fatal','error','warn','info','debug','trace','all') COLLATE utf8_bin NOT NULL DEFAULT 'all',
  `class_name` text COLLATE utf8_bin,
  `method_name` varchar(20) COLLATE utf8_bin DEFAULT NULL,
  `log_action` enum('create','read','update','delete','insert','util','attach') COLLATE utf8_bin NOT NULL,
  `message` text COLLATE utf8_bin,
  `date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2051 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `status_lcp` */

DROP TABLE IF EXISTS `status_lcp`;

CREATE TABLE `status_lcp` (
  `key` int(1) unsigned NOT NULL,
  `status` varchar(50) NOT NULL,
  PRIMARY KEY (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

/*Table structure for table `w_blocked_user` */

DROP TABLE IF EXISTS `w_blocked_user`;

CREATE TABLE `w_blocked_user` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `owner_id` int(15) unsigned NOT NULL,
  `blocked_id` int(15) unsigned NOT NULL,
  `title` varchar(64) COLLATE utf8_bin NOT NULL,
  `content` text COLLATE utf8_bin NOT NULL,
  `blocked_at` datetime NOT NULL,
  `unblocked_at` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_blocked_user` (`blocked_id`),
  KEY `FK_w_blocked_usero` (`owner_id`),
  CONSTRAINT `FK_w_blocked_user` FOREIGN KEY (`blocked_id`) REFERENCES `w_wallet` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_blocked_usero` FOREIGN KEY (`owner_id`) REFERENCES `w_wallet` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2431 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_card_transfer` */

DROP TABLE IF EXISTS `w_card_transfer`;

CREATE TABLE `w_card_transfer` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `t_purchase_id` int(15) unsigned NOT NULL,
  `credit_card_id` int(15) unsigned NOT NULL,
  `transfer_amount` double NOT NULL,
  `transfer_response_code` char(3) COLLATE utf8_unicode_ci DEFAULT NULL,
  `transfer_response_msg` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `transfer_dt` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=145 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `w_card_transfer_result` */

DROP TABLE IF EXISTS `w_card_transfer_result`;

CREATE TABLE `w_card_transfer_result` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `transfer_id` int(11) unsigned NOT NULL,
  `transaction_error` int(1) unsigned NOT NULL,
  `transaction_approved` int(1) unsigned NOT NULL,
  `transfer_response_msg` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EXact_resp_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `EXact_message` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `bank_resp_code` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `bank_message` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `bank_resp_code2` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `sequence_no` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `AVS` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CVV2` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `retrieval_ref_no` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CAVV_response` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `amount_requested` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `merchant_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `merchant_address` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `merchant_city` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `merchant_province` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `merchant_country` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `merchant_postal` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `merchantURL` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `current_balance` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `previous_balance` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `CTR` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_card_transfer_result` (`transfer_id`),
  CONSTRAINT `FK_w_card_transfer_result` FOREIGN KEY (`transfer_id`) REFERENCES `w_card_transfer` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_credit_card` */

DROP TABLE IF EXISTS `w_credit_card`;

CREATE TABLE `w_credit_card` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(15) unsigned NOT NULL,
  `wallet_id` int(15) unsigned NOT NULL,
  `holder_name` varchar(128) COLLATE utf8_bin NOT NULL,
  `number` varchar(19) COLLATE utf8_bin NOT NULL,
  `expiry_date` date NOT NULL,
  `cvv` varchar(3) COLLATE utf8_bin NOT NULL,
  `type` int(2) unsigned NOT NULL,
  `country` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `zip` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `state` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `city` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `is_enabled` smallint(1) unsigned NOT NULL,
  `is_deleted` smallint(1) unsigned NOT NULL,
  `is_blocked` smallint(1) unsigned DEFAULT NULL,
  `priority` int(2) unsigned NOT NULL,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_desc` text COLLATE utf8_bin,
  PRIMARY KEY (`id`),
  KEY `FK_w_credit_cardu` (`user_id`),
  KEY `FK_w_credit_cardw` (`wallet_id`),
  KEY `FK_w_credit_card` (`type`),
  CONSTRAINT `FK_w_credit_card` FOREIGN KEY (`type`) REFERENCES `w_transaction_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_credit_cardw` FOREIGN KEY (`wallet_id`) REFERENCES `w_wallet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_currency_type_lcp` */

DROP TABLE IF EXISTS `w_currency_type_lcp`;

CREATE TABLE `w_currency_type_lcp` (
  `id` int(3) unsigned NOT NULL,
  `code` varchar(3) COLLATE utf8_bin NOT NULL,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `NewIndex1` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_data` */

DROP TABLE IF EXISTS `w_data`;

CREATE TABLE `w_data` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `file_name` varchar(256) COLLATE utf8_bin NOT NULL,
  `content_type` varchar(256) COLLATE utf8_bin NOT NULL,
  `size` int(15) unsigned NOT NULL,
  `creation_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_exchange` */

DROP TABLE IF EXISTS `w_exchange`;

CREATE TABLE `w_exchange` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(15) unsigned NOT NULL,
  `wallet_amount` float(20,2) unsigned NOT NULL,
  `wallet_currency` int(3) unsigned NOT NULL,
  `rate_amount` float(15,2) unsigned NOT NULL,
  `rate_currency` int(3) unsigned NOT NULL,
  `result_amount` float(20,2) unsigned NOT NULL,
  `result_currency` int(3) unsigned NOT NULL,
  `exchange_date` date NOT NULL,
  `rate_id` int(15) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_exchange` (`rate_amount`),
  KEY `FK_w_exchange_r` (`rate_currency`),
  KEY `FK_w_exchange_res` (`result_currency`),
  KEY `FK_w_exchange_rr` (`rate_id`),
  KEY `FK_w_exchange_w` (`wallet_currency`),
  KEY `FK_w_exchange_wal` (`wallet_id`),
  CONSTRAINT `FK_w_exchange_r` FOREIGN KEY (`rate_currency`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_exchange_res` FOREIGN KEY (`result_currency`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_exchange_rr` FOREIGN KEY (`rate_id`) REFERENCES `w_exchange_rate` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_exchange_w` FOREIGN KEY (`wallet_currency`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_exchange_wal` FOREIGN KEY (`wallet_id`) REFERENCES `w_wallet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=396 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_exchange_rate` */

DROP TABLE IF EXISTS `w_exchange_rate`;

CREATE TABLE `w_exchange_rate` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `one_currency` int(3) unsigned NOT NULL,
  `to_currency` int(3) unsigned NOT NULL,
  `buy` float unsigned NOT NULL,
  `sell` float unsigned NOT NULL,
  `updated_date` datetime NOT NULL,
  `source_id` int(2) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_exchange_rate` (`one_currency`),
  KEY `FK_w_exchange_rate_to` (`to_currency`),
  KEY `FK_w_exchange_rate_s` (`source_id`),
  CONSTRAINT `FK_w_exchange_rate` FOREIGN KEY (`one_currency`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_exchange_rate_s` FOREIGN KEY (`source_id`) REFERENCES `w_exchange_source` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_exchange_rate_to` FOREIGN KEY (`to_currency`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=452150 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_exchange_source` */

DROP TABLE IF EXISTS `w_exchange_source`;

CREATE TABLE `w_exchange_source` (
  `id` int(2) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_setup_id` int(15) unsigned NOT NULL,
  `name` varchar(50) COLLATE utf8_bin NOT NULL,
  `address` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `phone` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `url` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `icon` int(15) unsigned DEFAULT NULL,
  `is_default` int(1) unsigned NOT NULL DEFAULT '0',
  `is_active` int(1) unsigned NOT NULL DEFAULT '1',
  `app_id` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `username` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `password` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_exchange_source` (`icon`),
  KEY `FK_w_exchange_sources` (`wallet_setup_id`),
  CONSTRAINT `FK_w_exchange_source` FOREIGN KEY (`icon`) REFERENCES `w_data` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_exchange_sources` FOREIGN KEY (`wallet_setup_id`) REFERENCES `wallet_setup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_merchant_deposit` */

DROP TABLE IF EXISTS `w_merchant_deposit`;

CREATE TABLE `w_merchant_deposit` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(15) unsigned NOT NULL,
  `name` varchar(1024) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `start_at` datetime DEFAULT NULL,
  `rational_stop_at` datetime DEFAULT NULL,
  `end_at` datetime DEFAULT NULL,
  `m_deposit_tax_id` int(15) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_merchant_deposit_tax` */

DROP TABLE IF EXISTS `w_merchant_deposit_tax`;

CREATE TABLE `w_merchant_deposit_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) NOT NULL,
  `setup_id` int(15) NOT NULL,
  `paid_tax_to_merchant` varchar(512) COLLATE utf8_bin NOT NULL,
  `paid_tax_ct` int(3) unsigned NOT NULL,
  `deposit_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `deposit_tax_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `deposit_tax_ct` int(3) unsigned NOT NULL,
  `deposit_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `deposit_tax_price_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `deposit_tax_price_ct` int(3) unsigned NOT NULL,
  `deposit_tax_type` int(1) unsigned NOT NULL,
  `exchange_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_merchant_withdraw` */

DROP TABLE IF EXISTS `w_merchant_withdraw`;

CREATE TABLE `w_merchant_withdraw` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(15) unsigned NOT NULL,
  `name` varchar(1024) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `start_at` datetime DEFAULT NULL,
  `rational_stop_at` datetime DEFAULT NULL,
  `end_at` datetime DEFAULT NULL,
  `m_withdraw_tax_id` int(15) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_merchant_withdraw_tax` */

DROP TABLE IF EXISTS `w_merchant_withdraw_tax`;

CREATE TABLE `w_merchant_withdraw_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) NOT NULL,
  `setup_id` int(15) NOT NULL,
  `paid_tax_to_merchant` varchar(512) COLLATE utf8_bin NOT NULL,
  `paid_tax_ct` int(3) unsigned NOT NULL,
  `withdraw_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `withdraw_tax_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `withdraw_tax_ct` int(3) unsigned NOT NULL,
  `withdraw_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `withdraw_tax_price_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `withdraw_tax_price_ct` int(3) unsigned NOT NULL,
  `withdraw_tax_type` int(1) unsigned NOT NULL,
  `exchange_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_merchant_withdraw_tax` (`exchange_id`),
  CONSTRAINT `FK_w_merchant_withdraw_tax` FOREIGN KEY (`exchange_id`) REFERENCES `w_t_withdraw_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_purchase_ticket` */

DROP TABLE IF EXISTS `w_purchase_ticket`;

CREATE TABLE `w_purchase_ticket` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(15) unsigned NOT NULL,
  `transaction_id` int(15) unsigned NOT NULL,
  `purchase_type` int(3) unsigned NOT NULL,
  `name` varchar(1024) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_purchase_ticket` (`transaction_id`),
  KEY `FK_w_purchase_ticket_pt` (`purchase_type`),
  CONSTRAINT `FK_w_purchase_ticket` FOREIGN KEY (`transaction_id`) REFERENCES `w_t_purchase` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_purchase_ticket_pt` FOREIGN KEY (`purchase_type`) REFERENCES `w_t_purchase_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_deposit` */

DROP TABLE IF EXISTS `w_t_deposit`;

CREATE TABLE `w_t_deposit` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `final_state` int(3) unsigned NOT NULL,
  `opened_at` datetime NOT NULL,
  `closed_at` datetime DEFAULT NULL,
  `deposit_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `deposit_amount_ct` int(3) unsigned NOT NULL,
  `deposit_m_total_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `deposit_m_total_tax_ct` int(3) unsigned NOT NULL,
  `wallet_total_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_total_price_ct` int(3) unsigned NOT NULL,
  `setup_total_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `setup_total_amount_ct` int(3) unsigned NOT NULL,
  `process_start_id` int(15) unsigned NOT NULL,
  `process_end_id` int(15) unsigned DEFAULT NULL,
  `tax_id` int(15) unsigned NOT NULL,
  `merchant_deposit_id` int(15) unsigned NOT NULL,
  `is_encoded` tinyint(1) NOT NULL,
  `order_code` varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_deposit_ct_da` (`deposit_amount_ct`),
  KEY `FK_w_t_deposit_ct_tt` (`deposit_m_total_tax_ct`),
  KEY `FK_w_t_deposit_ct_stt` (`setup_total_amount_ct`),
  KEY `FK_w_t_deposit_s` (`process_start_id`),
  KEY `FK_w_t_deposit_e` (`process_end_id`),
  KEY `FK_w_t_deposit` (`merchant_deposit_id`),
  KEY `FK_w_t_deposit_ct_wtc` (`wallet_total_price_ct`),
  KEY `FK_w_t_deposit_t` (`tax_id`),
  CONSTRAINT `FK_w_t_deposit` FOREIGN KEY (`merchant_deposit_id`) REFERENCES `w_merchant_deposit` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_deposit_ct_da` FOREIGN KEY (`deposit_amount_ct`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_deposit_ct_stt` FOREIGN KEY (`setup_total_amount_ct`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_deposit_ct_tt` FOREIGN KEY (`deposit_m_total_tax_ct`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_deposit_ct_wtc` FOREIGN KEY (`wallet_total_price_ct`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_deposit_e` FOREIGN KEY (`process_end_id`) REFERENCES `w_t_deposit_process` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_deposit_s` FOREIGN KEY (`process_start_id`) REFERENCES `w_t_deposit_process` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_deposit_t` FOREIGN KEY (`tax_id`) REFERENCES `w_t_deposit_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_deposit_exchange` */

DROP TABLE IF EXISTS `w_t_deposit_exchange`;

CREATE TABLE `w_t_deposit_exchange` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `rate_id` int(15) unsigned NOT NULL,
  `exchange_date` datetime NOT NULL,
  `setup_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `setup_ct` int(3) unsigned NOT NULL,
  `rate` varchar(512) COLLATE utf8_bin NOT NULL,
  `rate_ct` int(3) unsigned NOT NULL,
  `wallet_bought_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_bought_ct` int(3) unsigned NOT NULL,
  `wallet_paid_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_paid_ct` int(3) unsigned NOT NULL,
  `exchange_tax_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_deposit_exchange_t` (`exchange_tax_id`),
  CONSTRAINT `FK_w_t_deposit_exchange_t` FOREIGN KEY (`exchange_tax_id`) REFERENCES `w_t_deposit_exchange_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_deposit_exchange_tax` */

DROP TABLE IF EXISTS `w_t_deposit_exchange_tax`;

CREATE TABLE `w_t_deposit_exchange_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) NOT NULL,
  `setup_id` int(15) NOT NULL,
  `exchange_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `exchange_tax_ct` int(3) unsigned NOT NULL,
  `exchange_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `exchange_tax_price_ct` int(3) unsigned NOT NULL,
  `exchange_tax_type` int(1) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_deposit_process` */

DROP TABLE IF EXISTS `w_t_deposit_process`;

CREATE TABLE `w_t_deposit_process` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `state` int(1) unsigned NOT NULL,
  `action_date` datetime DEFAULT NULL,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `deposit_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `deposit_amount_ct` int(3) unsigned NOT NULL,
  `wallet_deposit_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_total_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_total_price_ct` int(3) unsigned NOT NULL,
  `setup_deposit_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `setup_total_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `setup_total_price_ct` int(3) unsigned NOT NULL,
  `process_tax_id` int(15) unsigned NOT NULL,
  `setup_deposit_tax_id` int(15) unsigned NOT NULL,
  `exchange_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_deposit_process_t` (`process_tax_id`),
  KEY `FK_w_t_deposit_process_setup_deposit_t` (`setup_deposit_tax_id`),
  KEY `FK_w_t_deposit_process_e` (`exchange_id`),
  CONSTRAINT `FK_w_t_deposit_process_e` FOREIGN KEY (`exchange_id`) REFERENCES `w_t_deposit_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_deposit_process_setup_deposit_t` FOREIGN KEY (`setup_deposit_tax_id`) REFERENCES `w_t_wallet_setup_deposit_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_deposit_process_t` FOREIGN KEY (`process_tax_id`) REFERENCES `w_t_deposit_process_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_deposit_process_tax` */

DROP TABLE IF EXISTS `w_t_deposit_process_tax`;

CREATE TABLE `w_t_deposit_process_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) NOT NULL,
  `setup_id` int(15) NOT NULL,
  `process_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_ct` int(3) unsigned NOT NULL,
  `process_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_price_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_price_ct` int(3) unsigned NOT NULL,
  `process_tax_type` int(1) unsigned NOT NULL,
  `exchange_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_deposit_process_tax_e` (`exchange_id`),
  CONSTRAINT `FK_w_t_deposit_process_tax` FOREIGN KEY (`exchange_id`) REFERENCES `w_t_deposit_exchange` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_deposit_tax` */

DROP TABLE IF EXISTS `w_t_deposit_tax`;

CREATE TABLE `w_t_deposit_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `total_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `total_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_id` int(15) unsigned NOT NULL,
  `exchange_tax_id` int(15) unsigned DEFAULT NULL,
  `setup_deposit_tax_id` int(15) unsigned DEFAULT NULL,
  `merchant_deposit_tax_id` int(15) unsigned NOT NULL,
  `is_paid` tinyint(1) unsigned NOT NULL,
  UNIQUE KEY `id` (`id`),
  KEY `FK_w_t_deposit_tax_pt` (`process_tax_id`),
  KEY `FK_w_t_deposit_tax_e` (`exchange_tax_id`),
  KEY `FK_w_t_deposit_tax_dst` (`setup_deposit_tax_id`),
  KEY `FK_w_t_deposit_tax_mdt` (`merchant_deposit_tax_id`),
  CONSTRAINT `FK_w_t_deposit_tax_dst` FOREIGN KEY (`setup_deposit_tax_id`) REFERENCES `w_t_wallet_setup_deposit_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_deposit_tax_e` FOREIGN KEY (`exchange_tax_id`) REFERENCES `w_t_deposit_exchange_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_deposit_tax_mdt` FOREIGN KEY (`merchant_deposit_tax_id`) REFERENCES `w_merchant_deposit_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_deposit_tax_pt` FOREIGN KEY (`process_tax_id`) REFERENCES `w_t_deposit_process_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_m_transfer_ticket` */

DROP TABLE IF EXISTS `w_t_m_transfer_ticket`;

CREATE TABLE `w_t_m_transfer_ticket` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `m_transaction_id` int(15) unsigned NOT NULL,
  `m_company_id` int(15) unsigned NOT NULL,
  `wallet_id` int(15) unsigned NOT NULL,
  `name` varchar(1024) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_m_transfer_transaction` */

DROP TABLE IF EXISTS `w_t_m_transfer_transaction`;

CREATE TABLE `w_t_m_transfer_transaction` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `transfer_ticket_id` int(15) unsigned NOT NULL,
  `tsm_company_id` int(15) unsigned NOT NULL,
  `wallet_id` int(15) unsigned NOT NULL,
  `action_date` datetime DEFAULT NULL,
  `transfer_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `transfer_amount_ct` int(3) unsigned NOT NULL,
  `is_encoded` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_m_transfer_ticket_id` (`transfer_ticket_id`),
  KEY `FK_w_m_transfer_wallet_id` (`wallet_id`),
  CONSTRAINT `FK_w_m_transfer_ticket_id` FOREIGN KEY (`transfer_ticket_id`) REFERENCES `w_t_m_transfer_ticket` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_m_transfer_wallet_id` FOREIGN KEY (`wallet_id`) REFERENCES `w_wallet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=41 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_purchase` */

DROP TABLE IF EXISTS `w_t_purchase`;

CREATE TABLE `w_t_purchase` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `final_state` int(3) unsigned NOT NULL,
  `opened_at` datetime NOT NULL,
  `closed_at` datetime DEFAULT NULL,
  `purchase_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `purchase_ct` int(3) unsigned NOT NULL,
  `wallet_total_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_total_price_ct` int(3) unsigned NOT NULL,
  `setup_total_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `setup_total_amount_ct` int(3) unsigned NOT NULL,
  `process_start_id` int(15) unsigned NOT NULL,
  `process_end_id` int(15) unsigned DEFAULT NULL,
  `tax_id` int(15) unsigned NOT NULL,
  `is_encoded` tinyint(1) NOT NULL,
  `order_code` varchar(64) COLLATE utf8_bin NOT NULL,
  `message` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `session_id` varchar(256) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_purchase_ps` (`process_start_id`),
  KEY `FK_w_t_purchase_pe` (`process_end_id`),
  KEY `FK_w_t_purchase_tax` (`tax_id`),
  KEY `FK_w_t_purchase_pct` (`purchase_ct`),
  KEY `FK_w_t_purchase_tct` (`setup_total_amount_ct`),
  KEY `FK_w_t_purchase_wtt` (`wallet_total_price_ct`),
  CONSTRAINT `FK_w_t_purchase_pct` FOREIGN KEY (`purchase_ct`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_purchase_pe` FOREIGN KEY (`process_end_id`) REFERENCES `w_t_purchase_process` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_purchase_ps` FOREIGN KEY (`process_start_id`) REFERENCES `w_t_purchase_process` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_purchase_tax` FOREIGN KEY (`tax_id`) REFERENCES `w_t_purchase_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_purchase_tct` FOREIGN KEY (`setup_total_amount_ct`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_purchase_wtt` FOREIGN KEY (`wallet_total_price_ct`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_purchase_exchange` */

DROP TABLE IF EXISTS `w_t_purchase_exchange`;

CREATE TABLE `w_t_purchase_exchange` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `rate_id` int(15) unsigned NOT NULL,
  `exchange_date` datetime NOT NULL,
  `setup_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `setup_ct` int(3) unsigned NOT NULL,
  `rate` varchar(512) COLLATE utf8_bin NOT NULL,
  `rate_ct` int(3) unsigned NOT NULL,
  `wallet_bought_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_bought_ct` int(3) unsigned NOT NULL,
  `wallet_paid_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_paid_ct` int(3) unsigned NOT NULL,
  `exchange_tax_id` int(15) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_purchase_exchange_t` (`exchange_tax_id`),
  CONSTRAINT `FK_w_t_purchase_exchange_t` FOREIGN KEY (`exchange_tax_id`) REFERENCES `w_t_purchase_exchange_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_purchase_exchange_tax` */

DROP TABLE IF EXISTS `w_t_purchase_exchange_tax`;

CREATE TABLE `w_t_purchase_exchange_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) NOT NULL,
  `setup_id` int(15) NOT NULL,
  `exchange_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `exchange_tax_ct` int(3) unsigned NOT NULL,
  `exchange_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `exchange_tax_price_ct` int(3) unsigned NOT NULL,
  `exchange_tax_type` int(1) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_purchase_process` */

DROP TABLE IF EXISTS `w_t_purchase_process`;

CREATE TABLE `w_t_purchase_process` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `state` int(1) unsigned NOT NULL,
  `action_date` datetime DEFAULT NULL,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `purchase_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `purchase_amount_ct` int(3) unsigned NOT NULL,
  `wallet_purchase_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_total_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_total_price_ct` int(3) unsigned NOT NULL,
  `setup_purchase_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `setup_total_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `setup_total_price_ct` int(3) unsigned NOT NULL,
  `process_tax_id` int(15) unsigned NOT NULL,
  `exchange_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_purchase_process_t` (`process_tax_id`),
  KEY `FK_w_t_purchase_process_e` (`exchange_id`),
  CONSTRAINT `FK_w_t_purchase_process_e` FOREIGN KEY (`exchange_id`) REFERENCES `w_t_purchase_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_purchase_process_t` FOREIGN KEY (`process_tax_id`) REFERENCES `w_t_purchase_process_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_purchase_process_tax` */

DROP TABLE IF EXISTS `w_t_purchase_process_tax`;

CREATE TABLE `w_t_purchase_process_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) NOT NULL,
  `setup_id` int(15) NOT NULL,
  `process_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_ct` int(3) unsigned NOT NULL,
  `process_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_price_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_price_ct` int(3) unsigned NOT NULL,
  `process_tax_type` int(1) unsigned NOT NULL,
  `exchange_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_purchase_process_tax_e` (`exchange_id`),
  CONSTRAINT `FK_w_t_purchase_process_tax_e` FOREIGN KEY (`exchange_id`) REFERENCES `w_t_purchase_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_purchase_tax` */

DROP TABLE IF EXISTS `w_t_purchase_tax`;

CREATE TABLE `w_t_purchase_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `total_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `total_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_id` int(15) unsigned NOT NULL,
  `process_tax_exchange_tax_id` int(15) unsigned DEFAULT NULL,
  `exchange_tax_id` int(15) unsigned DEFAULT NULL,
  `is_paid` tinyint(1) unsigned NOT NULL,
  UNIQUE KEY `id` (`id`),
  KEY `FK_w_t_purchase_tax_pt` (`process_tax_id`),
  KEY `FK_w_t_purchase_tax_e` (`exchange_tax_id`),
  KEY `FK_w_t_purchase_tax_et` (`process_tax_exchange_tax_id`),
  CONSTRAINT `FK_w_t_purchase_tax_e` FOREIGN KEY (`exchange_tax_id`) REFERENCES `w_t_purchase_exchange_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_purchase_tax_et` FOREIGN KEY (`process_tax_exchange_tax_id`) REFERENCES `w_t_purchase_exchange_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_purchase_tax_pt` FOREIGN KEY (`process_tax_id`) REFERENCES `w_t_purchase_process_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_purchase_type_lcp` */

DROP TABLE IF EXISTS `w_t_purchase_type_lcp`;

CREATE TABLE `w_t_purchase_type_lcp` (
  `id` int(1) unsigned NOT NULL,
  `type` varchar(32) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_transfer_ticket` */

DROP TABLE IF EXISTS `w_t_transfer_ticket`;

CREATE TABLE `w_t_transfer_ticket` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(15) unsigned NOT NULL,
  `wallet_id` int(15) unsigned NOT NULL,
  `name` varchar(1024) COLLATE utf8_bin NOT NULL,
  `description` text COLLATE utf8_bin NOT NULL,
  `sys_admin_username` varchar(30) COLLATE utf8_bin NOT NULL,
  `sys_admin_partition_id` int(11) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_transfer_transaction` */

DROP TABLE IF EXISTS `w_t_transfer_transaction`;

CREATE TABLE `w_t_transfer_transaction` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `transfer_ticket_id` int(15) unsigned NOT NULL,
  `core_system_admin_id` int(15) unsigned NOT NULL,
  `wallet_setup_id` int(15) unsigned NOT NULL,
  `wallet_id` int(15) unsigned NOT NULL,
  `action_date` datetime DEFAULT NULL,
  `transfer_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `transfer_amount_ct` int(3) unsigned NOT NULL,
  `is_encoded` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_transfer_ticket_id` (`transfer_ticket_id`),
  KEY `FK_w_transfer_setup_id` (`wallet_setup_id`),
  KEY `FK_w_transfer_wallet_id` (`wallet_id`),
  CONSTRAINT `FK_w_transfer_setup_id` FOREIGN KEY (`wallet_setup_id`) REFERENCES `wallet_setup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_transfer_ticket_id` FOREIGN KEY (`transfer_ticket_id`) REFERENCES `w_t_transfer_ticket` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_transfer_wallet_id` FOREIGN KEY (`wallet_id`) REFERENCES `w_wallet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_wallet_setup_deposit_tax` */

DROP TABLE IF EXISTS `w_t_wallet_setup_deposit_tax`;

CREATE TABLE `w_t_wallet_setup_deposit_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) NOT NULL,
  `setup_id` int(15) NOT NULL,
  `deposit_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `deposit_tax_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `deposit_tax_ct` int(3) unsigned NOT NULL,
  `deposit_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `deposit_tax_price_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `deposit_tax_price_ct` int(3) unsigned NOT NULL,
  `deposit_tax_type` int(1) unsigned NOT NULL,
  `exchange_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_wallet_setup_deposit_tax_e` (`exchange_id`),
  CONSTRAINT `FK_w_t_wallet_setup_deposit_tax` FOREIGN KEY (`exchange_id`) REFERENCES `w_t_deposit_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_wallet_setup_withdraw_tax` */

DROP TABLE IF EXISTS `w_t_wallet_setup_withdraw_tax`;

CREATE TABLE `w_t_wallet_setup_withdraw_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) NOT NULL,
  `setup_id` int(15) NOT NULL,
  `withdraw_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `withdraw_tax_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `withdraw_tax_ct` int(3) unsigned NOT NULL,
  `withdraw_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `withdraw_tax_price_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `withdraw_tax_price_ct` int(3) unsigned NOT NULL,
  `withdraw_tax_type` int(1) unsigned NOT NULL,
  `exchange_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_wallet_setup_withdraw_tax_e` (`exchange_id`),
  CONSTRAINT `FK_w_t_wallet_setup_withdraw_tax` FOREIGN KEY (`exchange_id`) REFERENCES `w_t_withdraw_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_withdraw` */

DROP TABLE IF EXISTS `w_t_withdraw`;

CREATE TABLE `w_t_withdraw` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `final_state` int(3) unsigned NOT NULL,
  `opened_at` datetime NOT NULL,
  `closed_at` datetime DEFAULT NULL,
  `withdraw_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `withdraw_amount_ct` int(3) unsigned NOT NULL,
  `withdraw_m_total_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `withdraw_m_total_tax_ct` int(3) unsigned NOT NULL,
  `wallet_total_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_total_price_ct` int(3) unsigned NOT NULL,
  `setup_total_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `setup_total_amount_ct` int(3) unsigned NOT NULL,
  `process_start_id` int(15) unsigned NOT NULL,
  `process_end_id` int(15) unsigned DEFAULT NULL,
  `tax_id` int(15) unsigned NOT NULL,
  `merchant_withdraw_id` int(15) unsigned NOT NULL,
  `is_encoded` tinyint(1) NOT NULL,
  `order_code` varchar(64) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_withdraw_ct_da` (`withdraw_amount_ct`),
  KEY `FK_w_t_withdraw_ct_tt` (`withdraw_m_total_tax_ct`),
  KEY `FK_w_t_withdraw_ct_stt` (`setup_total_amount_ct`),
  KEY `FK_w_t_withdraw_s` (`process_start_id`),
  KEY `FK_w_t_withdraw_e` (`process_end_id`),
  KEY `FK_w_t_withdraw` (`merchant_withdraw_id`),
  KEY `FK_w_t_withdraw_ct_wtc` (`wallet_total_price_ct`),
  KEY `FK_w_t_withdraw_t` (`tax_id`),
  CONSTRAINT `FK_w_t_withdraw` FOREIGN KEY (`merchant_withdraw_id`) REFERENCES `w_merchant_withdraw` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_withdraw_ct_da` FOREIGN KEY (`withdraw_amount_ct`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_withdraw_ct_stt` FOREIGN KEY (`setup_total_amount_ct`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_withdraw_ct_tt` FOREIGN KEY (`withdraw_m_total_tax_ct`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_withdraw_ct_wtc` FOREIGN KEY (`wallet_total_price_ct`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_withdraw_e` FOREIGN KEY (`process_end_id`) REFERENCES `w_t_withdraw_process` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_withdraw_s` FOREIGN KEY (`process_start_id`) REFERENCES `w_t_withdraw_process` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_withdraw_t` FOREIGN KEY (`tax_id`) REFERENCES `w_t_withdraw_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_withdraw_exchange` */

DROP TABLE IF EXISTS `w_t_withdraw_exchange`;

CREATE TABLE `w_t_withdraw_exchange` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `rate_id` int(15) unsigned NOT NULL,
  `exchange_date` datetime NOT NULL,
  `setup_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `setup_ct` int(3) unsigned NOT NULL,
  `rate` varchar(512) COLLATE utf8_bin NOT NULL,
  `rate_ct` int(3) unsigned NOT NULL,
  `wallet_bought_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_bought_ct` int(3) unsigned NOT NULL,
  `wallet_paid_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_paid_ct` int(3) unsigned NOT NULL,
  `exchange_tax_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_withdraw_exchange_t` (`exchange_tax_id`),
  CONSTRAINT `FK_w_t_withdraw_exchange_t` FOREIGN KEY (`exchange_tax_id`) REFERENCES `w_t_withdraw_exchange_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_withdraw_exchange_tax` */

DROP TABLE IF EXISTS `w_t_withdraw_exchange_tax`;

CREATE TABLE `w_t_withdraw_exchange_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) NOT NULL,
  `setup_id` int(15) NOT NULL,
  `exchange_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `exchange_tax_ct` int(3) unsigned NOT NULL,
  `exchange_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `exchange_tax_price_ct` int(3) unsigned NOT NULL,
  `exchange_tax_type` int(1) unsigned NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_withdraw_process` */

DROP TABLE IF EXISTS `w_t_withdraw_process`;

CREATE TABLE `w_t_withdraw_process` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `state` int(1) unsigned NOT NULL,
  `action_date` datetime DEFAULT NULL,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `withdraw_amount` varchar(512) COLLATE utf8_bin NOT NULL,
  `withdraw_amount_ct` int(3) unsigned NOT NULL,
  `wallet_withdraw_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_total_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `wallet_total_price_ct` int(3) unsigned NOT NULL,
  `setup_withdraw_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `setup_total_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `setup_total_price_ct` int(3) unsigned NOT NULL,
  `process_tax_id` int(15) unsigned NOT NULL,
  `setup_withdraw_tax_id` int(15) unsigned NOT NULL,
  `exchange_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_withdraw_process_t` (`process_tax_id`),
  KEY `FK_w_t_withdraw_process_setup_withdraw_t` (`setup_withdraw_tax_id`),
  KEY `FK_w_t_withdraw_process_e` (`exchange_id`),
  CONSTRAINT `FK_w_t_withdraw_process_e` FOREIGN KEY (`exchange_id`) REFERENCES `w_t_withdraw_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_withdraw_process_setup_withdraw_t` FOREIGN KEY (`setup_withdraw_tax_id`) REFERENCES `w_t_wallet_setup_withdraw_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_withdraw_process_t` FOREIGN KEY (`process_tax_id`) REFERENCES `w_t_withdraw_process_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_withdraw_process_tax` */

DROP TABLE IF EXISTS `w_t_withdraw_process_tax`;

CREATE TABLE `w_t_withdraw_process_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) NOT NULL,
  `setup_id` int(15) NOT NULL,
  `process_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_ct` int(3) unsigned NOT NULL,
  `process_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_price_total` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_price_ct` int(3) unsigned NOT NULL,
  `process_tax_type` int(1) unsigned NOT NULL,
  `exchange_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_t_withdraw_process_tax_e` (`exchange_id`),
  CONSTRAINT `FK_w_t_withdraw_process_tax` FOREIGN KEY (`exchange_id`) REFERENCES `w_t_withdraw_exchange` (`id`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_t_withdraw_tax` */

DROP TABLE IF EXISTS `w_t_withdraw_tax`;

CREATE TABLE `w_t_withdraw_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `total_tax` varchar(512) COLLATE utf8_bin NOT NULL,
  `total_tax_price` varchar(512) COLLATE utf8_bin NOT NULL,
  `process_tax_id` int(15) unsigned NOT NULL,
  `exchange_tax_id` int(15) unsigned DEFAULT NULL,
  `setup_withdraw_tax_id` int(15) unsigned NOT NULL,
  `merchant_withdraw_tax_id` int(15) unsigned NOT NULL,
  `is_paid` tinyint(1) unsigned NOT NULL,
  UNIQUE KEY `id` (`id`),
  KEY `FK_w_t_withdraw_tax_pt` (`process_tax_id`),
  KEY `FK_w_t_withdraw_tax_e` (`exchange_tax_id`),
  KEY `FK_w_t_withdraw_tax_dmt` (`merchant_withdraw_tax_id`),
  KEY `FK_w_t_withdraw_tax_dst` (`setup_withdraw_tax_id`),
  CONSTRAINT `FK_w_t_withdraw_tax_e` FOREIGN KEY (`exchange_tax_id`) REFERENCES `w_t_withdraw_exchange_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_withdraw_tax_mwt` FOREIGN KEY (`merchant_withdraw_tax_id`) REFERENCES `w_merchant_withdraw_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_withdraw_tax_pt` FOREIGN KEY (`process_tax_id`) REFERENCES `w_t_withdraw_process_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_t_withdraw_tax_st` FOREIGN KEY (`setup_withdraw_tax_id`) REFERENCES `w_t_wallet_setup_withdraw_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_transaction` */

DROP TABLE IF EXISTS `w_transaction`;

CREATE TABLE `w_transaction` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `frozen_transaction_id` int(15) unsigned DEFAULT NULL,
  `state` int(2) unsigned NOT NULL,
  `action_state` int(2) unsigned NOT NULL,
  `opened_at` datetime NOT NULL,
  `closed_at` datetime DEFAULT NULL,
  `product_amount` float(15,2) unsigned NOT NULL,
  `product_currency_type` int(3) unsigned NOT NULL,
  `setup_amount` float(15,2) unsigned DEFAULT NULL,
  `setup_amount_currency_type` int(3) unsigned DEFAULT NULL,
  `from_total_price` float(15,2) unsigned DEFAULT NULL,
  `from_total_price_currency_type` int(3) unsigned DEFAULT NULL,
  `to_total_price` float(15,2) unsigned DEFAULT NULL,
  `to_total_price_currency_type` int(3) unsigned DEFAULT NULL,
  `transaction_type` int(2) unsigned DEFAULT NULL,
  `order_code` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `message` text COLLATE utf8_bin,
  `session_id` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `from_wallet_id` int(15) unsigned DEFAULT NULL,
  `from_wallet_setup_id` int(15) unsigned DEFAULT NULL,
  `to_wallet_id` int(15) unsigned DEFAULT NULL,
  `to_wallet_setup_id` int(15) unsigned DEFAULT NULL,
  `from_process_id` int(15) unsigned DEFAULT NULL,
  `to_process_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_wallet_transaction` (`from_process_id`),
  KEY `FK_w_wallet_transactiont` (`to_process_id`),
  KEY `FK_w_wallet_transactionc` (`product_currency_type`),
  KEY `FK_w_wallet_transactiontt` (`transaction_type`),
  KEY `FK_w_wallet_transactiontw` (`to_wallet_id`),
  KEY `FK_w_wallet_transactionfw` (`from_wallet_id`),
  KEY `FK_w_wallet_transactionfs` (`from_wallet_setup_id`),
  KEY `FK_w_wallet_transactionts` (`to_wallet_setup_id`),
  CONSTRAINT `FK_w_wallet_transaction` FOREIGN KEY (`from_process_id`) REFERENCES `w_transaction_process` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_wallet_transactionc` FOREIGN KEY (`product_currency_type`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_wallet_transactionfs` FOREIGN KEY (`from_wallet_setup_id`) REFERENCES `wallet_setup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_wallet_transactionfw` FOREIGN KEY (`from_wallet_id`) REFERENCES `w_wallet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_wallet_transactiont` FOREIGN KEY (`to_process_id`) REFERENCES `w_transaction_process` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_wallet_transactionts` FOREIGN KEY (`to_wallet_setup_id`) REFERENCES `wallet_setup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_wallet_transactiontt` FOREIGN KEY (`transaction_type`) REFERENCES `w_transaction_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_wallet_transactiontw` FOREIGN KEY (`to_wallet_id`) REFERENCES `w_wallet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=135 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_transaction_data` */

DROP TABLE IF EXISTS `w_transaction_data`;

CREATE TABLE `w_transaction_data` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `transaction_id` int(15) unsigned DEFAULT NULL,
  `dispute_id` int(15) unsigned DEFAULT NULL,
  `file_name` varchar(256) COLLATE utf8_bin NOT NULL,
  `content_type` varchar(256) COLLATE utf8_bin NOT NULL,
  `size` int(15) unsigned NOT NULL,
  `creation_date` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_transaction_data` (`transaction_id`),
  KEY `FK_w_transaction_datad` (`dispute_id`),
  CONSTRAINT `FK_w_transaction_data` FOREIGN KEY (`transaction_id`) REFERENCES `w_transaction` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_transaction_datad` FOREIGN KEY (`dispute_id`) REFERENCES `w_transaction_dispute` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=37 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_transaction_dispute` */

DROP TABLE IF EXISTS `w_transaction_dispute`;

CREATE TABLE `w_transaction_dispute` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `transaction_id` int(15) unsigned DEFAULT NULL,
  `wallet_exchange_id` int(15) unsigned DEFAULT NULL,
  `dispute_id` int(15) unsigned DEFAULT NULL,
  `disputed_by` int(15) unsigned DEFAULT NULL,
  `disputed_at` datetime DEFAULT NULL,
  `reason` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `content` text COLLATE utf8_bin,
  `answer` text COLLATE utf8_bin,
  `answered_at` datetime DEFAULT NULL,
  `answered_partition_user_id` int(8) unsigned DEFAULT NULL,
  `answered_by` int(15) unsigned DEFAULT NULL,
  `state` int(1) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_tranaction_dispute_tr` (`transaction_id`),
  KEY `FK_w_tranaction_dispute_d` (`dispute_id`),
  KEY `FK_w_tranaction_dispute_a` (`answered_by`),
  KEY `FK_w_tranaction_disputed_by` (`disputed_by`),
  KEY `FK_w_transaction_dispute` (`state`),
  KEY `FK_w_transaction_disputee` (`wallet_exchange_id`),
  CONSTRAINT `FK_w_tranaction_dispute_d` FOREIGN KEY (`dispute_id`) REFERENCES `w_transaction_dispute` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_tranaction_dispute_tr` FOREIGN KEY (`transaction_id`) REFERENCES `w_transaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_w_transaction_dispute` FOREIGN KEY (`state`) REFERENCES `w_transaction_dispute_state` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_transaction_dispute_ans` FOREIGN KEY (`answered_by`) REFERENCES `w_wallet` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_transaction_dispute_d` FOREIGN KEY (`disputed_by`) REFERENCES `w_wallet` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_transaction_disputee` FOREIGN KEY (`wallet_exchange_id`) REFERENCES `wallet_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=47 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_transaction_dispute_state` */

DROP TABLE IF EXISTS `w_transaction_dispute_state`;

CREATE TABLE `w_transaction_dispute_state` (
  `id` int(1) unsigned NOT NULL AUTO_INCREMENT,
  `state` varchar(15) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_transaction_process` */

DROP TABLE IF EXISTS `w_transaction_process`;

CREATE TABLE `w_transaction_process` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `action_date` datetime NOT NULL,
  `wallet_id` int(15) unsigned DEFAULT NULL,
  `wallet_setup_id` int(15) unsigned DEFAULT NULL,
  `frozen_amount` float(15,2) unsigned DEFAULT NULL,
  `total_amount` float(15,2) unsigned DEFAULT NULL,
  `total_amount_price` float(15,2) unsigned DEFAULT NULL,
  `total_amount_exchange_id` int(14) unsigned DEFAULT NULL,
  `tax_id` int(15) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_wallet_transaction_action` (`tax_id`),
  KEY `FK_w_wallet_transaction_actionw` (`wallet_id`),
  KEY `FK_w_wallet_transaction_actionee` (`total_amount_exchange_id`),
  CONSTRAINT `FK_w_wallet_transaction_action` FOREIGN KEY (`tax_id`) REFERENCES `w_transaction_tax` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_wallet_transaction_actionee` FOREIGN KEY (`total_amount_exchange_id`) REFERENCES `w_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_wallet_transaction_actionw` FOREIGN KEY (`wallet_id`) REFERENCES `w_wallet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=275 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_transaction_purchase_ticket` */

DROP TABLE IF EXISTS `w_transaction_purchase_ticket`;

CREATE TABLE `w_transaction_purchase_ticket` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `item_id` int(15) NOT NULL,
  `transaction_id` int(15) unsigned NOT NULL,
  `purchase_type` int(2) unsigned NOT NULL,
  `name` varchar(32) COLLATE utf8_bin NOT NULL,
  `description` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_p_ticket_tr` (`transaction_id`),
  KEY `fk_p_ticket_tp` (`purchase_type`),
  CONSTRAINT `fk_p_ticket_tp` FOREIGN KEY (`purchase_type`) REFERENCES `w_t_purchase_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_p_ticket_tr` FOREIGN KEY (`transaction_id`) REFERENCES `w_transaction` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_transaction_state_lcp` */

DROP TABLE IF EXISTS `w_transaction_state_lcp`;

CREATE TABLE `w_transaction_state_lcp` (
  `id` int(1) unsigned NOT NULL,
  `state` varchar(32) COLLATE utf8_bin NOT NULL,
  `description` varchar(256) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_transaction_tax` */

DROP TABLE IF EXISTS `w_transaction_tax`;

CREATE TABLE `w_transaction_tax` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(15) unsigned DEFAULT NULL,
  `w_setup_id` int(15) unsigned DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `payment_date` datetime DEFAULT NULL,
  `total_amount_tax` float(15,2) unsigned NOT NULL,
  `total_amount_tax_price` float(15,2) unsigned NOT NULL,
  `t_tax` float(15,2) unsigned NOT NULL,
  `t_tax_price` float(15,2) unsigned NOT NULL,
  `t_tax_type` int(1) unsigned NOT NULL,
  `t_tax_exchange_id` int(1) unsigned DEFAULT NULL,
  `t_exchange_tax` float(15,2) unsigned DEFAULT NULL,
  `t_exchange_tax_price` float(15,2) unsigned DEFAULT NULL,
  `t_exchange_tax_type` int(1) unsigned DEFAULT NULL,
  `t_exchange_tax_exchange_id` int(1) unsigned DEFAULT NULL,
  `is_paid` int(1) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_transaction_tax` (`t_exchange_tax_exchange_id`),
  KEY `FK_w_transaction_taxe` (`t_tax_exchange_id`),
  KEY `FK_w_transaction_taxt` (`t_exchange_tax_type`),
  KEY `FK_w_transaction_taxtt` (`t_tax_type`),
  KEY `FK_w_transaction_taxw` (`wallet_id`),
  KEY `FK_w_transaction_taxws` (`w_setup_id`),
  CONSTRAINT `FK_w_transaction_tax` FOREIGN KEY (`t_exchange_tax_exchange_id`) REFERENCES `w_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_transaction_taxe` FOREIGN KEY (`t_tax_exchange_id`) REFERENCES `w_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_transaction_taxt` FOREIGN KEY (`t_exchange_tax_type`) REFERENCES `w_transaction_tax_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_transaction_taxtt` FOREIGN KEY (`t_tax_type`) REFERENCES `w_transaction_tax_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_transaction_taxw` FOREIGN KEY (`wallet_id`) REFERENCES `w_wallet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_transaction_taxws` FOREIGN KEY (`w_setup_id`) REFERENCES `wallet_setup` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=275 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_transaction_tax_type_lcp` */

DROP TABLE IF EXISTS `w_transaction_tax_type_lcp`;

CREATE TABLE `w_transaction_tax_type_lcp` (
  `id` int(1) unsigned NOT NULL,
  `type` varchar(32) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_transaction_type_lcp` */

DROP TABLE IF EXISTS `w_transaction_type_lcp`;

CREATE TABLE `w_transaction_type_lcp` (
  `id` int(2) unsigned NOT NULL,
  `value` varchar(50) COLLATE utf8_bin NOT NULL,
  `is_credit_card` smallint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_transfer_settings` */

DROP TABLE IF EXISTS `w_transfer_settings`;

CREATE TABLE `w_transfer_settings` (
  `partition_id` int(11) NOT NULL,
  `exact_id` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  `password` varchar(32) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`partition_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

/*Table structure for table `w_wallet` */

DROP TABLE IF EXISTS `w_wallet`;

CREATE TABLE `w_wallet` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `user_id` int(15) unsigned DEFAULT NULL,
  `user_name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `user_surname` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `user_email` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `money` double(15,2) unsigned NOT NULL DEFAULT '0.00',
  `frozen_amount` double(15,2) unsigned NOT NULL DEFAULT '0.00',
  `receiving_amount` double(15,2) unsigned NOT NULL DEFAULT '0.00',
  `currency_type` int(3) unsigned DEFAULT NULL,
  `password` varchar(256) COLLATE utf8_bin NOT NULL,
  `partition_id` int(5) unsigned NOT NULL,
  `updated_at` datetime DEFAULT NULL,
  `updated_by` int(15) unsigned DEFAULT NULL,
  `profile_id` int(1) unsigned NOT NULL,
  `logged_at` timestamp NULL DEFAULT NULL,
  `reset_password_token` varchar(16) COLLATE utf8_bin DEFAULT NULL,
  `is_locked` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `FK_w_wallet_u` (`user_id`),
  KEY `FK_w_wallet_up` (`updated_by`),
  KEY `FK_w_wallet` (`currency_type`),
  KEY `FK_w_wallet_pr` (`profile_id`),
  CONSTRAINT `FK_w_wallet` FOREIGN KEY (`currency_type`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_w_wallet_pr` FOREIGN KEY (`profile_id`) REFERENCES `w_wallet_profile_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=8756 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_wallet_current_location` */

DROP TABLE IF EXISTS `w_wallet_current_location`;

CREATE TABLE `w_wallet_current_location` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(15) unsigned NOT NULL,
  `latitude` varchar(15) COLLATE utf8_bin DEFAULT NULL,
  `longitude` varchar(15) COLLATE utf8_bin DEFAULT NULL,
  `login_date` datetime NOT NULL,
  `logout_date` datetime DEFAULT NULL,
  `r_user_agent` varchar(1024) COLLATE utf8_bin NOT NULL,
  `device_manufacturer` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `device_model` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `previous_url` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  `encrypt_key` varchar(256) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_w_wallet_current_location` (`wallet_id`),
  CONSTRAINT `FK_w_wallet_current_location` FOREIGN KEY (`wallet_id`) REFERENCES `w_wallet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=5171 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `w_wallet_profile_lcp` */

DROP TABLE IF EXISTS `w_wallet_profile_lcp`;

CREATE TABLE `w_wallet_profile_lcp` (
  `id` int(1) unsigned NOT NULL,
  `profile` varchar(50) COLLATE utf8_bin NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `wallet_exchange` */

DROP TABLE IF EXISTS `wallet_exchange`;

CREATE TABLE `wallet_exchange` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `wallet_id` int(15) unsigned NOT NULL,
  `setup_id` int(15) unsigned NOT NULL,
  `money` float(15,2) unsigned NOT NULL,
  `new_m_p_t` float(15,2) unsigned NOT NULL,
  `new_m_p_t_price` float(15,2) unsigned NOT NULL,
  `new_m_p_t_e_id` int(15) unsigned NOT NULL,
  `f_a` float(15,2) unsigned NOT NULL,
  `f_a_price` float(15,2) unsigned NOT NULL,
  `f_a_e_id` int(15) unsigned NOT NULL,
  `r_a` float(15,2) unsigned NOT NULL,
  `r_a_price` float(15,2) unsigned NOT NULL,
  `r_a_e_id` int(15) unsigned NOT NULL,
  `total_tax` float(15,2) unsigned DEFAULT NULL,
  `total_tax_price` float(15,2) unsigned DEFAULT NULL,
  `total_exchange_id` int(15) unsigned DEFAULT NULL,
  `total_tax_type` int(1) unsigned DEFAULT NULL,
  `old_currency` int(3) unsigned NOT NULL,
  `new_currency` int(3) unsigned NOT NULL,
  `exchanged_at` datetime NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_wallet_exchange` (`wallet_id`),
  KEY `FK_wallet_exchangefa` (`f_a_e_id`),
  KEY `FK_wallet_exchangete` (`total_exchange_id`),
  KEY `FK_wallet_exchangere` (`r_a_e_id`),
  KEY `FK_wallet_exchangenmpl` (`new_m_p_t_e_id`),
  KEY `FK_wallet_exchangeoc` (`old_currency`),
  KEY `FK_wallet_exchangenc` (`new_currency`),
  KEY `FK_wallet_exchangettt` (`total_tax_type`),
  CONSTRAINT `FK_wallet_exchange` FOREIGN KEY (`wallet_id`) REFERENCES `w_wallet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchange_fe` FOREIGN KEY (`f_a_e_id`) REFERENCES `w_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchangenc` FOREIGN KEY (`new_currency`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchangenmpl` FOREIGN KEY (`new_m_p_t_e_id`) REFERENCES `w_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchangeoc` FOREIGN KEY (`old_currency`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchangere` FOREIGN KEY (`r_a_e_id`) REFERENCES `w_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchangete` FOREIGN KEY (`total_exchange_id`) REFERENCES `w_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchangettt` FOREIGN KEY (`total_tax_type`) REFERENCES `w_transaction_tax_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `wallet_exchange_pending` */

DROP TABLE IF EXISTS `wallet_exchange_pending`;

CREATE TABLE `wallet_exchange_pending` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `w_id` int(15) unsigned NOT NULL,
  `w_e_id` int(15) unsigned NOT NULL,
  `tr_id` int(15) unsigned NOT NULL,
  `old_from_t_p` float(15,2) unsigned DEFAULT NULL,
  `new_from_t_p` float(15,2) unsigned DEFAULT NULL,
  `old_from_t_p_c` int(3) unsigned DEFAULT NULL,
  `new_from_t_p_c` int(3) unsigned DEFAULT NULL,
  `from_t_p_e_id` int(15) unsigned DEFAULT NULL,
  `old_to_t_p` float(15,2) unsigned DEFAULT NULL,
  `new_to_t_p` float(15,2) unsigned DEFAULT NULL,
  `old_to_t_p_c` int(3) unsigned DEFAULT NULL,
  `new_to_t_p_c` int(3) unsigned DEFAULT NULL,
  `to_t_p_e_id` int(15) unsigned DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_wallet_exchange_pending` (`w_e_id`),
  KEY `FK_wallet_exchange_pendingt` (`tr_id`),
  KEY `FK_wallet_exchange_pendingnto` (`new_to_t_p_c`),
  KEY `FK_wallet_exchange_pendingnfc` (`new_from_t_p_c`),
  KEY `FK_wallet_exchange_pendingof` (`old_from_t_p_c`),
  KEY `FK_wallet_exchange_pendingoc` (`old_to_t_p_c`),
  KEY `FK_wallet_exchange_pendingfe` (`from_t_p_e_id`),
  KEY `FK_wallet_exchange_pendingoe` (`to_t_p_e_id`),
  KEY `FK_wallet_exchange_pendingw` (`w_id`),
  CONSTRAINT `FK_wallet_exchange_pending` FOREIGN KEY (`w_e_id`) REFERENCES `wallet_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchange_pendingfe` FOREIGN KEY (`from_t_p_e_id`) REFERENCES `w_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchange_pendingnfc` FOREIGN KEY (`new_from_t_p_c`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchange_pendingnto` FOREIGN KEY (`new_to_t_p_c`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchange_pendingoc` FOREIGN KEY (`old_to_t_p_c`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchange_pendingoe` FOREIGN KEY (`to_t_p_e_id`) REFERENCES `w_exchange` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchange_pendingof` FOREIGN KEY (`old_from_t_p_c`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchange_pendingt` FOREIGN KEY (`tr_id`) REFERENCES `w_transaction` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_exchange_pendingw` FOREIGN KEY (`w_id`) REFERENCES `w_wallet` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=169 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Table structure for table `wallet_setup` */

DROP TABLE IF EXISTS `wallet_setup`;

CREATE TABLE `wallet_setup` (
  `id` int(15) unsigned NOT NULL AUTO_INCREMENT,
  `partition_id` int(11) NOT NULL,
  `currency_type` int(3) unsigned NOT NULL,
  `balance` double(15,2) unsigned NOT NULL DEFAULT '0.00',
  `available_rates` varchar(1024) COLLATE utf8_bin DEFAULT NULL,
  `available_cards` varbinary(1024) DEFAULT NULL,
  `frozen_amount` double(15,2) unsigned NOT NULL DEFAULT '0.00',
  `receiving_amount` double(15,2) unsigned NOT NULL DEFAULT '0.00',
  `transfer_tax_amount` float(15,2) unsigned NOT NULL DEFAULT '0.00',
  `transfer_fee_percent` double(15,2) unsigned NOT NULL,
  `transfer_min_fee` float(15,2) unsigned NOT NULL,
  `transfer_max_fee` float(15,2) unsigned NOT NULL,
  `receiver_fee_percent` float(15,2) unsigned NOT NULL,
  `receiver_min_fee` float(15,2) unsigned NOT NULL,
  `receiver_max_fee` float(15,2) unsigned NOT NULL,
  `m_deposit_fee_percent` float(15,2) unsigned NOT NULL,
  `m_deposit_min_fee` float(15,2) unsigned NOT NULL,
  `m_deposit_max_fee` float(15,2) unsigned NOT NULL,
  `m_withdraw_fee_percent` float(15,2) unsigned NOT NULL,
  `m_withdraw_min_fee` float(15,2) unsigned NOT NULL,
  `m_withdraw_max_fee` float(15,2) unsigned NOT NULL,
  `exchange_transfer_fee_percent` float(15,2) unsigned NOT NULL,
  `exchange_transfer_min_fee` float(15,2) unsigned NOT NULL,
  `exchange_transfer_max_fee` float(15,2) unsigned NOT NULL,
  `exchange_receiver_fee_percent` float(15,2) unsigned NOT NULL,
  `exchange_receiver_min_fee` float(15,2) unsigned NOT NULL,
  `exchange_receiver_max_fee` float(15,2) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_wallet_settup` (`partition_id`),
  KEY `FK_wallet_setup` (`currency_type`),
  CONSTRAINT `FK_wallet_settup` FOREIGN KEY (`partition_id`) REFERENCES `core_partitions` (`partition_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_wallet_setup` FOREIGN KEY (`currency_type`) REFERENCES `w_currency_type_lcp` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
