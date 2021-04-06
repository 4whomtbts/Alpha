INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`,`last_instance_allocation_time`) VALUES (1,'1','192.168.1.11',9000,10000,1,8081,'/home/tako1',0);
INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`,`last_instance_allocation_time`) VALUES (2,'1','192.168.1.12',9100,10000,2,8082,'/home/tako2',0);
INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`,`last_instance_allocation_time`) VALUES (3,'1','192.168.1.13',9200,10000,3,8083,'/home/tako3',0);
INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`,`last_instance_allocation_time`) VALUES (4,'0','192.168.1.14',9300,10000,4,8084,'/home/tako4',0);
INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`,`last_instance_allocation_time`) VALUES (5,'0','192.168.1.15',9400,10000,5,8085,'/home/tako5',0);
INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`,`last_instance_allocation_time`) VALUES (6,'0','192.168.1.16',9500,10000,6,8086,'/home/tako6',0);-- INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`) VALUES (8,'0','192.168.1.18',9500,10000,6,8088,'/home/tako6');

INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (0, 'GTX 2080', 0, 'FOO1234', 1);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (1, 'GTX 2080', 1, 'FOO1234', 1);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (2, 'GTX 2080', 2, 'FOO1234', 1);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (3, 'GTX 2080', 3, 'FOO1234', 1);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (4, 'GTX 2080', 4, 'FOO1234', 1);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (5, 'GTX 2080', 5, 'FOO1234', 1);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (6, 'GTX 2080', 6, 'FOO1234', 1);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (7, 'GTX 2080', 7, 'FOO1234', 1);

INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (8, 'GTX 2080', 0, 'FOO1234', 2);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (9, 'GTX 2080', 1, 'FOO1234', 2);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (10, 'GTX 2080', 2, 'FOO1234', 2);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (11, 'GTX 2080', 3, 'FOO1234', 2);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (12, 'GTX 2080', 4, 'FOO1234', 2);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (13, 'GTX 2080', 5, 'FOO1234', 2);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (14, 'GTX 2080', 6, 'FOO1234', 2);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (15, 'GTX 2080', 7, 'FOO1234', 2);

INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (16, 'GTX 2080', 0, 'FOO1234', 3);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (17, 'GTX 2080', 1, 'FOO1234', 3);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (18, 'GTX 2080', 2, 'FOO1234', 3);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (19, 'GTX 2080', 3, 'FOO1234', 3);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (20, 'GTX 2080', 4, 'FOO1234', 3);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (21, 'GTX 2080', 5, 'FOO1234', 3);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (22, 'GTX 2080', 6, 'FOO1234', 3);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (23, 'GTX 2080', 7, 'FOO1234', 3);

INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (24, 'GTX 2080', 0, 'FOO1234', 4);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (25, 'GTX 2080', 1, 'FOO1234', 4);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (26, 'GTX 2080', 2, 'FOO1234', 4);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (27, 'GTX 2080', 3, 'FOO1234', 4);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (28, 'GTX 2080', 4, 'FOO1234', 4);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (29, 'GTX 2080', 5, 'FOO1234', 4);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (30, 'GTX 2080', 6, 'FOO1234', 4);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (31, 'GTX 2080', 7, 'FOO1234', 4);

INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (32, 'GTX 2080', 0, 'FOO1234', 5);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (33, 'GTX 2080', 1, 'FOO1234', 5);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (34, 'GTX 2080', 2, 'FOO1234', 5);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (35, 'GTX 2080', 3, 'FOO1234', 5);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (36, 'GTX 2080', 4, 'FOO1234', 5);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (37, 'GTX 2080', 5, 'FOO1234', 5);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (38, 'GTX 2080', 6, 'FOO1234', 5);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (39, 'GTX 2080', 7, 'FOO1234', 5);

INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (40, 'GTX 2080', 0, 'FOO1234', 6);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (41, 'GTX 2080', 1, 'FOO1234', 6);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (42, 'GTX 2080', 2, 'FOO1234', 6);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (43, 'GTX 2080', 3, 'FOO1234', 6);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (44, 'GTX 2080', 4, 'FOO1234', 6);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (45, 'GTX 2080', 5, 'FOO1234', 6);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (46, 'GTX 2080', 6, 'FOO1234', 6);
INSERT INTO `gpu` (`gpu_id`, `model_name`, `slot_index`, `uuid`, `server_id`) VALUES (47, 'GTX 2080', 7, 'FOO1234', 6);

INSERT INTO `user` (`user_id`,`created_at`,`modified_at`,`email`,`login_id`,`organization`,`password`,`phone`,`user_name`,`user_type`) VALUES (1,'2021-03-01 12:35:09','2021-03-01 12:35:10','abc@dgu.ailab','admin','dgu','$2a$10$Gq5.pMEWMNwbOgDOLsEH5.XZjmRtKuvUzFgWpSPOQySfPGmWOQn6.','01000000000','John doe',0);
INSERT INTO `user_role` (`id`,`role_name`,`user_id`) VALUES (1,'ADMIN',1);
INSERT INTO `container_image` (`container_image_id`,`container_image_desc`,`container_image_name`,`container_image_nick_name`) VALUES (1,'이미지 설명','이미지 이름','이미지 닉네임');
