-- Convert data structure for import data adapters (remove ImportDataAdapter entity).
alter table DataImport
  add column adapterUid varchar(36) null;

update DataImport 
  set adapterUid = (select uidAdapter from ImportDataAdapter where id = adapter_id);

alter table DataImport 
  modify column adapterUid varchar(36) not null;

alter table DataImport 
  drop foreign key dataimport_ibfk_2;

alter table DataImport
  drop column adapter_id;

drop table ImportDataAdapter;
