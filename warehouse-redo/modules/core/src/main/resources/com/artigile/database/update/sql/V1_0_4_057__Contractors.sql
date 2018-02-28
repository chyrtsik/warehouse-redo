alter table Contractor
  add column fullname varchar(255) default null;

alter table Contractor
  change address legal_address varchar(255) default null;

alter table Contractor
  add column postal_address varchar(255) default null;

alter table Contractor
  add column phone varchar(255) default null;
