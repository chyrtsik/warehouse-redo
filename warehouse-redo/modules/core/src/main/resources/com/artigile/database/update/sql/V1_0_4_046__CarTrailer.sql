update Car set full_name='' where full_name is null;

alter table Car
  modify column full_name varchar(255) not null;

alter table Car
  add column trailer varchar(255) default null;
