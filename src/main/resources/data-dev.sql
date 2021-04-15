INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`,`last_instance_allocation_time`) VALUES (1,'1','192.168.1.11',9000,10000,1,8081,'/home/tako1',0);
INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`,`last_instance_allocation_time`) VALUES (2,'1','192.168.1.12',9100,10000,2,8082,'/home/tako2',0);

INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (0, 'GTX 2080', 0, 'FOO1234', 1);

INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (1, 'GTX 2080', 0, 'FOO1234', 2);

INSERT INTO `user` (`user_id`,`created_at`,`modified_at`,`email`,`login_id`,`organization`,`password`,`phone`,`user_name`,`user_type`) VALUES (1,'2021-03-01 12:35:09','2021-03-01 12:35:10','abc@dgu.ailab','admin','dgu','$2a$10$Gq5.pMEWMNwbOgDOLsEH5.XZjmRtKuvUzFgWpSPOQySfPGmWOQn6.','01000000000','John doe',0);
INSERT INTO `user_role` (`id`,`role_name`,`user_id`) VALUES (1,'ADMIN',1);
INSERT INTO `container_image` (`container_image_id`,`container_image_desc`,`container_image_name`,`container_image_nick_name`) VALUES (1,'이미지 설명','기본 이미지','tensorflow');

INSERT INTO `instance` (`instance_id`,`created_at`,`modified_at`,`error`,`expired_at`,`initialized`,`instance_container_id`,`instance_hash`,`instance_name`,`purpose`,`container_image_id`,`user_id`,`server_id`,`server`) VALUES (1,'2021-04-06 11:55:13','2021-04-06 11:57:54','0','2021-04-06 12:55:13','1','admin-1a0c8094-12e3-4b71-ac44-4f6b9d81e1fd','63a74888bfc50f272a8d7059ca69de801e7cb93821af13458da6bce449165b67',NULL,NULL,1,1,1,NULL);

INSERT INTO `instance_gpu` (`server_gpu_id`,`is_exclusively_occupied`,`gpu_id`,`instance_id`,`gpu`) VALUES (1,'0',0,1,NULL);
