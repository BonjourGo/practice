CREATE TABLE `order` (
                         `order_id` varchar(64) NOT NULL COMMENT '主键',
                         `user_id` varchar(32) DEFAULT NULL COMMENT '用户id',
                         `order_status` char(2) DEFAULT NULL,
                         `product_id` varchar(32) DEFAULT NULL,
                         `pay_money` decimal(16,2) DEFAULT NULL,
                         `create_time` varchar(18) DEFAULT NULL,
                         `pay_time` varchar(18) DEFAULT NULL,
                         `cancel_time` varchar(18) DEFAULT NULL,
                         `number` int(8) DEFAULT NULL,
                         PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE TABLE `user` (
                        `id` varchar(32) NOT NULL COMMENT 'id',
                        `nike_name` varchar(255) NOT NULL,
                        `phone` varchar(11) NOT NULL,
                        `sex` varchar(1) DEFAULT NULL,
                        `status` varchar(1) DEFAULT NULL,
                        `register_type` varchar(2) DEFAULT NULL COMMENT '注册方式',
                        `password` varchar(255) DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;