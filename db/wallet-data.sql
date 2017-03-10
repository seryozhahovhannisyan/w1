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

/*Data for the table `status_lcp` */

insert  into `status_lcp`(`key`,`status`) values 
(1,'active'),
(2,'deleted'),
(3,'hidden'),
(4,'blocked'),
(5,'unverified');

/*Data for the table `w_currency_type_lcp` */

insert  into `w_currency_type_lcp`(`id`,`code`,`name`) values 
(1,'AED','United Arab Emirates Dirham (AED)'),
(2,'AFN','Afghan Afghani (AFN)'),
(3,'ALL','Albanian Lek (ALL)'),
(4,'AMD','Armenian Dram (AMD)'),
(5,'ANG','Netherlands Antillean Guilder (ANG)'),
(6,'AOA','Angolan Kwanza (AOA)'),
(7,'ARS','Argentine Peso (ARS)'),
(8,'AUD','Australian Dollar (A$)'),
(9,'AWG','Aruban Florin (AWG)'),
(10,'AZN','Azerbaijani Manat (AZN)'),
(11,'BAM','Bosnia-Herzegovina Convertible Mark (BAM)'),
(12,'BBD','Barbadian Dollar (BBD)'),
(13,'BDT','Bangladeshi Taka (BDT)'),
(14,'BGN','Bulgarian Lev (BGN)'),
(15,'BHD','Bahraini Dinar (BHD)'),
(16,'BIF','Burundian Franc (BIF)'),
(17,'BMD','Bermudan Dollar (BMD)'),
(18,'BND','Brunei Dollar (BND)'),
(19,'BOB','Bolivian Boliviano (BOB)'),
(20,'BRL','Brazilian Real (R$)'),
(21,'BSD','Bahamian Dollar (BSD)'),
(22,'BTC','Bitcoin (฿)'),
(23,'BTN','Bhutanese Ngultrum (BTN)'),
(24,'BWP','Botswanan Pula (BWP)'),
(25,'BYR','Belarusian Ruble (BYR)'),
(26,'BZD','Belize Dollar (BZD)'),
(27,'CAD','Canadian Dollar (CA$)'),
(28,'CDF','Congolese Franc (CDF)'),
(29,'CHF','Swiss Franc (CHF)'),
(30,'CLF','Chilean Unit of Account (UF) (CLF)'),
(31,'CLP','Chilean Peso (CLP)'),
(32,'CNH','CNH (CNH)'),
(33,'CNY','Chinese Yuan (CN¥)'),
(34,'COP','Colombian Peso (COP)'),
(35,'CRC','Costa Rican Colón (CRC)'),
(36,'CUP','Cuban Peso (CUP)'),
(37,'CVE','Cape Verdean Escudo (CVE)'),
(38,'CZK','Czech Republic Koruna (CZK)'),
(39,'DEM','German Mark (DEM)'),
(40,'DJF','Djiboutian Franc (DJF)'),
(41,'DKK','Danish Krone (DKK)'),
(42,'DOP','Dominican Peso (DOP)'),
(43,'DZD','Algerian Dinar (DZD)'),
(44,'EGP','Egyptian Pound (EGP)'),
(45,'ERN','Eritrean Nakfa (ERN)'),
(46,'ETB','Ethiopian Birr (ETB)'),
(47,'EUR','Euro (€)'),
(48,'FIM','Finnish Markka (FIM)'),
(49,'FJD','Fijian Dollar (FJD)'),
(50,'FKP','Falkland Islands Pound (FKP)'),
(51,'FRF','French Franc (FRF)'),
(52,'GBP','British Pound Sterling (£)'),
(53,'GEL','Georgian Lari (GEL)'),
(54,'GHS','Ghanaian Cedi (GHS)'),
(55,'GIP','Gibraltar Pound (GIP)'),
(56,'GMD','Gambian Dalasi (GMD)'),
(57,'GNF','Guinean Franc (GNF)'),
(58,'GTQ','Guatemalan Quetzal (GTQ)'),
(59,'GYD','Guyanaese Dollar (GYD)'),
(60,'HKD','Hong Kong Dollar (HK$)'),
(61,'HNL','Honduran Lempira (HNL)'),
(62,'HRK','Croatian Kuna (HRK)'),
(63,'HTG','Haitian Gourde (HTG)'),
(64,'HUF','Hungarian Forint (HUF)'),
(65,'IDR','Indonesian Rupiah (IDR)'),
(66,'IEP','Irish Pound (IEP)'),
(67,'ILS','Israeli New Sheqel (₪)'),
(68,'INR','Indian Rupee (Rs.)'),
(69,'IQD','Iraqi Dinar (IQD)'),
(70,'IRR','Iranian Rial (IRR)'),
(71,'ISK','Icelandic Króna (ISK)'),
(72,'ITL','Italian Lira (ITL)'),
(73,'JMD','Jamaican Dollar (JMD)'),
(74,'JOD','Jordanian Dinar (JOD)'),
(75,'JPY','Japanese Yen (¥)'),
(76,'KES','Kenyan Shilling (KES)'),
(77,'KGS','Kyrgystani Som (KGS)'),
(78,'KHR','Cambodian Riel (KHR)'),
(79,'KMF','Comorian Franc (KMF)'),
(80,'KPW','North Korean Won (KPW)'),
(81,'KRW','South Korean Won (₩)'),
(82,'KWD','Kuwaiti Dinar (KWD)'),
(83,'KYD','Cayman Islands Dollar (KYD)'),
(84,'KZT','Kazakhstani Tenge (KZT)'),
(85,'LAK','Laotian Kip (LAK)'),
(86,'LBP','Lebanese Pound (LBP)'),
(87,'LKR','Sri Lankan Rupee (LKR)'),
(88,'LRD','Liberian Dollar (LRD)'),
(89,'LSL','Lesotho Loti (LSL)'),
(90,'LTL','Lithuanian Litas (LTL)'),
(91,'LVL','Latvian Lats (LVL)'),
(92,'LYD','Libyan Dinar (LYD)'),
(93,'MAD','Moroccan Dirham (MAD)'),
(94,'MDL','Moldovan Leu (MDL)'),
(95,'MGA','Malagasy Ariary (MGA)'),
(96,'MKD','Macedonian Denar (MKD)'),
(97,'MMK','Myanmar Kyat (MMK)'),
(98,'MNT','Mongolian Tugrik (MNT)'),
(99,'MOP','Macanese Pataca (MOP)'),
(100,'MRO','Mauritanian Ouguiya (MRO)'),
(101,'MUR','Mauritian Rupee (MUR)'),
(102,'MVR','Maldivian Rufiyaa (MVR)'),
(103,'MWK','Malawian Kwacha (MWK)'),
(104,'MXN','Mexican Peso (MX$)'),
(105,'MYR','Malaysian Ringgit (MYR)'),
(106,'MZN','Mozambican Metical (MZN)'),
(107,'NAD','Namibian Dollar (NAD)'),
(108,'NGN','Nigerian Naira (NGN)'),
(109,'NIO','Nicaraguan Córdoba (NIO)'),
(110,'NOK','Norwegian Krone (NOK)'),
(111,'NPR','Nepalese Rupee (NPR)'),
(112,'NZD','New Zealand Dollar (NZ$)'),
(113,'OMR','Omani Rial (OMR)'),
(114,'PAB','Panamanian Balboa (PAB)'),
(115,'PEN','Peruvian Nuevo Sol (PEN)'),
(116,'PGK','Papua New Guinean Kina (PGK)'),
(117,'PHP','Philippine Peso (Php)'),
(118,'PKG','PKG (PKG)'),
(119,'PKR','Pakistani Rupee (PKR)'),
(120,'PLN','Polish Zloty (PLN)'),
(121,'PYG','Paraguayan Guarani (PYG)'),
(122,'QAR','Qatari Rial (QAR)'),
(123,'RON','Romanian Leu (RON)'),
(124,'RSD','Serbian Dinar (RSD)'),
(125,'RUB','Russian Ruble (RUB)'),
(126,'RWF','Rwandan Franc (RWF)'),
(127,'SAR','Saudi Riyal (SAR)'),
(128,'SBD','Solomon Islands Dollar (SBD)'),
(129,'SCR','Seychellois Rupee (SCR)'),
(130,'SDG','Sudanese Pound (SDG)'),
(131,'SEK','Swedish Krona (SEK)'),
(132,'SGD','Singapore Dollar (SGD)'),
(133,'SHP','Saint Helena Pound (SHP)'),
(134,'SLL','Sierra Leonean Leone (SLL)'),
(135,'SOS','Somali Shilling (SOS)'),
(136,'SRD','Surinamese Dollar (SRD)'),
(137,'STD','São Tomé and Príncipe Dobra (STD)'),
(138,'SVC','Salvadoran Colón (SVC)'),
(139,'SYP','Syrian Pound (SYP)'),
(140,'SZL','Swazi Lilangeni (SZL)'),
(141,'THB','Thai Baht (฿)'),
(142,'TJS','Tajikistani Somoni (TJS)'),
(143,'TMT','Turkmenistani Manat (TMT)'),
(144,'TND','Tunisian Dinar (TND)'),
(145,'TOP','Tongan Paʻanga (TOP)'),
(146,'TRY','Turkish Lira (TRY)'),
(147,'TTD','Trinidad and Tobago Dollar (TTD)'),
(148,'TWD','New Taiwan Dollar (NT$)'),
(149,'TZS','Tanzanian Shilling (TZS)'),
(150,'UAH','Ukrainian Hryvnia (UAH)'),
(151,'UGX','Ugandan Shilling (UGX)'),
(152,'USD','US Dollar ($)'),
(153,'UYU','Uruguayan Peso (UYU)'),
(154,'UZS','Uzbekistan Som (UZS)'),
(155,'VEF','Venezuelan Bolívar (VEF)'),
(156,'VND','Vietnamese Dong (₫)'),
(157,'VUV','Vanuatu Vatu (VUV)'),
(158,'WST','Samoan Tala (WST)'),
(159,'XAF','CFA Franc BEAC (FCFA)'),
(160,'XCD','East Caribbean Dollar (EC$)'),
(161,'XDR','Special Drawing Rights (XDR)'),
(162,'XOF','CFA Franc BCEAO (CFA)'),
(163,'XPF','CFP Franc (CFPF)'),
(164,'YER','Yemeni Rial (YER)'),
(165,'ZAR','South African Rand (ZAR)'),
(166,'ZMK','Zambian Kwacha (1968–2012) (ZMK)'),
(167,'ZMW','Zambian Kwacha (ZMW)'),
(168,'ZWL','Zimbabwean Dollar (2009) (ZWL)');

