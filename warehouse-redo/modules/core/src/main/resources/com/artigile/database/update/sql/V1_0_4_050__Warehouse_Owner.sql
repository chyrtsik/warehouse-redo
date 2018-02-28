-- Add warehouse owner
alter table Warehouse
  add column owner_id bigint(20) default null;

alter table Warehouse
 add CONSTRAINT FOREIGN KEY (owner_id) REFERENCES Contractor (id);

-- Add warehouse worker
 alter table Warehouse
  add column responsible_id bigint(20) default null;

alter table Warehouse
 add CONSTRAINT FOREIGN KEY (responsible_id) REFERENCES Contact (id);

 -- Add warehouse address
 alter table Warehouse
  add column address varchar(255) default null;