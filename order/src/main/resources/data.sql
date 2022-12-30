 INSERT INTO `microservices`.`order_table` (created, user_id, status) VALUES (CURRENT_TIMESTAMP, 1, 'COMPLETED');
 INSERT INTO `microservices`.`order_table` (created, user_id, status) VALUES (CURRENT_TIMESTAMP, 2, 'COMPLETED');
 INSERT INTO `microservices`.`order_table` (created, user_id, status) VALUES (CURRENT_TIMESTAMP, 1, 'COMPLETED');

 INSERT INTO `microservices`.`order_item` (product_id, fk_order_id, cost, count) VALUES (1, 1, 2050, 1);
 INSERT INTO `microservices`.`order_item` (product_id, fk_order_id, cost, count) VALUES (1, 2, 999.99, 1);
 INSERT INTO `microservices`.`order_item` (product_id, fk_order_id, cost, count) VALUES (2, 1, 1400, 1);
 INSERT INTO `microservices`.`order_item` (product_id, fk_order_id, cost, count) VALUES (3, 1, 2000, 1);
 INSERT INTO `microservices`.`order_item` (product_id, fk_order_id, cost, count) VALUES (2, 3, 5000, 2);