/*Data for the table `w_exchange_source` */

insert  into `w_exchange_source`(`id`,`wallet_setup_id`,`name`,`address`,`phone`,`url`,`icon`,`is_default`,`is_active`,`app_id`,`username`,`password`) values 
(5,2,'Open Exchange Rates','','','https://openexchangerates.org/api/latest.json?app_id=',10,1,1,'b0cdf4c111294bf5b05966721bd02cc9 ',' seryozha.hovhannisyan@gmail.com','12345678');

/*Data for the table `w_t_purchase_type_lcp` */

insert  into `w_t_purchase_type_lcp`(`id`,`type`) values 
(1,'purchase'),
(2,'service'),
(3,'other');

/*Data for the table `w_transaction_state_lcp` */

insert  into `w_transaction_state_lcp`(`id`,`state`,`description`) values 
(1,'pending','when somebody starts transaction for pass to money'),
(2,'delay','when somebody cancels transaction before received '),
(3,'allow','when transaction is successfully done, and allowin'),
(4,'transfer','when transaction is successfully done, and transfe'),
(5,'cancel','when transaction is successfully done but canceled'),
(6,'disable','canceled by system, transaction open period expire'),
(7,'freeze','when somebody starts transaction for pass money to walletSetup'),
(8,'cancel_fre','when after freeze asked to cancel freeze , transaction is successfully done but canceled'),
(9,'charge','when transaction is successfully done, and transferring was completed, wallet frozen and money fields will minussed, walletSetup receiving_amount will'),
(10,'charge_amo','when transaction is successfully done, and transferring was completed, walletSetup receiving_amount will minussed and balance plussed immediately'),
(11,'send money','new send money'),
(12,'request transaction','new request transaction'),
(13,'approved','new approved'),
(14,'rejected','new rejected'),
(15,'pending','new pending'),
(16,'passed to charge','new passed to charge'),
(17,'exchange_balance','new exchange balance');

