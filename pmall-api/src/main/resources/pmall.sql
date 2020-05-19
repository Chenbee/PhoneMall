-- ----------------------------
-- Table structure for oms_cart_item
-- ----------------------------
DROP TABLE IF EXISTS `oms_cart_item`;
CREATE TABLE `oms_cart_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `product_id` bigint(20) DEFAULT NULL,
  `product_sku_id` bigint(20) DEFAULT NULL,
  `member_id` bigint(20) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL COMMENT '购买数量',
  `price` decimal(10,2) DEFAULT NULL COMMENT '添加到购物车的价格',
  `product_pic` varchar(1000) DEFAULT NULL COMMENT '商品主图',
  `product_name` varchar(500) DEFAULT NULL COMMENT '商品名称',
  `member_nickname` varchar(500) DEFAULT NULL COMMENT '会员昵称',
  `create_date` datetime DEFAULT NULL COMMENT '创建时间',
  `is_checked` varchar(1),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='购物车表';

-- ----------------------------
-- Table structure for oms_order
-- ----------------------------
DROP TABLE IF EXISTS `oms_order`;
CREATE TABLE `oms_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单id',
  `member_id` bigint(20) NOT NULL,
  `create_time` datetime DEFAULT NULL COMMENT '提交时间',
  `member_username` varchar(64) DEFAULT NULL COMMENT '用户帐号',
  `total_amount` decimal(10,2) DEFAULT NULL COMMENT '订单总金额',
  `pay_amount` decimal(10,2) DEFAULT NULL COMMENT '应付金额（实际支付金额）',
  `status` int(1) DEFAULT NULL COMMENT '订单状态：0->待付款；1->待发货；2->已发货；3->已完成；4->已关闭；5->无效订单',
  `receiver_name` varchar(100) NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(32) NOT NULL COMMENT '收货人电话',
  `receiver_post_code` varchar(32) DEFAULT NULL COMMENT '收货人邮编',
  `receiver_province` varchar(32) DEFAULT NULL COMMENT '省份/直辖市',
  `receiver_city` varchar(32) DEFAULT NULL COMMENT '城市',
  `receiver_region` varchar(32) DEFAULT NULL COMMENT '区',
  `receiver_detail_address` varchar(200) DEFAULT NULL COMMENT '详细地址',
  `note` varchar(500) DEFAULT NULL COMMENT '订单备注',
  `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime DEFAULT NULL COMMENT '确认收货时间',
  `modify_time` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8 COMMENT='订单表';

-- ----------------------------
-- Table structure for oms_order_item
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_item`;
CREATE TABLE `oms_order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` bigint(20) DEFAULT NULL COMMENT '订单id',
  `order_sn` varchar(64) DEFAULT NULL COMMENT '订单编号',
  `product_id` bigint(20) DEFAULT NULL,
  `product_pic` varchar(500) DEFAULT NULL,
  `product_name` varchar(200) DEFAULT NULL,
  `product_price` decimal(10,2) DEFAULT NULL COMMENT '销售价格',
  `product_quantity` int(11) DEFAULT NULL COMMENT '购买数量',
  `product_sku_id` bigint(20) DEFAULT NULL COMMENT '商品sku编号',
  `product_category_id` bigint(20) DEFAULT NULL COMMENT '商品分类id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8 COMMENT='订单中所包含的商品';

-- ----------------------------
-- Table structure for pms_base_attr_info
-- ----------------------------
DROP TABLE IF EXISTS `pms_base_attr_info`;
CREATE TABLE `pms_base_attr_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `attr_name` varchar(100) NOT NULL COMMENT '属性名称',
  `catalog3_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=43 DEFAULT CHARSET=utf8 COMMENT='属性表';

-- ----------------------------
-- Table structure for pms_base_attr_value
-- ----------------------------
DROP TABLE IF EXISTS `pms_base_attr_value`;
CREATE TABLE `pms_base_attr_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `value_name` varchar(100) NOT NULL COMMENT '属性值名称',
  `attr_id` bigint(20) DEFAULT NULL COMMENT '属性id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8 COMMENT='属性值表';

-- ----------------------------
-- Table structure for pms_base_catalog1
-- ----------------------------
DROP TABLE IF EXISTS `pms_base_catalog1`;
CREATE TABLE `pms_base_catalog1` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(200) NOT NULL COMMENT '分类名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8 COMMENT='一级分类表';

-- ----------------------------
-- Table structure for pms_base_catalog2
-- ----------------------------
DROP TABLE IF EXISTS `pms_base_catalog2`;
CREATE TABLE `pms_base_catalog2` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(200) NOT NULL COMMENT '二级分类名称',
  `catalog1_id` int(11) DEFAULT NULL COMMENT '一级分类编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=144 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pms_base_catalog3
-- ----------------------------
DROP TABLE IF EXISTS `pms_base_catalog3`;
CREATE TABLE `pms_base_catalog3` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(200) NOT NULL COMMENT '三级分类名称',
  `catalog2_id` bigint(20) DEFAULT NULL COMMENT '二级分类编号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1260 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for pms_base_sale_attr
-- ----------------------------
DROP TABLE IF EXISTS `pms_base_sale_attr`;
CREATE TABLE `pms_base_sale_attr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `name` varchar(100) NOT NULL COMMENT '销售属性名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;


-- ----------------------------
-- Table structure for pms_product_image
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_image`;
CREATE TABLE `pms_product_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `img_name` varchar(200) DEFAULT NULL COMMENT '图片名称',
  `img_url` varchar(200) DEFAULT NULL COMMENT '图片路径',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=156 DEFAULT CHARSET=utf8 COMMENT='商品图片表';

