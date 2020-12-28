/*
 Navicat Premium Data Transfer

 Source Server         : localhost_3306
 Source Server Type    : MariaDB
 Source Server Version : 100408
 Source Host           : localhost:3306
 Source Schema         : reportbuild

 Target Server Type    : MariaDB
 Target Server Version : 100408
 File Encoding         : 65001

 Date: 14/01/2020 22:48:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for customer
-- ----------------------------
DROP TABLE IF EXISTS `customer`;
CREATE TABLE `customer`  (
  `customer_id` int(10) UNSIGNED ZEROFILL NOT NULL,
  `c_organization_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `c_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `c_contact` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`customer_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of customer
-- ----------------------------
INSERT INTO `customer` VALUES (0000000274, 'สหกรณ์ออมทรัพย์วาปีปทุม จำกัด', '375/13-14 หมู่ที่ 25;sdist_id;dist_id;prov_id;44120', 'โทร.062-3260926, 098-4560347 เรียน คุณอรทัย 085-3199012');
INSERT INTO `customer` VALUES (0000000275, 'Test edit v3', '1234567890', '0878978467');
INSERT INTO `customer` VALUES (0000000276, 'eeedd', 'sadasd', 'eweqeq');
INSERT INTO `customer` VALUES (0000000277, 'ewqeqwe', 'qeqweqwe', 'qweqweqwe');
INSERT INTO `customer` VALUES (0000000278, 'ewqeqwedsfsdf', 'qeqweqwe', 'qweqweqwe');
INSERT INTO `customer` VALUES (0000000279, 'test', '123456', '55555');

-- ----------------------------
-- Table structure for paper
-- ----------------------------
DROP TABLE IF EXISTS `paper`;
CREATE TABLE `paper`  (
  `paper_id` int(3) UNSIGNED ZEROFILL NOT NULL,
  `paper_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `paper_weight` double DEFAULT NULL,
  `paper_color` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `paper_default` enum('top','bottom') CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`paper_id`) USING BTREE,
  UNIQUE INDEX `index`(`paper_default`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of paper
-- ----------------------------
INSERT INTO `paper` VALUES (001, 'test 01', 69, 'ดำ', NULL);
INSERT INTO `paper` VALUES (002, 'test01', 60, 'ขาว', NULL);
INSERT INTO `paper` VALUES (003, '0003', 20, 'น้ำตาล', 'top');
INSERT INTO `paper` VALUES (004, 'qeqweq', 20, 'eqeqeqeq', 'bottom');
INSERT INTO `paper` VALUES (005, 'BOND', 60, 'เหลือง', NULL);
INSERT INTO `paper` VALUES (007, 'test paper', 123, 'black', NULL);
INSERT INTO `paper` VALUES (008, 'paper 002', 1, 'white', NULL);

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
  `product_id` int(7) UNSIGNED ZEROFILL NOT NULL,
  `product_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `product_quantity` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `product_total_page` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `product_constat` tinyint(1) DEFAULT NULL,
  `product_other` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `product_unit_set` tinyint(1) DEFAULT NULL,
  `product_form_depth` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `product_form_width` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `product_start_page` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `product_end_page` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`product_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES (0000001, 'test id 001', '-', '5', 1, '-', 0, '12', '12', '-', '-');
INSERT INTO `product` VALUES (0000002, 'test id 001', '-', '5', 1, '-', 0, '12', '12', '-', '-');

-- ----------------------------
-- Table structure for product_specification
-- ----------------------------
DROP TABLE IF EXISTS `product_specification`;
CREATE TABLE `product_specification`  (
  `product_id` int(7) UNSIGNED ZEROFILL NOT NULL,
  `paper_id` int(3) UNSIGNED ZEROFILL NOT NULL,
  `part_ink_onface` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `part_ink_backface` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `part_vert_perf` tinyint(1) DEFAULT NULL,
  `part_horz_perf` tinyint(1) DEFAULT NULL,
  `part_remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  INDEX `proId`(`product_id`) USING BTREE,
  INDEX `get paper`(`paper_id`) USING BTREE,
  CONSTRAINT `get paper` FOREIGN KEY (`paper_id`) REFERENCES `paper` (`paper_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `get pro` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of product_specification
-- ----------------------------
INSERT INTO `product_specification` VALUES (0000001, 003, '-', '-', 0, 1, '-');
INSERT INTO `product_specification` VALUES (0000001, 002, '-', '-', 1, 0, '-');
INSERT INTO `product_specification` VALUES (0000001, 003, '-', '-', 1, 0, '-');
INSERT INTO `product_specification` VALUES (0000001, 005, '-', '-', 1, 0, '-');
INSERT INTO `product_specification` VALUES (0000001, 004, '-', '-', 0, 1, '-');
INSERT INTO `product_specification` VALUES (0000002, 003, '-', '-', 0, 1, '-');
INSERT INTO `product_specification` VALUES (0000002, 002, '-', '-', 1, 0, '-');
INSERT INTO `product_specification` VALUES (0000002, 003, '-', '-', 1, 0, '-');
INSERT INTO `product_specification` VALUES (0000002, 005, '-', '-', 1, 0, '-');
INSERT INTO `product_specification` VALUES (0000002, 004, '-', '-', 0, 1, '-');

-- ----------------------------
-- Table structure for quotation
-- ----------------------------
DROP TABLE IF EXISTS `quotation`;
CREATE TABLE `quotation`  (
  `quotation_id` int(7) UNSIGNED ZEROFILL NOT NULL,
  `product_id` int(7) UNSIGNED ZEROFILL DEFAULT NULL,
  `customer_id` int(10) UNSIGNED ZEROFILL DEFAULT NULL,
  `sale_id` int(2) DEFAULT NULL,
  `quotation_date` date DEFAULT NULL,
  `despatch_require` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `additional_detail` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `c_order_id` int(11) DEFAULT NULL COMMENT '?? for sale? or customer?',
  `your_ref` varchar(10) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `status` enum('approve','notapprove','pending') CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT 'pending',
  `price_detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`quotation_id`) USING BTREE,
  INDEX `cusId`(`customer_id`) USING BTREE,
  INDEX `proId`(`product_id`) USING BTREE,
  INDEX `get sale`(`sale_id`) USING BTREE,
  CONSTRAINT `get customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `get product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `get sale` FOREIGN KEY (`sale_id`) REFERENCES `sale` (`sale_id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of quotation
-- ----------------------------
INSERT INTO `quotation` VALUES (0000001, 0000001, 0000000274, 1, '2020-01-14', '-', 'test', 1, '-', 'pending', 'test');
INSERT INTO `quotation` VALUES (0000002, 0000002, 0000000274, 1, '2020-01-14', '-', 'test', 1, '-', 'approve', 'test');

-- ----------------------------
-- Table structure for sale
-- ----------------------------
DROP TABLE IF EXISTS `sale`;
CREATE TABLE `sale`  (
  `sale_id` int(2) NOT NULL,
  `sale_no` varchar(4) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `s_fname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `s_lname` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`sale_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sale
-- ----------------------------
INSERT INTO `sale` VALUES (1, 'SW', 'ee', 'aa');
INSERT INTO `sale` VALUES (2, 'GG', 'Good', 'Game');
INSERT INTO `sale` VALUES (3, 'TS', 'testing', 'testing');

-- ----------------------------
-- Table structure for workorder
-- ----------------------------
DROP TABLE IF EXISTS `workorder`;
CREATE TABLE `workorder`  (
  `workorder_id` int(6) UNSIGNED ZEROFILL NOT NULL,
  `quotation_id` int(7) UNSIGNED DEFAULT NULL,
  `customer_id` int(10) UNSIGNED ZEROFILL DEFAULT NULL,
  `product_id` int(7) UNSIGNED ZEROFILL DEFAULT NULL,
  `sale_id` int(2) DEFAULT NULL,
  `work_date` date DEFAULT NULL,
  `work_dispatch` date DEFAULT NULL,
  `work_sprocket_hole` tinyint(1) DEFAULT NULL,
  `work_crimping` tinyint(1) DEFAULT NULL,
  `work_glue` tinyint(1) DEFAULT NULL,
  `work_form_folding` varchar(5) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `work_packing_set` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `work_packing` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  `work_additional_detail` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL,
  PRIMARY KEY (`workorder_id`) USING BTREE,
  INDEX `qwe`(`quotation_id`) USING BTREE,
  INDEX `sale`(`sale_id`) USING BTREE,
  INDEX `product`(`product_id`) USING BTREE,
  INDEX `customer`(`customer_id`) USING BTREE,
  CONSTRAINT `customer` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `product` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `qwe` FOREIGN KEY (`quotation_id`) REFERENCES `quotation` (`quotation_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `sale` FOREIGN KEY (`sale_id`) REFERENCES `sale` (`sale_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of workorder
-- ----------------------------
INSERT INTO `workorder` VALUES (000001, 2, 0000000274, 0000002, 1, '2020-01-14', '2020-01-31', 1, 0, 1, '-', '1000', '10', 'test finally?');
INSERT INTO `workorder` VALUES (000002, 2, 0000000274, 0000002, 1, '2020-01-14', '2020-01-31', 1, 0, 1, '-', '1000', '10', 'test finally?');
INSERT INTO `workorder` VALUES (000003, 2, 0000000274, 0000002, 1, '2020-01-14', '2020-01-31', 1, 0, 1, '-', '1000', '10', 'test finally?');

-- ----------------------------
-- Procedure structure for DUPLICATE_QUOTATION
-- ----------------------------
DROP PROCEDURE IF EXISTS `DUPLICATE_QUOTATION`;
delimiter ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `DUPLICATE_QUOTATION`(IN quotationId int)
BEGIN
	#Routine body goes here...

END
;;
delimiter ;

SET FOREIGN_KEY_CHECKS = 1;