/*Data for the table `w_transaction_tax_type_lcp` */

insert  into `w_transaction_tax_type_lcp`(`id`,`type`) values 
(1,'min'),
(2,'max'),
(3,'percent');

/*Data for the table `w_transaction_type_lcp` */

insert  into `w_transaction_type_lcp`(`id`,`value`,`is_credit_card`) values 
(1,'Merchant Wallet',0),
(2,'Game Point',0),
(3,'Cash',0),
(4,'Bit Coin',0),
(5,'Master Card',1),
(6,'Visa Card',1),
(7,'American Express',1),
(8,'Discover',1),
(9,'Diners Club',0),
(10,'JCB',0);

/*Data for the table `w_wallet_profile_lcp` */

insert  into `w_wallet_profile_lcp`(`id`,`profile`) values 
(1,'admin'),
(2,'moderator'),
(3,'user'),
(4,'driver');

/*Data for the table `wallet_setup` */

insert  into `wallet_setup`(`id`,`partition_id`,`currency_type`,`balance`,`available_rates`,`available_cards`,`frozen_amount`,`receiving_amount`,`transfer_tax_amount`,`transfer_fee_percent`,`transfer_min_fee`,`transfer_max_fee`,`receiver_fee_percent`,`receiver_min_fee`,`receiver_max_fee`,`m_deposit_fee_percent`,`m_deposit_min_fee`,`m_deposit_max_fee`,`m_withdraw_fee_percent`,`m_withdraw_min_fee`,`m_withdraw_max_fee`,`exchange_transfer_fee_percent`,`exchange_transfer_min_fee`,`exchange_transfer_max_fee`,`exchange_receiver_fee_percent`,`exchange_receiver_min_fee`,`exchange_receiver_max_fee`) values 
(2,8,152,100000000.00,'1,4,125,152,153','5,6,7,8,9,10',0.00,0.00,10.68,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00),
(3,10,152,100000000.00,'4,125,152,154','5,8',0.00,0.00,0.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00),
(4,11,152,100000000.00,'4,125,152,155','5,8',0.00,0.00,0.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00),
(5,13,152,100000000.00,'4,125,152,156','5,8',0.00,0.00,0.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00),
(6,1,152,100000000.00,'4,125,152,157','5,8',0.00,0.00,0.24,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00),
(7,14,152,100000000.00,'4,125,152,158','5,8',0.00,0.00,0.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00),
(8,16,152,99999999.60,'4,125,152,158','5,8',0.00,0.00,0.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00),
(9,17,152,100000000.00,'4,125,152,158','5,8',0.00,0.00,0.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00),
(10,15,152,100000000.00,'4,125,152,156','5,8',0.00,0.00,0.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00,1.00,0.00,1.00);

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
