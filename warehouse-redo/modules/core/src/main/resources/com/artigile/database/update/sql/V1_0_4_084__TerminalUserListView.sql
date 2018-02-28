alter table `User` drop column terminal_access;

drop view user_list;

CREATE VIEW user_list AS SELECT login FROM `User` where simplified_workplace=1;