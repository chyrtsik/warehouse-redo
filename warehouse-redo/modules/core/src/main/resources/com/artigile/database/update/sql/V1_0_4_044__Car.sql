create table car (
  id bigint(20) not null auto_increment,
  brand varchar(255) not null,
  state_number varchar(255) not null,
  full_name varchar(255) default null,
  owner varchar(255) default null,
  description varchar(255) default null,
  primary key (id)
);
