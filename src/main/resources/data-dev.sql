INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`,`last_instance_allocation_time`) VALUES (1,'1','192.168.1.11',9000,10000,1,8081,'/home/tako1',0);
INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`,`last_instance_allocation_time`) VALUES (2,'1','192.168.1.12',9100,10000,2,8082,'/home/tako2',0);

INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (0, 'GTX 2080', 0, 'FOO1234', 1);

INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `serr_id`) VALUES (8, 'GTX 2080', 0, 'FOO1234', 2);

INSERT INTO `user` (`user_id`,`created_at`,`modified_at`,`email`,`login_id`,`organization`,`password`,`phone`,`user_name`,`user_type`) VALUES (1,'2021-03-01 12:35:09','2021-03-01 12:35:10','abc@dgu.ailab','admin','dgu','$2a$10$Gq5.pMEWMNwbOgDOLsEH5.XZjmRtKuvUzFgWpSPOQySfPGmWOQn6.','01000000000','John doe',0);
INSERT INTO `user_role` (`id`,`role_name`,`user_id`) VALUES (1,'ADMIN',1);
INSERT INTO `container_image` (`container_image_id`,`container_image_desc`,`container_image_name`,`container_image_nick_name`) VALUES (1,'이미지 설명','이미지 이름','이미지 닉네임');
