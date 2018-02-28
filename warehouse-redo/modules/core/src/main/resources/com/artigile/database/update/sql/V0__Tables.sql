create database whclient default charset utf8;

create user 'admin'@'%' identified by 'd2d41e9192f13edfe8b0467a4a65fea8';

grant all privileges on *.* to 'admin'@'%' with grant option;

use whclient;

CREATE TABLE User (
  id bigint(20) NOT NULL AUTO_INCREMENT,
  email varchar(255) DEFAULT NULL,
  firstName varchar(255) DEFAULT NULL,
  lastName varchar(255) DEFAULT NULL,
  login varchar(16) NOT NULL,
  password varchar(255) DEFAULT NULL,
  predefined boolean NOT NULL DEFAULT FALSE,
  PRIMARY KEY (id),
  UNIQUE KEY login (login)
);

insert into User (login, password, predefined) values ('admin', 'd2d41e9192f13edfe8b0467a4a65fea8', 1);

commit;
