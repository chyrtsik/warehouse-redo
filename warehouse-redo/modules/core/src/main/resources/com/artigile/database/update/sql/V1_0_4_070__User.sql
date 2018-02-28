alter table User add column name_on_product varchar(255) default null;
alter table User add column simplified_workplace boolean not null default false;
create unique index ind_user_name_on_product on User(name_on_product);
