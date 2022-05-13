 INSERT INTO `microservices`.`user` (name, email) VALUES ('Mike','mike123@gmail.com');
 INSERT INTO `microservices`.`user` (name, email) VALUES ('John','john1@gmail.com');
 INSERT INTO `microservices`.`user` (name, email) VALUES ('Diane','diane999@gmail.com');

 INSERT INTO `microservices`.`product_id` (product_id) VALUES (1);
 INSERT INTO `microservices`.`product_id` (product_id) VALUES (2);
 INSERT INTO `microservices`.`product_id` (product_id) VALUES (3);

 INSERT INTO `microservices`.`order_table` (created, total_price, fk_user_id) VALUES (CURRENT_TIMESTAMP, 2500, 1);
 INSERT INTO `microservices`.`order_table` (created, total_price, fk_user_id) VALUES (CURRENT_TIMESTAMP, 1500, 2);
 INSERT INTO `microservices`.`order_table` (created, total_price, fk_user_id) VALUES (CURRENT_TIMESTAMP, 500, 1);

 INSERT INTO `microservices`.`order_item` (fk_product_id, fk_order_id, count) VALUES (1, 1, 1);
 INSERT INTO `microservices`.`order_item` (fk_product_id, fk_order_id, count) VALUES (1, 2, 1);
 INSERT INTO `microservices`.`order_item` (fk_product_id, fk_order_id, count) VALUES (2, 1, 1);
 INSERT INTO `microservices`.`order_item` (fk_product_id, fk_order_id, count) VALUES (3, 1, 1);
 INSERT INTO `microservices`.`order_item` (fk_product_id, fk_order_id, count) VALUES (2, 3, 2);