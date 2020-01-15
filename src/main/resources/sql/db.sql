/**
 * 订单表
 */
CREATE TABLE `t_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `product_id` int(11) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=155 DEFAULT CHARSET=utf8;
/**
 * 产品表
 */
CREATE TABLE `t_product` (
     `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
     `name` varchar(45) DEFAULT NULL,
     `number` int(11) DEFAULT NULL,
     `version` int(11) DEFAULT '0',
     PRIMARY KEY (`id`),
     UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