-- ----------------------------
-- Table structure for pms_product_info
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_info`;
CREATE TABLE `pms_product_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `spu_name` varchar(200) DEFAULT NULL COMMENT '商品名称',
  `description` varchar(1000) DEFAULT NULL COMMENT '商品描述(后台简述）',
  `catalog3_id` bigint(20) DEFAULT NULL COMMENT '三级分类id',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=66 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pms_product_sale_attr
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_sale_attr`;
CREATE TABLE `pms_product_sale_attr` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `sale_attr_id` bigint(20) DEFAULT NULL COMMENT '销售属性id',
  `sale_attr_name` varchar(20) DEFAULT NULL COMMENT '销售属性名称(冗余)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=101 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for pms_product_sale_attr_value
-- ----------------------------
DROP TABLE IF EXISTS `pms_product_sale_attr_value`;
CREATE TABLE `pms_product_sale_attr_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `sale_attr_id` bigint(20) DEFAULT NULL COMMENT '销售属性id',
  `sale_attr_value_name` varchar(20) DEFAULT NULL COMMENT '销售属性值名称',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=239 DEFAULT CHARSET=utf8 COMMENT='spu销售属性值';


-- ----------------------------
-- Table structure for pms_sku_attr_value
-- ----------------------------
DROP TABLE IF EXISTS `pms_sku_attr_value`;
CREATE TABLE `pms_sku_attr_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `attr_id` bigint(20) DEFAULT NULL COMMENT '属性id（冗余)',
  `value_id` bigint(20) DEFAULT NULL COMMENT '属性值id',
  `sku_id` bigint(20) DEFAULT NULL COMMENT 'skuid',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=837 DEFAULT CHARSET=utf8 COMMENT='sku平台属性值关联表';

-- ----------------------------
-- Table structure for pms_sku_image
-- ----------------------------
DROP TABLE IF EXISTS `pms_sku_image`;
CREATE TABLE `pms_sku_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '编号',
  `sku_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `img_name` varchar(200) DEFAULT NULL COMMENT '图片名称（冗余）',
  `img_url` varchar(200) DEFAULT NULL COMMENT '图片路径(冗余)',
  `product_img_id` bigint(20) DEFAULT NULL COMMENT '商品图片id',
  `is_default` varchar(4000) DEFAULT '0' COMMENT '是否默认',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1007 DEFAULT CHARSET=utf8 COMMENT='库存单元图片表';

-- ----------------------------
-- Table structure for pms_sku_info
-- ----------------------------
DROP TABLE IF EXISTS `pms_sku_info`;
CREATE TABLE `pms_sku_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '库存id(itemID)',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `price` double DEFAULT NULL COMMENT '价格',
  `sku_name` varchar(200) DEFAULT NULL COMMENT 'sku名称',
  `sku_desc` varchar(2000) DEFAULT NULL COMMENT '商品规格描述',
  `weight` double DEFAULT NULL,
  `catalog3_id` bigint(20) DEFAULT NULL COMMENT '三级分类id（冗余)',
  `sku_default_img` varchar(200) DEFAULT NULL COMMENT '默认显示图片(冗余)',
  PRIMARY KEY (`id`),
  KEY `idx_sku_info_sku_name` (`sku_name`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=106 DEFAULT CHARSET=utf8 COMMENT='库存单元表';

-- ----------------------------
-- Table structure for pms_sku_sale_attr_value
-- ----------------------------
DROP TABLE IF EXISTS `pms_sku_sale_attr_value`;
CREATE TABLE `pms_sku_sale_attr_value` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `sale_attr_id` bigint(20) DEFAULT NULL COMMENT '销售属性id（冗余)',
  `sale_attr_value_id` bigint(20) DEFAULT NULL COMMENT '销售属性值id',
  `sale_attr_name` varchar(20) DEFAULT NULL COMMENT '销售属性名称(冗余)',
  `sale_attr_value_name` varchar(20) DEFAULT NULL COMMENT '销售属性值名称(冗余)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=464 DEFAULT CHARSET=utf8 COMMENT='sku销售属性值';

-- ----------------------------
-- Table structure for ums_member
-- ----------------------------
DROP TABLE IF EXISTS `ums_member`;
CREATE TABLE `ums_member` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(64) DEFAULT NULL COMMENT '用户名',
  `password` varchar(64) DEFAULT NULL COMMENT '密码',
  `nickname` varchar(64) DEFAULT NULL COMMENT '昵称',
  `phone` varchar(64) DEFAULT NULL COMMENT '手机号码',
  `create_time` datetime DEFAULT NULL COMMENT '注册时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_username` (`username`),
  UNIQUE KEY `idx_phone` (`phone`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='会员表';


-- ----------------------------
-- Table structure for ums_member_receive_address
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_receive_address`;
CREATE TABLE `ums_member_receive_address` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `member_id` bigint(20) DEFAULT NULL,
  `name` varchar(100) DEFAULT NULL COMMENT '收货人名称',
  `phone_number` varchar(64) DEFAULT NULL,
  `post_code` varchar(100) DEFAULT NULL COMMENT '邮政编码',
  `province` varchar(100) DEFAULT NULL COMMENT '省份/直辖市',
  `city` varchar(100) DEFAULT NULL COMMENT '城市',
  `region` varchar(100) DEFAULT NULL COMMENT '区',
  `detail_address` varchar(128) DEFAULT NULL COMMENT '详细地址(街道)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COMMENT='会员收货地址表';

