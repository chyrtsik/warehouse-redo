alter table `User` add column terminal_access boolean not null default false;

CREATE VIEW user_list AS SELECT login FROM `User` where terminal_access=1;