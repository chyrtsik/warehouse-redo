-- Make StoredFile table referencing PriceImport directly (to perform cascade deletion).

alter table StoredFile 
  add column priceImport_id bigint(20) null;

update StoredFile set priceImport_id = (select link.priceImport_id from PriceImport_StoredFile link where link.storedFile_id = StoredFile.id);

alter table StoredFile 
  add constraint foreign key (priceImport_id) references PriceImport (id) on delete cascade;

create index IND_SortedFile_PriceImport on StoredFile (priceImport_id);

drop table PriceImport_StoredFile;