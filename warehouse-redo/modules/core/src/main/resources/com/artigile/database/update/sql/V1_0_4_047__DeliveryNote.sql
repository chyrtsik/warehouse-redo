alter table DeliveryNote
  add column brand varchar(255) default null;

alter table DeliveryNote
  add column state_number varchar(255) default null;

alter table DeliveryNote
  add column full_name varchar(255) default null;

alter table DeliveryNote
  add column owner varchar(255) default null;

alter table DeliveryNote
  add column trailer varchar(255) default null;
