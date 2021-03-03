DROP TABLE IF EXISTS daily_nifty;
DROP TABLE IF EXISTS daily_nifty_count;


CREATE TABLE `daily_nifty` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stock_name` varchar(200) DEFAULT NULL,
  `stock_price` varchar(200) DEFAULT NULL,
  `stock_chg_date` date DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1101 DEFAULT CHARSET=latin1;


CREATE TABLE `daily_nifty_count` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `stock_name` varchar(200) DEFAULT NULL,
  `count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=latin1;
