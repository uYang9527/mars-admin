/*
 Navicat Premium Data Transfer

 Source Server         : localhost8.0
 Source Server Type    : MySQL
 Source Server Version : 80036
 Source Host           : localhost:3307
 Source Schema         : easy-admin-boot3

 Target Server Type    : MySQL
 Target Server Version : 80036
 File Encoding         : 65001

 Date: 23/08/2024 17:54:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address_book
-- ----------------------------
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book`  (
  `id` bigint NOT NULL COMMENT '主键',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `consignee` varchar(50) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '收货人',
  `sex` tinyint NOT NULL COMMENT '性别 0 女 1 男',
  `phone` varchar(11) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '手机号',
  `province_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省级区划编号',
  `province_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省级名称',
  `city_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '市级区划编号',
  `city_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '市级名称',
  `district_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区级区划编号',
  `district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区级名称',
  `detail` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '默认 0 否 1是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建人',
  `update_user` bigint NOT NULL COMMENT '修改人',
  `is_deleted` int NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '地址管理' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of address_book
-- ----------------------------

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint NOT NULL COMMENT '主键',
  `type` int NULL DEFAULT NULL COMMENT '类型   1 菜品分类 2 套餐分类',
  `name` varchar(64) CHARACTER SET utf8mb3 COLLATE utf8mb3_bin NOT NULL COMMENT '分类名称',
  `sort` int NOT NULL DEFAULT 0 COMMENT '顺序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint NOT NULL COMMENT '创建人',
  `update_user` bigint NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_category_name`(`name` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb3 COLLATE = utf8mb3_bin COMMENT = '菜品及套餐分类' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------

-- ----------------------------
-- Table structure for generator_table
-- ----------------------------
DROP TABLE IF EXISTS `generator_table`;
CREATE TABLE `generator_table`  (
  `table_id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '表名称',
  `table_comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '表描述',
  `class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '实体类名称',
  `camel_class_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'camel实体类名称',
  `tpl_category` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '使用的模版',
  `package_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成包路径',
  `module_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成模块名',
  `business_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成业务名',
  `function_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成功能名',
  `function_author` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成作者名',
  `type` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成方式(0 zip压缩包；1 自定义路径)',
  `options` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其他参数',
  `parent_menu_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '上级菜单id',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生成路径',
  `path_api` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'api生成路径',
  `path_web` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'web生成路径',
  `menu_init_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否自动创建菜单路由（1 是）',
  `btn_permission_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否自动创建按钮权限 (1 是)',
  `has_import` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否支持导入(1 是)',
  `has_export` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '1' COMMENT '是否支持导出(1 是)',
  `is_autofill` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0',
  `generate_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'all' COMMENT '生成类型（全量：all，后端：server，接口：service）',
  `create_id` int NULL DEFAULT NULL COMMENT '创建者编号',
  `update_id` int NULL DEFAULT NULL COMMENT '更新者编号',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`table_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Generator Table' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of generator_table
-- ----------------------------
INSERT INTO `generator_table` VALUES (2, 'address_book', '地址管理', 'AddressBook', 'addressBook', 'crud', 'com.mars.easy.admin.modules.business', 'addressbook', 'addressBook', '地址管理', 'Mars', '0', '', '0', '/', 'D:\\java-project3\\easy-admin-boot3', 'D:\\java-project3\\easy-admin-boot3\\easy-admin-web', '1', '1', '1', '1', '0', 'server', 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table` VALUES (3, 'category', '菜品及套餐分类', 'Category', 'category', 'crud', 'com.mars.easy.admin.modules.business', 'category', 'category', '菜品及套餐分类', 'Mars', '0', '', '0', '/', 'D:\\java-project3\\easy-admin-boot3', 'D:\\java-project3\\easy-admin-boot3\\easy-admin-web', '1', '1', '1', '1', '0', 'all', 1, 1, '2024-08-22 16:21:59', '2024-08-22 16:23:22');

-- ----------------------------
-- Table structure for generator_table_column
-- ----------------------------
DROP TABLE IF EXISTS `generator_table_column`;
CREATE TABLE `generator_table_column`  (
  `column_id` int NOT NULL AUTO_INCREMENT COMMENT '编号',
  `table_id` int NULL DEFAULT NULL COMMENT '归属表编号',
  `column_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列名称',
  `column_comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列描述',
  `column_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '列类型',
  `java_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA类型',
  `search_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '搜索类型',
  `ts_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'ts类型',
  `java_type_package` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'java类型包名',
  `java_field` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'JAVA字段名',
  `up_camel_field` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'get开头的驼峰字段名',
  `is_pk` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否主键（1是）',
  `is_increment` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否自增（1是）',
  `is_required` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否必填（1是）',
  `is_insert` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否为插入字段（1是）',
  `is_edit` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否编辑字段（1是）',
  `is_list` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否列表字段（1是）',
  `is_query` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否查询字段（1是）',
  `is_import` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否导入字段(1 是)',
  `is_export` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否导出字段(1 是)',
  `is_autofill` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否自动填充(1 是)',
  `is_unique_valid` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否进行唯一校验(1 是)',
  `autofill_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '自动填充类型',
  `query_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '查询方式',
  `html_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '显示类型',
  `dict_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '字典类型',
  `is_logic_del` varchar(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '0' COMMENT '是否逻辑删除(1 是)',
  `options` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '其他设置',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_id` int NULL DEFAULT NULL COMMENT '创建者编号',
  `update_id` int NULL DEFAULT NULL COMMENT '更新者编号',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`column_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 43 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'Generator Table Column' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of generator_table_column
-- ----------------------------
INSERT INTO `generator_table_column` VALUES (16, 2, 'id', '主键', 'bigint', 'Long', 'input-number', 'number', '', 'id', 'Id', '1', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '', 'EQ', 'input-number', '', '0', '', 1, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (17, 2, 'user_id', '用户id', 'bigint', 'Integer', 'input-number', 'number', '', 'userId', 'UserId', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input-number', '', '0', '', 2, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (18, 2, 'consignee', '收货人', 'varchar(50)', 'String', 'input', 'string', '', 'consignee', 'Consignee', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input', '', '0', '', 3, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (19, 2, 'sex', '性别 0 女 1 男', 'tinyint', 'Integer', 'select', 'number', '', 'sex', 'Sex', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'select', '', '0', '', 4, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (20, 2, 'phone', '手机号', 'varchar(11)', 'String', 'input', 'string', '', 'phone', 'Phone', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input', '', '0', '', 5, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (21, 2, 'province_code', '省级区划编号', 'varchar(12)', 'String', 'input', 'string', '', 'provinceCode', 'ProvinceCode', '0', '0', '0', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input', '', '0', '', 6, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (22, 2, 'province_name', '省级名称', 'varchar(32)', 'String', 'input', 'string', '', 'provinceName', 'ProvinceName', '0', '0', '0', '1', '1', '1', '1', '1', '1', '0', '0', '', 'LIKE', 'input', '', '0', '', 7, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (23, 2, 'city_code', '市级区划编号', 'varchar(12)', 'String', 'input', 'string', '', 'cityCode', 'CityCode', '0', '0', '0', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input', '', '0', '', 8, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (24, 2, 'city_name', '市级名称', 'varchar(32)', 'String', 'input', 'string', '', 'cityName', 'CityName', '0', '0', '0', '1', '1', '1', '1', '1', '1', '0', '0', '', 'LIKE', 'input', '', '0', '', 9, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (25, 2, 'district_code', '区级区划编号', 'varchar(12)', 'String', 'input', 'string', '', 'districtCode', 'DistrictCode', '0', '0', '0', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input', '', '0', '', 10, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (26, 2, 'district_name', '区级名称', 'varchar(32)', 'String', 'input', 'string', '', 'districtName', 'DistrictName', '0', '0', '0', '1', '1', '1', '1', '1', '1', '0', '0', '', 'LIKE', 'input', '', '0', '', 11, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (27, 2, 'detail', '详细地址', 'varchar(200)', 'String', 'input', 'string', '', 'detail', 'Detail', '0', '0', '0', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input', '', '0', '', 12, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (28, 2, 'label', '标签', 'varchar(100)', 'String', 'input', 'string', '', 'label', 'Label', '0', '0', '0', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input', '', '0', '', 13, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (29, 2, 'is_default', '默认 0 否 1是', 'tinyint(1)', 'Integer', 'input-number', 'number', '', 'isDefault', 'IsDefault', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input-number', '', '0', '', 14, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (30, 2, 'create_time', '创建时间', 'datetime', 'LocalDateTime', 'datetime', 'string', 'java.time.LocalDateTime', 'createTime', 'CreateTime', '0', '0', '1', '0', '0', '0', '0', '0', '0', '1', '0', 'FieldFill.INSERT', 'BETWEEN', 'datetime', '', '0', '', 15, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (31, 2, 'update_time', '更新时间', 'datetime', 'LocalDateTime', 'datetime', 'string', 'java.time.LocalDateTime', 'updateTime', 'UpdateTime', '0', '0', '1', '0', '0', '0', '0', '0', '0', '1', '0', 'FieldFill.UPDATE', 'BETWEEN', 'datetime', '', '0', '', 16, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (32, 2, 'create_user', '创建人', 'bigint', 'Integer', 'input-number', 'number', '', 'createUser', 'CreateUser', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input-number', '', '0', '', 17, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (33, 2, 'update_user', '修改人', 'bigint', 'Integer', 'input-number', 'number', '', 'updateUser', 'UpdateUser', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input-number', '', '0', '', 18, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (34, 2, 'is_deleted', '是否删除', 'int', 'Integer', 'input-number', 'number', '', 'isDeleted', 'IsDeleted', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input-number', '', '0', '', 19, 1, 1, '2024-08-22 16:09:16', '2024-08-22 16:21:17');
INSERT INTO `generator_table_column` VALUES (35, 3, 'id', '主键', 'bigint', 'Long', 'input-number', 'number', '', 'id', 'Id', '1', '0', '0', '0', '1', '1', '0', '0', '0', '0', '0', '', 'EQ', 'input-number', '', '0', '', 1, 1, 1, '2024-08-22 16:21:59', '2024-08-22 16:23:22');
INSERT INTO `generator_table_column` VALUES (36, 3, 'type', '类型   1 菜品分类 2 套餐分类', 'int', 'Integer', 'select', 'number', '', 'type', 'Type', '0', '0', '0', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'select', '', '0', '', 2, 1, 1, '2024-08-22 16:21:59', '2024-08-22 16:23:22');
INSERT INTO `generator_table_column` VALUES (37, 3, 'name', '分类名称', 'varchar(64)', 'String', 'input', 'string', '', 'name', 'Name', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '', 'LIKE', 'input', '', '0', '', 3, 1, 1, '2024-08-22 16:21:59', '2024-08-22 16:23:22');
INSERT INTO `generator_table_column` VALUES (38, 3, 'sort', '顺序', 'int', 'Integer', 'input-number', 'number', '', 'sort', 'Sort', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input-number', '', '0', '', 4, 1, 1, '2024-08-22 16:21:59', '2024-08-22 16:23:22');
INSERT INTO `generator_table_column` VALUES (39, 3, 'create_time', '创建时间', 'datetime', 'LocalDateTime', 'datetime', 'string', 'java.time.LocalDateTime', 'createTime', 'CreateTime', '0', '0', '1', '0', '0', '0', '0', '0', '0', '1', '0', 'FieldFill.INSERT', 'BETWEEN', 'datetime', '', '0', '', 5, 1, 1, '2024-08-22 16:21:59', '2024-08-22 16:23:22');
INSERT INTO `generator_table_column` VALUES (40, 3, 'update_time', '更新时间', 'datetime', 'LocalDateTime', 'datetime', 'string', 'java.time.LocalDateTime', 'updateTime', 'UpdateTime', '0', '0', '1', '0', '0', '0', '0', '0', '0', '1', '0', 'FieldFill.UPDATE', 'BETWEEN', 'datetime', '', '0', '', 6, 1, 1, '2024-08-22 16:21:59', '2024-08-22 16:23:22');
INSERT INTO `generator_table_column` VALUES (41, 3, 'create_user', '创建人', 'bigint', 'Integer', 'input-number', 'number', '', 'createUser', 'CreateUser', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input-number', '', '0', '', 7, 1, 1, '2024-08-22 16:22:00', '2024-08-22 16:23:22');
INSERT INTO `generator_table_column` VALUES (42, 3, 'update_user', '修改人', 'bigint', 'Integer', 'input-number', 'number', '', 'updateUser', 'UpdateUser', '0', '0', '1', '1', '1', '1', '1', '1', '1', '0', '0', '', 'EQ', 'input-number', '', '0', '', 8, 1, 1, '2024-08-22 16:22:00', '2024-08-22 16:23:22');

-- ----------------------------
-- Table structure for mini_user
-- ----------------------------
DROP TABLE IF EXISTS `mini_user`;
CREATE TABLE `mini_user`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `sys_user_id` int NULL DEFAULT NULL COMMENT '关联的系统用户ID',
  `openid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '小程序用户的唯一标识',
  `unionid` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '公众号的唯一标识',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '真实姓名',
  `phone` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar_url` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '用户头像URL',
  `subscribe` tinyint NULL DEFAULT NULL COMMENT '是否订阅公众号（1是0否）',
  `sex` tinyint NULL DEFAULT NULL COMMENT '性别，0-未知 1-男性，2-女性',
  `del_flag` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'F' COMMENT '删除标识',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `index_openid`(`openid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '小程序用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of mini_user
-- ----------------------------

-- ----------------------------
-- Table structure for sys_client
-- ----------------------------
DROP TABLE IF EXISTS `sys_client`;
CREATE TABLE `sys_client`  (
  `client_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '客户端id',
  `client_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '客户端key',
  `client_secret` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '客户端秘钥',
  `grant_type_cd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'password' COMMENT '授权类型',
  `device_type_cd` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '设备类型',
  `active_timeout` int NULL DEFAULT 1800 COMMENT 'token活跃超时时间',
  `timeout` int NULL DEFAULT 604800 COMMENT 'token固定超时',
  `client_status_cd` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT '0' COMMENT '状态（正常 禁用）',
  `del_flag` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'F' COMMENT '删除标志',
  `create_dept` bigint NULL DEFAULT NULL COMMENT '创建部门',
  `create_id` bigint NULL DEFAULT NULL COMMENT '创建者',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_id` bigint NULL DEFAULT NULL COMMENT '更新者',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `remark` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注',
  `is_lock` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT 'F' COMMENT '是否锁定',
  PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin COMMENT = '系统授权表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_client
-- ----------------------------
INSERT INTO `sys_client` VALUES ('195da9fcce574852b850068771cde034', 'easy-admin', '839ce050d3814949af9b2e1f815bc620', 'password', '1004001', 86400, 604800, '1003001', 'F', NULL, 1, '2024-01-22 13:43:51', 1, '2024-04-12 16:06:49', '演示client，禁止删除', 'T');

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数名',
  `config_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数key',
  `config_value` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '参数value',
  `is_lock` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'F' COMMENT '是否锁定',
  `create_id` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT NULL,
  `update_id` int NULL DEFAULT NULL,
  `update_time` datetime NULL DEFAULT NULL,
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '参数配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '主体名称', 'sys.dept.entityName', '源码字节科技有限公司', 'T', 1, '2024-03-22 10:42:46', 1, '2024-08-22 16:59:31', '公司主体名称');
INSERT INTO `sys_config` VALUES (2, '系统账户-初始密码', 'sys.user.initPwd', '123456', 'T', 1, '2024-04-10 09:56:58', 1, '2024-08-22 16:59:15', '');
INSERT INTO `sys_config` VALUES (3, '密码错误尝试次数限制', 'sys.pwd.errCnt', '5', 'T', 1, '2024-06-05 20:40:21', 1, '2024-06-05 20:50:11', '一段时间内的密码最大错误次数');
INSERT INTO `sys_config` VALUES (4, '密码错误冻结时间（分）', 'sys_pwd.lockTime', '30', 'T', 1, '2024-06-05 20:42:22', 1, '2024-06-05 20:43:30', '时间到期后自动解冻');
INSERT INTO `sys_config` VALUES (5, '业务字典起始号段', 'sys.dict.startNo', '2000', 'T', 1, '2024-07-08 17:29:16', NULL, NULL, '业务字典起始号段。1000作为默认的系统字典号段。');

-- ----------------------------
-- Table structure for sys_data_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_role`;
CREATE TABLE `sys_data_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `data_scope_cd` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '数据权限，data_scope字典',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '简介',
  `del_flag` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'F' COMMENT '删除与否',
  `is_lock` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'F' COMMENT '是否锁定',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `create_id` int NULL DEFAULT NULL COMMENT '创建人',
  `update_id` int NULL DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统数据角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_data_role
-- ----------------------------
INSERT INTO `sys_data_role` VALUES (1, '教师统计-本部门及以下', '1006002', '', 'F', 'T', '2024-07-15 15:35:05', '2024-07-15 16:57:19', 1, 1);
INSERT INTO `sys_data_role` VALUES (2, '教师统计-仅本部门', '1006003', '', 'F', 'T', '2024-07-15 15:36:03', NULL, 1, NULL);
INSERT INTO `sys_data_role` VALUES (3, '教师统计-仅本人', '1006004', '', 'F', 'T', '2024-07-15 15:36:46', NULL, 1, NULL);
INSERT INTO `sys_data_role` VALUES (4, '教师统计-自定义', '1006005', '', 'F', 'T', '2024-07-15 15:37:27', NULL, 1, NULL);

-- ----------------------------
-- Table structure for sys_data_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_role_menu`;
CREATE TABLE `sys_data_role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL COMMENT 'sys_data_role_id （数据角色表）',
  `menu_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sys_menu_id （菜单表）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 73 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统数据角色-菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_data_role_menu
-- ----------------------------
INSERT INTO `sys_data_role_menu` VALUES (69, 2, '85b54322630f43a39296488a5e76ba16');
INSERT INTO `sys_data_role_menu` VALUES (70, 3, '85b54322630f43a39296488a5e76ba16');
INSERT INTO `sys_data_role_menu` VALUES (71, 4, '85b54322630f43a39296488a5e76ba16');
INSERT INTO `sys_data_role_menu` VALUES (72, 1, '85b54322630f43a39296488a5e76ba16');

-- ----------------------------
-- Table structure for sys_data_role_relation
-- ----------------------------
DROP TABLE IF EXISTS `sys_data_role_relation`;
CREATE TABLE `sys_data_role_relation`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL COMMENT 'sys_data_role_id （数据角色表）',
  `relation_type_cd` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '关联类型，data_scope_relation_type',
  `relation_id` int NULL DEFAULT NULL COMMENT '关联表id，联动relation_type_cd',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 74 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统数据角色-关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_data_role_relation
-- ----------------------------
INSERT INTO `sys_data_role_relation` VALUES (69, 2, '1007001', 15);
INSERT INTO `sys_data_role_relation` VALUES (70, 3, '1007001', 15);
INSERT INTO `sys_data_role_relation` VALUES (71, 4, '1007002', 5);
INSERT INTO `sys_data_role_relation` VALUES (72, 4, '1007002', 3);
INSERT INTO `sys_data_role_relation` VALUES (73, 1, '1007001', 4);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '部门名称',
  `pid` int NOT NULL COMMENT '父级id',
  `deep` int NULL DEFAULT NULL COMMENT '层级',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `has_children` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'F' COMMENT '是否有子级',
  `is_lock` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'F' COMMENT '是否锁定',
  `del_flag` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'F' COMMENT '删除标识',
  `remark` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_id` int NULL DEFAULT NULL COMMENT '创建人',
  `update_id` int NULL DEFAULT NULL COMMENT '更新人',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, '火星科技有限公司', 0, 1, 100, 'T', 'F', 'F', '', 1, 1, '2024-05-10 21:40:03', '2024-08-22 16:54:47');
INSERT INTO `sys_dept` VALUES (2, '运营部', 1, 2, 200, 'T', 'F', 'F', '', 1, 1, '2024-05-10 21:40:13', '2024-08-22 16:54:02');
INSERT INTO `sys_dept` VALUES (3, '财务部', 1, 2, 300, 'T', 'F', 'F', '', 1, 1, '2024-05-10 21:40:19', '2024-08-22 16:53:52');
INSERT INTO `sys_dept` VALUES (4, '研发团队', 1, 4, 100, 'T', 'F', 'T', '', 1, 1, '2024-05-10 21:40:29', '2024-05-11 14:30:38');
INSERT INTO `sys_dept` VALUES (5, '测试团队', 1, 2, 200, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:40:36', NULL);
INSERT INTO `sys_dept` VALUES (6, '运维团队', 1, 2, 300, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:40:46', NULL);
INSERT INTO `sys_dept` VALUES (7, '直播运营', 2, 3, 100, 'T', 'F', 'F', '', 1, 1, '2024-05-10 21:41:06', '2024-08-22 16:51:02');
INSERT INTO `sys_dept` VALUES (8, '用户运营', 2, 2, 200, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:41:34', NULL);
INSERT INTO `sys_dept` VALUES (9, '会计团队', 3, 2, 100, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:41:49', NULL);
INSERT INTO `sys_dept` VALUES (10, '审计团队', 3, 2, 200, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:42:03', NULL);
INSERT INTO `sys_dept` VALUES (11, '人力资源部', 0, 1, 400, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:42:19', NULL);
INSERT INTO `sys_dept` VALUES (12, '销售部', 0, 1, 500, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:42:27', NULL);
INSERT INTO `sys_dept` VALUES (13, '法务部', 0, 1, 600, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:42:37', NULL);
INSERT INTO `sys_dept` VALUES (14, '行政部', 0, 1, 700, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:42:43', NULL);
INSERT INTO `sys_dept` VALUES (15, '移动组', 4, 3, 100, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:43:28', NULL);
INSERT INTO `sys_dept` VALUES (16, '算法组', 4, 3, 200, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:43:36', NULL);
INSERT INTO `sys_dept` VALUES (17, '前端组', 4, 3, 300, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:43:44', NULL);
INSERT INTO `sys_dept` VALUES (18, '后端组', 4, 4, 400, 'T', 'F', 'T', '', 1, 1, '2024-05-10 21:43:53', '2024-05-10 21:44:12');
INSERT INTO `sys_dept` VALUES (19, '架构组', 4, 3, 500, 'F', 'F', 'T', NULL, 1, NULL, '2024-05-10 21:44:04', NULL);
INSERT INTO `sys_dept` VALUES (20, '销售部', 1, 2, 300, 'F', 'F', 'F', NULL, 1, NULL, '2024-08-22 16:54:15', NULL);
INSERT INTO `sys_dept` VALUES (21, '教培部', 1, 2, 400, 'F', 'F', 'F', NULL, 1, NULL, '2024-08-22 16:54:30', NULL);
INSERT INTO `sys_dept` VALUES (22, '总经办', 1, 2, 500, 'F', 'F', 'F', NULL, 1, NULL, '2024-08-22 16:54:47', NULL);

-- ----------------------------
-- Table structure for sys_dept_closure
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_closure`;
CREATE TABLE `sys_dept_closure`  (
  `ancestor_id` int NOT NULL COMMENT '祖先节点ID',
  `descendant_id` int NOT NULL COMMENT '后代节点ID',
  `depth` int NOT NULL COMMENT '祖先节点到后代节点的距离'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门祖籍关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept_closure
-- ----------------------------
INSERT INTO `sys_dept_closure` VALUES (1, 1, 0);
INSERT INTO `sys_dept_closure` VALUES (0, 1, 1);
INSERT INTO `sys_dept_closure` VALUES (2, 2, 0);
INSERT INTO `sys_dept_closure` VALUES (3, 3, 0);
INSERT INTO `sys_dept_closure` VALUES (0, 4, 2);
INSERT INTO `sys_dept_closure` VALUES (4, 4, 0);
INSERT INTO `sys_dept_closure` VALUES (1, 4, 1);
INSERT INTO `sys_dept_closure` VALUES (0, 5, 2);
INSERT INTO `sys_dept_closure` VALUES (5, 5, 0);
INSERT INTO `sys_dept_closure` VALUES (1, 5, 1);
INSERT INTO `sys_dept_closure` VALUES (0, 6, 2);
INSERT INTO `sys_dept_closure` VALUES (6, 6, 0);
INSERT INTO `sys_dept_closure` VALUES (1, 6, 1);
INSERT INTO `sys_dept_closure` VALUES (7, 7, 0);
INSERT INTO `sys_dept_closure` VALUES (2, 7, 1);
INSERT INTO `sys_dept_closure` VALUES (8, 8, 0);
INSERT INTO `sys_dept_closure` VALUES (2, 8, 1);
INSERT INTO `sys_dept_closure` VALUES (9, 9, 0);
INSERT INTO `sys_dept_closure` VALUES (3, 9, 1);
INSERT INTO `sys_dept_closure` VALUES (10, 10, 0);
INSERT INTO `sys_dept_closure` VALUES (3, 10, 1);
INSERT INTO `sys_dept_closure` VALUES (11, 11, 0);
INSERT INTO `sys_dept_closure` VALUES (0, 11, 1);
INSERT INTO `sys_dept_closure` VALUES (12, 12, 0);
INSERT INTO `sys_dept_closure` VALUES (0, 12, 1);
INSERT INTO `sys_dept_closure` VALUES (13, 13, 0);
INSERT INTO `sys_dept_closure` VALUES (0, 13, 1);
INSERT INTO `sys_dept_closure` VALUES (14, 14, 0);
INSERT INTO `sys_dept_closure` VALUES (0, 14, 1);
INSERT INTO `sys_dept_closure` VALUES (0, 15, 3);
INSERT INTO `sys_dept_closure` VALUES (1, 15, 2);
INSERT INTO `sys_dept_closure` VALUES (15, 15, 0);
INSERT INTO `sys_dept_closure` VALUES (4, 15, 1);
INSERT INTO `sys_dept_closure` VALUES (0, 16, 3);
INSERT INTO `sys_dept_closure` VALUES (1, 16, 2);
INSERT INTO `sys_dept_closure` VALUES (16, 16, 0);
INSERT INTO `sys_dept_closure` VALUES (4, 16, 1);
INSERT INTO `sys_dept_closure` VALUES (0, 17, 3);
INSERT INTO `sys_dept_closure` VALUES (1, 17, 2);
INSERT INTO `sys_dept_closure` VALUES (17, 17, 0);
INSERT INTO `sys_dept_closure` VALUES (4, 17, 1);
INSERT INTO `sys_dept_closure` VALUES (0, 18, 3);
INSERT INTO `sys_dept_closure` VALUES (1, 18, 2);
INSERT INTO `sys_dept_closure` VALUES (18, 18, 0);
INSERT INTO `sys_dept_closure` VALUES (4, 18, 1);
INSERT INTO `sys_dept_closure` VALUES (0, 19, 3);
INSERT INTO `sys_dept_closure` VALUES (1, 19, 2);
INSERT INTO `sys_dept_closure` VALUES (19, 19, 0);
INSERT INTO `sys_dept_closure` VALUES (4, 19, 1);
INSERT INTO `sys_dept_closure` VALUES (0, 3, 2);
INSERT INTO `sys_dept_closure` VALUES (1, 3, 1);
INSERT INTO `sys_dept_closure` VALUES (0, 9, 3);
INSERT INTO `sys_dept_closure` VALUES (1, 9, 2);
INSERT INTO `sys_dept_closure` VALUES (0, 10, 3);
INSERT INTO `sys_dept_closure` VALUES (1, 10, 2);
INSERT INTO `sys_dept_closure` VALUES (0, 2, 2);
INSERT INTO `sys_dept_closure` VALUES (1, 2, 1);
INSERT INTO `sys_dept_closure` VALUES (0, 7, 3);
INSERT INTO `sys_dept_closure` VALUES (1, 7, 2);
INSERT INTO `sys_dept_closure` VALUES (0, 8, 3);
INSERT INTO `sys_dept_closure` VALUES (1, 8, 2);
INSERT INTO `sys_dept_closure` VALUES (0, 20, 2);
INSERT INTO `sys_dept_closure` VALUES (20, 20, 0);
INSERT INTO `sys_dept_closure` VALUES (1, 20, 1);
INSERT INTO `sys_dept_closure` VALUES (0, 21, 2);
INSERT INTO `sys_dept_closure` VALUES (21, 21, 0);
INSERT INTO `sys_dept_closure` VALUES (1, 21, 1);
INSERT INTO `sys_dept_closure` VALUES (0, 22, 2);
INSERT INTO `sys_dept_closure` VALUES (22, 22, 0);
INSERT INTO `sys_dept_closure` VALUES (1, 22, 1);

-- ----------------------------
-- Table structure for sys_dept_leader
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept_leader`;
CREATE TABLE `sys_dept_leader`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `dept_id` int NULL DEFAULT NULL,
  `leader_id` int NULL DEFAULT NULL COMMENT 'sys_user_id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '部门领导人表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dept_leader
-- ----------------------------

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` int NOT NULL COMMENT '字典id(规则)',
  `sys_dict_type_id` int NOT NULL COMMENT '关联sys_dict_type id',
  `code_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典名称',
  `alias` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典（Key）别名，某些情况下如果不想使用id作为key',
  `sort` int NOT NULL COMMENT '排序(正序)',
  `callback_show_style` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '回显样式',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '备注',
  `is_lock` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'F' COMMENT '是否锁定',
  `is_show` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'T' COMMENT '是否展示',
  `del_flag` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'F' COMMENT '是否删除',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `delete_time` datetime NULL DEFAULT NULL,
  `create_id` int NULL DEFAULT NULL,
  `update_id` int NULL DEFAULT NULL,
  `delete_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1000001, 1000, '正常', '', 1, 'success', '', 'F', 'T', 'F', '2023-08-20 16:30:23', '2024-04-12 15:58:41', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1000002, 1000, '禁用', '', 2, 'info', '', 'F', 'T', 'F', '2023-08-20 16:33:45', '2024-04-12 15:58:41', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1000003, 1000, '禁言', '', 3, 'info', '', 'F', 'T', 'F', '2023-08-20 16:33:54', '2024-04-12 15:58:41', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1001001, 1001, '测试用户', '', 0, 'info', '', 'T', 'T', 'F', '2023-08-20 16:38:58', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1001002, 1001, '超级管理员', '', 0, 'info', '', 'T', 'T', 'F', '2023-08-20 16:39:05', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1001003, 1001, '普通用户', '', 0, 'info', '', 'T', 'T', 'F', '2023-08-20 16:39:11', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1002001, 1002, '目录', '', 1, 'warning', '', 'T', 'T', 'F', '2023-08-21 11:23:05', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1002002, 1002, '菜单', '', 2, 'success', '', 'T', 'T', 'F', '2023-08-21 11:23:17', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1002003, 1002, '按钮', '', 3, 'danger', '', 'T', 'T', 'F', '2023-08-21 11:23:22', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1003001, 1003, '正常', '', 1, 'success', '', 'F', 'T', 'F', '2024-01-22 09:44:52', '2024-04-12 15:58:41', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1003002, 1003, '禁用', '', 2, 'info', '', 'F', 'T', 'F', '2024-01-22 09:45:16', '2024-04-12 15:58:41', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1004001, 1004, 'PC', '', 1, 'success', 'pc端', 'F', 'T', 'F', '2024-01-22 10:03:19', '2024-04-12 15:58:41', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1004002, 1004, '小程序', '', 2, 'success', '小程序端', 'F', 'T', 'F', '2024-01-22 10:03:47', '2024-04-12 15:58:41', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1004003, 1004, 'Androd', '', 3, 'success', '', 'F', 'T', 'F', '2024-01-22 10:04:35', '2024-04-12 15:58:41', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1004004, 1004, 'IOS', '', 4, 'success', '', 'F', 'T', 'F', '2024-01-22 10:04:42', '2024-04-12 15:58:41', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1005001, 1005, '密码认证', 'password', 100, 'success', '', 'T', 'T', 'F', '2024-01-22 10:20:32', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1005002, 1005, '小程序认证', 'applet', 300, 'success', '', 'F', 'T', 'F', '2024-01-22 10:20:40', '2024-04-12 16:51:58', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1005003, 1005, '三方认证', 'third', 400, 'success', '', 'F', 'T', 'F', '2024-01-22 10:20:51', '2024-04-12 16:51:49', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1005004, 1005, '短信认证', 'sms', 200, 'success', '', 'F', 'T', 'F', '2024-01-22 10:20:57', '2024-04-12 16:51:41', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1006001, 1006, '全部', '', 1, 'primary', '', 'T', 'T', 'F', '2024-06-25 18:55:48', '2024-06-25 19:11:28', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1006002, 1006, '本部门及以下', '', 2, 'primary', '', 'T', 'T', 'F', '2024-06-25 18:56:57', '2024-06-25 19:11:29', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1006003, 1006, '仅本部门', '', 3, 'primary', '', 'T', 'T', 'F', '2024-06-25 18:57:22', '2024-06-25 19:11:32', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1006004, 1006, '仅本人', '', 4, 'primary', '', 'T', 'T', 'F', '2024-06-25 18:57:57', '2024-06-25 19:11:34', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1006005, 1006, '自定义', '', 5, 'primary', '', 'T', 'T', 'F', '2024-06-25 18:58:11', '2024-06-25 19:11:36', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1007001, 1007, '部门权限', '', 1, 'primary', '', 'T', 'T', 'F', '2024-06-25 18:59:00', '2024-06-25 19:11:38', NULL, NULL, NULL, NULL);
INSERT INTO `sys_dict` VALUES (1007002, 1007, '个人权限', '', 2, 'primary', '个人权限高优先级', 'T', 'T', 'F', '2024-06-25 18:59:27', '2024-06-25 19:11:41', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_dict_type
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '字典类型id',
  `type_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '字典类型名(中文)',
  `type_code` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '字典类型码(英文)',
  `is_lock` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'F' COMMENT '是否锁定，锁定的属性无法在页面进行修改',
  `is_show` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'T' COMMENT '显示与否',
  `del_flag` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'F' COMMENT '删除与否',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '描述',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `delete_time` datetime NULL DEFAULT NULL,
  `create_id` int NULL DEFAULT NULL,
  `update_id` int NULL DEFAULT NULL,
  `delete_id` int NULL DEFAULT NULL,
  `type` enum('system','business') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'business' COMMENT '字典类型: system 系统, business 业务',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1008 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '字典类型' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_dict_type
-- ----------------------------
INSERT INTO `sys_dict_type` VALUES (1000, '账户状态', 'account_status', 'T', 'T', 'F', '', '2023-08-20 11:09:46', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL, 'system');
INSERT INTO `sys_dict_type` VALUES (1001, '用户标签', 'user_tag', 'T', 'T', 'F', '', '2023-08-20 14:22:40', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL, 'system');
INSERT INTO `sys_dict_type` VALUES (1002, '菜单类型', 'menu_type', 'T', 'T', 'F', '', '2023-08-21 11:20:47', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL, 'system');
INSERT INTO `sys_dict_type` VALUES (1003, '授权状态', 'sys_client_status', 'T', 'T', 'F', 'client授权状态', '2023-08-22 09:44:27', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL, 'system');
INSERT INTO `sys_dict_type` VALUES (1004, '设备类型', 'device_type', 'T', 'T', 'F', '', '2023-08-22 10:02:11', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL, 'system');
INSERT INTO `sys_dict_type` VALUES (1005, '授权类型', 'grant_type', 'T', 'T', 'F', '', '2023-08-22 10:15:58', '2024-08-21 01:48:56', NULL, NULL, NULL, NULL, 'system');
INSERT INTO `sys_dict_type` VALUES (1006, '数据权限', 'data_scope', 'T', 'T', 'F', '', '2024-06-25 18:54:21', '2024-06-25 19:12:46', NULL, NULL, NULL, NULL, 'system');
INSERT INTO `sys_dict_type` VALUES (1007, '数据权限关联类型', 'data_scope_relation_type', 'T', 'T', 'F', '自定义数据权限的关联类型', '2024-06-25 18:55:37', '2024-06-25 19:12:48', NULL, NULL, NULL, NULL, 'system');

-- ----------------------------
-- Table structure for sys_export_info
-- ----------------------------
DROP TABLE IF EXISTS `sys_export_info`;
CREATE TABLE `sys_export_info`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `file_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '导出的文件名称',
  `export_status_cd` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '导出状态,关联字典表export_status',
  `create_id` int NULL DEFAULT NULL,
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '导出信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_export_info
-- ----------------------------

-- ----------------------------
-- Table structure for sys_file
-- ----------------------------
DROP TABLE IF EXISTS `sys_file`;
CREATE TABLE `sys_file`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `filename` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '文件名',
  `type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '类型',
  `size` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '文件大小',
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '文件域名',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '文件表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_file
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单表id',
  `pid` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '父级id',
  `path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '路径',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '路由名称',
  `title` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '标题',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT 'icon图标',
  `component` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '组件路径',
  `redirect` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '外链地址',
  `sort` int NOT NULL COMMENT '排序',
  `deep` int NOT NULL COMMENT '层级',
  `menu_type_cd` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '菜单类型 （字典表menu_type）',
  `permissions` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '按钮权限',
  `is_hidden` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'F' COMMENT '是否隐藏',
  `has_children` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'F' COMMENT '是否有子级',
  `is_link` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'F' COMMENT '路由外链时填写的访问地址',
  `is_full` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'F' COMMENT '菜单是否全屏',
  `is_affix` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'F' COMMENT '菜单是否固定在标签页',
  `is_keep_alive` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'F' COMMENT '当前路由是否缓存',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '创建人',
  `update_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '更新人',
  `del_flag` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'F' COMMENT '是否删除',
  `delete_id` int NULL DEFAULT NULL,
  `delete_time` datetime NULL DEFAULT NULL,
  `use_data_scope` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('006bdbacd71a481f88b6acf895529acd', '8354d626cc65487594a7c38e98de1bad', '', '', '修改', '', '', '', 200, 3, '1002003', 'sys.dept.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-03-19 15:42:48', '2024-05-10 20:54:02', '', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('012efc4ef8d24304a8562534f319524a', '0e529e8a9dbf450898b695e051c36d48', '', '', '预览按钮', '', '', '', 600, 3, '1002003', 'generator.preview', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-15 10:23:47', '2024-05-10 20:54:31', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('013aad2eb09d4bfd8c4859a8c3a3a5ed', 'd0b2e5a6c6f043ffb027f3ef73160980', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:46:19', '2024-08-22 07:46:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('05194ef5fa7a4a308a44f6f5c6791c3a', '99c2ee7b882749e597bcd62385f368fb', '', '', '编辑菜单', '', '', '', 200, 3, '1002003', 'sys.menu.update_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-08-31 15:31:35', '2024-05-10 20:41:15', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('081a3d251c604d04b30d8b9b5d0e6ed6', '59a6daac957440adaa9af5c7ecf9153d', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:54:33', '2024-08-22 07:54:33', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('0933b165ffc14d558e8de43ccb6687f6', 'c6dd479d5b304731be403d7551c60d70', '', '', '编辑角色', '', '', '', 200, 3, '1002003', 'sys.role.update_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-09-06 10:30:43', '2024-05-10 20:41:01', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('09e368e1521245c58f6cde293853ab53', '716439e6e3d64521b3d4b4540a336c2f', '', '', '导出', '', '', '', 500, 2, '1002003', 'address.book.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:09:19', '2024-08-22 08:09:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('0c532e2fb7df41d5b9f6297685a08330', '2c2fdb109cd345f6bdfee324a2dda05a', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:41:07', '2024-08-22 07:41:07', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('0e529e8a9dbf450898b695e051c36d48', 'da1b46db642f42978f83ed5eb34870ce', '/toolbox/generator', 'generator', '代码生成', 'Brush', '/toolbox/generator/index', '', 100, 2, '1002002', 'generator.list', 'F', 'T', 'F', 'F', 'F', 'F', '2023-12-08 13:50:39', '2024-08-21 02:12:43', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('0f98b89c67e54cb0bcff2b56aa98832f', '140c9ed43ef54542bbcdde8a5d928400', '', '', '新增账号', '', '', '', 100, 3, '1002003', 'sys.user.create_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-09-06 10:31:55', '2024-05-10 20:40:31', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('123bcb42d5e743cfb68884112fea2e84', '82abd51f34654b9197b6f635779ea419', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:43:01', '2024-08-22 07:43:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('13f06419e1d74bda97c0709e3ea0c8f0', 'b06f77bf931947c78cc5883b465bbd19', '', '', '导入', '', '', '', 4, 1, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-05-10 21:45:48', '2024-05-10 21:45:48', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1406275e99be4dd7bb02cbe192d031da', '053f2943b7fd4331aeaeac4b1766ff07', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:47:01', '2024-08-22 07:47:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('140c9ed43ef54542bbcdde8a5d928400', '49a022c710e6445f97ff81c601e84dba', '/system/accountManage', 'accountManage', '用户管理', 'UserFilled', '/system/accountManage/index', '', 100, 2, '1002002', 'sys.user.query_table', 'F', 'T', 'F', 'F', 'F', 'T', '2023-08-29 13:53:49', '2024-08-21 10:23:27', '8', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('170a5b7ec83a43ec877b6be40d4aaa3a', '51850917b1b64b19b8f905f38cba101d', '/addressbook/addressBook', 'AddressBookView', '地址管理', 'Bicycle', '/addressbook/addressBook/index', '', 500, 2, '1002002', 'address.book.query_table', 'F', 'T', 'F', 'F', 'F', 'F', '2024-08-22 08:14:00', '2024-08-22 08:26:13', '', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('173fcd22ca9c41428be45dea9f2099b3', '82abd51f34654b9197b6f635779ea419', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:43:01', '2024-08-22 07:43:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('17d8715223c24cfd84cd122a01ae8ec6', '5d7dd77835a9440a843841bd6e6492d9', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:03:53', '2024-08-22 08:03:53', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('17ecc7446a104723a9b7c6d3fddafa38', '2c2fdb109cd345f6bdfee324a2dda05a', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:41:07', '2024-08-22 07:41:07', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('17fc60357760477d8d3ca9619e9b87c4', 'b06f77bf931947c78cc5883b465bbd19', '', '', '修改', '', '', '', 2, 1, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-05-10 21:45:48', '2024-05-10 21:45:48', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('183602fa9bcc44bb9091baad13f2b26e', '716439e6e3d64521b3d4b4540a336c2f', '', '', '删除', '', '', '', 300, 2, '1002003', 'address.book.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:09:19', '2024-08-22 08:09:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1a86a9d2b3ca49439277fff9f499c7cd', 'dcb6aabcd910469ebf3efbc7e43282d4', '', '', '删除字典', '', '', '', 600, 3, '1002003', 'sys.dict.delete_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-04 13:58:26', '2024-05-10 20:41:45', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1ac5493f52e344b5b004270f7f0e9e14', 'ba7ca04e011048f2a5b6be1c58f632a4', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:05:21', '2024-08-22 08:05:21', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1d65a0aa0ab4487f81a0a2bf6b31ccbb', 'd0b2e5a6c6f043ffb027f3ef73160980', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:46:19', '2024-08-22 07:46:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('1df2dc480b8544919cf715160a3a5cd2', '716439e6e3d64521b3d4b4540a336c2f', '', '', '修改', '', '', '', 200, 2, '1002003', 'address.book.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:09:19', '2024-08-22 08:09:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('21fc0a3b7cf54ef6a57fffab15477aac', '170a5b7ec83a43ec877b6be40d4aaa3a', '', '', '删除', '', '', '', 300, 3, '1002003', 'address.book.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:14:00', '2024-08-22 08:26:43', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('225c153f5ef44fdebfae786137f669c9', '5d7dd77835a9440a843841bd6e6492d9', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:03:53', '2024-08-22 08:03:53', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('26ecd3786ec74a369074f84b9269bc41', 'b06f77bf931947c78cc5883b465bbd19', '', '', '新增', '', '', '', 1, 1, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-05-10 21:45:48', '2024-05-10 21:45:48', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('29d33eba6b73420287d8f7e64aea62b3', '88b2e5def2ff474fa8bf3537d4a2fe5b', '/system/configManage', 'configManage', '系统参数', 'Key', '/system/configManage/index', '', 500, 2, '1002002', 'sys.config.query_table', 'F', 'T', 'F', 'F', 'F', 'T', '2023-11-24 09:54:25', '2024-08-21 10:20:28', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('2ac65a12992042c3967bc14778b3b3bb', '053f2943b7fd4331aeaeac4b1766ff07', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:47:01', '2024-08-22 07:47:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('2aed10e3c4774d2aad1d3e8a47b58323', 'ad37751e2c9343858030352eb7d6ff47', '/dataScreen', 'dataScreen', '大屏可视化', 'CreditCard', '/dataScreen/index', '', 100, 2, '1002002', '111', 'F', 'F', 'F', 'T', 'F', 'F', '2024-08-23 09:13:36', '2024-08-23 17:24:25', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('2c94bd9c788b4233b75b4bc43127ff60', 'da1b46db642f42978f83ed5eb34870ce', '/apiDoc', 'apiDoc', '接口文档', 'DocumentCopy', '', 'http://localhost:8888/api/admin/doc.html#/home', 200, 2, '1002002', '', 'F', 'F', 'T', 'F', 'F', 'F', '2024-08-23 09:51:48', '2024-08-23 17:53:44', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('2d6b78ad03de4cf1a3899f25cd7fe0ee', '0e529e8a9dbf450898b695e051c36d48', '', '', '删除按钮', '', '', '', 400, 3, '1002003', 'generator.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-15 10:22:50', '2024-05-10 20:54:24', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('2f301d6b09a1426f956036eda953524b', 'd0b2e5a6c6f043ffb027f3ef73160980', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:46:19', '2024-08-22 07:46:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('30764235550142dbb73680468aae8de5', 'd9a559dd03c74142ae130115c2bc0135', '', '', '导入', '', '', '', 400, 2, '1002003', 'address.book.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:10:01', '2024-08-22 08:10:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('307951cd49394947b1b7f7dca2266546', '43866b83ef834b73adbd95338f9e606b', '', '', '导出', '', '', '', 5, 1, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-05-10 21:45:32', '2024-05-10 21:45:32', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('30942929802f41cc850722c78db089e7', '140c9ed43ef54542bbcdde8a5d928400', '', '', '设置数据角色', '', '', '', 800, 3, '1002003', 'sys.user.data_role_set_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2024-07-02 19:22:37', '2024-07-11 20:27:03', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('310d02bb121645d1b7a7f949f48c981b', '0e529e8a9dbf450898b695e051c36d48', '', '', '生成按钮', '', '', '', 300, 3, '1002003', 'generator.generator', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-15 10:21:47', '2024-05-10 20:54:20', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('31ade914b61e477ab10902d6d2f9ea82', 'd31e0415568346d8931e996107a5a35f', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:52:35', '2024-08-22 07:52:35', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('320e8a75871c465c84eaefea7c4df1c0', 'c5090b9c5ceb47ec9028039e3485b61e', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:40:22', '2024-08-22 07:40:22', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('330a1a0a857c4ad1a95327db5134e420', '140c9ed43ef54542bbcdde8a5d928400', '', '', '解锁', '', '', '', 500, 3, '1002003', 'sys.user.unlock_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-29 14:06:49', '2024-05-10 20:40:49', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('3327c6d5ac244b40884463f0c232f4dc', '3edd396d3c234b66932aad46bd23cae8', '', '', '新增', '', '', '', 100, 2, '1002003', 'address.book.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:10:19', '2024-08-22 08:10:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('361b7f5afed84fd09e7fd94e0e39f62c', 'd0b2e5a6c6f043ffb027f3ef73160980', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:46:19', '2024-08-22 07:46:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('3788696d17a848fe81d004ef8627c2fe', '053f2943b7fd4331aeaeac4b1766ff07', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:47:01', '2024-08-22 07:47:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('3a54d488132b4331bf3cd5e6d86ffcf4', '29d33eba6b73420287d8f7e64aea62b3', '', '', '修改参数', '', '', '', 200, 3, '1002003', 'sys.config.update_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-11-24 09:57:38', '2024-05-10 20:42:50', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('3a97a9aabf1647af93f835696781568b', '15a90aeb4db7418c9f525ece768ec142', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:53:28', '2024-08-22 07:53:28', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('3b4e398cd8a147d8aef968041c8f9eca', 'ba7ca04e011048f2a5b6be1c58f632a4', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:05:21', '2024-08-22 08:05:21', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('3ba9407560a1490583fefa10b22bc74f', '8354d626cc65487594a7c38e98de1bad', '', '', '删除', '', '', '', 300, 3, '1002003', 'sys.dept.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-03-19 15:42:48', '2024-05-10 20:54:05', '', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('3d7eed8398d3457c897b2e8bf838e9c6', '0e529e8a9dbf450898b695e051c36d48', '', '', '编辑按钮', '', '', '', 200, 3, '1002003', 'generator.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-15 10:21:05', '2024-05-10 20:54:15', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('3de6d2f003bc40cc8ac9bfe209c9338b', '5d7dd77835a9440a843841bd6e6492d9', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:03:53', '2024-08-22 08:03:53', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('3e757380f98446e9a5c83a8ff95899c2', '7126ee0d2b534898a9c7962e8d1c8ecd', '', '', '删除', '', '', '', 300, 2, '1002003', 'address.book.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:11:23', '2024-08-22 08:11:23', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('409e0d9d9cdd4a4da15215a3cf3d08f3', 'ba7ca04e011048f2a5b6be1c58f632a4', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:05:21', '2024-08-22 08:05:21', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('421cf327b63e490d9ec386c524a4e256', 'c5090b9c5ceb47ec9028039e3485b61e', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:40:22', '2024-08-22 07:40:22', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('42b93f4bc13b42ffa7beb4f6d5f81a2a', '716439e6e3d64521b3d4b4540a336c2f', '', '', '新增', '', '', '', 100, 2, '1002003', 'address.book.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:09:19', '2024-08-22 08:09:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('43f101d9afd34f2daa1b2f47d820c31f', '577f5ad2b54043e98962bad29f0cef3e', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:06:36', '2024-08-22 08:06:36', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('4427c21387e841c4b65b3070f5737460', '59a6daac957440adaa9af5c7ecf9153d', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:54:33', '2024-08-22 07:54:33', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('445b73dda9a34ad681d2705a7abcf2f6', 'c6dd479d5b304731be403d7551c60d70', '', '', '删除角色', '', '', '', 300, 3, '1002003', 'sys.role.delete_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-09-06 10:31:05', '2024-05-10 20:41:04', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('48ae285ec8b34a759fe93c8a7c249687', '170a5b7ec83a43ec877b6be40d4aaa3a', '', '', '导入', '', '', '', 400, 3, '1002003', 'address.book.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:14:00', '2024-08-22 08:26:43', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('49a022c710e6445f97ff81c601e84dba', '0', '/power', 'power', '权限管理', 'Stamp', '', '', 1, 1, '1002001', '', 'F', 'T', 'F', 'F', 'F', 'F', '2024-08-21 02:22:42', '2024-08-21 10:44:34', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('4da4a737f7c14b7ba095aa6fae296542', '053f2943b7fd4331aeaeac4b1766ff07', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:47:01', '2024-08-22 07:47:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('4f39ef0fd2f748f6ab7d6d20d98bc4af', 'dcb6aabcd910469ebf3efbc7e43282d4', '', '', '新增字典类型', '', '', '', 100, 3, '1002003', 'sys.dict.add_type_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-08-31 15:52:38', '2024-05-10 20:41:28', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('51850917b1b64b19b8f905f38cba101d', '0', '/address', 'address', '地址管理', 'Baseball', '', '', 600, 1, '1002001', '', 'T', 'T', 'F', 'F', 'F', 'F', '2024-08-22 08:25:52', '2024-08-23 17:10:58', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('532236cbc86b45fe9f3a0e774c20d760', '170a5b7ec83a43ec877b6be40d4aaa3a', '', '', '修改', '', '', '', 200, 3, '1002003', 'address.book.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:14:00', '2024-08-22 08:26:43', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('5b0ef931e3aa49a3bee93e9bf89c5c9c', '71736f023ae142c7a6e77bba748d5aa7', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:51:27', '2024-08-22 07:51:27', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('5b33ac3d630543d09d1388fae4d13fc0', '9e731ff422184fc1be2022c5c985735e', '', '', '修改', '', '', '', 200, 3, '1002003', 'sys.client.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-01-22 10:52:09', '2024-08-22 08:44:09', '', '1', 'T', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('5b5fb3748c6a4ed5a4dda3877508c3a7', 'c6dd479d5b304731be403d7551c60d70', '', '', '设置权限', '', '', '', 400, 3, '1002003', 'sys.role.setting_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-09-06 10:31:35', '2024-05-10 20:41:07', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('5cb248e43b3b4a7ea729c58fa1063c21', 'd31e0415568346d8931e996107a5a35f', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:52:35', '2024-08-22 07:52:35', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('5f53304f035b4f65939189d310db9bff', '170a5b7ec83a43ec877b6be40d4aaa3a', '', '', '新增', '', '', '', 100, 3, '1002003', 'address.book.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:14:00', '2024-08-22 08:26:43', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('5f7e9abf825c486ab03c0627e03a0f5c', '716439e6e3d64521b3d4b4540a336c2f', '', '', '导入', '', '', '', 400, 2, '1002003', 'address.book.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:09:19', '2024-08-22 08:09:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('6068255e90a24b23bad7e7dd8464de83', '053f2943b7fd4331aeaeac4b1766ff07', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:47:01', '2024-08-22 07:47:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('60966137f6de4d80a0bc6e7781f7df12', '43866b83ef834b73adbd95338f9e606b', '', '', '修改', '', '', '', 2, 1, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-05-10 21:45:32', '2024-05-10 21:45:32', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('60b6985308e54911b86ca2cc184e1bf5', '170a5b7ec83a43ec877b6be40d4aaa3a', '', '', '导出', '', '', '', 500, 3, '1002003', 'address.book.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:14:00', '2024-08-22 08:26:43', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('63e71c4867974a86a71e3df8bcd3e2e6', '43866b83ef834b73adbd95338f9e606b', '', '', '删除', '', '', '', 3, 1, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-05-10 21:45:32', '2024-05-10 21:45:32', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('67397ce003a141e5a833578f37372cfb', '577f5ad2b54043e98962bad29f0cef3e', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:06:36', '2024-08-22 08:06:36', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('686a5522b0334d4da51aa15b3fd1a303', '140c9ed43ef54542bbcdde8a5d928400', '', '', '设置部门', '', '', '', 700, 3, '1002003', 'sys.user.dept_set_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2024-04-12 14:33:21', '2024-04-12 14:33:21', '1', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('6b5d78451f9248d5ab22c97df87bf531', '2c2fdb109cd345f6bdfee324a2dda05a', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:41:07', '2024-08-22 07:41:07', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('6c15038105da47fa9d519b9cdec7f1cf', '43866b83ef834b73adbd95338f9e606b', '', '', '新增', '', '', '', 1, 1, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-05-10 21:45:32', '2024-05-10 21:45:32', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('6c46fd01faf042fc9dd4a9c9b9ef2c5a', '9e731ff422184fc1be2022c5c985735e', '', '', '新增', '', '', '', 100, 3, '1002003', 'sys.client.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-01-22 10:52:09', '2024-08-22 08:44:05', '', '1', 'T', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('6d6e111a887944faa3c579608d7bb45c', 'ba7ca04e011048f2a5b6be1c58f632a4', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:05:21', '2024-08-22 08:05:21', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('6e25a716c1a646009a9be90b16f0a682', '140c9ed43ef54542bbcdde8a5d928400', '', '', '设置角色', '', '', '', 400, 3, '1002003', 'sys.user.role_set_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-09-06 10:33:53', '2024-05-10 20:40:46', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('7391f12ad51049c2b86d231d39708c71', '85b54322630f43a39296488a5e76ba16', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-17 10:19:32', '2024-08-21 02:14:12', '', '1', 'T', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('73d312f4fa8949ddba3d9807c0c56f00', '85b54322630f43a39296488a5e76ba16', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-17 10:19:32', '2024-08-21 02:14:15', '', '1', 'T', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('7a81ede5f1ed43cf90d368e542d63f25', 'd9a559dd03c74142ae130115c2bc0135', '', '', '删除', '', '', '', 300, 2, '1002003', 'address.book.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:10:01', '2024-08-22 08:10:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('7abbd9193f9746679e3eead86697e860', '43866b83ef834b73adbd95338f9e606b', '', '', '导入', '', '', '', 4, 1, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-05-10 21:45:32', '2024-05-10 21:45:32', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('7d16a48a55de44a4a59f80f9cdc1f10f', '577f5ad2b54043e98962bad29f0cef3e', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:06:36', '2024-08-22 08:06:36', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('7f1cd607331c4451859e389c8b74ab1b', '733bd8d9099b4cccb0f4faba9defeb7a', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-21 02:46:27', '2024-08-21 02:46:27', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('7f60993ea7a642449b2d4e13c7b01e4c', 'fdb5bb3ee6714fe0bc4ae6a1816ce141', '/category/category', 'CategoryView', '菜品及套餐分类', 'Basketball', '/category/category/index', '', 600, 2, '1002002', 'category.query_table', 'F', 'T', 'F', 'F', 'F', 'F', '2024-08-22 08:23:24', '2024-08-22 08:26:55', '', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('8061d8e79be744bf91b7b438f8e8e887', '85b54322630f43a39296488a5e76ba16', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-17 10:19:32', '2024-08-21 02:14:19', '', '1', 'T', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('81647226a2d047e8ab0b70472350ee69', 'dcb6aabcd910469ebf3efbc7e43282d4', '', '', '新增字典', '', '', '', 400, 3, '1002003', 'sys.dict.add_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-04 13:54:55', '2024-05-10 20:41:37', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('818cc6e1889d46579525ad8ab921eeb8', 'dcb6aabcd910469ebf3efbc7e43282d4', '', '', '编辑字典类型', '', '', '', 200, 3, '1002003', 'sys.dict.update_type_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-09-06 10:39:29', '2024-05-10 20:41:31', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('8255bac5eae748a0a8500167963b3e00', '140c9ed43ef54542bbcdde8a5d928400', '', '', '编辑账号', '', '', '', 200, 3, '1002003', 'sys.user.update_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-09-06 10:32:15', '2024-05-10 20:40:35', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('82f4776aaf714700802cfb6c93dffa15', 'b06f77bf931947c78cc5883b465bbd19', '', '', '导出', '', '', '', 5, 1, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-05-10 21:45:48', '2024-05-10 21:45:48', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('8354d626cc65487594a7c38e98de1bad', '49a022c710e6445f97ff81c601e84dba', '/system/deptManage', 'SysDeptView', '部门管理', 'svg-org', '/system/deptManage/index', '', 700, 2, '1002002', 'sys.dept.query_table', 'F', 'T', 'F', 'F', 'F', 'F', '2024-03-19 15:42:48', '2024-08-21 10:23:47', '', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('85b54322630f43a39296488a5e76ba16', '0', '/teacher/teacherStatistics', 'TeacherStatisticsView', '教师统计', 'svg-org', '/teacher/teacherStatistics/index', '', 300, 1, '1002002', 'teacher.statistics.query_table', 'F', 'T', 'F', 'F', 'F', 'F', '2024-02-17 10:19:32', '2024-08-21 02:14:22', '', '1', 'T', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('85e3504808b247d0b1140915a3468d2d', '15a90aeb4db7418c9f525ece768ec142', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:53:28', '2024-08-22 07:53:28', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('87a5d1a47f5a4d9bbd4e4dc6a2bc3018', '3edd396d3c234b66932aad46bd23cae8', '', '', '导入', '', '', '', 400, 2, '1002003', 'address.book.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:10:19', '2024-08-22 08:10:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('88b2e5def2ff474fa8bf3537d4a2fe5b', '0', '/system', 'system', '系统管理', 'Tools', '', '', 100, 1, '1002001', '', 'F', 'T', 'F', 'F', 'F', 'T', '2023-08-29 13:52:10', '2024-05-10 20:38:55', '8', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('8d0b8b57a58e41a5a5e840cc2b3703f4', '9e731ff422184fc1be2022c5c985735e', '', '', '删除', '', '', '', 300, 3, '1002003', 'sys.client.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-01-22 10:52:09', '2024-08-22 08:44:12', '', '1', 'T', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('8fd6721941494fd5bbe16bec82b235be', '8354d626cc65487594a7c38e98de1bad', '', '', '新增', '', '', '', 100, 3, '1002003', 'sys.dept.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-03-19 15:42:48', '2024-05-10 20:53:59', '', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('8ff96422dae94c3286b2e73237b54bfc', '15a90aeb4db7418c9f525ece768ec142', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:53:29', '2024-08-22 07:53:29', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('91c5d4b5d36a4857bb156def1e63e5bf', '7126ee0d2b534898a9c7962e8d1c8ecd', '', '', '导出', '', '', '', 500, 2, '1002003', 'address.book.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:11:23', '2024-08-22 08:11:23', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('91ccb13b5c174583803a4c492a5dfdb6', '85b54322630f43a39296488a5e76ba16', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-17 10:19:32', '2024-08-21 02:14:17', '', '1', 'T', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('9338bf2f57984825bc227bb618f9db81', '99c2ee7b882749e597bcd62385f368fb', '', '', '新增菜单', '', '', '', 100, 3, '1002003', 'sys.menu.create_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-08-31 14:27:50', '2024-05-10 20:41:13', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('96b7e630d04f462c802470333dc0b425', '82abd51f34654b9197b6f635779ea419', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:43:01', '2024-08-22 07:43:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('96b9a7de6c4149f88f060432f10467cf', '15a90aeb4db7418c9f525ece768ec142', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:53:29', '2024-08-22 07:53:29', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('97f11d74c98047ba80f011a3da9d882c', 'dcb6aabcd910469ebf3efbc7e43282d4', '', '', '编辑字典', '', '', '', 500, 3, '1002003', 'sys.dict.update_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-04 13:55:13', '2024-05-10 20:41:42', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('9830d86487184961b90fc527c9604720', 'dcb6aabcd910469ebf3efbc7e43282d4', '', '', '删除字典类型', '', '', '', 300, 3, '1002003', 'sys.dict.delete_type_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-04 13:57:33', '2024-05-10 20:41:34', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('99c2ee7b882749e597bcd62385f368fb', '49a022c710e6445f97ff81c601e84dba', '/system/menuMange', 'menuMange', '菜单管理', 'Menu', '/system/menuMange/index', '', 300, 2, '1002002', 'sys.menu.query_table', 'F', 'T', 'F', 'F', 'F', 'T', '2023-08-29 13:55:30', '2024-08-21 10:23:55', '8', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('9a41d8af17914ae4b81476ea55e4aacf', '733bd8d9099b4cccb0f4faba9defeb7a', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-21 02:46:27', '2024-08-21 02:46:27', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('9c20500f22ed4a0ea05fffab048118c8', 'd31e0415568346d8931e996107a5a35f', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:52:35', '2024-08-22 07:52:35', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('9c2a3207fdb04284b618f4bf9b479862', 'c5090b9c5ceb47ec9028039e3485b61e', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:40:22', '2024-08-22 07:40:22', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('9e731ff422184fc1be2022c5c985735e', '88b2e5def2ff474fa8bf3537d4a2fe5b', '/system/clientManage', 'ClientManageView', '客户端管理', 'Operation', '/system/clientManage/index', '', 600, 2, '1002002', 'sys.client.query_table', 'F', 'T', 'F', 'F', 'F', 'F', '2024-01-22 10:52:09', '2024-08-22 08:44:17', '', '1', 'T', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('a11a0b4be8494c03b4bb419fc3a2ebda', '3edd396d3c234b66932aad46bd23cae8', '', '', '修改', '', '', '', 200, 2, '1002003', 'address.book.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:10:19', '2024-08-22 08:10:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('a181c53b3ebc4aee86d966e944674efd', '7f60993ea7a642449b2d4e13c7b01e4c', '', '', '删除', '', '', '', 300, 3, '1002003', 'category.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:23:24', '2024-08-22 08:44:05', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('a4f2ae0611ff480e99ae0200cf6c1130', 'd9a559dd03c74142ae130115c2bc0135', '', '', '导出', '', '', '', 500, 2, '1002003', 'address.book.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:10:01', '2024-08-22 08:10:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('a57f93e9667e4985898ef4efd69225b3', '2c2fdb109cd345f6bdfee324a2dda05a', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:41:07', '2024-08-22 07:41:07', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('a5b673af36e74652947efd574ba2e87a', '82abd51f34654b9197b6f635779ea419', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:43:01', '2024-08-22 07:43:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('a845e977c64b4058928e726d74f8542c', 'd31e0415568346d8931e996107a5a35f', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:52:35', '2024-08-22 07:52:35', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('a982fc3dcd984cd78e001862794727f9', '7f60993ea7a642449b2d4e13c7b01e4c', '', '', '导出', '', '', '', 500, 3, '1002003', 'category.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:23:24', '2024-08-22 08:44:05', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('a98b1e7e9a6947efb6bac31693e35875', 'c5090b9c5ceb47ec9028039e3485b61e', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:40:22', '2024-08-22 07:40:22', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('abc2a2e040a3406d8efba7773f61fb59', '59a6daac957440adaa9af5c7ecf9153d', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:54:33', '2024-08-22 07:54:33', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('ad37751e2c9343858030352eb7d6ff47', '0', '/proTable', 'proTable', '大屏可视化', 'Postcard', '', '', 600, 1, '1002001', '', 'F', 'T', 'F', 'F', 'F', 'F', '2024-08-23 09:10:45', '2024-08-23 17:21:47', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('b00050f7adc14872b31ab96ad870dd1d', '71736f023ae142c7a6e77bba748d5aa7', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:51:27', '2024-08-22 07:51:27', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('b35b081acb8a4361a2ae89f35612995d', '3edd396d3c234b66932aad46bd23cae8', '', '', '删除', '', '', '', 300, 2, '1002003', 'address.book.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:10:19', '2024-08-22 08:10:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('b428eba3f9a34025a46c394df5390b88', '29d33eba6b73420287d8f7e64aea62b3', '', '', '删除参数', '', '', '', 300, 3, '1002003', 'sys.config.delete_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-11-24 09:58:06', '2024-05-10 20:53:43', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('b5ce6412c26447348a7267de3ea11a21', '0e529e8a9dbf450898b695e051c36d48', '', '', '导入按钮', '', '', '', 100, 3, '1002003', 'generator.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-15 10:19:21', '2024-05-10 20:54:12', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('bc3f518a69a349298fcd0cda4301e986', '7f60993ea7a642449b2d4e13c7b01e4c', '', '', '导入', '', '', '', 400, 3, '1002003', 'category.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:23:24', '2024-08-22 08:44:05', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('bccd3aaa57f141a4b14fd40d3fccf2e2', '733bd8d9099b4cccb0f4faba9defeb7a', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-21 02:46:27', '2024-08-21 02:46:27', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('bce041cf239840bba8de074a0a85e13a', '5d7dd77835a9440a843841bd6e6492d9', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:03:53', '2024-08-22 08:03:53', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('bdccdee513824704be956e1b1c1f3ed2', '82abd51f34654b9197b6f635779ea419', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:43:01', '2024-08-22 07:43:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('c01001916f3e43f2ba85d4b9e8a23a1e', 'ba7ca04e011048f2a5b6be1c58f632a4', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:05:21', '2024-08-22 08:05:21', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('c21fd5febd0a4450b0a537c50aafa295', '15a90aeb4db7418c9f525ece768ec142', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:53:29', '2024-08-22 07:53:29', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('c2c5f2d4c94b47d0881b3d9b527f6b92', '733bd8d9099b4cccb0f4faba9defeb7a', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-21 02:46:27', '2024-08-21 02:46:27', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('c3343ee927dd409b95a9aadc8b4923a0', '59a6daac957440adaa9af5c7ecf9153d', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:54:33', '2024-08-22 07:54:33', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('c6dd479d5b304731be403d7551c60d70', '49a022c710e6445f97ff81c601e84dba', '/system/roleManage', 'roleManage', '角色管理', 'User', '/system/roleManage/index', '', 200, 2, '1002002', 'sys.role.query_table', 'F', 'T', 'F', 'F', 'F', 'T', '2023-08-29 13:54:36', '2024-08-21 10:23:37', '8', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('c95bf036f1534a6087a8076ac990f91d', '7f60993ea7a642449b2d4e13c7b01e4c', '', '', '修改', '', '', '', 200, 3, '1002003', 'category.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:23:24', '2024-08-22 08:44:05', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('ca096e1cb1d7418a8a01cd66ec96eaf4', '71736f023ae142c7a6e77bba748d5aa7', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:51:27', '2024-08-22 07:51:27', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('cb3500315dba4c2d83e4d92edf36dff7', '85b54322630f43a39296488a5e76ba16', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-17 10:19:32', '2024-08-21 02:14:09', '', '1', 'T', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('cea01dcde9b24b5a8686bdc33c438cd7', '140c9ed43ef54542bbcdde8a5d928400', '', '', '删除账号', '', '', '', 300, 3, '1002003', 'sys.user.delete_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-09-06 10:33:27', '2024-05-10 20:40:41', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('d5481cb985be4d918af45610b7326a43', '5d7dd77835a9440a843841bd6e6492d9', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:03:53', '2024-08-22 08:03:53', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('d5777af0c775454ab82ef1fef53cc472', '577f5ad2b54043e98962bad29f0cef3e', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:06:36', '2024-08-22 08:06:36', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('d58022e3914f4c8db2ee19fbace3db13', 'd9a559dd03c74142ae130115c2bc0135', '', '', '新增', '', '', '', 100, 2, '1002003', 'address.book.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:10:01', '2024-08-22 08:10:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('d5b2ebf2020a4352a8054dab57760f11', '2c2fdb109cd345f6bdfee324a2dda05a', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:41:07', '2024-08-22 07:41:07', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('d6d136a4b8b5404ab0bac2ccf68f16f5', '7126ee0d2b534898a9c7962e8d1c8ecd', '', '', '修改', '', '', '', 200, 2, '1002003', 'address.book.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:11:23', '2024-08-22 08:11:23', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('d715cc160e4f4cb49953b0894907e7f7', '59a6daac957440adaa9af5c7ecf9153d', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:54:33', '2024-08-22 07:54:33', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('d9edf383343e44c8a9224976b71a9c08', '577f5ad2b54043e98962bad29f0cef3e', '', '', '修改', '', '', '', 200, 2, '1002003', 'teacher.statistics.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:06:36', '2024-08-22 08:06:36', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('da1b46db642f42978f83ed5eb34870ce', '0', '/toolbox', 'toolbox', '系统工具', 'Briefcase', '', '', 200, 1, '1002001', '', 'F', 'T', 'F', 'F', 'F', 'F', '2023-12-08 13:46:08', '2024-08-21 10:13:50', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('dcb6aabcd910469ebf3efbc7e43282d4', '88b2e5def2ff474fa8bf3537d4a2fe5b', '/system/dictManage', 'dictManage', '字典管理', 'Reading', '/system/dictManage/index', '', 400, 2, '1002002', 'sys.dict.query_table', 'F', 'T', 'F', 'F', 'F', 'T', '2023-08-29 13:57:12', '2024-05-10 20:39:39', '8', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('df2894b4c06e47cab84142d81edc494d', 'c6dd479d5b304731be403d7551c60d70', '', '', '新增角色', '', '', '', 100, 3, '1002003', 'sys.role.create_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-09-06 10:30:18', '2024-05-10 20:40:57', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('dfc14050c68f4bf8a753f82b5b13007a', 'd9a559dd03c74142ae130115c2bc0135', '', '', '修改', '', '', '', 200, 2, '1002003', 'address.book.update', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:10:01', '2024-08-22 08:10:01', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('e1230fd8af0b4451b13b9dfb4cd9b5d5', 'd0b2e5a6c6f043ffb027f3ef73160980', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:46:19', '2024-08-22 07:46:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('e19083a9ac404698b4362d63d7048b7b', 'b06f77bf931947c78cc5883b465bbd19', '', '', '删除', '', '', '', 3, 1, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-05-10 21:45:48', '2024-05-10 21:45:48', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('e5eddc85d4494299b1b313e2461bb2c3', '7126ee0d2b534898a9c7962e8d1c8ecd', '', '', '新增', '', '', '', 100, 2, '1002003', 'address.book.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:11:23', '2024-08-22 08:11:23', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('e6ba6d507c9b467782e6c12fce1df765', '71736f023ae142c7a6e77bba748d5aa7', '', '', '导出', '', '', '', 500, 2, '1002003', 'teacher.statistics.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:51:27', '2024-08-22 07:51:27', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('e91e2a445aa84d65915d0f848e93a649', '7126ee0d2b534898a9c7962e8d1c8ecd', '', '', '导入', '', '', '', 400, 2, '1002003', 'address.book.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:11:23', '2024-08-22 08:11:23', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('e91eeaea8f1546d3921839469fe247b6', '140c9ed43ef54542bbcdde8a5d928400', '', '', '重置密码', '', '', '', 600, 3, '1002003', 'sys.user_resetPwd', 'F', 'F', 'F', 'F', 'F', 'F', '2024-04-10 10:16:08', '2024-04-10 10:16:08', '1', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('ea9c07fa26ab4b7b80d9368edcc03758', '7f60993ea7a642449b2d4e13c7b01e4c', '', '', '新增', '', '', '', 100, 3, '1002003', 'category.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:23:24', '2024-08-22 08:44:05', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('ede76f5e60b640aa9de2ba7216b90ceb', '29d33eba6b73420287d8f7e64aea62b3', '', '', '新增参数', '', '', '', 100, 3, '1002003', 'sys.config.add_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-11-24 09:57:19', '2024-05-10 20:42:46', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('ee36ad68586e42fa8a896215c544cb76', '99c2ee7b882749e597bcd62385f368fb', '', '', 'SQL按钮', '', '', '', 400, 3, '1002003', 'sys.menu.sql_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2024-01-11 09:41:47', '2024-05-10 20:41:21', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('efb276e7ea384d418cfd60c542fe86d0', 'c5090b9c5ceb47ec9028039e3485b61e', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:40:22', '2024-08-22 07:40:22', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('f000a163e01445c48fc4cdf5766e8992', 'd31e0415568346d8931e996107a5a35f', '', '', '新增', '', '', '', 100, 2, '1002003', 'teacher.statistics.create', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:52:35', '2024-08-22 07:52:35', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('f03924eedabc492ebadc94cabd53fff8', '3edd396d3c234b66932aad46bd23cae8', '', '', '导出', '', '', '', 500, 2, '1002003', 'address.book.export', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 08:10:19', '2024-08-22 08:10:19', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('f67bf535049440ceaf06703a9b4026e4', '71736f023ae142c7a6e77bba748d5aa7', '', '', '导入', '', '', '', 400, 2, '1002003', 'teacher.statistics.import', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-22 07:51:27', '2024-08-22 07:51:27', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('f6a46cf9b38741398c78a02d55cf44e1', '733bd8d9099b4cccb0f4faba9defeb7a', '', '', '删除', '', '', '', 300, 2, '1002003', 'teacher.statistics.remove', 'F', 'F', 'F', 'F', 'F', 'F', '2024-08-21 02:46:27', '2024-08-21 02:46:27', '', '', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('fa0c65ad783d4bf9b919a6db02ef1428', '99c2ee7b882749e597bcd62385f368fb', '', '', '删除菜单', '', '', '', 300, 3, '1002003', 'sys.menu.delete_btn', 'F', 'F', 'F', 'F', 'F', 'F', '2023-08-31 16:28:57', '2024-05-10 20:41:18', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('fbdcbcc0ccf547b4b78a4fc2cf303236', '0e529e8a9dbf450898b695e051c36d48', '', '', 'zip下载按钮', '', '', '', 500, 3, '1002003', 'generator.zip', 'F', 'F', 'F', 'F', 'F', 'F', '2024-02-15 10:23:17', '2024-05-10 20:54:27', '1', '1', 'F', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES ('fdb5bb3ee6714fe0bc4ae6a1816ce141', '0', '/dish', 'dish', '菜品管理', 'Basketball', '', '', 600, 1, '1002001', '', 'T', 'T', 'F', 'F', 'F', 'F', '2024-08-22 08:26:43', '2024-08-23 17:10:53', '1', '1', 'F', NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '角色id',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色名称',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '简介',
  `del_flag` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'F' COMMENT '删除与否',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_id` int NULL DEFAULT NULL,
  `update_id` int NULL DEFAULT NULL,
  `is_lock` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT 'F' COMMENT '是否锁定',
  `permissions` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '标识，唯一',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', '超级管理员权限', 'F', '2024-05-10 21:28:31', '2024-08-22 16:47:02', NULL, 1, 'T', 'admin');
INSERT INTO `sys_role` VALUES (2, '销售人员', '销售人员', 'F', '2024-05-10 21:52:39', '2024-08-22 17:01:45', NULL, 1, 'F', 'saler');
INSERT INTO `sys_role` VALUES (3, '教师统计', '', 'T', '2024-05-10 21:53:15', '2024-08-22 09:00:53', NULL, NULL, 'F', 'teacher_statics_menu');
INSERT INTO `sys_role` VALUES (4, '教培老师', '教培老师', 'F', '2024-08-22 17:01:38', '2024-08-22 17:01:51', 1, 1, 'F', 'teacher');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `menu_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'sys_menu_id （菜单表）',
  `role_id` int NOT NULL COMMENT 'sys_role_id （角色表）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 387 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统角色-菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (61, '85b54322630f43a39296488a5e76ba16', 3);
INSERT INTO `sys_role_menu` VALUES (62, 'cb3500315dba4c2d83e4d92edf36dff7', 3);
INSERT INTO `sys_role_menu` VALUES (63, '7391f12ad51049c2b86d231d39708c71', 3);
INSERT INTO `sys_role_menu` VALUES (64, '73d312f4fa8949ddba3d9807c0c56f00', 3);
INSERT INTO `sys_role_menu` VALUES (65, '91ccb13b5c174583803a4c492a5dfdb6', 3);
INSERT INTO `sys_role_menu` VALUES (66, '8061d8e79be744bf91b7b438f8e8e887', 3);
INSERT INTO `sys_role_menu` VALUES (255, '51850917b1b64b19b8f905f38cba101d', 2);
INSERT INTO `sys_role_menu` VALUES (256, '170a5b7ec83a43ec877b6be40d4aaa3a', 2);
INSERT INTO `sys_role_menu` VALUES (257, '5f53304f035b4f65939189d310db9bff', 2);
INSERT INTO `sys_role_menu` VALUES (258, '532236cbc86b45fe9f3a0e774c20d760', 2);
INSERT INTO `sys_role_menu` VALUES (259, '21fc0a3b7cf54ef6a57fffab15477aac', 2);
INSERT INTO `sys_role_menu` VALUES (260, '48ae285ec8b34a759fe93c8a7c249687', 2);
INSERT INTO `sys_role_menu` VALUES (261, '60b6985308e54911b86ca2cc184e1bf5', 2);
INSERT INTO `sys_role_menu` VALUES (262, 'fdb5bb3ee6714fe0bc4ae6a1816ce141', 2);
INSERT INTO `sys_role_menu` VALUES (263, '7f60993ea7a642449b2d4e13c7b01e4c', 2);
INSERT INTO `sys_role_menu` VALUES (264, 'ea9c07fa26ab4b7b80d9368edcc03758', 2);
INSERT INTO `sys_role_menu` VALUES (265, 'c95bf036f1534a6087a8076ac990f91d', 2);
INSERT INTO `sys_role_menu` VALUES (266, 'a181c53b3ebc4aee86d966e944674efd', 2);
INSERT INTO `sys_role_menu` VALUES (267, 'bc3f518a69a349298fcd0cda4301e986', 2);
INSERT INTO `sys_role_menu` VALUES (268, 'a982fc3dcd984cd78e001862794727f9', 2);
INSERT INTO `sys_role_menu` VALUES (387, '49a022c710e6445f97ff81c601e84dba', 1);
INSERT INTO `sys_role_menu` VALUES (388, '140c9ed43ef54542bbcdde8a5d928400', 1);
INSERT INTO `sys_role_menu` VALUES (389, '0f98b89c67e54cb0bcff2b56aa98832f', 1);
INSERT INTO `sys_role_menu` VALUES (390, '8255bac5eae748a0a8500167963b3e00', 1);
INSERT INTO `sys_role_menu` VALUES (391, 'cea01dcde9b24b5a8686bdc33c438cd7', 1);
INSERT INTO `sys_role_menu` VALUES (392, '6e25a716c1a646009a9be90b16f0a682', 1);
INSERT INTO `sys_role_menu` VALUES (393, '330a1a0a857c4ad1a95327db5134e420', 1);
INSERT INTO `sys_role_menu` VALUES (394, 'e91eeaea8f1546d3921839469fe247b6', 1);
INSERT INTO `sys_role_menu` VALUES (395, '686a5522b0334d4da51aa15b3fd1a303', 1);
INSERT INTO `sys_role_menu` VALUES (396, '30942929802f41cc850722c78db089e7', 1);
INSERT INTO `sys_role_menu` VALUES (397, 'c6dd479d5b304731be403d7551c60d70', 1);
INSERT INTO `sys_role_menu` VALUES (398, 'df2894b4c06e47cab84142d81edc494d', 1);
INSERT INTO `sys_role_menu` VALUES (399, '0933b165ffc14d558e8de43ccb6687f6', 1);
INSERT INTO `sys_role_menu` VALUES (400, '445b73dda9a34ad681d2705a7abcf2f6', 1);
INSERT INTO `sys_role_menu` VALUES (401, '5b5fb3748c6a4ed5a4dda3877508c3a7', 1);
INSERT INTO `sys_role_menu` VALUES (402, '99c2ee7b882749e597bcd62385f368fb', 1);
INSERT INTO `sys_role_menu` VALUES (403, '9338bf2f57984825bc227bb618f9db81', 1);
INSERT INTO `sys_role_menu` VALUES (404, '05194ef5fa7a4a308a44f6f5c6791c3a', 1);
INSERT INTO `sys_role_menu` VALUES (405, 'fa0c65ad783d4bf9b919a6db02ef1428', 1);
INSERT INTO `sys_role_menu` VALUES (406, 'ee36ad68586e42fa8a896215c544cb76', 1);
INSERT INTO `sys_role_menu` VALUES (407, '8354d626cc65487594a7c38e98de1bad', 1);
INSERT INTO `sys_role_menu` VALUES (408, '8fd6721941494fd5bbe16bec82b235be', 1);
INSERT INTO `sys_role_menu` VALUES (409, '006bdbacd71a481f88b6acf895529acd', 1);
INSERT INTO `sys_role_menu` VALUES (410, '3ba9407560a1490583fefa10b22bc74f', 1);
INSERT INTO `sys_role_menu` VALUES (411, '88b2e5def2ff474fa8bf3537d4a2fe5b', 1);
INSERT INTO `sys_role_menu` VALUES (412, 'dcb6aabcd910469ebf3efbc7e43282d4', 1);
INSERT INTO `sys_role_menu` VALUES (413, '4f39ef0fd2f748f6ab7d6d20d98bc4af', 1);
INSERT INTO `sys_role_menu` VALUES (414, '818cc6e1889d46579525ad8ab921eeb8', 1);
INSERT INTO `sys_role_menu` VALUES (415, '9830d86487184961b90fc527c9604720', 1);
INSERT INTO `sys_role_menu` VALUES (416, '81647226a2d047e8ab0b70472350ee69', 1);
INSERT INTO `sys_role_menu` VALUES (417, '97f11d74c98047ba80f011a3da9d882c', 1);
INSERT INTO `sys_role_menu` VALUES (418, '1a86a9d2b3ca49439277fff9f499c7cd', 1);
INSERT INTO `sys_role_menu` VALUES (419, '29d33eba6b73420287d8f7e64aea62b3', 1);
INSERT INTO `sys_role_menu` VALUES (420, 'ede76f5e60b640aa9de2ba7216b90ceb', 1);
INSERT INTO `sys_role_menu` VALUES (421, '3a54d488132b4331bf3cd5e6d86ffcf4', 1);
INSERT INTO `sys_role_menu` VALUES (422, 'b428eba3f9a34025a46c394df5390b88', 1);
INSERT INTO `sys_role_menu` VALUES (423, 'da1b46db642f42978f83ed5eb34870ce', 1);
INSERT INTO `sys_role_menu` VALUES (424, '0e529e8a9dbf450898b695e051c36d48', 1);
INSERT INTO `sys_role_menu` VALUES (425, 'b5ce6412c26447348a7267de3ea11a21', 1);
INSERT INTO `sys_role_menu` VALUES (426, '3d7eed8398d3457c897b2e8bf838e9c6', 1);
INSERT INTO `sys_role_menu` VALUES (427, '310d02bb121645d1b7a7f949f48c981b', 1);
INSERT INTO `sys_role_menu` VALUES (428, '2d6b78ad03de4cf1a3899f25cd7fe0ee', 1);
INSERT INTO `sys_role_menu` VALUES (429, 'fbdcbcc0ccf547b4b78a4fc2cf303236', 1);
INSERT INTO `sys_role_menu` VALUES (430, '012efc4ef8d24304a8562534f319524a', 1);
INSERT INTO `sys_role_menu` VALUES (431, '2c94bd9c788b4233b75b4bc43127ff60', 1);
INSERT INTO `sys_role_menu` VALUES (432, '51850917b1b64b19b8f905f38cba101d', 1);
INSERT INTO `sys_role_menu` VALUES (433, '170a5b7ec83a43ec877b6be40d4aaa3a', 1);
INSERT INTO `sys_role_menu` VALUES (434, '5f53304f035b4f65939189d310db9bff', 1);
INSERT INTO `sys_role_menu` VALUES (435, '532236cbc86b45fe9f3a0e774c20d760', 1);
INSERT INTO `sys_role_menu` VALUES (436, '21fc0a3b7cf54ef6a57fffab15477aac', 1);
INSERT INTO `sys_role_menu` VALUES (437, '48ae285ec8b34a759fe93c8a7c249687', 1);
INSERT INTO `sys_role_menu` VALUES (438, '60b6985308e54911b86ca2cc184e1bf5', 1);
INSERT INTO `sys_role_menu` VALUES (439, 'ad37751e2c9343858030352eb7d6ff47', 1);
INSERT INTO `sys_role_menu` VALUES (440, '2aed10e3c4774d2aad1d3e8a47b58323', 1);
INSERT INTO `sys_role_menu` VALUES (441, 'fdb5bb3ee6714fe0bc4ae6a1816ce141', 1);
INSERT INTO `sys_role_menu` VALUES (442, '7f60993ea7a642449b2d4e13c7b01e4c', 1);
INSERT INTO `sys_role_menu` VALUES (443, 'ea9c07fa26ab4b7b80d9368edcc03758', 1);
INSERT INTO `sys_role_menu` VALUES (444, 'c95bf036f1534a6087a8076ac990f91d', 1);
INSERT INTO `sys_role_menu` VALUES (445, 'a181c53b3ebc4aee86d966e944674efd', 1);
INSERT INTO `sys_role_menu` VALUES (446, 'bc3f518a69a349298fcd0cda4301e986', 1);
INSERT INTO `sys_role_menu` VALUES (447, 'a982fc3dcd984cd78e001862794727f9', 1);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户名',
  `pwd` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '密码',
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `nickname` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别(0 未知 1 男 2 女)',
  `birthday` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '生日',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像地址',
  `age` int NULL DEFAULT NULL COMMENT '年龄，--废弃，以生日为主',
  `id_card` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '身份证',
  `email` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱地址',
  `account_status_cd` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '账户状态 (如 冻结；禁言；正常。 关联字典表account_status)',
  `user_tag_cd` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '' COMMENT '标签（自定义关联到字典表）',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最近一次登录时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `del_flag` enum('T','F') CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'F' COMMENT '是否删除',
  `create_id` int NULL DEFAULT NULL,
  `update_id` int NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `username_index`(`username` ASC) USING BTREE,
  INDEX `create_time_index`(`create_time` DESC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'admin', '$2a$10$oqd08QVWJ6Z6.7EhRlmfUepmO/0HtPlZBHCvtD3dN80RzHhugXrci', '19988887777', '系统管理员', 1, '2022-01-01', 'https://himg.bdimg.com/sys/portraitn/item/public.1.b5603a9e.VHbBY64WBETrjS8nbEPjWQ', 1, '', '', '1000001', '1001002', '2024-02-02 13:36:04', '2023-08-18 11:15:10', '2024-08-21 10:16:41', 'F', NULL, 1);
INSERT INTO `sys_user` VALUES (2, 'user', '$2a$10$HY5/E9mBq989oJ75y.4/mOyXR4B41tE5z8Me7DX3zHkE/IFFOImKW', '18483678377', '测试用户', 1, '2024-01-01', 'https://himg.bdimg.com/sys/portraitn/item/public.1.b5603a9e.VHbBY64WBETrjS8nbEPjWQ', 0, '', '', '1000001', '1001003', NULL, '2024-05-09 21:50:02', '2024-08-22 16:59:43', 'F', NULL, 1);

-- ----------------------------
-- Table structure for sys_user_data_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_data_role`;
CREATE TABLE `sys_user_data_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL COMMENT '数据角色id (sys_data_role_id)',
  `user_id` int NOT NULL COMMENT '用户id(sys_user_id)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户-数据角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_data_role
-- ----------------------------
INSERT INTO `sys_user_data_role` VALUES (1, 2, 4);
INSERT INTO `sys_user_data_role` VALUES (2, 1, 3);
INSERT INTO `sys_user_data_role` VALUES (3, 3, 5);
INSERT INTO `sys_user_data_role` VALUES (4, 4, 6);

-- ----------------------------
-- Table structure for sys_user_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_dept`;
CREATE TABLE `sys_user_dept`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `dept_id` int NULL DEFAULT NULL COMMENT 'sys_dept_id',
  `user_id` int NULL DEFAULT NULL COMMENT 'sys_user_id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户-部门关系表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_dept
-- ----------------------------
INSERT INTO `sys_user_dept` VALUES (2, 22, 1);
INSERT INTO `sys_user_dept` VALUES (3, 20, 2);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` int NOT NULL AUTO_INCREMENT,
  `role_id` int NOT NULL COMMENT '角色id (sys_role_id)',
  `user_id` int NOT NULL COMMENT '用户id(sys_user_id)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统用户-角色关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 1);
INSERT INTO `sys_user_role` VALUES (4, 2, 2);

-- ----------------------------
-- Table structure for t_db_version
-- ----------------------------
DROP TABLE IF EXISTS `t_db_version`;
CREATE TABLE `t_db_version`  (
  `installed_rank` int NOT NULL,
  `version` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `description` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `script` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `checksum` int NULL DEFAULT NULL,
  `installed_by` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`) USING BTREE,
  INDEX `t_db_version_s_idx`(`success` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of t_db_version
-- ----------------------------
INSERT INTO `t_db_version` VALUES (1, '1.1', '20230509 Init DDL', 'SQL', 'V1.1__20230509_Init_DDL.sql', 829744681, 'root', '2024-08-21 01:48:56', 4617, 1);
INSERT INTO `t_db_version` VALUES (2, '1.2', '20240511 Update DDL', 'SQL', 'V1.2__20240511_Update_DDL.sql', 372977814, 'root', '2024-08-21 01:48:56', 33, 1);
INSERT INTO `t_db_version` VALUES (3, '1.3', '20240530 Update DDL', 'SQL', 'V1.3__20240530_Update_DDL.sql', 1784154531, 'root', '2024-08-21 01:48:56', 76, 1);
INSERT INTO `t_db_version` VALUES (4, '1.4', '20240603 Update DDL', 'SQL', 'V1.4__20240603_Update_DDL.sql', 1997095868, 'root', '2024-08-21 01:48:56', 14, 1);
INSERT INTO `t_db_version` VALUES (5, '1.5', '20240605 Update DDL', 'SQL', 'V1.5__20240605_Update_DDL.sql', 1391658357, 'root', '2024-08-21 01:48:56', 15, 1);
INSERT INTO `t_db_version` VALUES (6, '1.6', '20240611 Update DDL', 'SQL', 'V1.6__20240611_Update_DDL.sql', 75627697, 'root', '2024-08-21 01:48:56', 15, 1);
INSERT INTO `t_db_version` VALUES (7, '1.7', '20240624 Update DDL', 'SQL', 'V1.7__20240624_Update_DDL.sql', -404704373, 'root', '2024-08-21 01:48:56', 389, 0);

-- ----------------------------
-- Table structure for teacher_statistics
-- ----------------------------
DROP TABLE IF EXISTS `teacher_statistics`;
CREATE TABLE `teacher_statistics`  (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'id',
  `year` varchar(4) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '统计年限',
  `month` varchar(2) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '统计月份',
  `during_time` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '统计年月',
  `teacher_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '教师id',
  `teacher_common_type` int NOT NULL COMMENT '讲师区分类型',
  `total_teaching` int NULL DEFAULT NULL COMMENT '授课总数',
  `total_class_count` int NULL DEFAULT NULL COMMENT '服务班次数',
  `total_hours` decimal(10, 2) NULL DEFAULT NULL COMMENT '课时总数',
  `check_status` int NOT NULL DEFAULT 0 COMMENT '核对状态',
  `check_time` datetime NULL DEFAULT NULL COMMENT '核对时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '生成时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `last_sync_time` datetime NULL DEFAULT NULL COMMENT '最近一次同步时间',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_id` int NULL DEFAULT NULL COMMENT '创建人id',
  `update_id` int NULL DEFAULT NULL COMMENT '更新人id',
  `dept_scope` json NULL COMMENT '部门范围',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '教师统计总览表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of teacher_statistics
-- ----------------------------
INSERT INTO `teacher_statistics` VALUES (1, '2022', '12', '2010-07', '1', 1000001, 466, 414, 201.98, 643, '2013-09-29 06:45:31', '2005-07-17 15:51:54', '2024-05-10 22:06:48', '2007-12-08 17:53:09', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (2, '2006', '10', '2009-11', '10', 1000002, 985, 716, 960.44, 751, '2008-06-16 06:32:13', '2009-06-23 15:22:50', '2024-05-10 22:06:53', '2020-01-16 19:12:48', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (3, '2001', '03', '2003-12', '14', 1000001, 511, 828, 634.67, 394, '2023-07-22 02:17:23', '2010-07-06 09:21:12', '2024-05-10 22:06:58', '2019-07-26 03:56:30', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (4, '2009', '12', '2002-10', '12', 1000001, 826, 582, 92.45, 789, '2018-01-31 09:32:51', '2023-01-09 16:06:21', '2024-05-10 22:07:02', '2002-11-15 07:50:19', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (5, '2023', '08', '2010-12', '11', 1000003, 952, 967, 46.67, 238, '2007-07-18 17:52:25', '2006-01-11 00:14:01', '2024-05-10 22:07:06', '2007-03-19 21:19:52', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (6, '2024', '10', '2007-10', '14', 1000001, 176, 892, 903.71, 257, '2009-09-07 11:52:29', '2009-06-21 17:58:22', '2024-05-10 22:07:10', '2012-08-11 23:42:25', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (7, '2013', '11', '2011-12', '13', 1000002, 919, 687, 328.96, 563, '2007-05-11 09:37:12', '2019-08-05 21:32:04', '2024-05-10 22:07:15', '2000-03-24 06:35:26', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (8, '2011', '12', '2006-10', '9', 1000003, 531, 290, 69.36, 849, '2013-09-06 14:28:38', '2002-08-17 12:52:52', '2024-05-10 22:07:19', '2001-05-29 16:26:40', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (9, '2012', '07', '2011-01', '2', 1000001, 266, 640, 579.83, 110, '2001-05-15 05:47:46', '2001-06-12 14:13:21', '2024-05-10 22:07:23', '2012-02-19 01:50:29', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (10, '2009', '11', '2004-10', '2', 1000001, 513, 5, 904.22, 695, '2000-06-22 01:38:19', '2019-12-09 06:55:27', '2024-05-10 22:07:39', '2006-01-29 01:00:09', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (11, '2003', '10', '2008-12', '12', 1000002, 299, 574, 983.94, 333, '2001-09-14 19:40:22', '2008-03-28 05:20:35', '2024-05-10 22:07:44', '2003-10-26 07:23:26', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (12, '2011', '12', '2003-06', '8', 1000002, 223, 604, 449.45, 501, '2018-08-07 09:29:13', '2003-09-02 19:24:23', '2024-05-10 22:07:49', '2010-11-11 04:14:00', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (13, '2003', '12', '2010-11', '2', 1000002, 833, 45, 624.98, 329, '2001-03-20 20:58:46', '2003-07-24 09:16:34', '2024-05-10 22:07:54', '2021-11-15 14:35:55', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (14, '2011', '12', '2006-01', '4', 1000003, 945, 879, 365.55, 533, '2003-02-02 22:13:38', '2020-03-26 11:54:46', '2024-05-10 22:07:58', '2023-04-14 01:22:50', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (15, '2002', '11', '2012-04', '2', 1000001, 883, 386, 610.66, 938, '2011-02-04 17:03:34', '2020-07-02 02:25:46', '2024-05-10 22:08:02', '2001-01-29 06:48:32', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (16, '2024', '11', '2005-10', '9', 1000001, 925, 561, 773.45, 312, '2001-08-19 06:41:23', '2002-11-26 04:22:26', '2024-05-10 22:08:06', '2019-11-12 07:43:03', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (17, '2013', '10', '2012-03', '14', 1000001, 220, 589, 916.78, 65, '2003-08-20 14:23:14', '2007-08-08 22:36:47', '2024-05-10 22:08:15', '2008-10-10 14:10:10', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (18, '2012', '11', '2004-06', '3', 1000002, 201, 619, 19.83, 45, '2013-03-14 12:30:50', '2002-02-11 17:41:37', '2024-05-10 22:08:18', '2010-11-02 14:31:15', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (19, '2013', '10', '2008-08', '10', 1000001, 574, 113, 675.62, 86, '2003-06-05 19:58:55', '2014-02-18 02:02:51', '2024-05-10 22:08:22', '2019-06-01 03:41:13', '', NULL, NULL, NULL);
INSERT INTO `teacher_statistics` VALUES (20, '2022', '10', '2014-01', '3', 611, 941, 338, 779.27, 1000001, '2022-07-29 00:42:30', '2024-04-12 13:44:06', '2024-05-10 22:08:26', '2002-02-02 18:22:06', '', NULL, NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
