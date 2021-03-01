INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`) VALUES (1,'1','192.168.1.11',9000,10000,1,8081,'/home/tako1');
INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`) VALUES (2,'1','192.168.1.12',9100,10000,2,8082,'/home/tako2');
INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`) VALUES (3,'1','192.168.1.13',9200,10000,3,8083,'/home/tako3');
INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`) VALUES (4,'0','192.168.1.14',9300,10000,4,8084,'/home/tako4');
INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`) VALUES (5,'1','192.168.1.15',9400,10000,5,8085,'/home/tako5');
INSERT INTO `server` (`server_id`,`excluded`,`internal_ip`,`min_external_port`,`min_internal_port`,`server_num`,`ssh_port`,`shared_directory_path`) VALUES (6,'1','192.168.1.16',9500,10000,6,8086,'/home/tako6');

INSERT INTO `user` (`user_id`,`created_at`,`modified_at`,`email`,`login_id`,`organization`,`password`,`phone`,`user_name`,`user_type`) VALUES (1,'2021-03-01 12:35:09','2021-03-01 12:35:10','abc@dgu.ailab','admin','dgu','$2a$10$Gq5.pMEWMNwbOgDOLsEH5.XZjmRtKuvUzFgWpSPOQySfPGmWOQn6.','01000000000','John doe',0);
INSERT INTO `user_role` (`id`,`role_name`,`user_id`) VALUES (1,'ADMIN',1);
INSERT INTO `container_image` (`container_image_id`,`container_image_desc`,`container_image_name`,`container_image_nick_name`) VALUES (1,'이미지 설명','이미지 이름','이미지 닉네임');

INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (1,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (1,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (1,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (1,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (1,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (1,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (1,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (1,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (2,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (2,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (2,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (2,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (2,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (2,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (2,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (2,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (3,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (3,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (3,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (3,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (3,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (3,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (3,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (3,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (4,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (4,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (4,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (4,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (4,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (4,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (4,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (4,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (5,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (5,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (5,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (5,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (5,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (5,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (5,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (5,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (6,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (6,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (6,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (6,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (6,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (6,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (6,0);
INSERT INTO `server_gpus` (`server_server_id`,`gpus`) VALUES (6,0);
